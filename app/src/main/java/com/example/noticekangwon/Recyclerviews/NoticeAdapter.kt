package com.example.noticekangwon.Recyclerviews

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.noticekangwon.DataBase.Notice
import com.example.noticekangwon.R
import kotlinx.android.synthetic.main.list_item_notice.view.*
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NoticeAdapter(private var NoticeList: List<Notice>, private val subject: String): RecyclerView.Adapter<NoticeAdapter.Holder>(), Filterable {

    private var unFilList = NoticeList
    private var filList : List<Notice> = arrayListOf<Notice>()
    private val today = SimpleDateFormat("yyyy.MM.dd",Locale.KOREA).format(Calendar.getInstance().time)

    // 화면을 최초 로딩하여 만들어진 View가 없는 경우, xml 파일을 inflate하여 ViewHolder를 생성한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_notice, parent, false)
        return Holder(view).apply {
            itemView.ImageButton.setOnClickListener {
                // getBindingAdapterPosition
                val curPos: Int = adapterPosition
                // Move to the URI_Site
                val url:String = NoticeList[curPos].mUrl.substring(7, NoticeList[curPos].mUrl.length)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://"+url))
                parent.context.startActivity(intent)
            }

        }
    }

     override fun getItemCount(): Int {
        return filList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if(filList[position].mDate == today) {
            holder.newTag.visibility = View.VISIBLE
        } else {
            holder.newTag.visibility = View.INVISIBLE
        }
        holder.noticeSubject.text = subject
        holder.noticeTitle.text = filList[position].mTitle
        holder.date.text = filList[position].mDate
        holder.noticeTitle.isSelected = true
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(str: CharSequence?): FilterResults {
                val searchStr = str.toString()
                filList = if(searchStr.isEmpty()) {
                    unFilList
                } else {
                    var filteringList = ArrayList<Notice>()
                    for(item in unFilList) {
                        if(item.mTitle.contains(searchStr, true)) filteringList.add(item)
                    }
                    filteringList
                }
                val result = FilterResults()
                result.values = filList
                return result
            }

            override fun publishResults(str: CharSequence?, result: FilterResults?) {
                filList = result?.values as List<Notice>
                notifyDataSetChanged()
            }
        }
    }

    fun changeList(list: List<Notice>){
        unFilList = list
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val newTag       : TextView = itemView.new_tag
        val noticeTitle   : TextView = itemView.Title
        val noticeSubject: TextView = itemView.department
        val date         : TextView = itemView.date
    }

}