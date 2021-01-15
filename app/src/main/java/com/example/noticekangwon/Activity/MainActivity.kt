package com.example.noticekangwon.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.noticekangwon.*
import com.example.noticekangwon.DataBase.AppDataBase
import com.example.noticekangwon.DataBase.College
import com.example.noticekangwon.DataBase.Major
import com.example.noticekangwon.DataBase.Notice
import com.example.noticekangwon.Recyclerviews.NoticeAdapter
import com.example.noticekangwon.Recyclerviews.RecyclerDecoration
import com.example.noticekangwon.notifiThread.MyService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.*


class MainActivity : AppCompatActivity() {

    private var noticeList: List<Notice> = arrayListOf<Notice>()
    private lateinit var noticeAdapter: NoticeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "과제 정리 앱"

        filBtn.setOnClickListener {
            startActivity(Intent(this, FilterActivity::class.java))
        }

        // 1000 = 1초 >> 1000*60*60*3 = 3시간 vvv 3 시간마다 데이터 패치 진행
        val intent = Intent(this, MyService::class.java)
        startService(intent)

        var db = Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB")
            .allowMainThreadQueries().build()
        noticeList = db.noticeDao().all
        // ?: DB 내에서 정렬하여 나오는 방법은 없나?
        noticeList = noticeList.sortedByDescending{ it -> it.mDate }
        db.close()

        noticeAdapter = NoticeAdapter(noticeList, "학사공지")

        recyclerview.adapter = noticeAdapter
        recyclerview.setHasFixedSize(true)
        val spaceDecoration = RecyclerDecoration(0)
        recyclerview.addItemDecoration(spaceDecoration)

        recyclerview.layoutManager = LinearLayoutManager(
            this@MainActivity,
            LinearLayoutManager.VERTICAL,
            false
        )

        noticeAdapter.filter.filter("")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.developInfo -> {
                startActivity(Intent(this, DevelopInfoActivity::class.java))
                true
            }
            R.id.switchBtn -> {
                when (item.title) {
                    "Dark Mode" -> {
                        item.title = "White Mode"
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }
                    "White Mode" -> {
                        item.title = "Dark Mode"
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}