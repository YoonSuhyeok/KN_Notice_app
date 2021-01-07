package com.example.noticekangwon.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var menu: Menu
    var noticeList: List<Notice> = arrayListOf<Notice>()
//    var noticeAdapter = NoticeAdapter(noticeList, "학사 공지")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        actionBar?.title = "과제 정리 앱"

        initDB()
        filterbutton.setOnClickListener {
            startActivity(Intent(this, FilterActivity::class.java))
        }

//        fetchData(0)

        var db = Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB")
            .allowMainThreadQueries().build()
        noticeList = db.noticeDao().getAll()
        db.close()

        var noticeAdapter = NoticeAdapter(noticeList, "학사공지")

        recyclerview.layoutManager = LinearLayoutManager(
            this@MainActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerview.setHasFixedSize(true)
        recyclerview.adapter = noticeAdapter
        val spaceDecoration = RecyclerDecoration(0)
        recyclerview.addItemDecoration(spaceDecoration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var temp = item.itemId
        if (temp == R.id.developInfo) {
            startActivity(Intent(this, DevelopInfoActivity::class.java))
        } else if(temp == R.id.switchBtn) {
            if(item.title == "다크 모드") {
                updateMenuTitle("화이트 모드")
                ThemeSet.applyTheme("dark")
            } else {
                updateMenuTitle("다크 모드")
                ThemeSet.applyTheme("light")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun updateMenuTitle(mode: String) {
        var mItem: MenuItem = menu.findItem(R.id.switchBtn)
        if(mode == "다크 모드") {
            mItem.title = "화이트 모드"
        } else {
            mItem.title = "다크 모드"
        }

    }

    private fun updateList() {
        TODO("Not yet implemented")
    }

    private fun insertNotice(Title: String, Url: String, Date: String) {

    }

    private fun initDB() {
        var colleageList: Array<String> = resources.getStringArray(R.array.colleage)
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
        for (x in colleageList.indices) {
            db.collegeDao().insert(College(colleageList[x]))
            println(colleageList[x])
            num = array[x]
            var majorlist = resources.getStringArray(num)
            for (y in majorlist) {
                db.majorDao().insert(Major(x, y))
            }
        }
    }

//    fun fetchData(id: Int) {
//        var db = Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB")
//            .allowMainThreadQueries().build()
//        // 일단 넣자
//        // db.majorDao().select(id)
//
//        CoroutineScope(Main).launch(Dispatchers.IO){
//            val fk = 1
//            val doc: Document? =
//                Jsoup.connect("https://www.kangwon.ac.kr/www/selectBbsNttList.do?bbsNo=37&key=1176")
//                    .get()
//            var contents: Elements
//            if (doc != null) {
//                contents = doc.select("table.bbs_default.list tbody tr")
//
//                for (content in contents) {
//                    // 링크
//                    val url = "http://www.kangwon.ac.kr/www/" + content.select("td")[2].select("a")
//                        .attr("href").substring(2)
//                    // 제목
//                    val title = content.select("td")[2].text()
//                    // 첨부파일 유무 <td class="web_block"> </td> 의 size값에 따라 다르게 해줘야할 것 같음
//                    // val extension = content.select("td")[4]
//                    val extension = false;
//                    // 날짜
//                    val date = content.select("td")[5].text()
//
//                    db.noticeDao().insert(Notice(fk, title, url, date, extension))
//                    println(title)
//                }
//            }
//        }
//
//        db.close()
//    }

}