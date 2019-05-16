package com.example.image_browser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.commit451.elasticdragdismisslayout.ElasticDragDismissFrameLayout
import com.commit451.elasticdragdismisslayout.ElasticDragDismissListener
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import kotlinx.android.synthetic.main.activity_image_browser.*

class ImageBrowserActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_image_browser)

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

            iv_browser.setImage(ImageSource.resource(item.toInt()))
            dragLayout.addListener(object :ElasticDragDismissListener{
                override fun onDrag(elasticOffset: Float, elasticOffsetPixels: Float, rawOffset: Float, rawOffsetPixels: Float) {
                }

                override fun onDragDismissed() {
                    this@ImageBrowserActivity.finish()
                }
            })
        }
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