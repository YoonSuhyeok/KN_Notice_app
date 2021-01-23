package com.example.noticekangwon.Activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.noticekangwon.DataBase.AppDataBase
import com.example.noticekangwon.DataBase.Notice
import com.example.noticekangwon.R
import com.example.noticekangwon.Recyclerviews.NoticeAdapter
import com.example.noticekangwon.Recyclerviews.RecyclerDecoration
import kotlinx.android.synthetic.main.bookmark_page.*

class BookmarkActivity : AppCompatActivity() {

    private var noticeList: List<Notice> = arrayListOf()
    private var noticeAdapter: NoticeAdapter = NoticeAdapter(this, noticeList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bookmark_page)

        val toolbar: Toolbar = findViewById(R.id.bookToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bookmarkRecycle.setHasFixedSize(true)
        bookmarkRecycle.addItemDecoration(RecyclerDecoration(0))
        bookmarkRecycle.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        var db =
            Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB").allowMainThreadQueries()
                .build()

        noticeList = db.noticeDao().getBookmark(true)

        db.close()

        noticeAdapter = NoticeAdapter(this, noticeList)

        bookmarkRecycle.adapter = noticeAdapter
    }

}