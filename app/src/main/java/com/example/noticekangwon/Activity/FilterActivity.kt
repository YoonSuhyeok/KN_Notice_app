package com.example.noticekangwon.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.noticekangwon.DataBase.AppDataBase
import com.example.noticekangwon.DataBase.College
import com.example.noticekangwon.DataBase.Major
import com.example.noticekangwon.R
import com.example.noticekangwon.Recyclerviews.CollegeAdapter
import com.example.noticekangwon.Recyclerviews.MajorAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.filter_page.*

class FilterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filter_page)

        var db =
            Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB").allowMainThreadQueries()
                .build()

        val majorList = db.majorDao().all
        var majorAdapter = MajorAdapter(majorList, loadSharedPreferencesMajor(majorList))

        majorRecyler.layoutManager = LinearLayoutManager(
            this@FilterActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        majorRecyler.setHasFixedSize(true)
        majorRecyler.adapter = majorAdapter

        majorAdapter.filter.filter("")

        val collegeList = db.collegeDao().all
        var collegeAdapter = CollegeAdapter(
            collegeList,
            majorList,
            majorAdapter,
            loadSharedPreferencesCollege(collegeList)
        )

        collegeRecyler.layoutManager = LinearLayoutManager(
            this@FilterActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        collegeRecyler.setHasFixedSize(true)
        collegeRecyler.adapter = collegeAdapter

        initSharedPreferencesFile(collegeList, majorList)

        imageButton.setOnClickListener { onBackPressed() }

        button.setOnClickListener {
            var sharedObject = getSharedPreferences("college", 0)
            var sharedEditor = sharedObject.edit()

            var maps = collegeAdapter.getMap()
            for (x in collegeList.indices) {
                val empty = maps.get("$x ${collegeList[x].cName}")
                if (empty != null) {
                    sharedEditor.putBoolean("$x ${collegeList[x].cName}", empty)
                }
            }

            sharedEditor.commit()

            sharedObject = getSharedPreferences("major", 0)
            sharedEditor = sharedObject.edit()
            maps = majorAdapter.getMap()
            for (x in majorList.indices) {
                sharedEditor.putBoolean(
                    "$x ${majorList[x].mName}",
                    maps.get("$x ${majorList[x].mName}")!!
                )
            }
            sharedEditor.commit()

            sharedObject = getSharedPreferences("isFirstSP", 0)
            var isFirst = sharedObject.getBoolean("isFirst", false)
            if (!isFirst) {
                sharedEditor = sharedObject.edit()
                sharedEditor.putBoolean("isFirst", true)
                sharedEditor.commit()
                startActivity(Intent(this, MainActivity::class.java))
            }

            finish()
        }
    }

    private fun initSharedPreferencesFile(collegeList: List<College>, majorList: List<Major>) {
        var sharedObject = getSharedPreferences("college", 0)
        var sharedEditor = sharedObject.edit()

        for (x in collegeList.indices) {
            sharedEditor.putBoolean("$x ${collegeList[x].cName}", false)
        }

        sharedEditor.commit()

        sharedObject = getSharedPreferences("major", 0)
        sharedEditor = sharedObject.edit()
        for (x in majorList.indices) {
            sharedEditor.putBoolean("$x ${majorList[x].mName}", false)
        }

        sharedEditor.commit()
    }

    private fun loadSharedPreferencesCollege(collegeList: List<College>): MutableMap<String, Boolean> {
        val sharedObject = getSharedPreferences("college", 0)
        val loadMap = mutableMapOf<String, Boolean>()
        for (x in collegeList.indices) {
            val isSelect = sharedObject.getBoolean("$x ${collegeList[x].cName}", false)
            loadMap["$x ${collegeList[x].cName}"] = isSelect
        }
        return loadMap
    }

    private fun loadSharedPreferencesMajor(majorList: List<Major>): MutableMap<String, Boolean> {
        val sharedObject = getSharedPreferences("major", 0)
        val loadMap = mutableMapOf<String, Boolean>()
        for (x in majorList.indices) {
            val isSelect = sharedObject.getBoolean("$x ${majorList[x].mName}", false)
            loadMap["$x ${majorList[x].mName}"] = isSelect
        }
        return loadMap
    }
}