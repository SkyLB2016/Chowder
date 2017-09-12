package com.sky.chowder.ui.widget

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.sky.chowder.R

/**
 * Created by SKY on 2015/8/7 17:45.
 */
class XferModeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var mBitmap: Bitmap? = null
    private var mOut: Bitmap? = null
    private var paint: Paint? = null

    init {
        initView()
    }

    private fun initView() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        mBitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        mOut = Bitmap.createBitmap(mBitmap!!.width, mBitmap!!.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mOut!!)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            canvas.drawRoundRect(0f, 0f, mBitmap!!.width.toFloat(), mBitmap!!.height.toFloat(), 50f, 50f, paint!!)
        else
            canvas.drawCircle((mBitmap!!.width / 2).toFloat(), (mBitmap!!.height / 2).toFloat(), (mBitmap!!.height / 2).toFloat(), paint!!)

//        val rectF = RectF(0f, 0f, mBitmap!!.width.toFloat(), mBitmap!!.height.toFloat())
//        canvas.drawRoundRect(rectF, 50f, 50f, paint!!)
//        canvas.drawOval(rectF, paint!!)

        paint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(mBitmap!!, 0f, 0f, paint)
//        canvas.drawBitmap(mBitmap!!, null, rectF, paint)
        paint!!.xfermode = null

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mOut!!, 0f, 0f, null)
    }
}
