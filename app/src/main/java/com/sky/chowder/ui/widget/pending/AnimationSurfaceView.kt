package com.sky.chowder.ui.widget.pending

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * Created by 李彬 on 2017/3/6.
 */

class AnimationSurfaceView(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {

    // 用于控制SurfaceView
    private var sfh: SurfaceHolder? = null
    // 声明一个画笔
    private val paint: Paint
    // 声明一条线程
    private var th: Thread? = null
    // 用于控制线程的标识符
    private var flag: Boolean = false
    // 声明一个画布
    private var canvas: Canvas? = null

    init {
        // ///////////SurfaceView框架/////////////////////////////
        sfh = this.holder as SurfaceHolder
        sfh!!.addCallback(this)
        canvas = Canvas()
        paint = Paint()
        paint.color = Color.WHITE
        paint.isAntiAlias = true
        this.isFocusable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {

        screenW = this.width
        screenH = this.height
        flag = true
        //        mQQqInfoView = new QQInfoView(context);
        th = Thread(this)
        th!!.start()
    }

    /**
     * 绘制画面
     */
    fun myDraw() {
        try {
            canvas = sfh!!.lockCanvas()
            if (canvas != null) {
                canvas!!.drawColor(Color.WHITE)
            }
        } catch (e: Exception) {

        } finally {
            if (canvas != null) {
                sfh!!.unlockCanvasAndPost(canvas)
            }
        }
    }

    /**
     * 页面逻辑
     */
    fun logic() {

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        return true
    }

    override fun run() {
        while (flag) {
            val start = System.currentTimeMillis()
            myDraw()
            logic()
            val end = System.currentTimeMillis()
            try {
                if (end - start < 50) {
                    Thread.sleep(50 - (end - start))
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 画布状态改变监听事件
     */
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int,
                                height: Int) {

    }

    /**
     * 画布被摧毁事件
     */
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        flag = false
    }

    companion object {
        // 定义高和宽
        var screenW: Int = 0
        var screenH: Int = 0
    }

}