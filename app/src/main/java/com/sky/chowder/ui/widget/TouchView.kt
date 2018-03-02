package com.sky.chowder.ui.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*

/**
 * Created by SKY on 2018/2/24 14:49.
 */
class TouchView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    internal var lastX = -1f
    internal var lastY = -1f
    internal var cX = -1f
    internal var cY = -1f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.rawX
                lastY = event.rawY
                cX = event.x
                cY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val dX = (event.rawX - lastX).toInt()
                val dY = (event.rawY - lastY).toInt()

//                layout(left + dX, top + dY, right + dX, bottom + dY)
//                offsetLeftAndRight(dX)
//                offsetTopAndBottom(dY)
//                (parent as View).scrollBy(-dX, -dY)
                cX = event.x
                cY = event.y
                lastX = event.rawX
                lastY = event.rawY
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                lastX = -1f
                lastY = -1f
                cY = -1f
                cX = cY
            }
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint()
        paint.color = Color.RED
        if (lastX != -1f) canvas.drawCircle(cX, cY, 88f, paint)
        paint.textSize = 80f
        canvas.drawText("道可道非常道", 0f, 100f, paint)

        paint.style = Paint.Style.STROKE
        canvas.drawCircle(540f, 960f, 540f, paint)
        paint.style = Paint.Style.FILL
        for (i in 0..23) {
            if (i === 0 || i === 6 || i === 12 || i === 18) {
                paint.strokeWidth = 8f
                paint.textSize = 30f
                canvas.drawLine(540f, 420f, 540f, 480f, paint)
                val w = paint.measureText(i.toString())
                canvas.drawText(i.toString(), 540f - w / 2, 480f + paint.descent() - paint.ascent(), paint)
            } else {
                paint.strokeWidth = 4f
                paint.textSize = 20f
                canvas.drawLine(540f, 420f, 540f, 440f, paint)
                val w = paint.measureText(i.toString())
                canvas.drawText(i.toString(), 540f - w / 2, 440f + paint.descent() - paint.ascent(), paint)
            }
            canvas.rotate(15f, 540f, 960f)
        }
        paint.strokeWidth = 12f
        canvas.save()
        canvas.translate(540f, 960f)
        canvas.drawCircle(0f, 0f, 16f, paint)
        canvas.drawLine(0f, 0f, 100f, 100f, paint)
        paint.strokeWidth = 8f
        canvas.drawLine(0f, 0f, 100f, 200f, paint)
        canvas.restore()
//        canvas.save()

        canvas.drawCircle(150f, 150f, 100f, paint)
        canvas.saveLayerAlpha(0f, 0f, 300f, 300f, 122, Canvas.ALL_SAVE_FLAG)
        paint.color = Color.GREEN
        canvas.drawCircle(200f, 200f, 100f, paint)
        canvas.drawCircle(300f, 300f, 100f, paint)
        canvas.restore()


        val random = Random()
        val list = (0..30).mapTo(ArrayList()) { Point(100 + it * 30, random.nextInt(500) + 500) }
        val ee = CornerPathEffect(10f)
        val path3 = Path()
        path3.moveTo(list[0].x.toFloat(), list[0].y.toFloat() + 700)//设置Path的起点
        for (i in 1 until list.size) {
            path3.lineTo(list[i].x.toFloat(), list[i].y.toFloat() + 700)
        }
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        paint.pathEffect = ee
        canvas.drawPath(path3, paint)
        animate().alpha(1f)
                .rotation(360f)
                .setDuration(900)
                .withStartAction {  }
                .withEndAction {  }
                .start()

    }
}
