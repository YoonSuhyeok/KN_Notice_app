package com.example.noticekangwon.Retrofit


import com.example.noticekangwon.DataBase.Notice
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitAPI {
    @GET("/")
    fun getNotice(): Call<Notice>
}