package me.simple.app.crop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.simple.app.R
import java.security.AccessControlContext

class CropActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop)

    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, CropActivity::class.java))
        }
    }
}