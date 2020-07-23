package com.example.layout_manager

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * https://www.bilibili.com/video/BV1fW411A76o?from=search&seid=2954416328739545743
 */
class StackLayoutManager : RecyclerView.LayoutManager() {

    companion object {
        const val TAG = "StackLayoutManager"
        const val LAYOUT_START = 1
        const val LAYOUT_END = 2
    }

    private var mLayoutDirection: Int = LAYOUT_START

    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (itemCount == 0) {
            removeAndRecycleAllViews(recycler)
            return
        }
        if (state.isPreLayout) return

        detachAndScrapAttachedViews(recycler)
        fillInit(recycler, state)

        Log.d(TAG, "onLayoutChildren -- childCount: $childCount --- scrapSize: ${recycler.scrapList.size}")
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    private var mOffset = 0

    //从右向左滑动，dx>0，从左往右滑动，dx<0
    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
//        super.scrollHorizontallyBy(dx, recycler, state)
//        Log.d(TAG, "scrollHorizontallyBy: dx == $dx")
        if (dx == 0 || childCount == 0) return 0

        mOffset += dx

        if (dx < 0) fillStart(recycler, state) else fillEnd(recycler, state)
        offsetChildrenHorizontal(-dx)
        if (dx < 0) recyclerEnd(recycler, state) else recyclerStart(recycler, state)

//        Log.d(TAG, "childCount: $childCount --- scrapSize: ${recycler.scrapList.size}")

        return dx
    }

    private fun fillInit(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        var left = 0
        val top = 0
        var right = 0
        var bottom = 0
        for (position in 0 until itemCount) {
            val child = recycler.getViewForPosition(position)
            addView(child)
            measureChildWithMargins(child, 0, 0)
            right = left + getDecoratedMeasuredWidth(child)
            bottom = top + getDecoratedMeasuredHeight(child)
            layoutDecoratedWithMargins(child, left, top, right, bottom)
            if (getDecoratedLeft(child) >= width) {
                break
            }
            left += getDecoratedMeasuredWidth(child)
        }
    }

    private fun fillStart(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
//        Log.d(TAG, "fillStart: ")
        if (childCount == 0) return

        val firstView = getChildAt(0) ?: return
        val firstPosition = getPosition(firstView)
        if (firstPosition <= 0) return

        val prePosition = firstPosition - 1
        if (prePosition < 0) return
        val preView = recycler.getViewForPosition(prePosition)
        if (getDecoratedRight(firstView) > 0) {
            addView(preView)
            measureChildWithMargins(preView, 0, 0)
            val top = 0
            val right = getDecoratedLeft(firstView)
            val left = right - getDecoratedMeasuredWidth(preView)
            val bottom = top + getDecoratedMeasuredHeight(preView)
            layoutDecoratedWithMargins(preView, left, top, right, bottom)
        }
    }

    private fun fillEnd(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
//        Log.d(TAG, "fillEnd")
        if (childCount == 0) return
        val lastView = getChildAt(childCount - 1) ?: return
        if (getDecoratedLeft(lastView) <= width) {
            val left = getDecoratedRight(lastView)
            val top = 0
            val nextView = recycler.getViewForPosition(childCount)
            addView(nextView)
            measureChildWithMargins(nextView, 0, 0)

            val right = left + getDecoratedMeasuredWidth(nextView)
            val bottom = top + getDecoratedMeasuredHeight(nextView)
            layoutDecoratedWithMargins(nextView, left, top, right, bottom)
        }
    }

    private fun recyclerViews(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (childCount == 0) return
        for (index in 0 until childCount) {
            val child = getChildAt(index) ?: continue
            if (getDecoratedRight(child) < 0 || getDecoratedLeft(child) > width) {
                removeAndRecycleView(child, recycler)
//                detachAndScrapView(child, recycler)
            }
        }
    }

    private fun recyclerChildren(recycler: RecyclerView.Recycler) {
        val scrapList = recycler.scrapList
        for (viewHolder in scrapList) {
            removeAndRecycleView(viewHolder.itemView, recycler)
        }
    }

    private fun recyclerStart(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
//        Log.d(TAG, "recyclerStart")
        if (childCount == 0) return

        for (childIndex in 0 until childCount) {
            val child = getChildAt(childIndex) ?: continue
            if (getDecoratedRight(child) < 0) {
                val position = getPosition(child)
                removeAndRecycleView(child, recycler)
                Log.d(TAG, "recyclerStart position == $position")
            }
        }
    }

    private fun recyclerEnd(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (childCount == 0) return
        for (childIndex in childCount downTo 0) {
            val child = getChildAt(childIndex) ?: continue
            if (getDecoratedLeft(child) > width) {
                val position = getPosition(child)
                removeAndRecycleView(child, recycler)
                Log.d(TAG, "recyclerEnd position == $position")
            }
        }
    }

//    private fun bilibiliFill(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
//        detachAndScrapAttachedViews(recycler)
//
//        for (....){
//            val nextPosition=...
//
//            val child = recycler.getViewForPosition(nextPosition)
//            addView(child)
//
//            measureChildWithMargins(child,0,0)
//            layoutDecoratedWithMargins(child,.....)
//        }
//
//        val scrapList = recycler.scrapList
//        for (viewHolder in scrapList) {
//            recycler.recycleView(viewHolder.itemView)
//        }
//    }

//    override fun onAdapterChanged(oldAdapter: RecyclerView.Adapter<*>?, newAdapter: RecyclerView.Adapter<*>?) {
//        super.onAdapterChanged(oldAdapter, newAdapter)
//        recyclerViews()
//    }
}