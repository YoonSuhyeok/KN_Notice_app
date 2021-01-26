package com.example.notice.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.room.Room
import com.example.notice.R
import com.example.notice.Recyclerviews.NoticeAdapter
import com.example.notice.dataBase.AppDataBase
import com.example.notice.dataBase.Notice
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback
import kotlinx.android.synthetic.main.longclick_dialog_menus.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LongClickMenu(private val context: Context, private val noticeAdapter: NoticeAdapter) {

    fun callFun(title: String) {
        val dig = Dialog(context)
        dig.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dig.setContentView(R.layout.longclick_dialog_menus)
        dig.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val db = Room.databaseBuilder(context, AppDataBase::class.java, "Major-DB").allowMainThreadQueries().build()
        val tmp:Notice = db.noticeDao().getNotice(title)
        val pinBtn = dig.pinBtn
        val bookBtn = dig.bookBtn
        val linkBtn = dig.linkBtn

        if(tmp.isPin == 0) pinBtn.text = "핀 고정 해제"
        else dig.pinBtn.text = "핀 고정"

        if(tmp.isBookmark) bookBtn.text = "북마크 해제"
        else bookBtn.text = "북마크 추가"

        dig.show()

        pinBtn.setOnClickListener {
            if(tmp.isPin == 0) {
                tmp.isPin = 1
                db.noticeDao().update(tmp)
                noticeAdapter.notifyDataSetChanged()
            } else {
                tmp.isPin = 0
                db.noticeDao().update(tmp)
            }
            noticeAdapter.changeList(db.noticeDao().getFil(noticeAdapter.getSelectIds()))
            db.close()
            dig.dismiss()
        }

        bookBtn.setOnClickListener {
            if(tmp.isBookmark) {
                tmp.isBookmark = false
                db.noticeDao().update(tmp)
            } else {
                tmp.isBookmark = true
                db.noticeDao().update(tmp)
            }
            noticeAdapter.changeList(db.noticeDao().getFil(noticeAdapter.getSelectIds()))
            db.close()
            dig.dismiss()
        }

        linkBtn.setOnClickListener {
            val mNaverService = Retrofit.Builder().baseUrl("https://openapi.naver.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NaverService::class.java)

            val call = mNaverService.getShortUrl(tmp.mUrl)!!
            call.enqueue(object : Callback<ShortUrlResult?>{
                override fun onResponse(
                    call: Call<ShortUrlResult?>,
                    response: Response<ShortUrlResult?>
                ) {
                    val serverCallbackArgs: MutableMap<String, String> = HashMap()
                    serverCallbackArgs["title"] = tmp.mTitle
                    val empty = response.body() as ShortUrlResult
                    serverCallbackArgs["url"] = empty.result.url
                    val templateId = "45708"
                    KakaoLinkService.getInstance().sendCustom(
                        context,
                        templateId,
                        serverCallbackArgs,
                        object : ResponseCallback<KakaoLinkResponse?>() {
                            override fun onFailure(errorResult: ErrorResult?) {
                            }

                            override fun onSuccess(result: KakaoLinkResponse?) {
                            }
                        }
                    )
                }

                override fun onFailure(call: Call<ShortUrlResult?>, t: Throwable) {
                    val serverCallbackArgs: MutableMap<String, String> = HashMap()
                    serverCallbackArgs["title"] = tmp.mTitle
                    serverCallbackArgs["url"] = tmp.mUrl
                    val templateId = "45708"
                    KakaoLinkService.getInstance().sendCustom(
                        context,
                        templateId,
                        serverCallbackArgs,
                        object : ResponseCallback<KakaoLinkResponse?>() {
                            override fun onFailure(errorResult: ErrorResult?) {
                            }

                            override fun onSuccess(result: KakaoLinkResponse?) {
                            }
                        }
                    )
                }
            })
            db.close()
            dig.dismiss()
        }
    }
}
