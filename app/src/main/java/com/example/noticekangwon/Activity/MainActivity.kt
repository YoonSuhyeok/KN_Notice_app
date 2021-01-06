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
import com.example.noticekangwon.Recyclerviews.NoticeAdapter
import com.example.noticekangwon.Recyclerviews.RecyclerDecoration
import com.example.noticekangwon.Retrofit.RetrofitAPI
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {


    val noticeList: ArrayList<Notice> = arrayListOf<Notice>()
    var noticeAdapter = NoticeAdapter(noticeList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar:Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        actionBar?.title = "과제 정리 앱"

        recyclerview.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        recyclerview.setHasFixedSize(true)
        recyclerview.adapter = noticeAdapter
        val spaceDecoration = RecyclerDecoration(0)
        recyclerview.addItemDecoration(spaceDecoration)
        sendRequest()
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

    private fun initDB() {
        var colleageList: Array<String> = resources.getStringArray(R.array.college)
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

//    public void moveDevelop(View view) {
//        startActivity(new Intent(this, DevelopInfoActivity.class));
//    }
}