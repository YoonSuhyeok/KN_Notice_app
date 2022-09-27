package com.example.notice.api

import com.example.notice.api.request.validateRequest
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface oauth {

    @POST("/api/member/validate")
    fun validate(@Body params: validateRequest): Call<String>

}