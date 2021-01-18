package com.example.noticekangwon.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.noticekangwon.DataBase.AppDataBase
import com.example.noticekangwon.DataBase.College
import com.example.noticekangwon.DataBase.Major
import com.example.noticekangwon.DataBase.Notice
import com.example.noticekangwon.R
import com.example.noticekangwon.defaultClass.ThemeSet
import kotlinx.android.synthetic.main.dialog_custom.*
import kotlinx.android.synthetic.main.dialog_custom.view.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        var shared: SharedPreferences = getSharedPreferences("themeSetSP", 0)
        var mode = shared?.getString("themeMode", ThemeSet.LIGHT_MODE)

        ThemeSet.applyTheme(mode)

        Handler().postDelayed(Runnable {
            shared = getSharedPreferences("isFirstSP", 0)
            var isInit = shared.getBoolean("isInitDB", false)
            var isFirst = shared.getBoolean("isFirst", false)

            if (!isInit) {
                println("initDB Run")
                initDB()
                var editor: SharedPreferences.Editor = shared.edit()
                editor.putBoolean("isInitDB", true)
                editor.commit()
            }

            if (!isFirst) {
                startActivity(Intent(this, FilterActivity::class.java))
                // val intent = Intent(this, MyService::class.java)
                // startService(intent)
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }, 2000)
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
                db.majorDao().insert(Major(x + 1, y))
            }
        }
        db.close()
    }
}