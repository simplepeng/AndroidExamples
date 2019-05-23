package com.example.image_browser

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import com.commit451.elasticdragdismisslayout.ElasticDragDismissFrameLayout
import com.commit451.elasticdragdismisslayout.ElasticDragDismissListener
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import kotlinx.android.synthetic.main.activity_image_browser.*

class ImageBrowserActivity : AppCompatActivity() {

    val TAG = "ImageBrowserActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_image_browser)

        rootView.setBackgroundColor(Color.BLACK)
        val urls = intent.getStringArrayListExtra("images")
        val index = intent.getIntExtra("index", 0)

        viewPager.run {
            adapter = VpAdapter(this@ImageBrowserActivity, urls)
        }


    }

    inner class VpAdapter : BaseVpAdapter<String> {

        constructor(context: Context?, datas: MutableList<String>) : super(context, datas)


        override fun getLayoutId() = R.layout.item_browser_image

        override fun convert(itemView: View, position: Int, item: String) {
            val iv_browser = itemView.findViewById<SubsamplingScaleImageView>(R.id.iv_browser)
            val dragLayout = itemView.findViewById<ElasticDragDismissFrameLayout>(R.id.dragLayout)
            val contentView = itemView.findViewById<View>(R.id.contentView)
            contentView.setBackgroundColor(Color.BLACK)

            iv_browser.setImage(ImageSource.resource(item.toInt()))
            dragLayout.addListener(object : ElasticDragDismissListener {
                override fun onDrag(elasticOffset: Float, elasticOffsetPixels: Float,
                                    rawOffset: Float, rawOffsetPixels: Float) {
                    debug("elasticOffset == " + elasticOffset)//0-0.5
//                    debug("elasticOffsetPixels == " + elasticOffsetPixels)
                    //0-255(0是完全透明),dis = 511
                    var alpha = 255-(elasticOffset * 511)
                    if (alpha > 255) {
                        alpha = 255f
                    } else if (alpha < 0) {
                        alpha = 0f
                    }
                    debug("alpha == " + alpha)
                    val color = ColorUtils.setAlphaComponent(Color.BLACK, alpha.toInt())
                    rootView.setBackgroundColor(color)
                }

                override fun onDragDismissed() {
                    this@ImageBrowserActivity.onBackPressed()
                }
            })
        }
    }

    fun debug(message: String) {
        Log.d(TAG, message)
    }

    companion object {
        fun start(context: Context, urls: ArrayList<String>, index: Int) {
            val intent = Intent(context, ImageBrowserActivity::class.java)
            intent.putExtra("images", urls)
            intent.putExtra("index", index)
            context.startActivity(intent)
        }
    }
}