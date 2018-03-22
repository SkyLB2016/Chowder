package com.sky.chowder.ui.widget.pending

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

/**
 * Created by SKY on 2015/8/17 15:30.
 */
class EyeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0 ) : FrameLayout(context, attrs, defStyleAttr) {

    private var paint: Paint? = null
    private var bitmap: Bitmap? = null

    init {
        isDrawingCacheEnabled = true
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    /**
     * 测试ondraw与dispatchDraw的区别，定义view用ondraw，定义viewgroup用dispatchDraw
     * dispatchDraw
     * View组件的绘制会调用draw(Canvas canvas)方法，
     * draw过程中主要是先画Drawable背景，
     * 对 drawable调用setBounds()然后是draw(Canvas c)方法.
     * 有点注意的是背景drawable的实际大小会影响view组件的大小，
     * drawable的实际大小通过getIntrinsicWidth()和getIntrinsicHeight()获取，
     * 当背景比较大时view组件大小等于背景drawable的大小.
     * 画完背景后，draw过程会调用onDraw(Canvas canvas)方法，
     * 然后就是dispatchDraw(Canvas canvas)方法, dispatchDraw()主要是分发给子组件进行绘制，
     * 我们通常定制组件的时候重写的是onDraw()方法。值得注意的是ViewGroup容器组件的绘制，
     * 当它没有背景时直接调用的是dispatchDraw()方法, 而绕过了draw()方法，
     * 当它有背景的时候就调用draw()方法，而draw()方法里包含了dispatchDraw()方法的调用。
     * 因此要在ViewGroup上绘制东西的时候往往重写的是dispatchDraw()方法而不是onDraw()方法，
     * 或者自定制一个Drawable，重写它的draw(Canvas c)和 getIntrinsicWidth(),getIntrinsicHeight()方法，然后设为背景。
     * @param canvas
     */
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (bitmap != null) {
            paint?.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
            canvas.drawBitmap(bitmap!!, 0f, 0f, paint)
            paint?.xfermode = null
        }
    }

    fun setRadius(radius: Int) {
        if (bitmap != null && !bitmap!!.isRecycled) {
            bitmap?.recycle()
        }
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap!!)
        canvas.drawCircle(width / 2f, height / 2f, radius.toFloat(), paint!!)
        invalidate()
    }

}
