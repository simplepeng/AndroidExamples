package me.simple.example_custom_views.canvas

import android.os.Bundle
import me.simple.example_custom_views.BaseActivity
import me.simple.example_custom_views.R

class CanvasActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas)
    }
}