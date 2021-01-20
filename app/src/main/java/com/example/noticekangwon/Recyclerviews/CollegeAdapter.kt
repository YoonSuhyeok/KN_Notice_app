package com.example.noticekangwon.Recyclerviews

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noticekangwon.DataBase.College
import com.example.noticekangwon.DataBase.Major
import com.example.noticekangwon.R
import kotlinx.android.synthetic.main.list_item_filter_college.view.*

class CollegeAdapter(
    private val collegeList: MutableList<College>, private val majorList: MutableList<Major>,
    private val majorAdapter: MajorAdapter, private val isSelectList: MutableMap<String, Boolean>
) : RecyclerView.Adapter<CollegeAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_filter_college, parent, false)
        println("onCreateViewHolder")
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        println("onBindViewHolder")
        holder.collegeText.text = collegeList[position].cName
        val name = "$position ${collegeList[position].cName}"
        if (isSelectList[name] == true) {
            holder.collegeText.setBackgroundColor(Color.BLUE)
            majorAdapter.plusFilterPatten(collegeList[position].cId)
        } else {
            holder.collegeText.setBackgroundColor(Color.TRANSPARENT)
            majorAdapter.minusFilterPatten(collegeList[position].cId)
        }
        holder.itemView.setOnClickListener {
            if (isSelectList[name] == false) {
                holder.collegeText.setBackgroundColor(Color.BLUE)
                isSelectList[name] = true
                majorAdapter.plusFilterPatten(collegeList[position].cId)
            } else {
                holder.collegeText.setBackgroundColor(Color.TRANSPARENT)
                isSelectList[name] = false
                majorAdapter.minusFilterPatten(collegeList[position].cId)
                offSelect(collegeList[position].cId)
            }
            majorAdapter.filter.filter("")
        }
        majorAdapter.filter.filter("")
    }

    private fun offSelect(cId: Int) {
        val map = majorAdapter.getMap()
        for (x in majorList.indices) {
            if (majorList[x].cIdFk == cId)
                map["$x ${majorList[x].mName}"] = false
        }
        majorAdapter.setMap(map)
    }

    override fun getItemCount(): Int {
        println("getItemCount")
        return collegeList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //var collegeText: TextView = itemView.findViewById(R.id.CollegeName)
        var collegeText: TextView = itemView.CollegeName
    }

    fun getMap(): Map<String, Boolean> {
        return isSelectList
    }
}