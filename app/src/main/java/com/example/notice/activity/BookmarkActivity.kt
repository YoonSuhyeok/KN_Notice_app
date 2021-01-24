package com.example.notice.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.notice.dataBase.AppDataBase
import com.example.notice.dataBase.Notice
import com.example.notice.R
import com.example.notice.Recyclerviews.NoticeAdapter
import com.example.notice.Recyclerviews.RecyclerDecoration
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

        val db =
            Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB").allowMainThreadQueries()
                .build()

        noticeList = db.noticeDao().getBookmark(true)

        db.close()

        noticeAdapter = NoticeAdapter(this, noticeList)

        bookmarkRecycle.adapter = noticeAdapter
    }

    override fun onResume() {
        super.onResume()
        val db = Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB").allowMainThreadQueries().build()

        noticeList = db.noticeDao().getBookmark(true)

        db.close()

        noticeAdapter = NoticeAdapter(this, noticeList)

        bookmarkRecycle.adapter = noticeAdapter
    }

}