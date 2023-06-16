package com.capstone.nasco.network

import com.capstone.nasco.model.AnimalResponse
import com.capstone.nasco.model.AnimalResponseItem
import com.capstone.nasco.model.PredictResponse
import okhttp3.MultipartBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("/predict")
    fun identifyCreature(
        @Part file: MultipartBody.Part
    ) : Call<PredictResponse>

    @GET("/animal")
    fun getAllAnimal() : Call<List<AnimalResponseItem>>
}