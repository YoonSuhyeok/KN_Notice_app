package com.example.notice.Recyclerviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.notice.R
import com.example.notice.activity.MainActivity
import com.example.notice.dataBase.AppDataBase
import com.example.notice.defaultClass.NoticeNameId
import kotlinx.android.synthetic.main.list_item_filter.view.*

class FilterAdapter(noticeList: ArrayList<NoticeNameId>, private val noticeAdapter: NoticeAdapter) : RecyclerView.Adapter<FilterAdapter.Holder>() {

    private var filterList = noticeList
    private lateinit var context:Context
    private var initNum = 0

    // 화면을 최초 로딩하여 만들어진 View 가 없는 경우, xml 파일을 inflate 하여 ViewHolder 를 생성한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_filter, parent, false)
        if(initNum==0){
            context = parent.context
            initNum++
        }
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.filName.text = filterList[position].name

        holder.itemView.setOnClickListener {
            val db = Room.databaseBuilder(context, AppDataBase::class.java, "Major-DB").allowMainThreadQueries().build()
            noticeAdapter.changeList(db.noticeDao().getFil(filterList[position].id))
            MainActivity.position = position
        }
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filName: TextView = itemView.filterName
    }

}