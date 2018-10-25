package com.simple.scroller_demo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class ScrollerView extends LinearLayout {

    private Scroller mScroller;

    public ScrollerView(Context context) {
        this(context, null);
    }

    public ScrollerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(VERTICAL);
        mScroller = new Scroller(context);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            this.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    public void smoothScroll() {
        int startY = getScrollY();
        int toY = getScrollY() + 10;
        mScroller.startScroll(0, startY, 0, toY, 2000);
        invalidate();
    }
}
