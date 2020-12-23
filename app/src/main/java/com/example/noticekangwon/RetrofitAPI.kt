package com.example.noticekangwon

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitAPI {
    @GET("/")
    fun getNotice(): Call<Notice>
}