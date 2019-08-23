package me.simple.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import me.drakeet.multitype.ItemViewBinder
import me.drakeet.multitype.MultiTypeAdapter

class MainActivity : AppCompatActivity() {

    private var mItems = mutableListOf<String>()
    private var mAdapter = MultiTypeAdapter(mItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAdapter.register(String::class, ItemBinder())

        recyclerView.run {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mAdapter
        }

        mItems.add("crop")

        mAdapter.notifyDataSetChanged()
    }

    inner class ItemBinder : ItemViewBinder<String, VH>() {

        override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH {
            return VH(inflater.inflate(R.layout.item_holder, parent, false))
        }

        override fun onBindViewHolder(holder: VH, item: String) {
            holder.tvModule.text = item
        }
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvModule: TextView = itemView.findViewById(R.id.tv_module)
    }
}
