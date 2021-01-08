package com.example.noticekangwon.Recyclerviews

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

class MajorAdapter(private val MajorList: MutableList<Major>): RecyclerView.Adapter<MajorAdapter.Holder>(), Filterable {
    var littleMajor: MutableList<Major>

    init {
        littleMajor = MajorList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_filter_major, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.majorName.text = littleMajor[position].mName
    }

    override fun getItemCount(): Int {
        return littleMajor.size
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var majorName: TextView = itemView.findViewById(R.id.MajorName)
    }

    override fun getFilter(): Filter {
        return object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                val SelectID = constraint.toString().toInt()
                var SelectList = mutableListOf<Major>()
                for( x in MajorList){
                    if(x.cIdFk == SelectID){
                        SelectList.add(x)
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

}