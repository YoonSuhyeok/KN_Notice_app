package com.example.noticekangwon

import com.google.gson.annotations.SerializedName

data class Notice(
    @SerializedName("공지") val itUni:ArrayList<NoticeObject>
)
class NoticeObject(val name:String, val title: String, val date: String, val url: String)