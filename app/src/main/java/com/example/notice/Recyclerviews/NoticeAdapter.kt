package com.example.notice.Recyclerviews

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.notice.dialog.LongClickMenu
import com.example.notice.dataBase.Notice
import com.example.notice.R
import com.example.notice.Recyclerviews.Something.Companion.nameList
import com.example.notice.activity.MainActivity
import com.example.notice.defaultClass.NoticeNameId
import kotlinx.android.synthetic.main.list_item_notice.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NoticeAdapter(
    context: Context,
    private var NoticeList: List<Notice>
) : RecyclerView.Adapter<NoticeAdapter.Holder>(), Filterable {
    private lateinit var filters: ArrayList<NoticeNameId>

    constructor(context: Context, NoticeList: List<Notice>, filter: ArrayList<NoticeNameId>) : this(context, NoticeList) {
        filters = filter
    }

    private var unFilList = NoticeList
    private var filList: List<Notice> = NoticeList
    private val today = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(Calendar.getInstance().time)
    private val longClick = LongClickMenu(context, this)
    // 화면을 최초 로딩하여 만들어진 View 가 없는 경우, xml 파일을 inflate 하여 ViewHolder 를 생성한다.
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_notice, parent, false)
        return Holder(view).apply {
            itemView.setOnClickListener {
                // getBindingAdapterPosition
                val curPos: Int = adapterPosition
                // Move to the URI_Site
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(filList[curPos].mUrl))
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                parent.context.startActivity(intent)
            }

            itemView.setOnLongClickListener{
                val curPos: Int = adapterPosition
                longClick.callFun(filList[curPos].mTitle)
                true
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // Animation 넣는다면 -> holder.itemView.animation = AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation)

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

        holder.noticeSubject.text = nameList[filList[position].mIdFk-1]
        holder.noticeTitle.text = filList[position].mTitle
        holder.date.text = filList[position].mDate
        holder.noticeTitle.isSelected = true
    }

    override fun getItemCount(): Int {
        return filList.size
    }

    fun getSelectIds(): List<Int> {
        return filters[MainActivity.position].id
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(str: CharSequence?): FilterResults {
                val searchStr = str.toString()
                filList = if (searchStr.isEmpty()) {
                    unFilList
                } else {
                    val filteringList = ArrayList<Notice>()
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

    fun changeList(lists: List<Notice>){
        filList = lists
        notifyDataSetChanged()
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newTag: TextView = itemView.new_tag
        val pinTag: TextView = itemView.pin_tag
        val bookTag: TextView = itemView.book_tag
        val noticeTitle: TextView = itemView.Title
        val noticeSubject: TextView = itemView.department
        val date: TextView = itemView.date
    }

}