package com.example.arc

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var color1 = Color.parseColor("#FF8CC6")
    private var color2 = Color.parseColor("#8CA7FF")
    private var color3 = Color.parseColor("#FF6E9C")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        avatar.setColors(color1)
//        avatar.setColors(color1, color2)
//        avatar.setColors(color1, color2, color3)
    }
}
