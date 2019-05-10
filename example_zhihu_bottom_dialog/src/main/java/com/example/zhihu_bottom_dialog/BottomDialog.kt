package com.example.zhihu_bottom_dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup

class BottomDialog(context: Context) : Dialog(context, R.style.CustomDialog) {

    private var isExpand = false

    init {
        setContentView(R.layout.dialog_bottom)
        val a = window.attributes
        a.width = ViewGroup.LayoutParams.MATCH_PARENT
        a.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window.setGravity(Gravity.BOTTOM)
        window.attributes = a
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootView = findViewById<View>(R.id.rootView)
//        val a = window.attributes
        val layoutParams = rootView.layoutParams
        findViewById<View>(R.id.tv_expand)
                .setOnClickListener {
                    if (isExpand) {
//                        a.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        isExpand = false
                    } else {
//                        a.height = ViewGroup.LayoutParams.MATCH_PARENT
                        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                        isExpand = true
                    }
//                    window.attributes = a
                    rootView.layoutParams = layoutParams
                    rootView.requestLayout()
                    invalidateOptionsMenu()
                }
    }
}