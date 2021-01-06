package com.example.noticekangwon.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.example.noticekangwon.Retrofit.RetrofitAPI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var noticeList: List<Notice> = arrayListOf<Notice>()
//    var noticeAdapter = NoticeAdapter(noticeList, "학사 공지")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar:Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        actionBar?.title = "과제 정리 앱"

        initDB()
        filterbutton.setOnClickListener {
            startActivity(Intent(this, FilterActivity::class.java))
        }

        fetchData(0)

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
        if(temp == R.id.developInfo) {
            startActivity(Intent(this, DevelopInfoActivity::class.java))
        } else {

        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendRequest(){
        noticeList.clear()
        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl("https://thawing-wave-08101.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit: Retrofit = builder.build()
        val client = retrofit.create(RetrofitAPI::class.java)
        val call = client.getNotice()
        call.enqueue(object: Callback<Notice>{
            override fun onFailure(call: Call<Notice>, t: Throwable) {}

            override fun onResponse(call: Call<Notice>, response: Response<Notice>) {
                val repos = response.body()

                noticeAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun initDB(){
        var db = Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB").allowMainThreadQueries().build()
        var majorlist = resources.getStringArray(R.array.major)
        var colleaelist = resources.getStringArray(R.array.colleage)
        for (element in majorlist) db.majorDao().insert(Major(element))
        for (element in colleaelist) db.collegeDao().insert(College(element))
    }

    fun fetchData(id: Int) {
        var db = Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB")
            .allowMainThreadQueries().build()
        // 일단 넣자
        // db.majorDao().select(id)

        CoroutineScope(Main).launch(Dispatchers.IO){
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