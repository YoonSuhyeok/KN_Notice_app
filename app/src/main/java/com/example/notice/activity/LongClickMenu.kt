package com.example.notice.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.notice.dataBase.AppDataBase
import com.example.notice.dataBase.Notice
import com.example.notice.R
import kotlinx.android.synthetic.main.longclick_dialog_menus.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LongClickMenu(private var context: Context) {

    fun callFun(title: String) {
        val dig = Dialog(context)

        dig.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dig.setContentView(R.layout.longclick_dialog_menus)
        dig.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val db = Room.databaseBuilder(context, AppDataBase::class.java, "Major-DB").allowMainThreadQueries().build()
        val tmp:Notice = db.noticeDao().getNotice(title)
        val pinBtn = dig.pinBtn
        val bookBtn = dig.bookBtn
        if(tmp.isPin == 0)
            pinBtn.text = "핀 고정 해제"
        else
            dig.pinBtn.text = "핀 고정"

        if(tmp.isBookmark)
            bookBtn.text = "북마크 해제"
        else
            bookBtn.text = "북마크 추가"

        dig.show()

        pinBtn.setOnClickListener {
            if(tmp.isPin == 0) {
                tmp.isPin = 1
                db.noticeDao().update(tmp)
                CoroutineScope(Dispatchers.Main).launch {
                    ContextCompat.startActivity(context, Intent(context, TmpClass::class.java), null)
                }
            } else {
                tmp.isPin = 0
                db.noticeDao().update(tmp)
                CoroutineScope(Dispatchers.Main).launch {
                    ContextCompat.startActivity(context, Intent(context, TmpClass::class.java), null)
                }
            }
            dig.dismiss()
        }

        bookBtn.setOnClickListener {
            if(tmp.isBookmark) {
                tmp.isBookmark = false
                db.noticeDao().update(tmp)
                CoroutineScope(Dispatchers.Main).launch {
                    ContextCompat.startActivity(context, Intent(context, TmpClass::class.java), null)
                }
            } else {
                tmp.isBookmark = true
                db.noticeDao().update(tmp)
                CoroutineScope(Dispatchers.Main).launch {
                    ContextCompat.startActivity(context, Intent(context, TmpClass::class.java), null)
                }
            }
            dig.dismiss()
        }
    }
}