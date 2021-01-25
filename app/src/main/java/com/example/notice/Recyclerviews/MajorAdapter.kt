package com.example.notice.Recyclerviews

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notice.dataBase.Major
import com.example.notice.R
import kotlinx.android.synthetic.main.list_item_filter_major.view.*

class MajorAdapter(
    context: Context,
    private val MajorList: MutableList<Major>,
    private var isSelectList: MutableMap<String, Boolean>
) : RecyclerView.Adapter<MajorAdapter.Holder>(), Filterable {
    private val filterLists: ArrayList<Int> = ArrayList()

    // private val mainToLists:ArrayList<Int> = ArrayList()
    private var littleMajor: MutableList<Major>
    private var nonClickColor = context.resources.getColor(R.color.background)
    private val clickColor = context.resources.getColor(R.color.ClickMajorViewColor)
    init {
        littleMajor = MajorList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_filter_major, parent, false)
        return Holder(view)
    }


    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.majorName.text = littleMajor[position].mName
        val name = littleMajor[position].mName
        if (isSelectList[name] == true) {
            holder.itemView.setBackgroundColor(clickColor)
        } else {
            holder.itemView.setBackgroundColor(nonClickColor)
        }

        holder.itemView.setOnClickListener {
            if (isSelectList[name] == true) { // nonClick
                holder.itemView.setBackgroundColor(nonClickColor)
                isSelectList[name] = false
            } else {
                holder.itemView.setBackgroundColor(clickColor)
                isSelectList[name] = true
            }
        }
    }

    override fun getItemCount(): Int {
        return littleMajor.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var majorName: TextView = itemView.MajorName
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val selectList = mutableListOf<Major>()
                filterLists.sort()
                for (y in filterLists) {
                    for (x in MajorList) {
                        if (x.cIdFk == y) {
                            selectList.add(x)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = selectList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                littleMajor = results?.values as MutableList<Major>
                notifyDataSetChanged()
            }

        }
    }

    fun plusFilterPatten(plusElement: Int) {
        if (!filterLists.contains(plusElement))
            filterLists.add(plusElement)
    }

    fun minusFilterPatten(minusElement: Int) {
        if (filterLists.contains(minusElement))
            for (x in filterLists.indices)
                if (minusElement == filterLists[x]) {
                    filterLists.removeAt(x)
                    break
                }
    }

    fun getMap(): MutableMap<String, Boolean> {
        return isSelectList
    }

    fun setMap(map: MutableMap<String, Boolean>) {
        isSelectList = map
    }
}