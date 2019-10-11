package com.sky.oa.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.sky.oa.R
import com.sky.sdk.utils.BitmapUtils

/**
 * Created by SKY on 2015/8/7 17:45.
 */
class XferModeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var outBitmap: Bitmap? = null

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
//        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_banner)
        val bitmap = BitmapUtils.getBitmapFromId(context, R.mipmap.ic_banner)
        outBitmap = Bitmap.createBitmap(bitmap!!.width, bitmap!!.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(outBitmap!!)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            canvas.drawRoundRect(0f, 0f, bitmap!!.width.toFloat(), bitmap!!.height.toFloat(), 50f, 50f, paint!!)
//        else
        canvas.drawCircle((bitmap!!.width / 2).toFloat(), (bitmap!!.height / 2).toFloat(), (bitmap!!.height / 2).toFloat(), paint!!)

        paint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap!!, 0f, 0f, paint)
        paint!!.xfermode = null

    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(outBitmap!!, 0f, 0f, null)
    }
}
