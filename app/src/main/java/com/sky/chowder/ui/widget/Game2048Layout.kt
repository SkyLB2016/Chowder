package com.sky.chowder.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.RelativeLayout
import com.sky.chowder.R
import com.sky.utils.ScreenUtils
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by SKY on 2015/12/24 10:58.
 * 拼图游戏
 */
class Game2048Layout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr) {

    val list = ArrayList<Int>()
    private val random = ArrayList<Int>()
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
        list[Random().nextInt(15)] = 4
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

    private var downX: Float = 0f
    private var downY: Float = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
            }
            MotionEvent.ACTION_UP -> {
                val diffX = event.x - downX
                val diffY = event.y - downY
                //移动距离过小则返回
                if (Math.abs(diffX) < 300 && Math.abs(diffY) < 300) return true
                //复制原数据
                val oldList = list.clone() as ArrayList<Int>
                //开始移动数据
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (diffX > 300) /*右滑*/ slideRight()
                    else if (diffX < -300) /*左滑*/ slideLeft()
                } else {
                    if (diffY > 300) /*下滑*/ slideDown()
                    else if (diffY < -300) /*上滑*/ slideUp()
                }
                //现数据与原数据比较，不同则重置数组
                if (list != oldList) resetView()
            }
        }
        return true
    }

    private fun resetView() {
        random.clear()
        val count = childCount
        for (i in 0 until count) {
            (getChildAt(i) as ImageView).setImageDrawable(getImageDrawable(i))
            if (list[i] == 0) random.add(i)
        }
        if (random.isNotEmpty()) {
            val randomNum = if (random.size > 1) random[Random().nextInt(random.size - 1)] else random[0]
            list[randomNum] = 2
            (getChildAt(randomNum) as ImageView).setImageDrawable(getImageDrawable(randomNum))
        }
    }
    var one = 0
    var two = 0
    var three = 0
    var four = 0
    var nums = ArrayList<Int>()
    private fun slideRight() {
        for (i in 0..3) {
            one = list[i * 4]
            two = list[i * 4 + 1]
            three = list[i * 4 + 2]
            four = list[i * 4 + 3]
            nums.clear()
            if (one !== 0) nums.add(one)
            if (two !== 0) nums.add(two)
            if (three !== 0) nums.add(three)
            if (four !== 0) nums.add(four)
            list[i * 4] = 0
            list[i * 4 + 1] = 0
            list[i * 4 + 2] = 0
            list[i * 4 + 3] = 0
            when (nums.size) {
                1 -> list[i * 4 + 3] = nums[0]
                2 -> {
                    if (nums[0] == nums[1])
                        list[i * 4 + 3] = nums[0] + nums[1]
                    else {
                        list[i * 4 + 2] = nums[0]
                        list[i * 4 + 3] = nums[1]
                    }
                }
                3 -> {
                    when {
                        nums[1] == nums[2] -> {
                            list[i * 4 + 2] = nums[0]
                            list[i * 4 + 3] = nums[1] + nums[2]
                        }
                        nums[0] == nums[1] -> {
                            list[i * 4 + 2] = nums[0] + nums[1]
                            list[i * 4 + 3] = nums[2]
                        }
                        else -> {
                            list[i * 4 + 1] = nums[0]
                            list[i * 4 + 2] = nums[1]
                            list[i * 4 + 3] = nums[2]
                        }
                    }
                }
                4 -> {
                    when {
                        nums[0] == nums[1] && nums[2] == nums[3] -> {
                            list[i * 4 + 2] = nums[0] + nums[1]
                            list[i * 4 + 3] = nums[2] + nums[3]
                        }
                        nums[2] == nums[3] -> {
                            list[i * 4 + 1] = nums[0]
                            list[i * 4 + 2] = nums[1]
                            list[i * 4 + 3] = nums[2] + nums[3]
                        }
                        nums[1] == nums[2] -> {
                            list[i * 4 + 1] = nums[0]
                            list[i * 4 + 2] = nums[1] + nums[2]
                            list[i * 4 + 3] = nums[3]
                        }
                        nums[0] == nums[1] -> {
                            list[i * 4 + 1] = nums[0] + nums[1]
                            list[i * 4 + 2] = nums[2]
                            list[i * 4 + 3] = nums[3]
                        }
                        else -> {
                            list[i * 4] = nums[0]
                            list[i * 4 + 1] = nums[1]
                            list[i * 4 + 2] = nums[2]
                            list[i * 4 + 3] = nums[3]
                        }
                    }
                }
            }
        }
    }

    private fun slideLeft() {
        for (i in 0..3) {
            one = list[i * 4]
            two = list[i * 4 + 1]
            three = list[i * 4 + 2]
            four = list[i * 4 + 3]
            nums.clear()
            if (one !== 0) nums.add(one)
            if (two !== 0) nums.add(two)
            if (three !== 0) nums.add(three)
            if (four !== 0) nums.add(four)
            list[i * 4] = 0
            list[i * 4 + 1] = 0
            list[i * 4 + 2] = 0
            list[i * 4 + 3] = 0
            when (nums.size) {
                1 -> list[i * 4] = nums[0]
                2 -> {
                    if (nums[0] == nums[1])
                        list[i * 4] = nums[0] + nums[1]
                    else {
                        list[i * 4] = nums[0]
                        list[i * 4 + 1] = nums[1]
                    }
                }
                3 -> {
                    when {
                        nums[0] == nums[1] -> {
                            list[i * 4] = nums[0] + nums[1]
                            list[i * 4 + 1] = nums[2]
                        }
                        nums[1] == nums[2] -> {
                            list[i * 4] = nums[0]
                            list[i * 4 + 1] = nums[1] + nums[2]
                        }
                        else -> {
                            list[i * 4] = nums[0]
                            list[i * 4 + 1] = nums[1]
                            list[i * 4 + 2] = nums[2]
                        }
                    }
                }
                4 -> {
                    when {
                        nums[0] == nums[1] && nums[2] == nums[3] -> {
                            list[i * 4] = nums[0] + nums[1]
                            list[i * 4 + 1] = nums[2] + nums[3]
                        }
                        nums[0] == nums[1] -> {
                            list[i * 4 + 0] = nums[0] + nums[1]
                            list[i * 4 + 1] = nums[2]
                            list[i * 4 + 2] = nums[3]
                        }
                        nums[1] == nums[2] -> {
                            list[i * 4 + 0] = nums[0]
                            list[i * 4 + 1] = nums[1] + nums[2]
                            list[i * 4 + 2] = nums[3]
                        }
                        nums[2] == nums[3] -> {
                            list[i * 4 + 0] = nums[0]
                            list[i * 4 + 1] = nums[1]
                            list[i * 4 + 2] = nums[2] + nums[3]
                        }
                        else -> {
                            list[i * 4] = nums[0]
                            list[i * 4 + 1] = nums[1]
                            list[i * 4 + 2] = nums[2]
                            list[i * 4 + 3] = nums[3]
                        }
                    }
                }
            }
        }
    }

    private fun slideDown() {
        for (i in 0..3) {
            one = list[i]
            two = list[i + 4]
            three = list[i + 8]
            four = list[i + 12]
            nums.clear()
            if (one !== 0) nums.add(one)
            if (two !== 0) nums.add(two)
            if (three !== 0) nums.add(three)
            if (four !== 0) nums.add(four)
            list[i] = 0
            list[i + 4] = 0
            list[i + 8] = 0
            list[i + 12] = 0
            when (nums.size) {
                1 -> list[i + 12] = nums[0]
                2 -> {
                    if (nums[0] == nums[1])
                        list[i + 12] = nums[0] + nums[1]
                    else {
                        list[i + 8] = nums[0]
                        list[i + 12] = nums[1]
                    }
                }
                3 -> {
                    when {
                        nums[1] == nums[2] -> {
                            list[i + 8] = nums[0]
                            list[i + 12] = nums[1] + nums[2]
                        }
                        nums[0] == nums[1] -> {
                            list[i + 8] = nums[0] + nums[1]
                            list[i + 12] = nums[2]
                        }
                        else -> {
                            list[i + 4] = nums[0]
                            list[i + 8] = nums[1]
                            list[i + 12] = nums[2]
                        }
                    }
                }
                4 -> {
                    when {
                        nums[0] == nums[1] && nums[2] == nums[3] -> {
                            list[i + 8] = nums[0] + nums[1]
                            list[i + 12] = nums[2] + nums[3]
                        }
                        nums[2] == nums[3] -> {
                            list[i + 4] = nums[0]
                            list[i + 8] = nums[1]
                            list[i + 12] = nums[2] + nums[3]
                        }
                        nums[1] == nums[2] -> {
                            list[i + 4] = nums[0]
                            list[i + 8] = nums[1] + nums[2]
                            list[i + 12] = nums[3]
                        }
                        nums[0] == nums[1] -> {
                            list[i + 4] = nums[0] + nums[1]
                            list[i + 8] = nums[2]
                            list[i + 12] = nums[3]
                        }
                        else -> {
                            list[i] = nums[0]
                            list[i + 4] = nums[1]
                            list[i + 8] = nums[2]
                            list[i + 12] = nums[3]
                        }
                    }
                }
            }
        }
    }

    private fun slideUp() {
        for (i in 0..3) {
            one = list[i]
            two = list[i + 4]
            three = list[i + 8]
            four = list[i + 12]
            nums.clear()
            if (one !== 0) nums.add(one)
            if (two !== 0) nums.add(two)
            if (three !== 0) nums.add(three)
            if (four !== 0) nums.add(four)
            list[i] = 0
            list[i + 4] = 0
            list[i + 8] = 0
            list[i + 12] = 0
            when (nums.size) {
                1 -> list[i] = nums[0]
                2 -> {
                    if (nums[0] == nums[1])
                        list[i] = nums[0] + nums[1]
                    else {
                        list[i] = nums[0]
                        list[i + 4] = nums[1]
                    }
                }
                3 -> {
                    when {
                        nums[0] == nums[1] -> {
                            list[i] = nums[0] + nums[1]
                            list[i + 4] = nums[2]
                        }
                        nums[1] == nums[2] -> {
                            list[i] = nums[0]
                            list[i + 4] = nums[1] + nums[2]
                        }
                        else -> {
                            list[i] = nums[0]
                            list[i + 4] = nums[1]
                            list[i + 8] = nums[2]
                        }
                    }
                }
                4 -> {
                    when {
                        nums[0] == nums[1] && nums[2] == nums[3] -> {
                            list[i] = nums[0] + nums[1]
                            list[i + 4] = nums[2] + nums[3]
                        }
                        nums[0] == nums[1] -> {
                            list[i] = nums[0] + nums[1]
                            list[i + 4] = nums[2]
                            list[i + 8] = nums[3]
                        }
                        nums[1] == nums[2] -> {
                            list[i] = nums[0]
                            list[i + 4] = nums[1] + nums[2]
                            list[i + 8] = nums[3]
                        }
                        nums[2] == nums[3] -> {
                            list[i] = nums[0]
                            list[i + 4] = nums[1]
                            list[i + 8] = nums[2] + nums[3]
                        }
                        else -> {
                            list[i] = nums[0]
                            list[i + 4] = nums[1]
                            list[i + 8] = nums[2]
                            list[i + 12] = nums[3]
                        }
                    }
                }
            }
        }
    }
}