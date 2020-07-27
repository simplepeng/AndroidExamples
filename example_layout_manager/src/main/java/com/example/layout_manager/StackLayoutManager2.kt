package com.example.layout_manager

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * 使用detachAndScrapAttachedViews方法重新摆放子View
 */
class StackLayoutManager2 : RecyclerView.LayoutManager() {

    companion object {
        const val TAG = "StackLayoutManager2"
    }

    private var mOffsetX: Int = 0
    private var mItemWidth = 0
    private var mItemCount = 0

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (state.itemCount == 0) {
            removeAndRecycleAllViews(recycler)
            return
        }

        detachAndScrapAttachedViews(recycler)

        val firstItem = recycler.getViewForPosition(0)
        addView(firstItem)
        measureChild(firstItem, 0, 0)
        mItemWidth = getDecoratedMeasuredWidth(firstItem)
        mItemCount = width / mItemWidth + 1
        removeAndRecycleView(firstItem, recycler)

        fill(recycler, state, 0)
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    //手指从右向左滑动，dx > 0; 手指从左向右滑动，dx < 0;
    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        Log.d(TAG, "dx == $dx")
        if (dx == 0 || childCount == 0) return 0

        mOffsetX += dx
//        Log.d(TAG, "mOffsetX: $mOffsetX")

//        detachAndScrapAttachedViews(recycler)
//        val consumed = fill(recycler, state, dx)
//        offsetChildrenHorizontal(dx)
//        recyclerChildren(recycler)

        return dx
    }

    private fun fill(recycler: RecyclerView.Recycler, state: RecyclerView.State, dx: Int): Int {
//        Log.d(TAG, "fill")
        var consumed = dx
        if (dx < 0 && mOffsetX < 0) {//已达左边界
            mOffsetX = 0
            consumed = 0
        }

        if (dx > 0) {

        }

        var firstVisiblePosition = 0
        if (mOffsetX > 0) {
            firstVisiblePosition = mOffsetX / mItemWidth + 1
        }

        var lastVisiblePosition = itemCount

        var left = 0
        var right: Int
        for (position in firstVisiblePosition until lastVisiblePosition) {
            val item = recycler.getViewForPosition(position)
            addView(item)
            measureChild(item, 0, 0)
            right = left + getDecoratedMeasuredWidth(item)
            layoutDecorated(item, left, 0, right, getDecoratedMeasuredHeight(item))
            if (right > width) {
                lastVisiblePosition = position
                break
            }
            left = right
        }

        return consumed
    }

    private fun recyclerChildren(recycler: RecyclerView.Recycler) {
        val scrapList = recycler.scrapList
        if (scrapList.isEmpty()) return
        for (i in 0 until scrapList.size) {
            val viewHolder = scrapList[i]
            removeAndRecycleView(viewHolder.itemView, recycler)
        }

    }
}