package com.example.activity_lifecycle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_base.*

class OneActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        tv_num.text = "one"

        tv_num.setOnClickListener {
            startActivity(Intent(this@OneActivity, TwoActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(MainActivity.TAG, "OneActivity onDestroy")
    }
}