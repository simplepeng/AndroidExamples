package com.example.layout_manager

import android.graphics.Color
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
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

        for (i in 0..10000) {
            mItems.add(Color.BLACK)
//            mItems.add(Color.RED)
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
            layoutManager = StackLayoutManager()
//            layoutManager = StackLayoutManager2()
            adapter = ItemAdapter()
        }
//        recyclerView.adapter?.notifyDataSetChanged()

//        recyclerView2.run {
//            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
//            adapter = ItemAdapter()
//        }
    }

    inner class ItemAdapter : RecyclerView.Adapter<ItemHolder>() {

        private var createHolderCount = 1

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
//            Log.d(TAG, "onCreateViewHolder -- ${createHolderCount++}")
            return ItemHolder(LayoutInflater.from(this@MainActivity).inflate(R.layout.item_layout, parent, false))
        }

        override fun getItemCount(): Int {
            return mItems.size
        }

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
//            Log.d(TAG, "onBindViewHolder")
            holder.iv_item.run {
                text = position.toString()
//                setBackgroundColor(mItems[position])
                setColor(mItems[position])
            }
        }

    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv_item: CircleTextView = itemView.findViewById(R.id.iv_item)
    }
}
