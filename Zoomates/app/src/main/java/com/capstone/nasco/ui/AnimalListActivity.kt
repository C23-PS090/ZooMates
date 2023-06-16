package com.capstone.nasco.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.nasco.adapter.AnimalAdapter
import com.capstone.nasco.databinding.ActivityAnimalListBinding
import com.capstone.nasco.model.AnimalResponseItem
import com.capstone.nasco.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnimalListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnimalListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAnimalListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAnimalList()

    }

    private fun getAnimalList() {
        val apiService = ApiConfig().getApiService().getAllAnimal()
        apiService.enqueue(object: Callback<List<AnimalResponseItem>> {
            override fun onResponse(
                call: Call<List<AnimalResponseItem>>,
                response: Response<List<AnimalResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val adapter = AnimalAdapter(response.body())
                    binding.rvListItem.layoutManager = LinearLayoutManager(this@AnimalListActivity)
                    binding.rvListItem.setHasFixedSize(true)
                    binding.rvListItem.adapter = adapter

                    adapter.setOnItemClickCallback(object: AnimalAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: AnimalResponseItem?) {
                            val intent = Intent(this@AnimalListActivity, DetailsActivity::class.java)
                            intent.putExtra("MODE", 2)
                            intent.putExtra("DESKRIPSI", data?.description)
                            intent.putExtra("NAMA", data?.nama)
                            intent.putExtra("JENIS", data?.jenis)
                            intent.putExtra("DIET", data?.diet)
                            intent.putExtra("MAKANAN", data?.makanan)
                            intent.putExtra("REPRODUKSI", data?.reproduksi)
                            intent.putExtra("CIRI_TUBUH", data?.tubuh)
                            intent.putExtra("KEMAMPUAN", data?.kemampuan)
                            intent.putExtra("HABITAT", data?.habitat)
                            intent.putExtra("PERNAPASAN", data?.pernapasan)
                            intent.putExtra("PERGERAKAN", data?.pergerakan)
                            intent.putExtra("CURRENT_MODEL", data?.namaHewan)

                            startActivity(intent)
                        }
                    })
                }
            }

            override fun onFailure(call: Call<List<AnimalResponseItem>>, t: Throwable) {
                Log.e("ERROR", "${t.message}")
            }

        })
    }
}