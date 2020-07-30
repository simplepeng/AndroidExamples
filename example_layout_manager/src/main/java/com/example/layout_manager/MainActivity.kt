package com.example.layout_manager

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

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
            layoutManager = AvatarLinearLayoutManager(listener = { childCount, scrapSize ->

            })
            adapter = ItemAdapter("avatar")
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
