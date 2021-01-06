package com.example.noticekangwon.Activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.noticekangwon.*
import com.example.noticekangwon.DataBase.AppDataBase
import com.example.noticekangwon.DataBase.College
import com.example.noticekangwon.DataBase.Major
import com.example.noticekangwon.Recyclerviews.NoticeAdapter
import com.example.noticekangwon.Recyclerviews.RecyclerDecoration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    val noticeList: ArrayList<Notice> = arrayListOf<Notice>()
    var noticeAdapter = NoticeAdapter(noticeList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        actionBar?.title = "과제 정리 앱"

        recyclerview.layoutManager = LinearLayoutManager(
            this@MainActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerview.setHasFixedSize(true)
        recyclerview.adapter = noticeAdapter
        val spaceDecoration = RecyclerDecoration(0)
        recyclerview.addItemDecoration(spaceDecoration)


        initDB()
        filterbutton.setOnClickListener {
            startActivity(Intent(this, FilterActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var temp = item.itemId
        if (temp == R.id.developInfo) {
            startActivity(Intent(this, DevelopInfoActivity::class.java))
        } else if(temp == R.id.switchBtn){
            if(item.title == "다크 모드") {
                ThemeSet.applyTheme("dark")
                item.title = "화이트 모드"
            } else {
                item.title = "다크 모드"
                ThemeSet.applyTheme("light")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateList() {
        TODO("Not yet implemented")
    }

    private fun insertNotice(Title: String, Url: String, Date: String) {

    }

    private fun initDB() {
        var collegeList: Array<String> = resources.getStringArray(R.array.college)
        val array = arrayOf(
            R.array.간호대학,
            R.array.경영대학,
            R.array.농업생명과학대학,
            R.array.동물생명과학대학,
            R.array.문화예술공과대학,
            R.array.사범대학,
            R.array.사회과학대학,
            R.array.산림과학대학,
            R.array.수의과대학,
            R.array.약학대학,
            R.array.의과대학,
            R.array.의생명과학대학,
            R.array.인문대학,
            R.array.자연과학대학,
            R.array.아이티대학,
            R.array.기타학부,
            R.array.주요공지사항
        )

        var db =
            Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB").allowMainThreadQueries()
                .build()
        var num: Int
        for (x in collegeList.indices) {
            db.collegeDao().insert(College(collegeList[x]))
            println(collegeList[x])
            num = array[x]
            var majorlist = resources.getStringArray(num)
            for (y in majorlist) {
                db.majorDao().insert(Major(x, y))
            }
        }
    }
}