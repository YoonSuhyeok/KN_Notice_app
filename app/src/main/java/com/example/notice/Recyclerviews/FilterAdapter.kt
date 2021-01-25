package com.example.notice.Recyclerviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notice.R
import kotlinx.android.synthetic.main.list_item_filter.view.*

class FilterAdapter(private var NoticeList: List<String>) : RecyclerView.Adapter<FilterAdapter.Holder>() {

    private var filterList = NoticeList

    // 화면을 최초 로딩하여 만들어진 View 가 없는 경우, xml 파일을 inflate 하여 ViewHolder 를 생성한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_filter, parent, false)
        return Holder(view).apply {
        }
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // Animation 넣는다면 -> holder.itemView.animation = AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation)
        holder.filName.text = filterList[position]
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filName: TextView = itemView.filterName
    }

}