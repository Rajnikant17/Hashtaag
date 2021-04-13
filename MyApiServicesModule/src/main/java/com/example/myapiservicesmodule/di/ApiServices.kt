package com.example.myapiservicesmodule.di

import com.example.myapiservicesmodule.di.models.ResponseFromApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming
import retrofit2.http.Url

interface ApiServices {
    @POST("/image/test")
    suspend fun getImageUrl() : ResponseFromApi

    @GET
    @Streaming
    fun getImage(@Url url: String): Call<ResponseBody>
}