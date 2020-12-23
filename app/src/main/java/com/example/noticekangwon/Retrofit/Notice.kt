package com.example.noticekangwon

import com.google.gson.annotations.SerializedName

data class Notice(
    @SerializedName("IT대학") val itUni:ArrayList<NoticeObejct>,
    @SerializedName("간호대학") val nurseUni:ArrayList<NoticeObejct>,
    @SerializedName("경영대학") val BusinessUni:ArrayList<NoticeObejct>,
    @SerializedName("기타학부") val etcUni:ArrayList<NoticeObejct>,
    @SerializedName("농업생명과학대학") val farmUni:ArrayList<NoticeObejct>,
    @SerializedName("동물생명과학대학") val animalUni:ArrayList<NoticeObejct>,
    @SerializedName("문화예술·공과대학") val artUni:ArrayList<NoticeObejct>,
    @SerializedName("사범대학") val masterUni:ArrayList<NoticeObejct>,
    @SerializedName("사회과학대학") val socialUni:ArrayList<NoticeObejct>,
    @SerializedName("산림과학대학") val forestUni:ArrayList<NoticeObejct>,
    @SerializedName("수의과대학") val shroudUni:ArrayList<NoticeObejct>,
    @SerializedName("약학대학") val pharmacyUni:ArrayList<NoticeObejct>,
    @SerializedName("의과대학") val medicineUni:ArrayList<NoticeObejct>,
    @SerializedName("의생명과학대학") val biomedicalUni:ArrayList<NoticeObejct>,
    @SerializedName("인문대학") val humanitiesUni:ArrayList<NoticeObejct>,
    @SerializedName("자연과학대학") val natureUni:ArrayList<NoticeObejct>,
    @SerializedName("주요공지사항") val mainUni:ArrayList<NoticeObejct>,
)
class NoticeObejct(val title: String, val date: String, val url: String)