package com.example.noticekangwon

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class NoticeAdapter(var NoticeList: ArrayList<Notice>): RecyclerView.Adapter<NoticeAdapter.Holder>() {
    // 화면을 최초 로딩하여 만들어진 View가 없는 경우, xml 파일을 inflate하여 ViewHolder를 생성한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_notice, parent, false)
        return Holder(view).apply {
            itemView.setOnClickListener {
                val curPos: Int = adapterPosition
                // Move to the URI_Site
                Toast.makeText(parent.context, "클릭 테스트 ${curPos}", Toast.LENGTH_LONG).show()
            }

        }
    }
     override fun getItemCount(): Int {
        return NoticeList.size
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.noticeSubject.text = NoticeList.get(position).name
        holder.noticeText.text = NoticeList.get(position).title
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val noticeText = itemView.findViewById<TextView>(R.id.noticeTextView)
        val noticeSubject = itemView.findViewById<TextView>(R.id.subjectName)
    }

}