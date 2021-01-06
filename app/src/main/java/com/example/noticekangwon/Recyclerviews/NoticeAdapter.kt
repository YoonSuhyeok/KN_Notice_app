package com.example.noticekangwon.Recyclerviews

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noticekangwon.DataBase.Notice
import com.example.noticekangwon.R

class NoticeAdapter(private val NoticeList: List<Notice>, private val subject: String): RecyclerView.Adapter<NoticeAdapter.Holder>() {
    // 화면을 최초 로딩하여 만들어진 View가 없는 경우, xml 파일을 inflate하여 ViewHolder를 생성한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_notice, parent, false)
        return Holder(view).apply {
            itemView.setOnClickListener {
                val curPos: Int = adapterPosition
                // Move to the URI_Site
                val url:String = NoticeList[curPos].mUrl.substring(6, NoticeList[curPos].mUrl.length-1)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http"+url))
                parent.context.startActivity(intent)
            }

        }
    }
     override fun getItemCount(): Int {
        return NoticeList.size
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.noticeSubject.text = subject
        holder.noticeText.text = NoticeList[position].mTitle
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var noticeText: TextView = itemView.findViewById(R.id.Title)
        var noticeSubject: TextView = itemView.findViewById(R.id.department)
    }

}