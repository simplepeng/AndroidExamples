package com.example.layout_manager

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StackLayoutManager2 : LinearLayoutManager {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
        reLayoutChildren(recycler, state)
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        return super.scrollHorizontallyBy(dx, recycler, state)
    }

    private fun reLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        for (i in 0 until childCount) {
            val child = getChildAt(i) ?: continue

        }
    }
}