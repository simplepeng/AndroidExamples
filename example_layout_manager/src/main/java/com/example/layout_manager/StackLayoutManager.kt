package com.example.layout_manager

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min

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
//https://blog.csdn.net/zxt0601/article/details/52948009
//https://blog.csdn.net/zxt0601/article/details/52956504
//https://blog.csdn.net/u011387817/article/details/81875021
class StackLayoutManager : RecyclerView.LayoutManager() {

    private val TAG = "StackLayoutManager"

    private var mLastRight = 0
    private var mFirstLeft = 0
    private var mOffsetX = 0

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

        mLastRight = 0

        initFill(recycler)
    }

    private fun initFill(recycler: RecyclerView.Recycler) {
        var left = 0
        val top = 0
        var right = 0
        var bottom = 0
        for (i in 0 until itemCount) {
            val child = recycler.getViewForPosition(i)
            addView(child)
            measureChild(child, 0, 0)

            left = mLastRight
            right = left + getDecoratedMeasuredWidth(child)
            bottom = top + getDecoratedMeasuredHeight(child)
            layoutDecorated(child, left, top, right, bottom)

            mLastRight = right
            if (mLastRight > width) break
        }
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        if (dx == 0 || childCount == 0) return 0
        Log.d(TAG, "dx == $dx")

//        val consumed = edgeCheck(dx)
//        Log.d(TAG, "consumed == $consumed")

        //填充
        val consumed = fill(recycler, dx)
        Log.d(TAG, "consumed == $consumed")
        //回收
        recycler(recycler, consumed)
        //移动
        offsetChildrenHorizontal(-consumed)

//        Log.d(TAG, "childCount=$childCount ---- scrapSize=${recycler.scrapList.size}")
        return consumed
    }

    private fun edgeCheck(dx: Int): Int {
        val firstView = getChildAt(0) ?: return dx
        val lastView = getChildAt(childCount - 1) ?: return dx
        val firstPosition = getPosition(firstView)
        val lastPosition = getPosition(lastView)

        //item未填充满一屏的情况，不能滑动
        val viewSpan = getDecoratedRight(lastView) - getDecoratedLeft(firstView)
        if (firstPosition == 0 && lastPosition == itemCount - 1 && viewSpan <= width) return 0

        if (dx < 0) {//拖动到最左边，无法继续滑动
            val firstLeft = getDecoratedLeft(firstView)
            if (firstPosition == 0 && firstLeft + dx <= 0) {
                return firstLeft
            }
        } else {//拖动到最右边，无法继续滑动
            val lastRight = getDecoratedRight(lastView)
            if (lastPosition == itemCount - 1 && lastRight - dx <= width) {
                return (lastRight - width)
            }
        }
        return dx
    }

    private fun fill(recycler: RecyclerView.Recycler, dx: Int): Int {
        return if (dx > 0) {
            fillEnd(recycler, dx)
        } else {
            fillStar(recycler, dx)
        }
    }

    //dx>0
    private fun fillEnd(recycler: RecyclerView.Recycler, dx: Int): Int {
        val lastView = getChildAt(childCount - 1) ?: return 0
        val lastRight = getDecoratedRight(lastView)
        val lastPosition = getPosition(lastView)

        if (lastPosition == itemCount - 1) {
            return min(dx, lastRight - width)
        }

        //如果最后一个item的right-dx小于rv的width就要填充item
        if (lastRight - dx <= width) {
            mLastRight = lastRight
            val nextPosition = lastPosition + 1
            for (i in nextPosition until itemCount) {
                Log.d(TAG, "fillEnd: $i")

                val child = recycler.getViewForPosition(i)
                addView(child)
                measureChild(child, 0, 0)
                layoutChildEnd(child, mLastRight)


                if (mLastRight - dx > width) break
            }
        }
        return dx
    }

    private fun layoutChildEnd(child: View, left: Int) {
        val right = left + getDecoratedMeasuredWidth(child)
        layoutDecorated(child, left, 0, right, getDecoratedMeasuredHeight(child))
        mLastRight = right
    }

    //dx<0
    private fun fillStar(recycler: RecyclerView.Recycler, dx: Int): Int {
        val firstView = getChildAt(0) ?: return dx
        val firstLeft = getDecoratedLeft(firstView)
        val firstPosition = getPosition(firstView)

        if (firstPosition == 0) {
            return max(dx, firstLeft)
        }

        if (firstLeft - dx >= 0) {
            mFirstLeft = firstLeft
            val prePosition = firstPosition - 1
            for (i in prePosition downTo 0) {
//                Log.d(TAG, "fillStar: $i")

                val child = recycler.getViewForPosition(i)
                addView(child, 0)
                measureChild(child, 0, 0)
                layoutChildStart(child, mFirstLeft)

                if (mFirstLeft - dx < 0) break
            }
        }
        return dx
    }

    private fun layoutChildStart(child: View, right: Int) {
        val left = right - getDecoratedMeasuredWidth(child)
        layoutDecorated(child, left, 0, right, getDecoratedMeasuredHeight(child))
        mFirstLeft = left
    }

    private fun recycler(recycler: RecyclerView.Recycler, dx: Int) {
        if (dx > 0) {
            recyclerStart(recycler, dx)
        } else {
            recyclerEnd(recycler, dx)
        }
    }

    //dx>0
    private fun recyclerStart(recycler: RecyclerView.Recycler, dx: Int) {
        val firstView = getChildAt(0) ?: return
        val firstRight = getDecoratedRight(firstView)
        if (firstRight - dx >= 0) {
            return
        }
        for (i in 0 until childCount) {
            val child = getChildAt(i) ?: continue
            if (getDecoratedRight(child) - dx < 0) {
//                Log.d(TAG, "recyclerStart: ${getPosition(child)}")
                removeAndRecycleView(child, recycler)
            }
        }
    }

    //dx<0
    private fun recyclerEnd(recycler: RecyclerView.Recycler, dx: Int) {
        val lastView = getChildAt(childCount - 1) ?: return
        val lastLeft = getDecoratedLeft(lastView)
        if (lastLeft - dx <= width) {
            return
        }
        for (i in childCount - 1 downTo 0) {
            val child = getChildAt(i) ?: return
            if (getDecoratedLeft(child) - dx > width) {
                Log.d(TAG, "recyclerEnd: ${getPosition(child)}")
                Log.d(TAG, "recyclerEnd: -- ${getDecoratedLeft(child) - dx} -- $width")
                removeAndRecycleView(child, recycler)
            }
        }
    }
}