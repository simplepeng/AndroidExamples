package me.simple.example_custom_views.constructor;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import me.simple.example_custom_views.R;

public class CustomView extends View {

    private static final String TAG = "CustomView";

    /**
     * Simple constructor to use when creating a view from code
     * 当直接使用代码创建View时使用
     */
    public CustomView(Context context) {
        super(context);
    }

    /**
     * Constructor that is called when inflating a view from XML
     * The method onFinishInflate() will be called after all children have been
     */
    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        int[] attrArr = new int[]{R.attr.attr1, android.R.attr.text};
        TypedArray typedArray = context.obtainStyledAttributes(attrs, attrArr);
        String attr1 = typedArray.getString(R.styleable.CustomView_attr1);
        Log.d(TAG, "CustomView: " + attr1);

        final int index = 1;
        String text = typedArray.getString(index);
        Log.d(TAG, "CustomView: " + text);

        typedArray.recycle();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Api21新增的方法
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
