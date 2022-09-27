package com.example.notice.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var retrofitClient: Retrofit? = null

    private val BASE_URL = "http://192.168.0.10:8080"

    fun getClient(): Retrofit? {
        if(retrofitClient == null) {
            val gson = GsonBuilder().setLenient().create()
            retrofitClient = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        return retrofitClient
    }

}