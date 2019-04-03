package com.example.databinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databinding.databinding.ActivityMainBinding
import com.example.databinding.databinding.ItemLayotBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DataBindingActivity<ActivityMainBinding>() {

    override fun getLayoutRes() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var items = listOf<String>("java", "python", "c++", "go", "rust")

        var adapter = Adapter()
        adapter.setItems(items)

        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }


    inner class Adapter : DataBindingAdapter<String, ItemLayotBinding>() {

        override fun onBindViewHolder(viewHolder: DataBindingViewHolder<ItemLayotBinding>, dataBinding: ItemLayotBinding, item: String) {
            dataBinding.text.text = item
        }


        override fun getLayoutRes() = R.layout.item_layot

    }
}
