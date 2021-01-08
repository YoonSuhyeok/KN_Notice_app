package com.example.noticekangwon.Activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noticekangwon.R
import com.example.noticekangwon.Recyclerviews.CollegeAdapter
import com.example.noticekangwon.Recyclerviews.MajorAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.filter_page.*

class FilterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filter_page)

        val collegeList: ArrayList<String> = arrayListOf()
        collegeList.add("IT대학")
        collegeList.add("간호대학")

        var collegeAdapter = CollegeAdapter(collegeList)
        collegeAdapter.itemClick = object: CollegeAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                Toast.makeText(this@FilterActivity, position.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        collegeRecyler.layoutManager = LinearLayoutManager(
            this@FilterActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        collegeRecyler.setHasFixedSize(true)

        collegeRecyler.adapter = collegeAdapter

        val majorList: ArrayList<String> = arrayListOf()
        majorList.add("컴공")
        majorList.add("전기전자")

        var majorAdapter = MajorAdapter(majorList)

        majorRecyler.layoutManager = LinearLayoutManager(
            this@FilterActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        majorRecyler.setHasFixedSize(true)
        majorRecyler.adapter = majorAdapter

    }
}