package com.example.activity_lifecycle_export

import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_export.setOnClickListener {
            //第一个参数是Activity所在的package包名，第二个参数是完整的Class类名（包括包路径）
            val componetName =  ComponentName("com.example.activity_lifecycle",
                    "com.example.activity_lifecycle.MainActivity")
            val intent =  Intent()
            intent.setComponent(componetName);
            startActivity(intent);
        }
    }
}
