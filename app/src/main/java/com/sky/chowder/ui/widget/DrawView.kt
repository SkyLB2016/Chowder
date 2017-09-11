package com.sky.chowder.ui.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

/**
 * 采用双缓冲实现画图板
 * Created by SKY on 2016/9/5.
 */
class DrawView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var cacheBitmap: Bitmap? = null
    private var cacheCanvas: Canvas? = null
    var cachePaint: Paint? = null
    private var cachePath: Path? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //创建一个与该view具有相同大小的缓冲区
        cacheBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        //创建画布并绘制cacheBitmap
        cacheCanvas = Canvas(cacheBitmap!!)
        //初始化路径
        cachePath = Path()
        //设置画笔
        cachePaint = Paint(Paint.DITHER_FLAG)
        cachePaint!!.color = Color.GREEN
        cachePaint!!.strokeWidth = 3f
        cachePaint!!.style = Paint.Style.STROKE
        //反锯齿
        cachePaint!!.isAntiAlias = true
        cachePaint!!.isDither = true
    }

    private var preX: Float = 0.toFloat()
    private var preY: Float = 0.toFloat()

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //添加到path
                cachePath!!.moveTo(x, y)
                //记录xy点
                preX = x
                preY = y
            }
            MotionEvent.ACTION_MOVE -> {
                //二次贝塞尔曲线
                cachePath!!.quadTo(preX, preY, x, y)
                preX = x
                preY = y
            }
            MotionEvent.ACTION_UP -> {
                //只在抬起的时候向缓冲区中绘图
                cacheCanvas!!.drawPath(cachePath!!, cachePaint!!)
                cachePath!!.reset()//清空path
            }
        }//cachePath.lineTo(x, y);//此方法不需要记忆xy
        //在此绘图太过频繁
        //cacheCanvas.drawPath(cachePath, cachePaint);
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //        Paint paint = new Paint();
        //将缓冲区中的bitmap对象会知道view上
        canvas.drawBitmap(cacheBitmap!!, 0f, 0f, cachePaint)
        //沿着path绘制
        canvas.drawPath(cachePath!!, cachePaint!!)
        if (cachePath!!.isEmpty)
            Toast.makeText(context, "cachePath已经清空", Toast.LENGTH_SHORT).show()

    }
}
