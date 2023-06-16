package com.capstone.nasco.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.capstone.nasco.R
import com.capstone.nasco.ar.kotlin.arcore.ArActivity
import com.capstone.nasco.databinding.ActivityDetailsBinding
import com.capstone.nasco.model.PredictResponse
import com.capstone.nasco.network.ApiConfig
import com.capstone.nasco.utils.rotateFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import java.util.*

class DetailsActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var binding: ActivityDetailsBinding

    // tts
    private lateinit var textToSpeech: TextToSpeech
    private var savedDescription: String? = null
    private var isTextToSpeechRunning = false

    private var currentModel: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.getIntExtra("MODE", 0) == 1) {
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == CAMERA_X_RESULT) {
                    val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        it.data?.getSerializableExtra("picture", File::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        it.data?.getSerializableExtra("picture")
                    } as? File
                    val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
                    myFile?.let { file ->
                        rotateFile(file, isBackCamera)
                        binding.ivPhoto.setImageBitmap(BitmapFactory.decodeFile(file.path))
                        uploadImage(file)
                    }
                }
            }.launch(Intent(this, CameraActivity::class.java))
        } else if (intent.getIntExtra("MODE", 0) == 2) {
            Glide.with(this@DetailsActivity)
                .load(R.drawable.ic_capture)
                .into(binding.ivPhoto)

            savedDescription = intent.getStringExtra("DESKRIPSI")

            binding.tvNamaHewan.text = intent.getStringExtra("NAMA")
            binding.tvJenisHewan.text = intent.getStringExtra("JENIS")
            binding.tvDietHewan.text = intent.getStringExtra("DIET")
            binding.tvMakananHewan.text = intent.getStringExtra("MAKANAN")
            binding.tvReproduksiHewan.text = intent.getStringExtra("REPRODUKSI")
            binding.tvCiriTubuhHewan.text = intent.getStringExtra("CIRI_TUBUH")
            binding.tvKemampuanHewan.text = intent.getStringExtra("KEMAMPUAN")
            binding.tvHabitatHewan.text = intent.getStringExtra("HABITAT")
            binding.tvPernapasanHewan.text = intent.getStringExtra("PERNAPASAN")
            binding.tvPergerakanHewan.text = intent.getStringExtra("PERGERAKAN")

            currentModel = intent.getStringExtra("CURRENT_MODEL") ?: ""
        }else {
            val file = intent.extras?.get("IMAGE") as? File
            file?.let {
                val reducedFile = reduceFileImage(file)
                uploadImage(reducedFile)
                Glide.with(this@DetailsActivity)
                    .load(reducedFile)
                    .into(binding.ivPhoto)
            }
        }

        // tts
        textToSpeech = TextToSpeech(this, this) //ts

        binding.btnPlay.setOnClickListener {
            toggleTextToSpeech()
        }

        // ar
        binding.btnAr.setOnClickListener {
            startHelloArActivity()
        }
    }

    // tts toggle
    private fun toggleTextToSpeech() {
        if (isTextToSpeechRunning) {
            stopTextToSpeech()
        } else {
            startTextToSpeech()
        }
    }

    private fun startTextToSpeech() {
        savedDescription?.let { description ->
            // Run tts
            textToSpeech.speak(description, TextToSpeech.QUEUE_FLUSH, null, null)
            isTextToSpeechRunning = true
            binding.btnPlay.text = getString(R.string.stop_text_to_speech)
        }
    }

    private fun stopTextToSpeech() {
        // Stop tts
        textToSpeech.stop()
        isTextToSpeechRunning = false
        binding.btnPlay.text = getString(R.string.start_text_to_speech)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.getDefault())
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language not supported")
            }
        } else {
            Log.e("TTS", "Initialization failed")
        }
    }

    override fun onDestroy() {
        // Shutdown the text-to-speech engine
        textToSpeech.stop()
        textToSpeech.shutdown()
        super.onDestroy()
    }

    // ar
    private fun startHelloArActivity() {
        val intent = Intent(this, ArActivity::class.java)
        intent.putExtra("MODEL_NAME", currentModel)
        startActivity(intent)
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 40
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }


    private fun uploadImage(file: File) {
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestImageFile
        )

        val apiService = ApiConfig().getApiService()
        val uploadImageRequest = apiService.identifyCreature(imageMultipart)
        uploadImageRequest.enqueue(object : Callback<PredictResponse> {
            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    binding.tvNamaHewan.text = responseBody?.nama
                    binding.tvJenisHewan.text = responseBody?.jenis
                    binding.tvDietHewan.text = responseBody?.diet
                    binding.tvMakananHewan.text = responseBody?.makanan
                    binding.tvReproduksiHewan.text = responseBody?.reproduksi
                    binding.tvCiriTubuhHewan.text = responseBody?.tubuh
                    binding.tvKemampuanHewan.text = responseBody?.kemampuan
                    binding.tvHabitatHewan.text = responseBody?.habitat
                    binding.tvPernapasanHewan.text = responseBody?.pernapasan
                    binding.tvPergerakanHewan.text = responseBody?.pergerakan

                    currentModel = responseBody?.prediction
                    Log.d("Debug Implementation", responseBody.toString())

                    savedDescription = responseBody?.description ?: ""
                    startTextToSpeech() //textToSpeech.speak(responseBody?.description ?: "", TextToSpeech.QUEUE_FLUSH, null, null)
                }

            }

            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                val unidentified = "unidentified"

                binding.tvNamaHewan.text = unidentified
                binding.tvJenisHewan.text = unidentified
                binding.tvDietHewan.text = unidentified
                binding.tvMakananHewan.text = unidentified
                binding.tvReproduksiHewan.text = unidentified
                binding.tvCiriTubuhHewan.text = unidentified
                binding.tvKemampuanHewan.text = unidentified
                binding.tvHabitatHewan.text = unidentified
                binding.tvPernapasanHewan.text = unidentified
                binding.tvPergerakanHewan.text = unidentified
            }
        })
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        const val MAXIMAL_SIZE = 1000000
    }
}