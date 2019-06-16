package com.example.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FortuneView extends View {

    private int[] values = {3, 1, 3, 2, 4, 1, 2};

    private Paint leftPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private String[] leftTexts = {"100", "80", "60", "40", "20"};

    private Paint bottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private String[] bottomTexts = {"今天", "明天", "后天", "1", "2", "3", "4"};

    private Paint chartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private List<Integer> mXList = new ArrayList<>();
    private List<Integer> mYList = new ArrayList<>();

    //垂直方向一段的距离
    private int mYDis;
    private List<Point> mPoints = new ArrayList<>();
    private Path path = new Path();

    private Shader mShader;
    private Paint shaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path shaderPath = new Path();

    public FortuneView(Context context) {
        super(context);
    }

    public FortuneView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        leftPaint.setTextSize(dp2px(11));
        bottomPaint.setTextSize(dp2px(13));

        chartPaint.setColor(Color.RED);
        chartPaint.setStyle(Paint.Style.STROKE);
        chartPaint.setStrokeWidth(dp2px(3));
        chartPaint.setPathEffect(new CornerPathEffect(dp2px(10)));

        shaderPaint.setColor(Color.BLACK);
        shaderPaint.setStyle(Paint.Style.FILL);
        shaderPaint.setStrokeWidth(dp2px(3));
        shaderPaint.setPathEffect(new CornerPathEffect(dp2px(10)));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mShader == null) {
            int[] colors = {Color.RED, Color.TRANSPARENT};
            mShader = new LinearGradient(0, 0, 0, getHeight(), colors, null, Shader.TileMode.CLAMP);
            shaderPaint.setShader(mShader);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(Color.YELLOW);
        drawLeft(canvas);
        drawBottom(canvas);
        drawChart(canvas);
    }

    private void drawLeft(Canvas canvas) {
        Paint.FontMetrics metrics = bottomPaint.getFontMetrics();
        int bottomText_h = (int) Math.abs(metrics.top - metrics.bottom);
        int leftHeight = (int) (getHeight() - bottomText_h * 2);
        int dis = leftHeight / leftTexts.length;
        mYDis = dis;
        for (int i = 1; i <= leftTexts.length; i++) {
            int y = dis * i;
            canvas.drawText(leftTexts[i - 1], 0, y, leftPaint);
            mYList.add(y);
        }
    }

    private void drawBottom(Canvas canvas) {
        canvas.save();
        leftPaint.setTextAlign(Paint.Align.CENTER);
        int leftText_w = (int) leftPaint.measureText("100");
        canvas.translate(leftText_w, 0);
        int bottomWidth = getWidth() - leftText_w;
        Paint.FontMetrics metrics = bottomPaint.getFontMetrics();
        int bottomTextH = (int) Math.abs(metrics.top - metrics.bottom);
        int dis = bottomWidth / bottomTexts.length;
        for (int i = 0; i < bottomTexts.length; i++) {
            String text = bottomTexts[i];
            int x = dis * i;
            int y = getHeight() - bottomTextH;
            canvas.drawText(text, x, y, bottomPaint);
            mXList.add(x);
        }
        canvas.restore();
    }

    private void drawChart(Canvas canvas) {
        canvas.save();
        int leftText_w = (int) leftPaint.measureText("100");
        canvas.translate(leftText_w+(leftText_w/2), 0);
        Integer x1 = mXList.get(0);
        Integer x2 = mXList.get(1);
//        Integer x3 = mXList.get(2);
//        Integer x4 = mXList.get(3);
//        Integer x5 = mXList.get(4);
//        Integer x6 = mXList.get(5);
        Integer x7 = mXList.get(6);
//
        Integer y1 = mYList.get(0);
//        Integer y2 = mYList.get(1);
//        Integer y3 = mYList.get(2);
        Integer y4 = mYList.get(3);
        Integer y5 = mYList.get(4);
//
        for (Integer x : mXList) {
            Point point = new Point(x, getRowValue(values[mXList.indexOf(x)]));
            mPoints.add(point);
        }
//
//        Point p1 = mPoints.get(0);
//        Point p2 = mPoints.get(1);
//        Point p3 = mPoints.get(2);
//        Point p4 = mPoints.get(3);
//        Point p5 = mPoints.get(4);
//        Point p6 = mPoints.get(5);
        Point p7 = mPoints.get(6);
//
//        path.moveTo(x1, y5);
//        path.lineTo(p1.x, p1.y);
//        path.cubicTo(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
//        path.cubicTo(p3.x, p3.y, p4.x, p4.y, p5.x, p5.y);
//        path.cubicTo(p2.x, p2.y, p3.x, p3.y, p4.x, p4.y);
//        path.cubicTo(p4.x, p4.y, p5.x, p5.y, p6.x, p6.y);

//        path.lineTo(p7.x, y5);
//        path.close();
        for (int i = 0; i < mPoints.size(); i++) {
            Point point = mPoints.get(i);
            if (i == 0) {
                path.moveTo(point.x, point.y);
            } else {
                path.lineTo(point.x, point.y);
            }
        }
        shaderPath.set(path);
        shaderPath.lineTo(p7.x,p7.y);
        shaderPath.lineTo(x7,y5);
        shaderPath.lineTo(x7,y5);
        shaderPath.lineTo(x1,y5);
        shaderPath.lineTo(x1,y5);
//        shaderPath.lineTo(x1,y1);
        shaderPath.close();

        canvas.drawPath(path, chartPaint);
        canvas.drawPath(shaderPath,shaderPaint);

        canvas.restore();
    }

    private int getRowValue(int value) {
        return (5 - value+1) * mYDis;
    }

    public int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
