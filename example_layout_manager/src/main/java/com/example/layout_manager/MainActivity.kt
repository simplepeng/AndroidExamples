package com.example.layout_manager

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

//https://www.bilibili.com/video/BV1fW411A76o?from=search&seid=2954416328739545743
//http://wiresareobsolete.com/2014/09/building-a-recyclerview-layoutmanager-part-1/
//http://wiresareobsolete.com/2014/09/recyclerview-layoutmanager-2/
//http://wiresareobsolete.com/2015/02/recyclerview-layoutmanager-3/

//https://blog.csdn.net/zxt0601/article/details/52948009-张旭童
//https://blog.csdn.net/zxt0601/article/details/52956504

//https://blog.csdn.net/u011387817/article/details/81875021-陈子缘

//https://www.jianshu.com/p/e0f9fd169cde--自定义LayoutManager简明教程

/**
 * 合格的LayoutManager，
 * onCreateViewHolder次数不能超过屏幕上显示Item的数量
 * childCount数量不应大于屏幕上显示的Item数量，而scrapCache缓存区域的Item数量应该是0.
 */
class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private val mItems = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 0..4) {
            mItems.add(Color.BLACK)
            mItems.add(Color.RED)
            mItems.add(Color.YELLOW)
            mItems.add(Color.GREEN)
            mItems.add(Color.GRAY)
            mItems.add(Color.BLUE)

//            mItems.add(Color.BLACK)
//            mItems.add(Color.RED)
//            mItems.add(Color.YELLOW)
//            mItems.add(Color.GREEN)
//            mItems.add(Color.GRAY)
//            mItems.add(Color.BLUE)
        }


        recyclerView.run {
//            layoutManager = LogLinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = CustomLinearLayoutManager(listener = { childCount, scrapSize ->
                tvRv1ChildCount.text = "childCount = $childCount --- scrapSize = $scrapSize"
            })
            adapter = ItemAdapter("cllm1", listener = { count ->
                tvRv1CreateNum.text = " onCreateViewHolder -- $count"
            })
        }

        recyclerView2.run {
            layoutManager = CustomLinearLayoutManager2(listener = { childCount, scrapSize ->

            })
            adapter = ItemAdapter("cllm2")
        }
    }

    inner class ItemAdapter(
            private val type: String,
            private val listener: (count: Int) -> Unit = {})
        : RecyclerView.Adapter<BaseHolder>() {

        private var createHolderCount = 1

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
//            Log.d(TAG, "onCreateViewHolder -- ${createHolderCount++}")
            listener(createHolderCount++)
            return when (type) {
                "avatar" -> ItemHolder(LayoutInflater.from(this@MainActivity).inflate(R.layout.item_avatar_layout, parent, false))
                else -> ItemHolder(LayoutInflater.from(this@MainActivity).inflate(R.layout.item_layout, parent, false))
            }

        }

        override fun getItemCount(): Int {
            return mItems.size
        }

        override fun onBindViewHolder(holder: BaseHolder, position: Int) {
            holder.bindItem()
        }

    }

    abstract class BaseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bindItem()
    }

    inner class ItemHolder(itemView: View) : BaseHolder(itemView) {

        private val ivItem: CircleTextView = itemView.findViewById(R.id.iv_item)

        override fun bindItem() {
            ivItem.text = adapterPosition.toString()
            ivItem.setColor(mItems[adapterPosition])
        }
    }
}
