package com.simple.itemdecoration_demo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class ItemDecoration2 extends RecyclerView.ItemDecoration {

    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        outRect.set(100, 0, 0, 10);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int childCount = parent.getLayoutManager().getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            ViewGroup.LayoutParams params = child.getLayoutParams();

            mPaint.setColor(Color.BLUE);
            c.drawLine(child.getLeft(), child.getBottom(),
                    child.getRight(), child.getBottom(),
                    mPaint);

            mPaint.setColor(Color.RED);
            mPaint.setTextSize(60);
            c.drawText("A", child.getLeft(),child.getTop(), mPaint);
        }
    }
}
