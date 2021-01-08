package com.example.noticekangwon.Recyclerviews

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noticekangwon.DataBase.College
import com.example.noticekangwon.R

class CollegeAdapter(private val CollegeList: MutableList<College>, val empty: MajorAdapter): RecyclerView.Adapter<CollegeAdapter.Holder>(){

    var 

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_filter_college, parent, false)
        return Holder(view).apply{
            itemView.setOnClickListener {
                itemView.setBackgroundColor(Color.BLUE)
                empty.filter.filter(CollegeList[position].cId.toString())
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.collegeText.text = CollegeList[position].cName
//        if(itemClick != null){
//            holder?.itemView?.setOnClickListener { v ->
//                itemClick?.onClick(v, position)
//            }
//        }
    }

    override fun getItemCount(): Int {
        return CollegeList.size
    }

    class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var collegeText: TextView = itemView.findViewById(R.id.CollegeName)
    }

}