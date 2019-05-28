package com.example.layout_manager

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * detachAndScrapAttachedViews 将清理容器中所有的子view
 * removeAndRecycleView 将指定view移除并放入recycle缓存中
 * layoutDecorated() 布局child view
 * recycler.getViewForPosition 从recycle缓存中拿到指定position的缓存itemview，如果没有则调用onCreateViewHolder来进行创建
 * getChildCount() 获取界面可见位置，有多少个子item。因此这个值很少，多的item被回收掉了！所以才是Recycler的View
 * measureChildWithMargins(view) 要求测量item的布局，把一个item addView之后要测量一下
 * getDecoratedMeasuredWidth() 这里的宽度，包含有你通过ItemDecoration装饰过的大小
 * offsetChildrenVertical(distance) 将整个列表移动，这里的distance，就是X0Y坐标系正常方向的位移了
 */

class StackLayoutManager : RecyclerView.LayoutManager() {

    val TAG = "StackLayoutManager"
    private lateinit var helper: OrientationHelper

    companion object {
        val VERTICAL = 0
        val HORIZONTAL = 1
    }

    private var mOrientation = VERTICAL

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
//        super.onLayoutChildren(recycler, state)
        log("onLayoutChildren")
        if (itemCount == 0) {//不需要布局
            detachAndScrapAttachedViews(recycler)
            return
        }

        if (state.itemCount == 0 || state.isPreLayout) {
            return
        }

//        在布局之前，将所有的子View先Detach掉，放入到Scrap缓存中
        detachAndScrapAttachedViews(recycler)

