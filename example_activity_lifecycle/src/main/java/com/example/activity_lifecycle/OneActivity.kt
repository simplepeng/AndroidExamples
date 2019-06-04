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

        Log.e(MainActivity.TAG, "OneActivity onCreate")

        tv_num.text = "one"

        tv_num.setOnClickListener {
            startActivity(Intent(this@OneActivity, TwoActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        Log.e(MainActivity.TAG, "OneActivity onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e(MainActivity.TAG, "OneActivity onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e(MainActivity.TAG, "OneActivity onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e(MainActivity.TAG, "OneActivity onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(MainActivity.TAG, "OneActivity onDestroy")
    }
}