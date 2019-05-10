package com.example.layout_manager

import android.view.ViewGroup
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

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
        if (state.itemCount == 0 || state.isPreLayout) {
            return
        }

        if (itemCount == 0) {//不需要布局
            detachAndScrapAttachedViews(recycler)
            return
        }

        layout(recycler)
    }

    private fun layout(recycler: RecyclerView.Recycler) {
        if (childCount == 0)return

    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?,
                                      state: RecyclerView.State?): Int {
        return super.scrollHorizontallyBy(dx, recycler, state)
    }

    override fun canScrollVertically(): Boolean {
        return super.canScrollVertically()
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?,
                                    state: RecyclerView.State?): Int {
        return super.scrollVerticallyBy(dy, recycler, state)
    }


}