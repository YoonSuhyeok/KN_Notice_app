package com.example.notice.Recyclerviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
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

        if(filterList[position].isClicked){
            holder.filName.backgroundTintList = ContextCompat.getColorStateList(context, R.color.rdBtnColor)
        } else {
            holder.filName.backgroundTintList = ContextCompat.getColorStateList(context, R.color.myGray)
        }

        holder.itemView.setOnClickListener{
            val db = Room.databaseBuilder(context, AppDataBase::class.java, "Major-DB").allowMainThreadQueries().build()
            noticeAdapter.changeList(db.noticeDao().getFil(filterList[position].id))
            MainActivity.position = position

            // 검사
            var isSelected = false
            for(x in filterList){
                if(x.isClicked){
                    isSelected = true
                    break
                }
            }
            //선택이 아무것도 안 되어있으면 전체를 참으로 만들고 색을 변경시켜준다.
           if(!isSelected){
               filterList[0].isClicked = true
           } else { // 하나 이상이 선택되어있다면 모두 "false"로 만든다.
               for(x in filterList){
                   x.isClicked = false
               }
               // 현재 자신이 선택한 것을 색을 변경해준다.
               holder.filName.backgroundTintList = ContextCompat.getColorStateList(context, R.color.subCollege)
               filterList[position].isClicked = true
           }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filName: TextView = itemView.filterName
    }

}