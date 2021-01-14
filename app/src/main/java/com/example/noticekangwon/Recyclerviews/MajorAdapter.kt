package com.example.noticekangwon.Recyclerviews

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.noticekangwon.DataBase.Major
import com.example.noticekangwon.R
import kotlinx.android.synthetic.main.list_item_filter_major.view.*

class MajorAdapter(private val MajorList: MutableList<Major>, private var isSelectList: MutableMap<String,Boolean>): RecyclerView.Adapter<MajorAdapter.Holder>(), Filterable {
    private val filterLists:ArrayList<Int> = ArrayList()
    // private val mainToLists:ArrayList<Int> = ArrayList()
    private var littleMajor: MutableList<Major>

    init {
        littleMajor = MajorList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_filter_major, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.majorName.text = littleMajor[position].mName
        val name = "major_" + MajorList[position].mName
        if( isSelectList[name] == true){
            holder.majorName.setBackgroundColor(Color.YELLOW)
        } else {
            holder.majorName.setBackgroundColor(Color.TRANSPARENT)
        }
        holder.itemView.setOnClickListener {
            if(isSelectList[name] == true) {
                holder.majorName.setBackgroundColor(Color.TRANSPARENT)
                isSelectList[name] = false
            } else {
                holder.majorName.setBackgroundColor(Color.YELLOW)
                isSelectList[name] = true
            }
        }
    }

    override fun getItemCount(): Int {
        return littleMajor.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var majorName: TextView = itemView.MajorName
    }

    override fun getFilter(): Filter {
        return object: Filter(){
            override fun performFiltering(constraint:CharSequence): FilterResults? {
                var SelectList = mutableListOf<Major>()
                filterLists.sort()
                for(y in filterLists){
                    val SelectID = y
                    for( x in MajorList){
                        if(x.cIdFk == SelectID){
                            SelectList.add(x)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = SelectList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                littleMajor = results?.values as MutableList<Major>
                notifyDataSetChanged()
            }

        }
    }

    fun plusFilterPatten(plusElement: Int){
        if(!filterLists.contains(plusElement))
            filterLists.add(plusElement)
    }

    fun minusFilterPatten(minusElement: Int){
        if(filterLists.contains(minusElement))
            for( x in filterLists.indices)
                if( minusElement == filterLists[x]){
                    filterLists.removeAt(x)
                    break
                }
    }

    fun getMap(): MutableMap<String, Boolean> {
        return isSelectList
    }

    fun setMap(map: MutableMap<String, Boolean>){
        isSelectList = map
    }
}