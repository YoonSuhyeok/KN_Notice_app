package com.example.notice.activity

import android.R.attr.*
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.notice.dataBase.AppDataBase
import com.example.notice.dataBase.College
import com.example.notice.dataBase.Major
import com.example.notice.R
import com.example.notice.Recyclerviews.CollegeAdapter
import com.example.notice.Recyclerviews.MajorAdapter
import com.example.notice.Recyclerviews.RecyclerDecoration
import com.example.notice.dataBase.Model
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.filter_page.*


class FilterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filter_page)
        overridePendingTransition(R.anim.horizon_enter, R.anim.none)

        val db = Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB").allowMainThreadQueries().build()

        // 학과 추가
        // 불어불문학과
        if(db.majorDao().getMId("불어불문학과") == 0 ) {
            db.majorDao().insert(Major(12, "불어불문학과"))
        }

        val majorList = db.majorDao().all
        val majorAdapter = MajorAdapter(this, majorList, loadSharedPreferencesMajor(majorList))

        majorRecycler.layoutManager = LinearLayoutManager(this@FilterActivity, LinearLayoutManager.VERTICAL, false)
        majorRecycler.setHasFixedSize(true)
        majorRecycler.adapter = majorAdapter

        majorAdapter.filter.filter("")

        val collegeList = db.collegeDao().all
        val collegeAdapter = CollegeAdapter(this, collegeList, majorList, majorAdapter, loadSharedPreferencesCollege(collegeList))

        collegeRecycler.layoutManager = LinearLayoutManager(this@FilterActivity, LinearLayoutManager.VERTICAL, false)
        collegeRecycler.setHasFixedSize(true)
        collegeRecycler.adapter = collegeAdapter
        val spaceDecoration = RecyclerDecoration(0)
        collegeRecycler.addItemDecoration(spaceDecoration)

        collegeRecycler.smoothScrollToPosition(collegeList.size)
        collegeRecycler.smoothScrollToPosition(0)

        if(intent.getBooleanExtra("first", false)){
            backButton.visibility = View.GONE
            initSharedPreferencesFile(collegeList, majorList)
        }

        backButton.setOnClickListener { onBackPressed() }

        button.setOnClickListener {
            var sharedObject = getSharedPreferences("college", 0)
            var sharedEditor = sharedObject.edit()

            var maps = collegeAdapter.getMap()
            for (x in collegeList) {
                val empty = maps[x.cName]
                if (empty != null) {
                    sharedEditor.putBoolean(x.cName, empty)
                }
            }

            sharedEditor.apply()

            sharedObject = getSharedPreferences("major", 0)
            sharedEditor = sharedObject.edit()
            maps = majorAdapter.getMap()
            var isClicked = false
            for (x in majorList) {
                val empty = maps[x.mName]
                if(empty != null ){
                    sharedEditor.putBoolean(x.mName, empty)
                    if(empty == true){
                        isClicked = true
                    }
                }
            }
            sharedEditor.commit()

            sharedObject = getSharedPreferences("isFirstSP", 0)
            val isFirst = sharedObject.getBoolean("isFirst", false)
            if(isClicked){
                if (!isFirst) {
                    sharedEditor = sharedObject.edit()
                    sharedEditor.putBoolean("isFirst", true)
                    sharedEditor.commit()
                    finishAffinity()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    intent.putExtra("UpdateFilter", true)
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(this, "하나 이상을 선택해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initSharedPreferencesFile(collegeList: List<College>, majorList: List<Major>) {
        var sharedObject = getSharedPreferences("college", 0)
        var sharedEditor = sharedObject.edit()

        for (x in collegeList) {
            sharedEditor.putBoolean(x.cName, false)
        }

        sharedEditor.apply()

        sharedObject = getSharedPreferences("major", 0)
        sharedEditor = sharedObject.edit()
        for (x in majorList) {
            sharedEditor.putBoolean(x.mName, false)
        }

        sharedEditor.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.none, R.anim.right_to_left)
    }

    private fun <T: Model> loadSharedPreferences(list: List<T>, name:String): MutableMap<String, Boolean>{
        val sharedObject = getSharedPreferences(name, 0)
        val loadMap = mutableMapOf<String, Boolean>()
        for (x in list) {
            val isSelect = sharedObject.getBoolean(x.getModel(), false)
            loadMap[x.getModel()] = isSelect
        }
        return loadMap
    }
    private fun loadSharedPreferencesCollege(collegeList: List<College>): MutableMap<String, Boolean> {
        val sharedObject = getSharedPreferences("college", 0)
        val loadMap = mutableMapOf<String, Boolean>()
        for (x in collegeList) {
            val isSelect = sharedObject.getBoolean(x.cName, false)
            loadMap[x.cName] = isSelect
        }
        return loadMap
    }

    private fun loadSharedPreferencesMajor(majorList: List<Major>): MutableMap<String, Boolean> {
        val sharedObject = getSharedPreferences("major", 0)
        val loadMap = mutableMapOf<String, Boolean>()
        for (x in majorList) {
            val isSelect = sharedObject.getBoolean(x.mName, false)
            loadMap[x.mName] = isSelect
        }
        return loadMap
    }
}