package com.example.noticekangwon.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.noticekangwon.*
import com.example.noticekangwon.DataBase.AppDataBase
import com.example.noticekangwon.DataBase.Notice
import com.example.noticekangwon.Jsoup.SoupClient
import com.example.noticekangwon.Recyclerviews.NoticeAdapter
import com.example.noticekangwon.Recyclerviews.RecyclerDecoration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_custom.*
import kotlinx.android.synthetic.main.longclick_dialog_menus.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var noticeList: List<Notice> = arrayListOf()
    private var selectedList: List<String> = arrayListOf()
    private var selectedIds: ArrayList<Int> = arrayListOf()
    private var noticeAdapter: NoticeAdapter = NoticeAdapter(this, noticeList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent.getBooleanExtra("UpdateFilter", false)) {
            overridePendingTransition(R.anim.center_to_right, R.anim.none)
        } else {
            overridePendingTransition(R.anim.horizon_enter, R.anim.none)
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        val anim = AnimationUtils.loadAnimation(this@MainActivity, R.anim.load)
        // progressBar.animation = anim
        progressBar.visibility = View.GONE

        var db =
            Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB").allowMainThreadQueries()
                .build()

        recyclerview.setHasFixedSize(true)
        recyclerview.addItemDecoration(RecyclerDecoration(0))
        recyclerview.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)

        var shared = getSharedPreferences("updateDate", 0)
        var edit: SharedPreferences.Editor = shared.edit()
        var f = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.KOREA)
        var beforeTime = shared.getString("lastUpdate", null)

        if (beforeTime == null) {
            println("날짜 초기 저장")
            fetchExp(db)
            edit.putString("lastUpdate", f.format(Date()).toString())
            edit.commit()
        } else {
            var beforeDate: Date = f.parse(beforeTime)
            var now: Date = f.parse(f.format(Date()))
            var diff = (now.time - beforeDate.time) / (1000 * 60 * 60)
            if (diff >= 1) {
                println("패치 재실행")
                fetchExp(db)
                edit.putString("lastUpdate", f.format(Date()).toString())
                edit.commit()
            }
        }

        filBtn.setOnClickListener {
            startActivity(Intent(this, FilterActivity::class.java))
        }

        if (intent.getBooleanExtra("UpdateFilter", false)) {
            fetchExp(db)
            // 이전 필터 값이랑 이후 필터값이랑 동일한지 비교하는 것이 필요할 것 같음
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
            R.id.question -> {
                true
            }
            R.id.bookmark -> {
                startActivity(Intent(this, BookmarkActivity::class.java))
                true
            }
            R.id.switchBtn -> {
                val dialog = CustomDialog.CustomDialogBuilder()
                    .setTitle("테마", this)
                    .create()
                dialog.show(supportFragmentManager, dialog.tag)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun searchList(view: View) {
        noticeAdapter.filter.filter(search.text)
    }

    override fun onResume() {
        super.onResume()
        fetchAdapter()
        if (search.text.toString() == "")
            noticeAdapter.filter.filter("")
    }

    private fun fetchAdapter() {
        var db =
            Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB").allowMainThreadQueries()
                .build()

        var shared: SharedPreferences = getSharedPreferences("major", 0)
        var mutSet: MutableSet<String> = shared.all.keys
        selectedIds = arrayListOf()
        selectedList = ArrayList(mutSet)

        for (sel in selectedList) {
            if (shared.all[sel] == true) {
                selectedIds.add(db.majorDao().getMId(sel))
            }
        }

        // progressBar.visibility = View.GONE

        noticeList = db.noticeDao().getFil(selectedIds)

        db.close()

        noticeAdapter = NoticeAdapter(this, noticeList)

        recyclerview.adapter = noticeAdapter

        noticeAdapter.filter.filter("")
    }

    private fun fetchExp(db: AppDataBase) {
        var shared: SharedPreferences = getSharedPreferences("major", 0)
        var mutSet: MutableSet<String> = shared.all.keys
        progressBar.visibility = View.VISIBLE
        selectedIds = arrayListOf()
        selectedList = ArrayList(mutSet)
        val client = SoupClient(db, noticeAdapter)

        for (x in selectedList) if (shared.all[x] == true)
            client.fetchInfoList[db.majorDao().getMId(x) - 1].isSelect = true

        for (x in client.fetchInfoList) {
            if (x.isSelect) {
                client.fetchData(x.index, x.baseUrl, x.cutBaseUrlNumber, x.cutpatchUrlNumber)
            }
        }

        fetchAdapter()
        progressBar.visibility = View.GONE
    }
}