package com.example.layout_manager

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LogLinearLayoutManager : LinearLayoutManager {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        log(recycler)
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State?): Int {
        val dx = super.scrollHorizontallyBy(dx, recycler, state)
        log(recycler)
        return dx
    }

    private fun log(recycler: RecyclerView.Recycler) {
        Log.d("LogLinearLayoutManager", "childCount=$childCount --- scrapSize=${recycler.scrapList.size}")
    }
}