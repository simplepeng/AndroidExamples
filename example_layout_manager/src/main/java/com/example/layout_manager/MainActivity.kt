package com.example.layout_manager

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mItems = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mItems.add(Color.RED)
        mItems.add(Color.YELLOW)
        mItems.add(Color.BLACK)
        mItems.add(Color.GREEN)
        mItems.add(Color.GRAY)
        mItems.add(Color.BLUE)

        mItems.add(Color.RED)
        mItems.add(Color.YELLOW)
        mItems.add(Color.BLACK)
        mItems.add(Color.GREEN)
        mItems.add(Color.GRAY)
        mItems.add(Color.BLUE)


        recyclerView.run {
            //            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = StackLayoutManager()
            adapter = ItemAdapter()
        }
    }

    inner class ItemAdapter : RecyclerView.Adapter<ItemHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            return ItemHolder(LayoutInflater.from(this@MainActivity).inflate(R.layout.item_layout, parent, false))
        }

        override fun getItemCount(): Int {
            return mItems.size
        }

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            holder.iv_item.run {
                setText(position.toString())
                setBackgroundColor(mItems[position])
            }
        }

    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv_item: TextView = itemView.findViewById(R.id.iv_item)
    }
}
