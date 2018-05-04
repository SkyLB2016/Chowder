package com.sky.chowder.ui.widget

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
class Game2048Layout1 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr) {

    var oldOrginal = Array(4) { IntArray(4) }
    var orginal = Array(4) { IntArray(4) }
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
        orginal[Random().nextInt(3)][Random().nextInt(3)] = 2
        orginal[Random().nextInt(3)][Random().nextInt(3)] = 4

        pieceWidth = (width!! - margin * (piece + 1)) / piece
        for (i in orginal.indices) {
            for (j in orginal.indices) {
                val child = ImageView(context)
                child.setImageDrawable(getImageDrawable(orginal[i][j]))
                val lp = RelativeLayout.LayoutParams(pieceWidth, pieceWidth)
                val leftMargin = j * pieceWidth + (j + 1) * margin
                val topMargin = i * pieceWidth + (i + 1) * margin
                lp.setMargins(leftMargin, topMargin, 0, 0)
                addView(child)
                child.layoutParams = lp//一定要放在addview之后
            }
        }
    }

    private fun getImageDrawable(num: Int): Drawable? =
            if (num in 2..4096) resources.getDrawable(resources.getIdentifier("i$num", "mipmap", context.packageName))
            else null

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
                if (Math.abs(diffX) < 100 && Math.abs(diffY) < 100) return true
                //复制原数据
                oldOrginal = orginal.clone()
                //开始移动数据
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (diffX > 100) /*右滑*/ slideRight()
                    else if (diffX < -100) /*左滑*/ slideLeft()
                } else {
                    if (diffY > 100) /*下滑*/ slideDown()
                    else if (diffY < -100) /*上滑*/ slideUp()
                }
                //现数据与原数据比较，不同则重置数组
                if (orginal != oldOrginal) resetView()
//                if (orginal != oldOrginal) {
//                    if (Math.abs(diffX) > Math.abs(diffY)) {
//                        if (diffX > 100) /*右滑*/ addAnimator()
//                        else if (diffX < -100) /*左滑*/ addAnimator()
//                    } else {
//                        if (diffY > 100) /*下滑*/ addAnimator()
//                        else if (diffY < -100) /*上滑*/ addAnimator()
//                    }
//                }
            }
        }
        return true
    }

    //现数据与原数据比较，不同则重置数组
    private fun resetView() {
        val random = ArrayList<Int>()
        val count = childCount
        for (i in 0 until count) {
            (getChildAt(i) as ImageView).setImageDrawable(getImageDrawable(orginal[i / piece][i % piece]))
            if (orginal[i / piece][i % piece] == 0) random.add(i)
        }
        //是否还有空白的位置
        if (random.isNotEmpty()) {
            val n = if (random.size > 1) random[Random().nextInt(random.size - 1)] else random[0]
            orginal[n / piece][n % piece] = 2
            val image = getChildAt(n) as ImageView
            image.setImageDrawable(getImageDrawable(orginal[n / piece][n % piece]))
            val set = AnimatorSet()
            set.playTogether(ObjectAnimator.ofFloat(image, "scaleX", 0f, 1f)
                    , ObjectAnimator.ofFloat(image, "scaleY", 0f, 1f))
            set.duration = 300
            set.start()
        }
//        if (random.size < 2) {
//            var isMove = true
//            for (i in 0..3) {
//                //横向对比
//                if (orginal[i * 4] == orginal[i * 4 + 1] || orginal[i * 4 + 1] == orginal[i * 4 + 2] || orginal[i * 4 + 2] == orginal[i * 4 + 3] ||
//                        //纵向对比
//                        orginal[i] == orginal[i + 4] || orginal[i + 4] == orginal[i + 8] || orginal[i + 8] == orginal[i + 12]) {
//                    isMove = false
//                    break
//                }
//            }
//            if (isMove) SkyApp.getInstance().showToast("游戏结束")
//        }
    }

    private fun addAnimator() {
        val rl = RelativeLayout(context)//遮罩层
        addView(rl)
        for (i in oldOrginal.indices) {
            val child = ImageView(context)
            child.setImageDrawable(getImageDrawable(oldOrginal[i / piece][i % piece]))
            val lp = RelativeLayout.LayoutParams(pieceWidth, pieceWidth)
            val leftMargin = i % piece * pieceWidth + (i % piece + 1) * margin
            val topMargin = i / piece * pieceWidth + (i / piece + 1) * margin
            lp.setMargins(leftMargin, topMargin, 0, 0)
            rl.addView(child)
            child.layoutParams = lp//一定要放在addview之后
        }
        val count = childCount
    }

    private fun randomTwoOrFour(): Int {
        var flag = 0.25//50
//        for (i in orginal) if (i > 512) flag = 0.5
        return if (Math.random() > flag) 2 else 4
    }


    private fun slideRight() {

    }

    private fun slideLeft() {
        var nums = ArrayList<Int>()
        var temp = -1
        for (i in orginal.indices) {
            nums.clear()
            temp = -1
            for (j in orginal.indices) {
                val num = orginal[i][j]
                if (num !== 0) {
                    temp = when (temp) {
                        -1 -> num
                        num -> {
                            nums.add(temp * 2)
                            -1
                        }
                        else -> {
                            nums.add(temp)
                            num
                        }
                    }
                }
            }
            if (temp != -1) nums.add(temp)
            for (j in nums.indices) {
                orginal[i][j] = nums[j]
            }
            for (j in nums.size until orginal.size) {
                orginal[i][j] = 0
            }
        }
    }

    private fun initDUNums(i: Int, nums: ArrayList<Int>) {
    }

    private fun slideDown() {
    }

    private fun slideUp() {
    }
}