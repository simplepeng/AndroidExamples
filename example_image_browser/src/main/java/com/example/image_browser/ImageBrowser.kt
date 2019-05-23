package com.example.image_browser

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat

class ImageBrowser {

    companion object {

        fun show(context: Context, view: View, url: String) {
            val urls = arrayListOf<String>()
            urls.add(url)
            show(context, view, urls)
        }

        fun show(context: Context, view: View, urls: ArrayList<String>, index: Int = 0) {
            val intent = Intent(context, ImageBrowserActivity::class.java)
            intent.putExtra("images", urls)
            intent.putExtra("index", index)


//            val optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(view,
//                    view.width / 2, view.height / 2,
//                    0, 0)

            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity,
                    view,"shareView")
            try {
                ActivityCompat.startActivity(context, intent, optionsCompat.toBundle())
            } catch (e: Exception) {
                ImageBrowserActivity.start(context, urls, index)
            }
        }


    }

}