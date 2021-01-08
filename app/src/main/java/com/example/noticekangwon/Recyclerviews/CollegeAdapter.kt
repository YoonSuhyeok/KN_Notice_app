package com.example.noticekangwon.Recyclerviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noticekangwon.R

class CollegeAdapter(private val CollegeList: ArrayList<String>): RecyclerView.Adapter<CollegeAdapter.Holder>(){
    interface ItemClick{
        fun onClick(view: View, position: Int)
    }
    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_filter_college, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.collegeText.text = CollegeList[position]
        if(itemClick != null){
            holder?.itemView?.setOnClickListener { v ->
                itemClick?.onClick(v, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return CollegeList.size
    }

    class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var collegeText: TextView = itemView.findViewById(R.id.CollegeName)
    }

}