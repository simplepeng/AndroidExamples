package com.simple.example_xfer_model

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.widget.ImageView
import android.graphics.Bitmap


class AvatarImageView : ImageView {

    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)

        val bitmapDrawable = drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        mPaint.isFilterBitmap = false

        val size = measuredWidth.toFloat()
        val sc = canvas?.saveLayer(0f, 0f, size, size, mPaint, Canvas.ALL_SAVE_FLAG)
        val dst = getCircleBitmap()
        canvas?.drawBitmap(dst, 0f, 0f, mPaint)
        mPaint.xfermode = mode
        val src = bitmap
        canvas?.drawBitmap(src, 0f, 0f, mPaint)
        mPaint.xfermode = null
        canvas?.restoreToCount(sc!!)
    }

    private fun getCircleBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        canvas.drawCircle(measuredWidth / 2.toFloat(), measuredHeight / 2.toFloat(),
                measuredHeight / 2.toFloat(), paint)
        return bitmap
    }
}