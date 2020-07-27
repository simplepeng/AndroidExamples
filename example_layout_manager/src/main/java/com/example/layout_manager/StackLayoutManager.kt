package com.example.layout_manager

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * https://www.bilibili.com/video/BV1fW411A76o?from=search&seid=2954416328739545743
 * http://wiresareobsolete.com/2014/09/building-a-recyclerview-layoutmanager-part-1/
 */
/**
 * 合格的LayoutManager，childCount数量不应大于屏幕上显示的Item数量，而scrapCache缓存区域的Item数量应该是0.
 */
/**
 * 使用offsetChildrenHorizontal方法移动子View
 */
class StackLayoutManager : RecyclerView.LayoutManager() {

    private val TAG = "StackLayoutManager"

    private var mLastWidth = 0

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (state.itemCount == 0) removeAndRecycleAllViews(recycler)
        if (state.isPreLayout) return
        detachAndScrapAttachedViews(recycler)

        mLastWidth = 0

        var left = 0
        var top = 0
        var right = 0
        var bottom = 0
        for (i in 0 until itemCount) {
            val child = recycler.getViewForPosition(i)
            addView(child)
            measureChild(child, 0, 0)

            left = mLastWidth
            right = left + getDecoratedMeasuredWidth(child)
            bottom = top + getDecoratedMeasuredHeight(child)
            layoutDecorated(child, left, top, right, bottom)

            mLastWidth = right
            if (mLastWidth > width) break
        }
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        if (dx == 0 || childCount == 0) return 0
        Log.d(TAG, "dx == $dx")


        offsetChildrenHorizontal(-dx)
        fill(recycler, dx)
        recycler(recycler, dx)

        Log.d(TAG, "childCount=$childCount ---- scrapSize=${recycler.scrapList.size}")
        return dx
    }

    private fun fill(recycler: RecyclerView.Recycler, dx: Int) {
        if (dx > 0) {
            fillEnd(recycler, dx)
        } else {
            fillStar()
        }
    }

    private fun fillEnd(recycler: RecyclerView.Recycler, dx: Int) {
        val lastView = getChildAt(childCount - 1) ?: return
        val lastRight = getDecoratedRight(lastView)
        if (lastRight + dx > width) {
            return
        }

        mLastWidth = getDecoratedRight(lastView)
        val nextPosition = getPosition(lastView) + 1
        for (i in nextPosition until itemCount) {
            val child = recycler.getViewForPosition(i)
            addView(child)
            measureChild(child, 0, 0)

            layoutChild(child, mLastWidth)
            if (mLastWidth > width) break
        }
    }

    private fun fillStar() {

    }

    private fun layoutChild(child: View, left: Int) {
        val right = left + getDecoratedMeasuredWidth(child)
        layoutDecorated(child, left, 0, right, getDecoratedMeasuredHeight(child))
        mLastWidth = right
    }

    private fun recycler(recycler: RecyclerView.Recycler, dx: Int) {
        if (dx > 0) {
            recyclerStart(recycler, dx)
        } else {
            recyclerEnd()
        }
    }

    private fun recyclerStart(recycler: RecyclerView.Recycler, dx: Int) {
        val firstView = getChildAt(0) ?: return
        val firstRight = getDecoratedRight(firstView)
        if (firstRight + dx >= 0) {
            return
        }
        for (i in 0 until childCount) {
            val child = getChildAt(i) ?: continue
            if (getDecoratedRight(child) < 0) {
                removeAndRecycleView(child, recycler)
            }
        }
    }

    private fun recyclerEnd() {

    }
}