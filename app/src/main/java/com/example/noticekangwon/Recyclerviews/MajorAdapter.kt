package com.example.noticekangwon.Recyclerviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noticekangwon.R

class MajorAdapter(private val MajorList: ArrayList<String>): RecyclerView.Adapter<MajorAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_filter_major, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.majorName.text = MajorList[position]
    }

    override fun getItemCount(): Int {
        return MajorList.size
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var majorName: TextView = itemView.findViewById(R.id.MajorName)
    }

}