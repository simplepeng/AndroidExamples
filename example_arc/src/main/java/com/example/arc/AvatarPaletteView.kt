package com.example.arc

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class AvatarPaletteView : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)


    private var mColors = mutableListOf<Int>()

    init {
        mPaint.style = Paint.Style.FILL
    }

    fun setColors(vararg colors: Int) {
        mColors.addAll(colors.asList())
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (mColors.size) {
            1 -> {
                drawOne(canvas)
            }
            2 -> {
                drawTwo(canvas)
            }
            3 -> {
                drawThree(canvas)
            }
            else -> {
                throw Exception("color num can not > 3")
            }
        }
    }

    private fun drawOne(canvas: Canvas) {
        mPaint.color = mColors[0]
        val oval1 = RectF(0F, 0F, measuredWidth.toFloat(), measuredHeight.toFloat())
        canvas.drawArc(oval1, 0F, 360F, true, mPaint)
    }

    private fun drawTwo(canvas: Canvas) {
        mPaint.color = mColors[0]
        val oval1 = RectF(0F, 0F, measuredWidth.toFloat(), measuredHeight.toFloat())
        canvas.drawArc(oval1, -180F, 180F, true, mPaint)
        mPaint.color = mColors[1]
        val oval2= RectF(0F, 0F, measuredWidth.toFloat(), measuredHeight.toFloat())
        canvas.drawArc(oval2, 0F, 180F, true, mPaint)
    }

    private fun drawThree(canvas: Canvas) {
        mPaint.color = mColors[0]
        val oval1 = RectF(0F, 0F, measuredWidth.toFloat(), measuredHeight.toFloat())
        canvas.drawArc(oval1, -120F, 120F, true, mPaint)
        mPaint.color = mColors[1]
        val oval2= RectF(0F, 0F, measuredWidth.toFloat(), measuredHeight.toFloat())
        canvas.drawArc(oval2, 0F, 120F, true, mPaint)
        mPaint.color = mColors[2]
        val oval3= RectF(0F, 0F, measuredWidth.toFloat(), measuredHeight.toFloat())
        canvas.drawArc(oval3, 120F, 120F, true, mPaint)
    }
}