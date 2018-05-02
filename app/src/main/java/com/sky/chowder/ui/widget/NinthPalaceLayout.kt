package com.sky.chowder.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.sky.chowder.R
import com.sky.chowder.model.NinthPalaceEntity
import com.sky.utils.ScreenUtils

/**
 * Created by SKY on 2015/12/24 10:58.
 * 拼图游戏
 */
class NinthPalaceLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    val list = ArrayList<NinthPalaceEntity>()
    val select = ArrayList<NinthPalaceEntity>()
    private val piece = 3//几行几列
    private var once = true
    private var pieceWidth: Int = 0
    private var width: Int? = 0

    init {
        setBackgroundResource(R.color.alpha_66)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = ScreenUtils.getWidthPX(context)
        if (once) {
            setView()
            once = false
        }
        setMeasuredDimension(width!!, ScreenUtils.getHeightPX(context)!!)
    }

    private fun setView() {
        pieceWidth = width!! / piece
        for (i in 0..8) {
            val paint = Paint()
            paint.color = Color.BLUE
            val left = i % piece * pieceWidth
            val top = i / piece * pieceWidth
            val rect = Rect(left + pieceWidth / 4, top + pieceWidth / 4, left + pieceWidth * 3 / 4, top + pieceWidth * 3 / 4)

            val nine = NinthPalaceEntity()
            nine.id = i
            nine.rect = rect
            list.add(nine)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for (i in list) {
            val paint = Paint()
            paint.color = Color.BLUE
            canvas?.drawCircle(i.rect.centerX().toFloat(), i.rect.centerY().toFloat(), 10f, paint)
        }
        for (i in select.indices) {
            val paint = Paint()
            paint.color = Color.RED
            canvas?.drawCircle(select[i].rect.centerX().toFloat(), select[i].rect.centerY().toFloat(), 10f, paint)
            paint.strokeWidth = 5f
            if (i + 1 <= select.size - 1)
                canvas?.drawLine(select[i].rect.centerX().toFloat(), select[i].rect.centerY().toFloat(), select[i + 1].rect.centerX().toFloat(), select[i + 1].rect.centerY().toFloat(), paint)
            else if (endX !== 0f && endY !== 0f) canvas?.drawLine(select[i].rect.centerX().toFloat(), select[i].rect.centerY().toFloat(), endX, endY, paint)
        }

    }

    private var downX: Float = 0f
    private var downY: Float = 0f
    private var endX: Float = 0f
    private var endY: Float = 0f


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                select.clear()
                for (i in list)
                    if (i.rect.contains(downX.toInt(), downY.toInt())) {
                        select.add(i)
                        invalidate()
                    }
            }
            MotionEvent.ACTION_MOVE -> {
                endX = event.x
                endY = event.y
                for (i in list)
                    if (i.rect.contains(endX.toInt(), endY.toInt())) {
                        if (!select.contains(i)) select.add(i)
                    } else {

                    }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                endX = 0f
                endY = 0f
//                if ()
                invalidate()
            }
        }
        return true
    }
}