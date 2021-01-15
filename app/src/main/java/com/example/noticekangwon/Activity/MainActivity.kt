package com.example.noticekangwon.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.RadioGroup
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
import com.example.noticekangwon.defaultClass.ThemeSet
import com.example.noticekangwon.notifiThread.MyService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_custom.*
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
    private lateinit var dialog: CustomDialog
    private lateinit var noticeAdapter: NoticeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "과제 정리 앱"

        dialog = CustomDialog.CustomDialogBuilder()
            .setTitle("모드 설정")
            .create()

        var shared: SharedPreferences = getSharedPreferences("isFirstSP", 0)
        var edit = shared.edit()
        var rdIndex = shared.getInt("themeMode", 1)

        if(rdIndex == R.id.radioBtn1) {
            radioBtn1.isChecked = true
        } else if(rdIndex == R.id.radioBtn2) {
            radioBtn1.isChecked = true
        } else if(rdIndex == R.id.radioBtn3) {
            radioBtn1.isChecked = true
        }

        rdG1.setOnCheckedChangeListener { radioGroup, i ->
            when(i) {
                R.id.radioBtn1 -> {
                    edit.clear()
                    edit.putInt("themeMode", R.id.radioBtn1)
                    edit.apply()
                }
                R.id.radioBtn2 -> {
                    edit.clear()
                    edit.putInt("themeMode", R.id.radioBtn2)
                    edit.apply()
                }
                R.id.radioBtn3 -> {
                    edit.clear()
                    edit.putInt("themeMode", R.id.radioBtn3)
                    edit.apply()
                }
            }
        }

        filBtn.setOnClickListener {
            startActivity(Intent(this, FilterActivity::class.java))
        }

        // 1000 = 1초 >> 1000*60*60*3 = 3시간 vvv 3 시간마다 데이터 패치 진행
        startService(Intent(applicationContext, MyService::class.java))

        var db = Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB")
            .allowMainThreadQueries().build()
        noticeList = db.noticeDao().getAll()
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
                dialog.show(supportFragmentManager, dialog.tag)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun fetchData(id: Int) {
        var db = Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB")
            .allowMainThreadQueries().build()
        // 일단 넣자
        // db.majorDao().select(id)

        CoroutineScope(Main).launch(Dispatchers.IO) {
            val fk = 1
            val doc: Document? =
                Jsoup.connect("https://www.kangwon.ac.kr/www/selectBbsNttList.do?bbsNo=37&key=1176")
                    .get()
            var contents: Elements
            if (doc != null) {
                contents = doc.select("table.bbs_default.list tbody tr")

                for (content in contents) {
                    // 링크
                    val url = "http://www.kangwon.ac.kr/www/" + content.select("td")[2].select("a")
                        .attr("href").substring(2)
                    // 제목
                    val title = content.select("td")[2].text()
                    // 첨부파일 유무 <td class="web_block"> </td> 의 size값에 따라 다르게 해줘야할 것 같음
                    // val extension = content.select("td")[4]
                    val extension = false;
                    // 날짜
                    val date = content.select("td")[5].text()

                    db.noticeDao().insert(Notice(fk, title, url, date, extension))
                    println(title)
                }
            }
        }

        db.close()
    }
}