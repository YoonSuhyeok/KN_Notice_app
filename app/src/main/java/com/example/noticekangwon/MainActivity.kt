package com.example.noticekangwon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    // 중복 제거 필요하지 않나?
    val NoticeList: ArrayList<Notice> = arrayListOf<Notice>()
    var noticeAdapter = NoticeAdapter(NoticeList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.setting_button).setOnClickListener {
            startActivity(Intent(this, settingpage::class.java))
        }

        findViewById<Button>(R.id.Loding).setOnClickListener {
            sendRequest()
        }

        NoticeList.add(0, Notice("이름", "제목", "URI", "date"))
        recyclerview.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        recyclerview.setHasFixedSize(true)
        recyclerview.adapter = noticeAdapter
        val spaceDecoration = RecyclerDecoration(0)
        recyclerview.addItemDecoration(spaceDecoration)
        sendRequest()
    }

    private fun sendRequest(){
        NoticeList.clear()
        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl("https://thawing-wave-08101.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit: Retrofit = builder.build()
        val client = retrofit.create(RetrofitAPI::class.java)
        val call = client.getNotice()
        call.enqueue(object: Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {}

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val repos = response.body()

                CheckPreference(repos)
                noticeAdapter.notifyDataSetChanged()

            }
        })
    }

    private fun CheckPreference(repos: JsonObject?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val isKangwon = prefs.getBoolean("강원대 학사 공지", false)
        val isComputer = prefs.getBoolean("강원대 컴공학부", false)
        val isEdu = prefs.getBoolean("강원대 교육혁신원", false)
        val isSW= prefs.getBoolean("강원대 SW중심대학", false)

        if(isKangwon) addNotice("강원대 학사 공지", repos)
        if(isComputer) addNotice("강원대 컴공학부", repos)
        if(isEdu) addNotice("강원대 SW중심대학", repos)
        if(isSW) addNotice("강원대 교육혁신원", repos)
        /*
        if(isKangwon!!) NoticeList += NoticeList.filter{ it.name == "강원대 학사 공지" } as ArrayList<Notice>
        if(isComputer!!)NoticeList += NoticeList.filter{ it.name == "강원대 컴공학부" } as ArrayList<Notice>
        if(isEdu!!)NoticeList += NoticeList.filter{ it.name == "강원대 교육혁신원" } as ArrayList<Notice>
        if(isSW!!)NoticeList += NoticeList.filter{ it.name == "강원대 SW중심대학" } as ArrayList<Notice>
        */
    }

    private fun addNotice(name: String, repos: JsonObject?){
        var lists = repos?.getAsJsonArray(name)
        if(lists != null){
            for( i in 0 until lists.size()){
                val a: JsonObject = lists.get(i) as JsonObject
                NoticeList.add( Notice(name, a.get("title").toString(), a.get("url").toString(), a.get("date").toString() ) )
            }
        }
    }
}