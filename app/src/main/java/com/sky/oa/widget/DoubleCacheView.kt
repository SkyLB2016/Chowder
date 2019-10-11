package com.sky.oa.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


/**
 * 采用双缓冲实现画图板
 * Created by SKY on 2016/9/5.
 */
class DoubleCacheView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var cacheBitmap: Bitmap? = null
    private var cacheCanvas: Canvas? = null
    var cachePaint: Paint? = null
    private var cachePath: Path? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //创建一个与该view具有相同大小的缓冲区
        cacheBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        cacheCanvas = Canvas(cacheBitmap!!)
        cachePath = Path()
        //设置画笔
        cachePaint = Paint(Paint.DITHER_FLAG)
        cachePaint?.color = Color.GREEN
        cachePaint?.strokeWidth = 30f
        cachePaint?.style = Paint.Style.STROKE
        //反锯齿
        cachePaint?.isAntiAlias = true
        cachePaint?.isDither = true
//        cachePaint?.maskFilter = BlurMaskFilter(80f, BlurMaskFilter.Blur.INNER)
        cachePaint?.maskFilter = EmbossMaskFilter(floatArrayOf(1.5f, 1.5f, 1.5f), 0.6f, 6f, 4.2f)
    }

    private var preX: Float = 0f
    private var preY: Float = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //添加到path
                cachePath?.moveTo(x, y)
                //记录xy点
                preX = x
                preY = y
            }
            MotionEvent.ACTION_MOVE -> {
                //二次贝塞尔曲线
                cachePath?.quadTo(preX, preY, x, y)
                preX = x
                preY = y
            }
            MotionEvent.ACTION_UP -> {
                //只在抬起的时候向缓冲区中绘图
                cacheCanvas?.drawPath(cachePath!!, cachePaint!!)
                cachePath?.reset()//清空path
            }
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //将缓冲区中的bitmap对象会知道view上
        canvas.drawBitmap(cacheBitmap!!, 0f, 0f, cachePaint)
        //沿着path绘制
        canvas.drawPath(cachePath!!, cachePaint!!)
    }
}
