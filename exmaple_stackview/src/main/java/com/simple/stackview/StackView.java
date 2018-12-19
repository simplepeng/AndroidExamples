package com.simple.stackview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class StackView extends ViewGroup {

    public StackView(Context context) {
        super(context);
    }

    public StackView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int width = 0;
        int height = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            if (child.getMeasuredHeight() > height) {
                height = child.getMeasuredHeight();
            }

            if (i != childCount - 1) {
                width += (child.getMeasuredWidth() / 2);
            } else {
                width += child.getMeasuredWidth();
            }
        }
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        switch (hMode) {
            case MeasureSpec.EXACTLY:
                height = MeasureSpec.getSize(heightMeasureSpec);
                break;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        if (!changed){
//            return;
//        }
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = getMeasuredHeight();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            right = left + child.getMeasuredHeight();
            child.layout(left, top, right, bottom);
            left += (child.getMeasuredWidth() / 2);
        }
    }
}
