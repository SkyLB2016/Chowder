package com.sky.chowder.ui.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet

/**
 * Created by SKY on 2018/2/24 14:49.
 */
class Text : android.support.v7.widget.AppCompatTextView {

    internal var matrix: Matrix?=null
    internal var width: Int = 0
    internal var paint: Paint?=null
    internal var linear: LinearGradient?=null

    //    @Override
    //    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    //        super.onSizeChanged(w, h, oldw, oldh);
    //        width = getMeasuredWidth();
    //        if (width > 0) {
    //            paint = getPaint();
    //            linear = new LinearGradient(0, 0, width, 0, new int[]{Color.RED, Color.GREEN, Color.BLUE}, null, Shader.TileMode.CLAMP);
    //            paint.setShader(linear);
    //            matrix = new Matrix();
    //
    //        }
    //    }

    internal var tran: Int = 0

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        width = measuredWidth
        if (width > 0) {
            paint = getPaint()
            linear = LinearGradient(0f, 0f, width.toFloat(), 0f, intArrayOf(Color.RED, Color.GREEN, Color.BLUE), null, Shader.TileMode.CLAMP)
            paint?.shader = linear
            matrix = Matrix()

        }


        tran += width / 5
        if (tran > width * 1.1) {
            tran = -width
        }
        matrix?.setTranslate(tran.toFloat(), 0f)
        linear?.setLocalMatrix(matrix)
        postInvalidateDelayed(300)
    }
}
