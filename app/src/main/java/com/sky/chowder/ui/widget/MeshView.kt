package com.sky.chowder.ui.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView

import com.sky.utils.BitmapUtils

/**
 * Created by SKY on 2015/8/14 15:27.
 * drawbitmapmesh应用
 */
class MeshView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ImageView(context, attrs, defStyleAttr) {

    private val MESHWIDTH = 200
    private val MESHHEIGHT = 200
    private val COUNT = (MESHWIDTH + 1) * (MESHHEIGHT + 1)
    private val oldPoint = FloatArray(COUNT * 2)
    private val newPoint = FloatArray(COUNT * 2)
    private var mBitmap: Bitmap? = null

    init {
        super.setScaleType(ImageView.ScaleType.CENTER)
        initView()
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        mBitmap = bm
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        mBitmap = (drawable as BitmapDrawable).bitmap
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        mBitmap = BitmapUtils.getBitmapFromId(context,resId)
    }

    private fun initView() {
        val bitWidth = mBitmap!!.width.toFloat()
        val bitHeight = mBitmap!!.height.toFloat()
        for (i in 0..MESHHEIGHT) {
            val bitY = bitHeight * i / MESHHEIGHT
            for (j in 0..MESHHEIGHT) {
                val bitX = bitWidth * j / MESHWIDTH
                oldPoint[(i * (MESHHEIGHT + 1) + j) * 2 + 0] = bitX
                newPoint[(i * (MESHHEIGHT + 1) + j) * 2 + 0] = oldPoint[(i * (MESHHEIGHT + 1) + j) * 2 + 0]
                oldPoint[(i * (MESHHEIGHT + 1) + j) * 2 + 1] = bitY
                newPoint[(i * (MESHHEIGHT + 1) + j) * 2 + 1] = oldPoint[(i * (MESHHEIGHT + 1) + j) * 2 + 1]
            }
        }

    }

    private var k = 1f

    override fun onDraw(canvas: Canvas) {
        for (i in 0..MESHHEIGHT) {
            for (j in 0..MESHWIDTH) {
                val offSetY = Math.sin((j.toFloat() / MESHWIDTH).toDouble() * 2.0 * Math.PI + k.toDouble() * 2.0 * Math.PI).toFloat()
                newPoint[(i * (MESHHEIGHT + 1) + j) * 2 + 0] = oldPoint[(i * (MESHHEIGHT + 1) + j) * 2 + 0] + offSetY * 100 + 200f
                newPoint[(i * (MESHHEIGHT + 1) + j) * 2 + 1] = oldPoint[(i * (MESHHEIGHT + 1) + j) * 2 + 1] + offSetY * 50 + 400f
            }
        }
        canvas.drawBitmapMesh(mBitmap!!, MESHWIDTH, MESHHEIGHT, newPoint, 0, null, 0, null)
        k += 0.05f
        invalidate()
    }
}
