package demo.simple.example_keyboard_new_api

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val btnToggle by lazy { findViewById<View>(R.id.btnToggle) }
    private val editText by lazy { findViewById<EditText>(R.id.editText) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val windowInsetsController = ViewCompat.getWindowInsetsController(editText)


        btnToggle.setOnClickListener {
            val rootWindowInsets = ViewCompat.getRootWindowInsets(editText)
                    ?: return@setOnClickListener
            if (rootWindowInsets.isVisible(WindowInsetsCompat.Type.ime())) {
                windowInsetsController?.hide(WindowInsetsCompat.Type.ime())
            } else {
                windowInsetsController?.show(WindowInsetsCompat.Type.ime())
            }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        ViewCompat.setOnApplyWindowInsetsListener(btnToggle) { view, insets ->
            val sysWindow = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            Log.d("MainActivity","bottom -- ${sysWindow.bottom}")
            view.translationY = -sysWindow.bottom.toFloat()
            insets
        }
    }


}