package com.example.notice.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.notice.R
import com.example.notice.Recyclerviews.NoticeAdapter
import com.example.notice.dataBase.AppDataBase
import com.example.notice.dataBase.Notice
import kotlinx.android.synthetic.main.longclick_dialog_menus.*

class LongClickMenu(private val context: Context, private val noticeAdapter: NoticeAdapter) {

    @RequiresApi(Build.VERSION_CODES.N)
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

            val intents = Intent(Intent.ACTION_SEND)
            intents.type = "text/plain"
            val text = tmp.mUrl
            intents.putExtra(Intent.EXTRA_TEXT, text)
            val chooser = Intent.createChooser(intents, "친구에게 공유하기")
            context.startActivity(chooser)
            db.close()
            dig.dismiss()
        }
    }
}
