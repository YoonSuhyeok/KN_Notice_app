package com.example.noticekangwon.Activity

import android.content.BroadcastReceiver
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
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var noticeList: List<Notice> = arrayListOf()
    private var selectedList: List<Integer> = arrayListOf()
    private lateinit var noticeAdapter: NoticeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Extension KNU Notice"

        var db = Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB").allowMainThreadQueries().build()

        recyclerview.setHasFixedSize(true)
        val spaceDecoration = RecyclerDecoration(0)
        recyclerview.addItemDecoration(spaceDecoration)

        recyclerview.layoutManager = LinearLayoutManager(
            this@MainActivity,
            LinearLayoutManager.VERTICAL,
            false
        )

        var shared: SharedPreferences = getSharedPreferences("updateDate", 0)
        var edit: SharedPreferences.Editor = shared.edit()
        var f: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.KOREA)
        var beforeTime = shared.getString("lastUpdate", null)
        if(beforeTime == null) {
            println("날짜 초기 저장")
            progressBar.visibility = View.VISIBLE
            fetchData(db, 0)
            edit.putString("lastUpdate", f.format(Date()).toString())
            edit.commit()
        } else {
            var beforeDate: Date = f.parse(beforeTime)
            var now: Date = f.parse(f.format(Date()))
            var diff = (now.time - beforeDate.time)/(1000*60*60)
            if(diff >= 1) {
                println("패치 재실행")
                progressBar.visibility = View.VISIBLE
                fetchData(db, 0)
                edit.putString("lastUpdate", f.format(Date()).toString())
                edit.commit()
            }
        }

        filBtn.setOnClickListener {
            startActivity(Intent(this, FilterActivity::class.java))
        }
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
                val dialog = CustomDialog.CustomDialogBuilder()
                    .setTitle("모드 설정", this)
                    .create()
                dialog.show(supportFragmentManager, dialog.tag)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun fetchData(appdatabse: AppDataBase, id: Int) {

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

                    appdatabse.noticeDao().insert(Notice(fk, title, url, date, extension))
                    println(title)
                }
            }
            CoroutineScope(Main).launch {
                progressBar.visibility = View.GONE
                noticeList = appdatabse.noticeDao().all
                // ?: DB 내에서 정렬하여 나오는 방법은 없나?
                if(noticeList.isNotEmpty())
                    noticeList = noticeList.sortedByDescending{ it -> it.mDate }
                // 1000 = 1초 >> 1000*60*60*3 = 3시간 vvv 3 시간마다 데이터 패치 진행

                appdatabse.close()

                noticeAdapter = NoticeAdapter(noticeList, "학사공지")

                recyclerview.adapter = noticeAdapter

                noticeAdapter.filter.filter("")
            }
        }
    }

    fun searchList(view: View) {
        noticeAdapter.filter.filter(search.text)
    }
}