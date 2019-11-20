package me.simple.app

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
import me.simple.app.base.BaseActivity
import me.simple.app.crop.CropActivity

class MainActivity : BaseActivity() {

    private var mItems = mutableListOf<String>()
    private var mAdapter = MultiTypeAdapter(mItems)

    companion object {
        const val module_crop = "crop"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mItems.add(module_crop)
        mAdapter.register(String::class, ItemBinder(onItemClick = { item ->
            when (item) {
                module_crop -> {
                    CropActivity.start(mContext)
                }
                else -> {
                }
            }
        }))

        recyclerView.run {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mAdapter
        }
    }

    inner class ItemBinder(var onItemClick: (item: String) -> Unit) : ItemViewBinder<String, VH>() {

        override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH {
            return VH(inflater.inflate(R.layout.item_holder, parent, false))
        }

        override fun onBindViewHolder(holder: VH, item: String) {
            holder.tvModule.text = item

            holder.itemView.setOnClickListener {
                onItemClick.invoke(item)
            }
        }
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvModule: TextView = itemView.findViewById(R.id.tv_module)
    }
}
