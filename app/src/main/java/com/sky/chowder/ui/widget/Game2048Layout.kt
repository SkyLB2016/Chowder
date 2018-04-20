package com.sky.chowder.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.RelativeLayout
import com.sky.chowder.R
import com.sky.utils.LogUtils
import com.sky.utils.ScreenUtils
import java.util.*

/**
 * Created by SKY on 2015/12/24 10:58.
 * 拼图游戏
 */
class Game2048Layout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr) {

    val list = ArrayList<Int>(16)
    private var margin = resources.getDimensionPixelSize(R.dimen.wh_8)//分割后图片之间的间隔
    private val piece = 4//几行几列
    private var once = true
    private var pieceWidth: Int = 0
    private var width: Int? = 0

    init {
        setBackgroundResource(R.mipmap.bg2048)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = ScreenUtils.getWidthPX(context)
        setMeasuredDimension(width!!, width!!)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (once) {
            setView()
            once = false
        }
    }

    private fun setView() {
        for (i in 0..15) list.add(0)
        list[Random().nextInt(15)] = 2
        list[Random().nextInt(15)] = 2
        pieceWidth = (width!! - margin * (piece + 1)) / piece
        for (i in list.indices) {
            val child = ImageView(context)
            child.setImageDrawable(getImageDrawable(i))
            val lp = RelativeLayout.LayoutParams(pieceWidth, pieceWidth)
            val leftMargin = i % piece * pieceWidth + (i % piece + 1) * margin
            val topMargin = i / piece * pieceWidth + (i / piece + 1) * margin
            lp.setMargins(leftMargin, topMargin, 0, 0)
            addView(child)
            child.layoutParams = lp//一定要放在addview之后
//            imageViews[i] = child
        }
    }

    private fun getImageDrawable(position: Int): Drawable? {
        return when (list[position]) {
            2 -> resources.getDrawable(R.mipmap.i2)
            4 -> resources.getDrawable(R.mipmap.i4)
            8 -> resources.getDrawable(R.mipmap.i8)
            16 -> resources.getDrawable(R.mipmap.i16)
            32 -> resources.getDrawable(R.mipmap.i32)
            64 -> resources.getDrawable(R.mipmap.i64)
            128 -> resources.getDrawable(R.mipmap.i128)
            256 -> resources.getDrawable(R.mipmap.i256)
            512 -> resources.getDrawable(R.mipmap.i512)
            1024 -> resources.getDrawable(R.mipmap.i1024)
            2048 -> resources.getDrawable(R.mipmap.i2048)
            4096 -> resources.getDrawable(R.mipmap.i4096)
//                    8192 -> resources.getDrawable(R.mipmap.i4096)
            else -> null
        }
    }

    var downX: Float = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        LogUtils.i("==jinglaile进来了")
        var one = 0
        var two = 0
        var three = 0
        var four = 0
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> downX = event.x
            MotionEvent.ACTION_UP -> {
                val d = event.x - downX
                if (d > 0) {//右滑

                    for ((i, num) in list.withIndex()) {
                        if (i % piece === 0) {
                            one = num
                        } else if (i % piece === 1) {
                            two = num
                        } else if (i % piece === 2) {
                            three = num
                        } else if (i % piece === 3) {
                            four = num
                            if (one !== 0) {

                            }
                        }

                    }
                }
            }
        }


        return true
    }
}