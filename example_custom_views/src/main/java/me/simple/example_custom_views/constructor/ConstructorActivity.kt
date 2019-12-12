package me.simple.example_custom_views.constructor

import android.os.Bundle
import me.simple.example_custom_views.BaseActivity
import me.simple.example_custom_views.R

class ConstructorActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constructor)


    }
}