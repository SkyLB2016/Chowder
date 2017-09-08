package com.sky.chowder.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 *  采用双缓冲实现画图板
 * Created by 李彬 on 2016/9/5.
 */

public class DrawView extends View {
    private Bitmap cacheBitmap;
    private Canvas cacheCanvas;
    private Paint cachePaint;
    private Path cachePath;
    private Context context;

    public Paint getCachePaint() {
        return cachePaint;
    }

    public void setCachePaint(Paint cachePaint) {
        this.cachePaint = cachePaint;
    }

    public DrawView(Context context) {
        this(context, null);
    }

    public DrawView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //创建一个与该view具有相同大小的缓冲区
        cacheBitmap = Bitmap.createBitmap(getMeasuredWidth()
                , getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        //创建画布并绘制cacheBitmap
        cacheCanvas = new Canvas(cacheBitmap);
        //初始化路径
        cachePath = new Path();
        //设置画笔
        cachePaint = new Paint(Paint.DITHER_FLAG);
        cachePaint.setColor(Color.GREEN);
        cachePaint.setStrokeWidth(3);
        cachePaint.setStyle(Paint.Style.STROKE);
        //反锯齿
        cachePaint.setAntiAlias(true);
        cachePaint.setDither(true);
    }

    private float preX;
    private float preY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //添加到path
                cachePath.moveTo(x, y);
                //记录xy点
                preX = x;
                preY = y;

                break;
            case MotionEvent.ACTION_MOVE:
                //二次贝塞尔曲线
                cachePath.quadTo(preX, preY, x, y);
                preX = x;
                preY = y;
                //cachePath.lineTo(x, y);//此方法不需要记忆xy

                //在此绘图太过频繁
                //cacheCanvas.drawPath(cachePath, cachePaint);
                break;
            case MotionEvent.ACTION_UP:
                //只在抬起的时候向缓冲区中绘图
                cacheCanvas.drawPath(cachePath, cachePaint);
                cachePath.reset();//清空path
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Paint paint = new Paint();
        //将缓冲区中的bitmap对象会知道view上
        canvas.drawBitmap(cacheBitmap, 0, 0, cachePaint);
        //沿着path绘制
        canvas.drawPath(cachePath, cachePaint);
        if (cachePath.isEmpty())
            Toast.makeText(context, "cachePath已经清空", Toast.LENGTH_SHORT).show();

    }
}
