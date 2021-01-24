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
    private var allList: List<String> = arrayListOf()
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

        var lastIds = shared.getString("lastIds", "")

        shared = getSharedPreferences("major", 0)
        var mutSet: MutableSet<String> = shared.all.keys
        var saveIds = StringBuffer("")
        allList = ArrayList(mutSet)

        for (sel in allList) {
            if (shared.all[sel] == true) {
                saveIds.append(db.majorDao().getMId(sel))
            }
        }

        if (beforeTime == null) {
            println("날짜 초기 저장")
            fetchExp(db)
            edit.putString("lastIds", saveIds.toString())
            edit.putString("lastUpdate", f.format(Date()).toString())
            edit.commit()
        } else {
            var beforeDate: Date = f.parse(beforeTime)
            var now: Date = f.parse(f.format(Date()))
            var diff = (now.time - beforeDate.time) / (1000 * 60 * 60)
            if (diff >= 1 || lastIds != saveIds.toString()) {
                println("패치 재실행")
                fetchExp(db)
                edit.putString("lastIds", saveIds.toString())
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
    }

    private fun fetchAdapter() {
        var db =
            Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB").allowMainThreadQueries()
                .build()

        var shared: SharedPreferences = getSharedPreferences("major", 0)
        var mutSet: MutableSet<String> = shared.all.keys
        selectedIds = arrayListOf()
        allList = ArrayList(mutSet)

        for (sel in allList) {
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
        allList = ArrayList(mutSet)
        val client = SoupClient(db, noticeAdapter)

        for (x in allList) if (shared.all[x] == true)
            client.fetchInfoList[db.majorDao().getMId(x) - 1].isSelect = true

        var count:Long = 0
        for (x in client.fetchInfoList) {
            if (x.isSelect) {
                count++
                client.fetchData(x.index, x.baseUrl, x.cutBaseUrlNumber, x.cutpatchUrlNumber)
            }
        }

        CoroutineScope(Main).launch {
            delay(count * 1500)
            fetchAdapter()
            progressBar.visibility = View.GONE
        }
    }
}