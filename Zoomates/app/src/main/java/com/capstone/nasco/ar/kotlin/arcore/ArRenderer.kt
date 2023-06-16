/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.capstone.nasco.ar.kotlin.arcore

import android.opengl.GLES30
import android.opengl.Matrix
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.ar.core.Anchor
import com.google.ar.core.Camera
import com.google.ar.core.DepthPoint
import com.google.ar.core.Frame
import com.google.ar.core.InstantPlacementPoint
import com.google.ar.core.LightEstimate
import com.google.ar.core.Plane
import com.google.ar.core.Point
import com.google.ar.core.Session
import com.google.ar.core.Trackable
import com.google.ar.core.TrackingFailureReason
import com.google.ar.core.TrackingState
import com.capstone.nasco.ar.java.common.helpers.DisplayRotationHelper
import com.capstone.nasco.ar.java.common.helpers.TrackingStateHelper
import com.capstone.nasco.ar.java.common.samplerender.Framebuffer
import com.capstone.nasco.ar.java.common.samplerender.GLError
import com.capstone.nasco.ar.java.common.samplerender.Mesh
import com.capstone.nasco.ar.java.common.samplerender.SampleRender
import com.capstone.nasco.ar.java.common.samplerender.Shader
import com.capstone.nasco.ar.java.common.samplerender.Texture
import com.capstone.nasco.ar.java.common.samplerender.VertexBuffer
import com.capstone.nasco.ar.java.common.samplerender.arcore.BackgroundRenderer
import com.capstone.nasco.ar.java.common.samplerender.arcore.PlaneRenderer
import com.capstone.nasco.ar.java.common.samplerender.arcore.SpecularCubemapFilter
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.NotYetAvailableException
import java.io.IOException
import java.nio.ByteBuffer
import com.capstone.nasco.R


