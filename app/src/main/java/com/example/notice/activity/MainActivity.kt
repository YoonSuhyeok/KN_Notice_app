package com.example.notice.activity

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.notice.*
import com.example.notice.Jsoup.SoupClient
import com.example.notice.Recyclerviews.FilterAdapter
import com.example.notice.Recyclerviews.NoticeAdapter
import com.example.notice.Recyclerviews.RecyclerDecoration
import com.example.notice.dataBase.AppDataBase
import com.example.notice.dataBase.Notice
import com.example.notice.defaultClass.NoticeNameId
import com.example.notice.dialog.CustomDialog
import com.example.notice.dialog.LoadingDialog
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

    private var filters: ArrayList<NoticeNameId> = arrayListOf()
    private var noticeList: List<Notice> = arrayListOf()
    private var allList: List<String> = arrayListOf()
    private var selectedIds: ArrayList<Int> = arrayListOf()
    private var noticeAdapter: NoticeAdapter = NoticeAdapter(this, noticeList, filters)
    private var filterAdapter: FilterAdapter = FilterAdapter(filters, noticeAdapter)
    //Initialize Loader
    private val loadingDialogFragment by lazy { LoadingDialog() }

    companion object {
        var position = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent.getBooleanExtra("UpdateFilter", false)) overridePendingTransition(
            R.anim.center_to_right,
            R.anim.none
        )
        else overridePendingTransition(R.anim.horizon_enter, R.anim.none)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        val db = Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB").allowMainThreadQueries().build()

        recyclerview.setHasFixedSize(true)
        recyclerview.addItemDecoration(RecyclerDecoration(0))
        recyclerview.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)

        filterRecycle.setHasFixedSize(true)
        filterRecycle.addItemDecoration(RecyclerDecoration(0))
        filterRecycle.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)


        var shared = getSharedPreferences("updateDate", 0)
        val edit: SharedPreferences.Editor = shared.edit()
        val f = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.KOREA)
        val beforeTime = shared.getString("lastUpdate", null)

        val lastIds = shared.getString("lastIds", "")

        shared = getSharedPreferences("major", 0)
        val mutSet: MutableSet<String> = shared.all.keys
        val saveIds = StringBuffer("")
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
            edit.apply()
        } else {
            val beforeDate: Date = f.parse(beforeTime)
            val now: Date = f.parse(f.format(Date()))
            val diff = (now.time - beforeDate.time) / (1000 * 60 * 60)
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

        fetchAdapter()
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
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLScb-WfexuBzW8dnyYME1L_5maBrtozwQT6XdCXp9iTez-Z4og/viewform?usp=pp_url")
                    )
                )
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
        val db = Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB").allowMainThreadQueries().build()
        val shared: SharedPreferences = getSharedPreferences("major", 0)
        val mutSet: MutableSet<String> = shared.all.keys
        selectedIds = arrayListOf()
        allList = ArrayList(mutSet)
        val filters = ArrayList<NoticeNameId>()
        
        println("시작")
        for (sel in allList) {
            if (shared.all[sel] == true) {
                println(sel)
                val num = db.majorDao().getMId(sel)
                filters.add(NoticeNameId(sel, arrayListOf(num), false))
                selectedIds.add(num)
            }
        }
        filters.add(0, NoticeNameId("전체", selectedIds, true))

        noticeList = db.noticeDao().getFil(selectedIds)

        db.close()

        noticeAdapter = NoticeAdapter(this, noticeList, filters)

        filterAdapter = FilterAdapter(filters, noticeAdapter)

        filterRecycle.adapter = filterAdapter
        recyclerview.adapter = noticeAdapter

        noticeAdapter.filter.filter("")
    }

    private fun fetchExp(db: AppDataBase) {
        val shared: SharedPreferences = getSharedPreferences("major", 0)
        val mutSet: MutableSet<String> = shared.all.keys

        selectedIds = arrayListOf()
        allList = ArrayList(mutSet)
        val client = SoupClient(db)

        for (x in allList) if (shared.all[x] == true)
            client.fetchInfoList[db.majorDao().getMId(x) - 1].isSelect = true

        var count:Long = 0
        for (x in client.fetchInfoList) {
            if (x.isSelect) {
                count++
                client.fetchData(x.index, x.baseUrl, x.cutBaseUrlNumber, x.cutFetchUrlNumber)
            }
        }
        // show Loader
        showProgressDialog()
        CoroutineScope(Main).launch {
            delay(count * 1000)
            fetchAdapter()
            //Hide Loader
            hideProgressDialog()
            noticeAdapter.notifyDataSetChanged()

        }
    }

    private fun showProgressDialog() {
        if (!loadingDialogFragment.isAdded) {
            loadingDialogFragment.show(supportFragmentManager, "loader")
        }
    }

    private fun hideProgressDialog() {
        if (loadingDialogFragment.isAdded) {
            loadingDialogFragment.dismissAllowingStateLoss()
        }
    }
}