package com.example.noticekangwon.Activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.noticekangwon.DataBase.AppDataBase
import com.example.noticekangwon.DataBase.Notice
import com.example.noticekangwon.R
import com.example.noticekangwon.Recyclerviews.CollegeAdapter
import com.example.noticekangwon.Recyclerviews.MajorAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.filter_page.*

class FilterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filter_page)

        var db =Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB").allowMainThreadQueries().build()

        val majorList = db.majorDao().all
        var majorAdapter = MajorAdapter(majorList)

        majorRecyler.layoutManager = LinearLayoutManager(
            this@FilterActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        majorRecyler.setHasFixedSize(true)
        majorRecyler.adapter = majorAdapter
        majorAdapter.filter.filter("")
        val collegeList = db.collegeDao().all
        var collegeAdapter = CollegeAdapter(collegeList, majorAdapter)

        collegeRecyler.layoutManager = LinearLayoutManager(
            this@FilterActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        collegeRecyler.setHasFixedSize(true)
        collegeRecyler.adapter = collegeAdapter

        imageButton.setOnClickListener {
            onBackPressed()
        }

        button.setOnClickListener {
            val sharedObject = getSharedPreferences("filter", 0)
            val sharedEditer = sharedObject.edit()
            val list = majorAdapter.getMajorList()
            for(x in list){
                sharedEditer.putInt("${x.mName}", x.mId)
            }
        }
    }
}