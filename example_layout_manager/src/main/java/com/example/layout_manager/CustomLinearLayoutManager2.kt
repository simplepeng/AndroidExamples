package com.example.layout_manager

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * 使用detachAndScrapAttachedViews方法重新摆放子View
 */
//https://blog.csdn.net/ccy0122/article/details/90515386
//https://github.com/CCY0122/FocusLayoutManager
//https://github.com/Ifxcyr/PathLayoutManager

//https://github.com/devunwired/recyclerview-playground
class CustomLinearLayoutManager2(private val listener: (childCount: Int, scrapSize: Int) -> Unit) : RecyclerView.LayoutManager() {

    private val TAG = "CLLM2"

    private var mOffsetX = 0
    private var mLastPosition = 0

    private var mItemWidth: Int = 0

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
        }
        if (state.isPreLayout) return

        val first = recycler.getViewForPosition(0)
        addView(first)
        measureChild(first, 0, 0)
        mItemWidth = getDecoratedMeasuredWidth(first)
        Log.d(TAG, "mItemWidth: $mItemWidth")
        removeAndRecycleView(first, recycler)

        detachAndScrapAttachedViews(recycler)
        fill(recycler, 0)
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        if (dx == 0 || childCount == 0) return 0
        Log.d(TAG, "dx == $dx")

        mOffsetX += dx

        val consumed = fill(recycler, dx)
        Log.d(TAG, "consumed == $consumed")

//        recyclerChildren(recycler)

//        Log.d(TAG, "childCount=$childCount ---- scrapSize=${recycler.scrapList.size}")
        return consumed
    }

    private fun fill(recycler: RecyclerView.Recycler, dx: Int): Int {
        if (dx < 0) {
            if (mOffsetX <= 0) {
                mOffsetX = 0
                return mOffsetX
            }
        }

        if (dx > 0) {

        }

        var startPosition = 0
        if (mOffsetX > 0) {
            startPosition = mOffsetX / mItemWidth
        }
        Log.d(TAG, "mOffsetX: $mOffsetX")
        Log.d(TAG, "startPosition: $startPosition")

        val firstView = getChildAt(0)
        var left: Int = if (firstView == null) 0 else getDecoratedLeft(firstView) - dx
        var right: Int

        detachAndScrapAttachedViews(recycler)

        for (i in startPosition until itemCount - 1) {
            val child = recycler.getViewForPosition(i)
            addView(child)
            measureChild(child, 0, 0)

            right = left + getDecoratedMeasuredWidth(child)
            layoutDecorated(child, left, 0, right, getDecoratedMeasuredHeight(child))

            if (right > width) {
                mLastPosition = i
                break
            }
            left = right
        }

//        recyclerChildren(recycler)

        return dx
    }

    private fun recyclerChildren(recycler: RecyclerView.Recycler) {
        for (i in 0 until childCount) {
            val child = getChildAt(i) ?: continue
            if (getDecoratedRight(child) < 0 || getDecoratedLeft(child) > width) {
                removeAndRecycleView(child, recycler)
            }
        }
    }

//    private fun recyclerChildren(recycler: RecyclerView.Recycler) {
//        val scrapList = recycler.scrapList
//        if (scrapList.isEmpty()) return
//
//        for (holder in scrapList) {
//            Log.d(TAG, "recyclerChildren: ${holder.adapterPosition}")
//            removeAndRecycleView(holder.itemView, recycler)
//        }
//    }
}