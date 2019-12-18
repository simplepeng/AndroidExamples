package me.simple.example_custom_views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import me.simple.example_custom_views.constructor.ConstructorActivity
import me.simple.example_custom_views.measure.MeasureActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun constructor(view: View) {
        navTo(ConstructorActivity::class.java)
    }

    fun measure(view: View) {
        navTo(MeasureActivity::class.java)
    }
}
