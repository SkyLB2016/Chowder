package com.sky.chowder.ui.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.sky.chowder.R

/**
 * Created by SKY on 2015/8/14 14:21.
 * shader应用，倒影效果
 */
class ShaderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var bitmap: Bitmap? = null
    private var refBitmap: Bitmap? = null
    private var paint: Paint? = null

    init {
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        val matrix = Matrix()
        matrix.setScale(1f, -1f)
        refBitmap = Bitmap.createBitmap(bitmap!!, 0, 0, bitmap!!.width, bitmap!!.height, matrix, true)
        paint = Paint()
        paint!!.shader = LinearGradient(0f, bitmap!!.height.toFloat(), 0f, bitmap!!.height * 1.8f, 0xEE000000.toInt(), 0x10000000, Shader.TileMode.CLAMP)
//        paint!!.shader = LinearGradient(0f, bitmap!!.height.toFloat(), 0f, bitmap!!.height * 1.8f, Color.RED, Color.BLUE, Shader.TileMode.CLAMP)
        paint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap!!, 0f, 0f, null)
        canvas.drawBitmap(refBitmap!!, 0f, bitmap!!.height.toFloat(), null)
        canvas.drawRect(0f, bitmap!!.height.toFloat(), bitmap!!.width.toFloat(), (bitmap!!.height * 2).toFloat(), paint!!)
    }
}
