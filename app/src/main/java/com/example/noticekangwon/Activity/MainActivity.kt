package com.example.noticekangwon.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.noticekangwon.*
import com.example.noticekangwon.DataBase.MajorDB.AppDataBase
import com.example.noticekangwon.DataBase.MajorDB.College
import com.example.noticekangwon.DataBase.MajorDB.Major
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

        recyclerview.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        recyclerview.setHasFixedSize(true)
        recyclerview.adapter = noticeAdapter
        val spaceDecoration = RecyclerDecoration(0)
        recyclerview.addItemDecoration(spaceDecoration)
        sendRequest()
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
        var db = Room.databaseBuilder(this, AppDataBase::class.java, "Major-DB").allowMainThreadQueries().build();
        var majorlist = resources.getStringArray(R.array.major)
        var colleaelist = resources.getStringArray(R.array.colleage)
        for (element in majorlist) db.majorDao().insert(Major(element))
        for (element in colleaelist) db.collegeDao().insert(College(element))
    }

//    public void moveDevelop(View view) {
//        startActivity(new Intent(this, DevelopInfoActivity.class));
//    }
}