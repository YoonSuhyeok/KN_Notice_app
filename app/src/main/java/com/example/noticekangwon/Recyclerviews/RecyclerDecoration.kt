package com.example.noticekangwon.Recyclerviews

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerDecoration(divHeight: Int) : RecyclerView.ItemDecoration() {
    private var divHeight: Int = 0

    init {
        this.divHeight = divHeight
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = divHeight
    }

}