        log("itemCount == " + itemCount)
        when (mOrientation) {
            HORIZONTAL -> {
                layoutHorizontal(recycler)
            }
            VERTICAL -> {
                layoutVertical(recycler, state)
            }
        }

    }

    private var offsetHorizontal: Int = 0
    private var totalWidth: Int = 0

    private fun layoutHorizontal(recycler: RecyclerView.Recycler) {
//        if (childCount == 0) return

        //定义偏移量
        var offsetX = 0
        for (position: Int in 0 until itemCount) {
            //从缓存中取出view
            val itemView = recycler.getViewForPosition(position)
            //讲view添加进recyclerView
            addView(itemView)
            //对view测量
//            measureChild()
            measureChildWithMargins(itemView, 0, 0)
            //拿到view的宽高,宽高都是包含ItemDecorate的尺寸
            val width = getDecoratedMeasuredWidth(itemView)
            val height = getDecoratedMeasuredHeight(itemView)
            //对view布局
//            layoutDecorated()
            layoutDecoratedWithMargins(itemView, offsetX, 0, width + offsetX, height)
            offsetX += width

            //
            totalWidth = offsetX
        }
    }

    private fun layoutVertical(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
//        if (childCount == 0) return
/*
        //定义偏移量
        var offsetY = 0
        for (position: Int in 0 until itemCount) {
            //从缓存中取出view
            val itemView = recycler.getViewForPosition(position)
            //讲view添加进recyclerView
            addView(itemView)
            //对view测量
//            measureChild()
            measureChildWithMargins(itemView, 0, 0)
            //拿到view的宽高,宽高都是包含ItemDecorate的尺寸
            val width = getDecoratedMeasuredWidth(itemView)
            val height = getDecoratedMeasuredHeight(itemView)
            //对view布局
            //如果越界了就回收
            if (offsetY > getHeight()) {
                removeAndRecycleView(itemView, recycler)
            } else {
//            layoutDecorated()
                val y = offsetY + height
                layoutDecoratedWithMargins(itemView, 0, offsetY, width, y)
                offsetY += height
            }
//            OrientationHelper
        }
 */

        helper = OrientationHelper.createOrientationHelper(this, RecyclerView.VERTICAL)
        val totalSpace = helper.totalSpace
        val itemCount = state.itemCount
        var offsetY = 0
        for (position in 0..itemCount) {
            val itemView = recycler.getViewForPosition(position)
            measureChildWithMargins(itemView, 0, 0)
            val itemWidth = getDecoratedMeasuredWidth(itemView)
            val itemHeight = getDecoratedMeasuredHeight(itemView)

            if (offsetY > totalSpace) break
            addView(itemView)
            layoutDecorated(itemView, 0, offsetY, itemWidth, offsetY + itemHeight)
            offsetY += itemHeight / 2
        }

        Log.d(TAG, "childCount == " + childCount)
        Log.d(TAG, "scrapList.size == " + recycler.scrapList.size)
    }


    override fun canScrollHorizontally(): Boolean {
//        return super.canScrollHorizontally()
        return mOrientation == HORIZONTAL
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler,
                                      state: RecyclerView.State?): Int {
        Log.d(TAG, "dx == " + dx)

        layoutHorizontal(recycler)

        return -dx
//        return super.scrollHorizontallyBy(dx, recycler, state)
    }

    override fun canScrollVertically(): Boolean {
        return mOrientation == VERTICAL
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler,
                                    state: RecyclerView.State): Int {
//        super.scrollVerticallyBy(dy, recycler, state)
//        Log.d(TAG, "dy == " + dy)
        if (childCount == 0 || dy == 0) {
            return 0
        }
        var willScroll = dy

        //回收不可见的childview
        val childCount = childCount

//        for (position in 0 until childCount) {
//            val itemView = getChildAt(position)
//            if (dy > 0) {//上拉
//                val end = helper.getDecoratedEnd(itemView)
//                Log.d(TAG, "end == " + end)
//            } else {//下拉
////                val end = helper.getDecoratedEnd(itemView);
////                Log.d(TAG, "end == " + end)
//            }
//        }

        if (willScroll > 0) {//上拉
            val firstItemView = getChildAt(0)
            firstItemView?.let {
                val endY = helper.getDecoratedEnd(firstItemView)
//            Log.d(TAG, "endY == " + endY)
                if (endY < paddingTop) {
//                    removeAndRecycleView(firstItemView, recycler)
                    detachAndScrapView(firstItemView, recycler)
                }
            }
        } else {//下拉
            val lastItemView = getChildAt(childCount - 1)
            lastItemView?.let {
                val endY = helper.getDecoratedStart(lastItemView)
                if (endY > height) {
//                    removeAndRecycleView(lastItemView, recycler)
                    detachAndScrapView(lastItemView, recycler)
                }
            }
        }

        //将新出现的childview layout 出来
        if (willScroll > 0) {//上拉
            val lastView = getChildAt(childCount - 1)
            lastView?.let { view ->
                val endY = helper.getDecoratedEnd(lastView)
//                Log.d(TAG, "lastView endY == " + endY)
                if (endY - (getDecoratedMeasuredHeight(lastView) / 2) < height) {//添加新的itemView
                    val nextPosition = getPosition(view) + 1
                    if (nextPosition < state.itemCount) {
                        val nextItemView = recycler.getViewForPosition(nextPosition)
                        addView(nextItemView)
                        measureChildWithMargins(nextItemView, 0, 0)
                        val height = getDecoratedMeasuredHeight(nextItemView)
                        val width = getDecoratedMeasuredWidth(nextItemView)
                        val top = endY - height / 2
                        layoutDecorated(nextItemView, 0, top, width, top + height)
                    } else {
                        willScroll = 0
                    }
                }
            }
        } else {//下拉
            val firstItemView = getChildAt(0)
            firstItemView?.let {
                val endY = helper.getDecoratedEnd(firstItemView)
                if (endY - (getDecoratedMeasuredHeight(firstItemView) / 2) >= paddingTop) {
                    val lastPosition = getPosition(it) - 1
                    if (lastPosition < state.itemCount && lastPosition > 0) {
                        val lastItemView = recycler.getViewForPosition(lastPosition)
//                        attachView(lastItemView)
                        addView(lastItemView, 0)
                        measureChildWithMargins(lastItemView, 0, 0)
                        val height = getDecoratedMeasuredHeight(lastItemView)
                        val width = getDecoratedMeasuredWidth(lastItemView)
                        val top = endY - height - (height / 2)
                        val bottom = endY - height / 2
                        layoutDecorated(lastItemView, 0, top, width, bottom)
                    } else {
                        willScroll = 0
                    }
                }
            }
        }
        //移动childview

//        offsetChildrenVertical(-willScroll)
        helper.offsetChildren(-willScroll)

//        Log.d(TAG, "childCount == " + childCount)
//        Log.d(TAG, "scrapList.size == " + recycler.scrapList.size)
        return willScroll
    }


    fun getVerticalSpace(): Int {
        return height - paddingTop - paddingBottom
    }

    fun getHorizontalSpace(): Int {
        return width - paddingLeft - paddingRight
    }

    /**
     * 获取某个childView在水平方向所占的空间
     *
     * @param view
     * @return
     */
    fun getDecoratedMeasurementHorizontal(view: View): Int {
        val params = view.layoutParams as RecyclerView.LayoutParams
        return getDecoratedMeasuredWidth(view) + params.leftMargin + params.rightMargin;
    }

    /**
     * 获取某个childView在竖直方向所占的空间
     *
     * @param view
     * @return
     */
    fun getDecoratedMeasurementVertical(view: View): Int {
        val params = view.layoutParams as RecyclerView.LayoutParams
        return getDecoratedMeasuredHeight(view) + params.topMargin + params.bottomMargin;
    }

    override fun onAdapterChanged(oldAdapter: RecyclerView.Adapter<*>?, newAdapter: RecyclerView.Adapter<*>?) {
        super.onAdapterChanged(oldAdapter, newAdapter)

    }

    fun log(message: String) {
        Log.d(TAG, message)
    }

}