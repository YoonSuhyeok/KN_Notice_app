package com.example.notice.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.notice.dataBase.AppDataBase
import com.example.notice.dataBase.College
import com.example.notice.dataBase.Major
import com.example.notice.R
import com.example.notice.defaultClass.ThemeSet

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var shared = getSharedPreferences("themeSetSP", 0)
        val mode = shared.getString("themeMode", "light")
        ThemeSet.applyTheme(mode)

        Handler().postDelayed({
            shared = getSharedPreferences("isFirstSP", 0)
            val isInit = shared.getBoolean("isInitDB", false)
            val isFirst = shared.getBoolean("isFirst", false)

            if (!isInit) {
                println("initDB Run")
                initDB()
                val editor: SharedPreferences.Editor = shared.edit()
                editor.putBoolean("isInitDB", true)
                editor.apply()
            }

            if (!isFirst) {
                val intent = Intent(this, FilterActivity::class.java)
                intent.putExtra("first", true)
                startActivity(intent)
                // val intent = Intent(this, MyService::class.java)
                // startService(intent)
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
            overridePendingTransition(R.anim.none, R.anim.horizon_exit)
        }, 2000)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.none, R.anim.horizon_exit)
    }

    private fun initDB() {
        val collegeList: Array<String> = resources.getStringArray(R.array.college)
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
            R.array.의생명과학대학,
            R.array.인문대학,
            R.array.자연과학대학,
            R.array.아이티대학,
            R.array.기타학부,
            R.array.주요공지사항
        )

        val db = Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB").allowMainThreadQueries().build()
        var num: Int
        for (x in collegeList.indices) {
            db.collegeDao().insert(College(collegeList[x]))
            num = array[x]
            for (y in resources.getStringArray(num)) {
                db.majorDao().insert(Major(x + 1, y))
            }
        }
        db.close()
    }
}