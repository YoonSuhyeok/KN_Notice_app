package com.example.notice.dialog

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NaverService {
    @Headers(
        "X-Naver-Client-Id: " + NaverConsts.CLIENT_ID,
        "X-Naver-Client-Secret: " + NaverConsts.CLIENT_SECRET)
    @GET("v1/util/shorturl")
    fun getShortUrl(@Query("url") url: String?): Call<ShortUrlResult?>?

}