/** Merender aplikasi AR menggunakan contoh Renderer */
class ArRenderer(val activity: ArActivity, val modelName: String) :
  SampleRender.Renderer, DefaultLifecycleObserver {
  companion object {
    val TAG = "HelloArRenderer"

    // Lihat definisi updateSphericalHarmonicsCoefficients untuk penjelasannya konstanta
    private val sphericalHarmonicFactors =
      floatArrayOf(
        0.282095f,
        -0.325735f,
        0.325735f,
        -0.325735f,
        0.273137f,
        -0.273137f,
        0.078848f,
        -0.273137f,
        0.136569f
      )

    private val Z_NEAR = 0.1f
    private val Z_FAR = 100f

    // Assumed distance from the device camera to the surface on which user will try to place
    // objects.
    // This value affects the apparent scale of objects while the tracking method of the
    // Instant Placement point is SCREENSPACE_WITH_APPROXIMATE_DISTANCE.
    // Values in the [0.2, 2.0] meter range are a good choice for most AR experiences. Use lower
    // values for AR experiences where users are expected to place objects on surfaces close to the
    // camera. Use larger values for experiences where the user will likely be standing and trying
    // to
    // place an object on the ground or floor in front of them.
    val APPROXIMATE_DISTANCE_METERS = 2.0f

    val CUBEMAP_RESOLUTION = 16
    val CUBEMAP_NUMBER_OF_IMPORTANCE_SAMPLES = 32
  }

  lateinit var render: SampleRender
  lateinit var planeRenderer: PlaneRenderer
  lateinit var backgroundRenderer: BackgroundRenderer
  lateinit var virtualSceneFramebuffer: Framebuffer
  var hasSetTextureNames = false

  // Point Cloud
  lateinit var pointCloudVertexBuffer: VertexBuffer
  lateinit var pointCloudMesh: Mesh
  lateinit var pointCloudShader: Shader

  // Lacak cloud titik terakhir yang dirender untuk menghindari pembaruan VBO jika cloud titik
  // tidak diubah. Lakukan ini menggunakan stempel waktu karena kita tidak dapat membandingkan
  // objek PointCloud.
  var lastPointCloudTimestamp: Long = 0

  // Virtual object (ARCore pawn)
  lateinit var virtualObjectMesh: Mesh
  lateinit var virtualObjectShader: Shader
  lateinit var virtualObjectAlbedoTexture: Texture
  lateinit var virtualObjectAlbedoInstantPlacementTexture: Texture

  private val wrappedAnchors = mutableListOf<WrappedAnchor>()

  // Environmental HDR
  lateinit var dfgTexture: Texture
  lateinit var cubemapFilter: SpecularCubemapFilter

  // Matriks sementara dialokasikan di sini untuk mengurangi jumlah alokasi untuk setiap frame
  val modelMatrix = FloatArray(16)
  val viewMatrix = FloatArray(16)
  val projectionMatrix = FloatArray(16)
  val modelViewMatrix = FloatArray(16) // view x model

  val modelViewProjectionMatrix = FloatArray(16) // projection x view x model

  val sphericalHarmonicsCoefficients = FloatArray(9 * 3)
  val viewInverseMatrix = FloatArray(16)
  val worldLightDirection = floatArrayOf(0.0f, 0.0f, 0.0f, 0.0f)
  val viewLightDirection = FloatArray(4) // view x world light direction

  val session
    get() = activity.arCoreSessionHelper.session

  val displayRotationHelper = DisplayRotationHelper(activity)
  val trackingStateHelper = TrackingStateHelper(activity)

  private var isModelDeployed = false
  override fun onResume(owner: LifecycleOwner) {
    displayRotationHelper.onResume()
    hasSetTextureNames = false
  }

  override fun onPause(owner: LifecycleOwner) {
    displayRotationHelper.onPause()
  }

  override fun onSurfaceCreated(render: SampleRender) {
    // Siapkan objek rendering
    // Ini melibatkan pembacaan shader dan file model 3D, jadi mungkin melempar IOException
    try {
      planeRenderer = PlaneRenderer(render)
      backgroundRenderer = BackgroundRenderer(render)
      virtualSceneFramebuffer = Framebuffer(render, /*width=*/ 1, /*height=*/ 1)

      cubemapFilter =
        SpecularCubemapFilter(render, CUBEMAP_RESOLUTION, CUBEMAP_NUMBER_OF_IMPORTANCE_SAMPLES)
      // Muat tabel pencarian nilai pencahayaan lingkungan
      dfgTexture =
        Texture(
          render,
          Texture.Target.TEXTURE_2D,
          Texture.WrapMode.CLAMP_TO_EDGE,
          /*useMipmaps=*/ false
        )
      // File dfg.raw adalah tekstur setengah mengapung mentah dengan dua saluran
      val dfgResolution = 64
      val dfgChannels = 2
      val halfFloatSize = 2

      val buffer: ByteBuffer =
        ByteBuffer.allocateDirect(dfgResolution * dfgResolution * dfgChannels * halfFloatSize)
      activity.assets.open("models/dfg.raw").use { it.read(buffer.array()) }

      // SampleRender abstraction
      GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, dfgTexture.textureId)
      GLError.maybeThrowGLException("Failed to bind DFG texture", "glBindTexture")
      GLES30.glTexImage2D(
        GLES30.GL_TEXTURE_2D,
        /*level=*/ 0,
        GLES30.GL_RG16F,
        /*width=*/ dfgResolution,
        /*height=*/ dfgResolution,
        /*border=*/ 0,
        GLES30.GL_RG,
        GLES30.GL_HALF_FLOAT,
        buffer
      )
      GLError.maybeThrowGLException("Failed to populate DFG texture", "glTexImage2D")

      // Point cloud
      pointCloudShader =
        Shader.createFromAssets(
          render,
          "shaders/point_cloud.vert",
          "shaders/point_cloud.frag",
          /*defines=*/ null
        )
          .setVec4("u_Color", floatArrayOf(31.0f / 255.0f, 188.0f / 255.0f, 210.0f / 255.0f, 1.0f))
          .setFloat("u_PointSize", 5.0f)

      // four entries per vertex: X, Y, Z, confidence
      pointCloudVertexBuffer =
        VertexBuffer(render, /*numberOfEntriesPerVertex=*/ 4, /*entries=*/ null)
      val pointCloudVertexBuffers = arrayOf(pointCloudVertexBuffer)
      pointCloudMesh =
        Mesh(render, Mesh.PrimitiveMode.POINTS, /*indexBuffer=*/ null, pointCloudVertexBuffers)

      // Virtual object to render
      virtualObjectAlbedoTexture =
        Texture.createFromAsset(
          render,
          "models/$modelName/diffuse_$modelName.jpg",
          Texture.WrapMode.CLAMP_TO_EDGE,
          Texture.ColorFormat.SRGB
        )
      virtualObjectAlbedoInstantPlacementTexture =
        Texture.createFromAsset(
          render,
          "models/$modelName/bump_$modelName.jpg",
          Texture.WrapMode.CLAMP_TO_EDGE,
          Texture.ColorFormat.SRGB
        )
      val virtualObjectPbrTexture =
        Texture.createFromAsset(
          render,
          "models/$modelName/diffuse_$modelName.jpg",
          Texture.WrapMode.CLAMP_TO_EDGE,
          Texture.ColorFormat.LINEAR
        )
      virtualObjectMesh = Mesh.createFromAsset(render, "models/$modelName/$modelName.obj")
      virtualObjectShader =
        Shader.createFromAssets(
          render,
          "shaders/environmental_hdr.vert",
          "shaders/environmental_hdr.frag",
          mapOf("NUMBER_OF_MIPMAP_LEVELS" to cubemapFilter.numberOfMipmapLevels.toString())
        )
          .setTexture("u_AlbedoTexture", virtualObjectAlbedoTexture)
          .setTexture("u_RoughnessMetallicAmbientOcclusionTexture", virtualObjectPbrTexture)
          .setTexture("u_Cubemap", cubemapFilter.filteredCubemapTexture)
          .setTexture("u_DfgTexture", dfgTexture)
    } catch (e: IOException) {
      Log.e(TAG, "Gagal membaca aset", e)
      showError("Gagal membaca aset: $e")
    }
  }

  override fun onSurfaceChanged(render: SampleRender, width: Int, height: Int) {
    displayRotationHelper.onSurfaceChanged(width, height)
    virtualSceneFramebuffer.resize(width, height)
  }

  override fun onDrawFrame(render: SampleRender) {
    val session = session ?: return

    // Nama tekstur hanya boleh disetel sekali pada utas GL kecuali jika berubah. Ini dilakukan
    // selama onDrawFrame daripada onSurfaceCreated karena sesi tidak dijamin
    // diinisialisasi selama eksekusi onSurfaceCreated
    if (!hasSetTextureNames) {
      session.setCameraTextureNames(intArrayOf(backgroundRenderer.cameraColorTexture.textureId))
      hasSetTextureNames = true
    }

    // -- Update per-frame state

    // Beri tahu sesi ARCore bahwa ukuran tampilan berubah sehingga matriks perspektif dan
    // latar belakang video dapat disesuaikan dengan benar
    displayRotationHelper.updateSessionIfNeeded(session)

    // Dapatkan bingkai saat ini dari ARSession. Ketika konfigurasi diatur ke
    // UpdateMode.BLOCKING (secara default), ini akan membatasi rendering ke
    // frekuensi gambar kamera
    val frame =
      try {
        session.update()
      } catch (e: CameraNotAvailableException) {
        Log.e(TAG, "Camera not available during onDrawFrame", e)
        showError("Camera not available. Try restarting the app.")
        return
      }

    val camera = frame.camera

    // Perbarui status BackgroundRenderer agar sesuai dengan setelan kedalaman
    try {
      backgroundRenderer.setUseDepthVisualization(
        render,
        activity.depthSettings.depthColorVisualizationEnabled()
      )
      backgroundRenderer.setUseOcclusion(render, activity.depthSettings.useDepthForOcclusion())
    } catch (e: IOException) {
      Log.e(TAG, "Failed to read a required asset file", e)
      showError("Failed to read a required asset file: $e")
      return
    }

    // BackgroundRenderer.updateDisplayGeometry harus dipanggil setiap frame untuk memperbarui koordinat
    // digunakan untuk menggambar gambar kamera latar belakang
    backgroundRenderer.updateDisplayGeometry(frame)
    val shouldGetDepthImage =
      activity.depthSettings.useDepthForOcclusion() ||
              activity.depthSettings.depthColorVisualizationEnabled()
    if (camera.trackingState == TrackingState.TRACKING && shouldGetDepthImage) {
      try {
        val depthImage = frame.acquireDepthImage16Bits()
        backgroundRenderer.updateCameraDepthTexture(depthImage)
        depthImage.close()
      } catch (e: NotYetAvailableException) {
        // Ini biasanya berarti bahwa data kedalaman belum tersedia. Ini normal jadi kami tidak akan melakukannya
        // spam logcat dengan ini
      }
    }

    // Tangani satu ketukan per bingkai
    handleTap(frame, camera)

    // Biarkan layar tidak terkunci saat melacak, tetapi biarkan terkunci saat pelacakan berhenti
    trackingStateHelper.updateKeepScreenOnFlag(camera.trackingState)

    // Tampilkan pesan berdasarkan apakah pelacakan gagal, jika pesawat terdeteksi, dan jika
    // pengguna telah menempatkan objek apapun
    val message: String? =
      when {
        camera.trackingState == TrackingState.PAUSED &&
                camera.trackingFailureReason == TrackingFailureReason.NONE ->
          activity.getString(R.string.searching_planes)
        camera.trackingState == TrackingState.PAUSED ->
          TrackingStateHelper.getTrackingFailureReasonString(camera)
        session.hasTrackingPlane() && wrappedAnchors.isEmpty() ->
          activity.getString(R.string.waiting_taps)
        session.hasTrackingPlane() && wrappedAnchors.isNotEmpty() -> null
        else -> activity.getString(R.string.searching_planes)
      }
    if (message == null) {
      activity.view.snackbarHelper.hide(activity)
    } else {
      activity.view.snackbarHelper.showMessage(activity, message)
    }

    // -- Draw background
    if (frame.timestamp != 0L) {
      // Menekan rendering jika kamera belum menghasilkan bingkai pertama. Ini untuk menghindari
      // menggambar kemungkinan sisa data dari sesi sebelumnya jika tekstur digunakan kembali
      backgroundRenderer.drawBackground(render)
    }

    // Jika tidak melacak, jangan menggambar objek 3D
    if (camera.trackingState == TrackingState.PAUSED) {
      return
    }

    // -- Draw non-occluded virtual objects (planes, point cloud)

    // Get projection matrix.
    camera.getProjectionMatrix(projectionMatrix, 0, Z_NEAR, Z_FAR)

    // Get camera matrix and draw.
    camera.getViewMatrix(viewMatrix, 0)
    frame.acquirePointCloud().use { pointCloud ->
      if (pointCloud.timestamp > lastPointCloudTimestamp) {
        pointCloudVertexBuffer.set(pointCloud.points)
        lastPointCloudTimestamp = pointCloud.timestamp
      }
      Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
      pointCloudShader.setMat4("u_ModelViewProjection", modelViewProjectionMatrix)
      render.draw(pointCloudMesh, pointCloudShader)
    }

    // Visualize planes.
    planeRenderer.drawPlanes(
      render,
      session.getAllTrackables<Plane>(Plane::class.java),
      camera.displayOrientedPose,
      projectionMatrix
    )

    // -- Draw occluded virtual objects

    // Update lighting parameters in the shader
    updateLightEstimation(frame.lightEstimate, viewMatrix)

    // Visualize anchors created by touch.
    render.clear(virtualSceneFramebuffer, 0f, 0f, 0f, 0f)
    for ((anchor, trackable) in
    wrappedAnchors.filter { it.anchor.trackingState == TrackingState.TRACKING }) {
      // Dapatkan pose Anchor saat ini di ruang dunia. Pose Anchor diperbarui
      // selama panggilan ke session.update() saat ARCore menyempurnakan perkiraannya tentang dunia.
      anchor.pose.toMatrix(modelMatrix, 0)

      // Calculate model/view/projection matrices
      Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0)
      Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0)

      // Update shader properties and draw
      virtualObjectShader.setMat4("u_ModelView", modelViewMatrix)
      virtualObjectShader.setMat4("u_ModelViewProjection", modelViewProjectionMatrix)
      val texture =
        if ((trackable as? InstantPlacementPoint)?.trackingMethod ==
          InstantPlacementPoint.TrackingMethod.SCREENSPACE_WITH_APPROXIMATE_DISTANCE
        ) {
          virtualObjectAlbedoInstantPlacementTexture
        } else {
          virtualObjectAlbedoTexture
        }
      virtualObjectShader.setTexture("u_AlbedoTexture", texture)
      render.draw(virtualObjectMesh, virtualObjectShader, virtualSceneFramebuffer)
    }

    // Susun adegan virtual dengan latar belakang
    backgroundRenderer.drawVirtualScene(render, virtualSceneFramebuffer, Z_NEAR, Z_FAR)
  }

  /** Memeriksa apakah kami mendeteksi setidaknya satu plane. */
  private fun Session.hasTrackingPlane() =
    getAllTrackables(Plane::class.java).any { it.trackingState == TrackingState.TRACKING }

  /** Perbarui status berdasarkan estimasi light frame saat ini. */
  private fun updateLightEstimation(lightEstimate: LightEstimate, viewMatrix: FloatArray) {
    if (lightEstimate.state != LightEstimate.State.VALID) {
      virtualObjectShader.setBool("u_LightEstimateIsValid", false)
      return
    }
    virtualObjectShader.setBool("u_LightEstimateIsValid", true)
    Matrix.invertM(viewInverseMatrix, 0, viewMatrix, 0)
    virtualObjectShader.setMat4("u_ViewInverse", viewInverseMatrix)
    updateMainLight(
      lightEstimate.environmentalHdrMainLightDirection,
      lightEstimate.environmentalHdrMainLightIntensity,
      viewMatrix
    )
    updateSphericalHarmonicsCoefficients(lightEstimate.environmentalHdrAmbientSphericalHarmonics)
    cubemapFilter.update(lightEstimate.acquireEnvironmentalHdrCubeMap())
  }

  private fun updateMainLight(
    direction: FloatArray,
    intensity: FloatArray,
    viewMatrix: FloatArray
  ) {
    // Kita membutuhkan arah dalam vec4 dengan 0.0 sebagai komponen terakhir untuk mengubahnya
    // menjadi ruang tampilan
    worldLightDirection[0] = direction[0]
    worldLightDirection[1] = direction[1]
    worldLightDirection[2] = direction[2]
    Matrix.multiplyMV(viewLightDirection, 0, viewMatrix, 0, worldLightDirection, 0)
    virtualObjectShader.setVec4("u_ViewLightDirection", viewLightDirection)
    virtualObjectShader.setVec3("u_LightIntensity", intensity)
  }

  private fun updateSphericalHarmonicsCoefficients(coefficients: FloatArray) {
    // Kalikan terlebih dahulu koefisien harmonik bola sebelum meneruskannya ke shader. Itu
    // konstanta dalam sphericalHarmonicFactors diturunkan dari tiga suku:
    //
    // 1. Fungsi basis harmonik bola yang dinormalisasi (y_lm)
    // 2. Faktor BRDF difus lambertian (1/pi)
    // 3. Konvolusi <cos>. Ini dilakukan agar fungsi yang dihasilkan menghasilkan radiasi
    // dari semua cahaya yang masuk ke belahan bumi untuk permukaan normal tertentu, yang
    // merupakan shader (environmental_hdr.frag) mengharapkan.
    //
    // https://google.github.io/filament/Filament.html#annex/sphericalharmonics
    require(coefficients.size == 9 * 3) {
      "The given coefficients array must be of length 27 (3 components per 9 coefficients"
    }

    // Terapkan setiap faktor ke setiap komponen dari setiap koefisien
    for (i in 0 until 9 * 3) {
      sphericalHarmonicsCoefficients[i] = coefficients[i] * sphericalHarmonicFactors[i / 3]
    }
    virtualObjectShader.setVec3Array(
      "u_SphericalHarmonicsCoefficients",
      sphericalHarmonicsCoefficients
    )
  }

  // Tangani hanya satu ketukan per bingkai, karena ketukan biasanya berfrekuensi rendah
  // dibandingkan dengan kecepatan bingkai.
  private fun handleTap(frame: Frame, camera: Camera) {
    if (isModelDeployed) {
      return
    }
    if (camera.trackingState != TrackingState.TRACKING) return
    val tap = activity.view.tapHelper.poll() ?: return

    val hitResultList =
      if (activity.instantPlacementSettings.isInstantPlacementEnabled) {
        frame.hitTestInstantPlacement(tap.x, tap.y, APPROXIMATE_DISTANCE_METERS)
      } else {
        frame.hitTest(tap)
      }

    // Hit diurutkan berdasarkan kedalaman. Pertimbangkan hanya hit terdekat di plane,
    // Oriented Point, Depth Point,  atau Titik Penempatan Instan.
    val firstHitResult =
      hitResultList.firstOrNull { hit ->
        when (val trackable = hit.trackable!!) {
          is Plane ->
            trackable.isPoseInPolygon(hit.hitPose) &&
                    PlaneRenderer.calculateDistanceToPlane(hit.hitPose, camera.pose) > 0
          is Point -> trackable.orientationMode == Point.OrientationMode.ESTIMATED_SURFACE_NORMAL
          is InstantPlacementPoint -> true
          // DepthPoints hanya dikembalikan jika Config.DepthMode diatur ke AUTOMATIC.
          is DepthPoint -> true
          else -> false
        }
      }

    if (firstHitResult != null) {
      // Batasi jumlah objek yang dibuat. Ini menghindari kelebihan keduanya
      // sistem rendering dan ARCore.
      if (wrappedAnchors.size >= 20) {
        wrappedAnchors[0].anchor.detach()
        wrappedAnchors.removeAt(0)
      }

      // Menambahkan Anchors memberi tahu ARCore bahwa ia harus melacak posisi ini
      // ruang. Anchors ini dibuat di Plane untuk menempatkan model 3D
      // dalam posisi yang benar relatif terhadap dunia dan bidang.
      wrappedAnchors.add(WrappedAnchor(firstHitResult.createAnchor(), firstHitResult.trackable))

      // Untuk perangkat yang mendukung Depth API, tampilkan dialog untuk menyarankan pengaktifan
      // oklusi berbasis kedalaman. Dialog ini harus muncul di utas UI.
      activity.runOnUiThread { activity.view.showOcclusionDialogIfNeeded() }
    }
    isModelDeployed = true

  }

  private fun showError(errorMessage: String) =
    activity.view.snackbarHelper.showError(activity, errorMessage)
}

/**
 * Kaitkan Anchor dengan trackable yang dilampirkannya. Ini digunakan untuk dapat memeriksa
 * apakah Anchor awalnya dilampirkan ke {@link InstantPlacementPoint} atau tidak.
 */
private data class WrappedAnchor(
  val anchor: Anchor,
  val trackable: Trackable,
)
