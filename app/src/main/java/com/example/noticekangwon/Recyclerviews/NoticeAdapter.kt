package com.example.noticekangwon.Recyclerviews

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noticekangwon.Activity.CustomDialog
import com.example.noticekangwon.Activity.LongClickMenu
import com.example.noticekangwon.Activity.MainActivity
import com.example.noticekangwon.DataBase.Notice
import com.example.noticekangwon.R
import com.example.noticekangwon.Recyclerviews.something.Companion.nameList
import kotlinx.android.synthetic.main.list_item_notice.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NoticeAdapter(private var context: Context, private var NoticeList: List<Notice>) : RecyclerView.Adapter<NoticeAdapter.Holder>(), Filterable {

    private var unFilList = NoticeList
    private var filList: List<Notice> = NoticeList
    private val today =
        SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(Calendar.getInstance().time)

    // 화면을 최초 로딩하여 만들어진 View가 없는 경우, xml 파일을 inflate하여 ViewHolder를 생성한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_notice, parent, false)
        return Holder(view).apply {
            itemView.setOnClickListener {
                // getBindingAdapterPosition
                val curPos: Int = adapterPosition
                // Move to the URI_Site
                val url: String =
                    NoticeList[curPos].mUrl.substring(7, NoticeList[curPos].mUrl.length)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://" + url))
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                parent.context.startActivity(intent)
            }

            /* itemView.setOnLongClickListener(OnLongClickListener {
            찾아본 결과 카카오톡으로 나에게 메시지 보내는 기능은 api key를 사용해야한다는 것
                true //true 설정
            }) */

            itemView.setOnLongClickListener(View.OnLongClickListener() {
                var longClick = LongClickMenu(context)
                println(filList[adapterPosition].mTitle)
                longClick.callFun(filList[adapterPosition].mTitle)
                true
            })
        }
    }



    override fun getItemCount(): Int {
        return filList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (filList[position].mDate == today) {
            holder.newTag.visibility = VISIBLE
        } else {
            holder.newTag.visibility = GONE
        }

        if(filList[position].isPin == 0) {
            holder.pinTag.visibility = VISIBLE
        } else {
            holder.pinTag.visibility = GONE
        }

        if(filList[position].isBookmark) {
            holder.bookTag.visibility = VISIBLE
        } else {
            holder.bookTag.visibility = INVISIBLE
        }

        holder.noticeSubject.text = nameList[filList[position].mIdFk]
        holder.noticeTitle.text = filList[position].mTitle
        holder.date.text = filList[position].mDate
        holder.noticeTitle.isSelected = true
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(str: CharSequence?): FilterResults {
                val searchStr = str.toString()
                filList = if (searchStr.isEmpty()) {
                    unFilList
                } else {
                    var filteringList = ArrayList<Notice>()
                    for (item in unFilList) {
                        if (item.mTitle.contains(searchStr, true)) filteringList.add(item)
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

    fun changeList(list: List<Notice>) {
        unFilList = list
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newTag: TextView = itemView.new_tag
        val pinTag: TextView = itemView.pin_tag
        val bookTag: TextView = itemView.book_tag
        val noticeTitle: TextView = itemView.Title
        val noticeSubject: TextView = itemView.department
        val date: TextView = itemView.date
    }

}