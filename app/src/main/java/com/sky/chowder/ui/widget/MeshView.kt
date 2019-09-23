package com.sky.chowder.ui.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.sky.utils.ScreenUtils

/**
 * Created by SKY on 2015/8/14 15:27.
 * drawbitmapmesh应用
 */
class MeshView @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyle: Int = 0) : AppCompatImageView(context, attrs, defStyle) {

    private val line = 200//行
    private val column = 200//列
    private val total = (line + 1) * (column + 1)//总数
    private val oldPoint = FloatArray(total * 2)
    private val newPoint = FloatArray(total * 2)

    private var bitmap: Bitmap? = null
    private var temp = 1.0//每次的变量


    init {
        scaleType = ImageView.ScaleType.CENTER
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val bitWidth = bitmap!!.width * 1f
        val bitHeight = bitmap!!.height * 1f
        for (i in 0 until total) {
            oldPoint[i * 2 + 0] = bitWidth * (i % (column + 1)) / column
            oldPoint[i * 2 + 1] = bitHeight * (i / (line + 1)) / line
            newPoint[i * 2 + 0] = oldPoint[i * 2 + 0]
            newPoint[i * 2 + 1] = oldPoint[i * 2 + 1]
        }
        setMeasuredDimension(ScreenUtils.getWidthPX(context), ScreenUtils.getWidthPX(context))
    }

    override fun onDraw(canvas: Canvas) {
        for (i in 0 until total) {
            val offSetY = Math.sin(i % (column + 1.0) / line * 2.0 * Math.PI + temp * 2.0 * Math.PI).toFloat()
            newPoint[i * 2 + 0] = oldPoint[i * 2 + 0] + offSetY * 25 + 100
            newPoint[i * 2 + 1] = oldPoint[i * 2 + 1] + offSetY * 25 + 100
        }
        canvas.drawBitmapMesh(bitmap!!, line, column, newPoint, 0, null, 0, null)
        temp += 0.05//每次增加多少
        invalidate()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        bitmap = (drawable as BitmapDrawable).bitmap
    }
}
