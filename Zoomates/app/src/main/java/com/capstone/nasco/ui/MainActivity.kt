package com.capstone.nasco.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.capstone.nasco.adapter.SliderAdapter
import com.capstone.nasco.databinding.ActivityMainBinding
import com.capstone.nasco.utils.uriToFile
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderView
import java.io.File

class MainActivity : AppCompatActivity() {
    private var sliderAdapter: SliderAdapter = SliderAdapter()
    private var getFile: File? = null

    private lateinit var binding: ActivityMainBinding
    private lateinit var sliderView: SliderView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sliderView = binding.flHero
        sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        sliderView.setSliderAdapter(sliderAdapter)
        sliderView.scrollTimeInSec = 3
        sliderView.isAutoCycle = true
        sliderView.setIndicatorAnimation(IndicatorAnimationType.DROP)
        sliderView.startAutoCycle()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnScan.setOnClickListener {
            startCameraX()
        }

        binding.btnGaleri.setOnClickListener {
            startGallery()
        }

        binding.btnListBinatang.setOnClickListener {
            val intent = Intent(this, AnimalListActivity::class.java)
            startActivity(intent)
        }

    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }


    private fun startCameraX() {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("MODE", CAMERA_X)
        startActivity(intent)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@MainActivity)
                getFile = myFile

                val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                intent.putExtra("IMAGE", getFile)
                startActivity(intent)
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val CAMERA_X = 1
    }
}