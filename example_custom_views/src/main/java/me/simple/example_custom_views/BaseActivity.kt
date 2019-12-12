package me.simple.example_custom_views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    protected  fun <T : Any> navTo(clazz: Class<T>){
        val intent = Intent(this,clazz)
        startActivity(intent)
    }
}