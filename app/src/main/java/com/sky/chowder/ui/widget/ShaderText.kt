package com.sky.chowder.ui.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet

/**
 * Created by SKY on 2018/2/24 14:49.
 */
class ShaderText @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : android.support.v7.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    internal var matrix: Matrix? = null
    internal var width: Int = 0
    internal var paint: Paint? = null
    private var linear: LinearGradient? = null


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        width = measuredWidth
        if (width > 0) {
            paint = getPaint()
            linear = LinearGradient(0f, 0f, width.toFloat(), 0f, intArrayOf(Color.RED, Color.GREEN, Color.BLUE), null, Shader.TileMode.CLAMP)
            paint?.shader = linear!!
//            matrix = Matrix()
            matrix = getMatrix()
        }
    }

    private var tran: Int = 0

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        tran += width / 20
        if (tran > width * 1.02) tran = -width
        matrix?.setTranslate(tran.toFloat(), 0f)
        linear?.setLocalMatrix(matrix)
        postInvalidateDelayed(300)
    }
}
