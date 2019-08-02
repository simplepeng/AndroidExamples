package com.example.activity_lifecycle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_base.*

class TwoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        tv_num.text = "two"

        tv_num.setOnClickListener {
            val intent = Intent(this@TwoActivity, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
//            startActivity(Intent(this@TwoActivity, MainActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(MainActivity.TAG, "TwoActivity onDestroy")
    }
}