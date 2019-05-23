package com.example.layout_manager

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CircleTextView : AppCompatTextView {

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawCircle(width / 2.toFloat(), width / 2.toFloat(), width / 2.toFloat(), mPaint)
        super.onDraw(canvas)
    }

    fun setColor(color: Int) {
        mPaint.color = color
        invalidate()
    }
}