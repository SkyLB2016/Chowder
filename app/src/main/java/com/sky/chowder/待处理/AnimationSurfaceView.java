package com.sky.chowder.待处理;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by 李彬 on 2017/3/6.
 */

public class AnimationSurfaceView extends SurfaceView
        implements SurfaceHolder.Callback, Runnable {

    private Context context = null;

    // 用于控制SurfaceView
    private SurfaceHolder sfh = null;
    // 声明一个画笔
    private Paint paint;
    // 声明一条线程
    private Thread th = null;
    // 用于控制线程的标识符
    private boolean flag;
    // 声明一个画布
    private Canvas canvas;
    // 定义高和宽
    public static int screenW, screenH;

    public AnimationSurfaceView(Context context) {
        super(context);
        this.context = context;
        // ///////////SurfaceView框架/////////////////////////////
        sfh = (SurfaceHolder) this.getHolder();
        sfh.addCallback(this);
        canvas = new Canvas();
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        this.setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        screenW = this.getWidth();
        screenH = this.getHeight();
        flag = true;
//        mQQqInfoView = new QQInfoView(context);
        th = new Thread(this);
        th.start();
    }

    /**
     * 绘制画面
     */
    public void myDraw() {
        try {
            canvas = sfh.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.WHITE);
            }
        } catch (Exception e) {

        } finally {
            if (canvas != null) {
                sfh.unlockCanvasAndPost(canvas);
            }
        }
    }

    /**
     * 页面逻辑
     */
    public void logic() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return true;
    }

    @Override
    public void run() {
        while (flag) {
            long start = System.currentTimeMillis();
            myDraw();
            logic();
            long end = System.currentTimeMillis();
            try {
                if (end - start < 50) {
                    Thread.sleep(50 - (end - start));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 画布状态改变监听事件
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    /**
     * 画布被摧毁事件
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;
    }

}