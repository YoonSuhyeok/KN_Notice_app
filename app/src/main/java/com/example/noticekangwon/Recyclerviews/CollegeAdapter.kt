package com.example.noticekangwon.Recyclerviews

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noticekangwon.DataBase.College
import com.example.noticekangwon.R

class CollegeAdapter(private val CollegeList: MutableList<College>, private val empty: MajorAdapter): RecyclerView.Adapter<CollegeAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_filter_college,
            parent,
            false
        )
        return Holder(view).apply{
            itemView.setOnClickListener {

                if(itemView.background == null) {
                    itemView.setBackgroundColor(Color.BLUE)
                    empty.plusFilterPatten(CollegeList[position].cId)
                } else if( (itemView.background as ColorDrawable).color == Color.BLUE ){
                    itemView.setBackgroundColor(Color.TRANSPARENT)
                    empty.minusFilterPatten(CollegeList[position].cId)
                } else{
                    itemView.setBackgroundColor(Color.BLUE)
                    empty.plusFilterPatten(CollegeList[position].cId)
                }
                empty.filter.filter("")
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.collegeText.text = CollegeList[position].cName
    }

    override fun getItemCount(): Int {
        return CollegeList.size
    }

    class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var collegeText: TextView = itemView.findViewById(R.id.CollegeName)
    }

}