package com.example.notice.Recyclerviews

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notice.dataBase.College
import com.example.notice.dataBase.Major
import com.example.notice.R
import kotlinx.android.synthetic.main.list_item_filter_college.view.*

class CollegeAdapter(
    context: Context,
    private val collegeList: MutableList<College>, private val majorList: MutableList<Major>,
    private val majorAdapter: MajorAdapter, private val isSelectList: MutableMap<String, Boolean>
) : RecyclerView.Adapter<CollegeAdapter.Holder>() {

    private val nonClickColor = context.resources.getColor(R.color.fontColor)
    private val clickColor = context.resources.getColor(R.color.ClickCollegeViewColor)
    private val clickFont = context.resources.getColor(R.color.subCollege)

    private val offViewBackground = context.resources.getDrawable(R.drawable.filter_border, null)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_filter_college, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.collegeText.text = collegeList[position].cName
        val name = collegeList[position].cName
        if (isSelectList[name] == true) {
            holder.itemView.setBackgroundColor(clickColor)
            holder.collegeText.setTextColor(clickFont)
            majorAdapter.plusFilterPatten(collegeList[position].cId)
        } else {
            holder.checkImg.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            if (isSelectList[name] == true) { // nonClick
                holder.itemView.background = offViewBackground
                holder.collegeText.setTextColor(nonClickColor)
                isSelectList[name] = false
                majorAdapter.minusFilterPatten(collegeList[position].cId)
                offSelect(collegeList[position].cId)
                holder.checkImg.visibility = View.GONE
            } else {
                holder.itemView.setBackgroundColor(clickColor)
                holder.collegeText.setTextColor(clickFont)
                isSelectList[name] = true
                majorAdapter.plusFilterPatten(collegeList[position].cId)
                holder.checkImg.visibility = View.VISIBLE
            }
            majorAdapter.filter.filter("")
        }
        majorAdapter.filter.filter("")
    }

    private fun offSelect(cId: Int) {
        val map = majorAdapter.getMap()
        for (x in majorList) {
            if (x.cIdFk == cId)
                map[x.mName] = false
        }
        majorAdapter.setMap(map)
    }

    override fun getItemCount(): Int {
        return collegeList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //var collegeText: TextView = itemView.findViewById(R.id.CollegeName)
        var collegeText: TextView = itemView.CollegeName
        var checkImg = itemView.check
    }

    fun getMap(): Map<String, Boolean> {
        return isSelectList
    }
}