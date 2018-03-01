package com.sky.chowder.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by SKY on 2018/3/1 18:26.
 */
public class SurfaceTest extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder holder;
    private Canvas canvas;
    private boolean flag;

    public SurfaceTest(Context context) {
        this(context, null);
    }

    public SurfaceTest(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
        holder.setFormat(PixelFormat.OPAQUE);

        path = new Path();
//        path.moveTo(1f, 0f);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(10f);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        flag = true;
        new Thread(this).start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;
    }

    int x = 0;
    int y = 0;
    Path path;
    Paint paint;

    @Override
    public void run() {
        while (flag) {
            draw();
//            x += 1;
//            y = (int) (100 * Math.sin(x * 2 * Math.PI / 180) + 400);
//            path.lineTo(x, y);
        }
    }

    private void draw() {
        try {
            canvas = holder.lockCanvas();
            canvas.drawPath(path, paint);

        } catch (Exception e) {

        } finally {
            if (canvas != null) holder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return true;
    }
}
