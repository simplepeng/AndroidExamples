package com.example.image_browser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val imageList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageList.add(R.drawable.and1.toString())
        imageList.add(R.drawable.and3.toString())
        imageList.add(R.drawable.and2.toString())
        imageList.add(R.drawable.and3.toString())
        imageList.add(R.drawable.and1.toString())
        imageList.add(R.drawable.and2.toString())
        imageList.add(R.drawable.iu1.toString())

        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = Adapter()
    }

    inner class Adapter : RecyclerView.Adapter<VH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            return VH(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_image, parent, false))
        }

        override fun getItemCount() = imageList.size

        override fun onBindViewHolder(holder: VH, position: Int) {
            holder.imageView.setImageResource(imageList[position].toInt())
            holder.imageView.setOnClickListener {
                ImageBrowser.show(this@MainActivity, holder.imageView, imageList[position])
            }
        }

    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
    }
}
