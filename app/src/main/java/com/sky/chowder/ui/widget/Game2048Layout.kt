package com.sky.chowder.ui.widget

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.RelativeLayout
import com.sky.SkyApp
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

    val orginal = ArrayList<Int>()
    var oldOrginal = ArrayList<Int>()
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
        for (i in 0..15) orginal.add(0)
        orginal[Random().nextInt(15)] = 2
        orginal[Random().nextInt(15)] = 4
        pieceWidth = (width!! - margin * (piece + 1)) / piece
        for (i in orginal.indices) {
            val child = ImageView(context)
            child.setImageDrawable(getImageDrawable(orginal[i]))
            val lp = RelativeLayout.LayoutParams(pieceWidth, pieceWidth)
            val leftMargin = i % piece * pieceWidth + (i % piece + 1) * margin
            val topMargin = i / piece * pieceWidth + (i / piece + 1) * margin
            lp.setMargins(leftMargin, topMargin, 0, 0)
            addView(child)
            child.layoutParams = lp//一定要放在addview之后
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
                oldOrginal = orginal.clone() as ArrayList<Int>
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
            (getChildAt(i) as ImageView).setImageDrawable(getImageDrawable(orginal[i]))
            if (orginal[i] == 0) random.add(i)
        }
        //是否还有空白的位置
        if (random.isNotEmpty()) {
            val randomNum = if (random.size > 1) random[Random().nextInt(random.size - 1)] else random[0]
            orginal[randomNum] = randomTwoOrFour()
            val image = getChildAt(randomNum) as ImageView
            image.setImageDrawable(getImageDrawable(orginal[randomNum]))
            addAni(image)
        }
        if (random.size < 2) {
            var isMove = true
            for (i in 0..3) {
                //横向对比
                if (orginal[i * 4] == orginal[i * 4 + 1] || orginal[i * 4 + 1] == orginal[i * 4 + 2] || orginal[i * 4 + 2] == orginal[i * 4 + 3] ||
                        //纵向对比
                        orginal[i] == orginal[i + 4] || orginal[i + 4] == orginal[i + 8] || orginal[i + 8] == orginal[i + 12]) {
                    isMove = false
                    break
                }
            }
            if (isMove) SkyApp.getInstance().showToast("游戏结束")
        }
    }

    private fun addAni(image: ImageView) {
        val set = AnimatorSet()
        set.playTogether(ObjectAnimator.ofFloat(image, "scaleX", 0f, 1f)
                , ObjectAnimator.ofFloat(image, "scaleY", 0f, 1f))
        set.duration = 300
        set.start()
    }

    private fun addAnimator() {
        val rl = RelativeLayout(context)//遮罩层
        addView(rl)
        for (i in oldOrginal.indices) {
            val child = ImageView(context)
            child.setImageDrawable(getImageDrawable(oldOrginal[i]))
            val lp = RelativeLayout.LayoutParams(pieceWidth, pieceWidth)
            val leftMargin = i % piece * pieceWidth + (i % piece + 1) * margin
            val topMargin = i / piece * pieceWidth + (i / piece + 1) * margin
            lp.setMargins(leftMargin, topMargin, 0, 0)
            rl.addView(child)
            child.layoutParams = lp//一定要放在addview之后
        }
        val count = childCount
        for (i in 0 until aniList.size) {

            if (orginal[i] !== oldOrginal[i]) {


            }
            val image = getChildAt(i) as ImageView
            val set = AnimatorSet()
            set.playTogether(ObjectAnimator.ofFloat(image, "translationX", 0f, i % 4f * pieceWidth))
            set.duration = 300
            set.start()
        }
    }

    private fun randomTwoOrFour(): Int {
        var flag = 0.25//50
        for (i in orginal) if (i > 512) flag = 0.5
        return if (Math.random() > flag) 2 else 4
    }

    private fun initRLNums(i: Int, nums: ArrayList<Int>) {
        nums.clear()
        if (orginal[i * 4] !== 0) nums.add(orginal[i * 4])
        if (orginal[i * 4 + 1] !== 0) nums.add(orginal[i * 4 + 1])
        if (orginal[i * 4 + 2] !== 0) nums.add(orginal[i * 4 + 2])
        if (orginal[i * 4 + 3] !== 0) nums.add(orginal[i * 4 + 3])
        orginal[i * 4] = 0
        orginal[i * 4 + 1] = 0
        orginal[i * 4 + 2] = 0
        orginal[i * 4 + 3] = 0
    }

    val aniList = ArrayList<List<Int>>()
    private fun slideRight() {
        val nums = ArrayList<Int>()
        for (i in 0..3) {
            initRLNums(i, nums)

            when (nums.size) {
                1 -> orginal[i * 4 + 3] = nums[0]
                2 -> if (nums[0] == nums[1]) orginal[i * 4 + 3] = nums[0] + nums[1]
                else {
                    orginal[i * 4 + 2] = nums[0]
                    orginal[i * 4 + 3] = nums[1]
                }
                3 -> when {
                    nums[1] == nums[2] -> {
                        orginal[i * 4 + 2] = nums[0]
                        orginal[i * 4 + 3] = nums[1] + nums[2]
                    }
                    nums[0] == nums[1] -> {
                        orginal[i * 4 + 2] = nums[0] + nums[1]
                        orginal[i * 4 + 3] = nums[2]
                    }
                    else -> {
                        orginal[i * 4 + 1] = nums[0]
                        orginal[i * 4 + 2] = nums[1]
                        orginal[i * 4 + 3] = nums[2]
                    }
                }
                4 -> when {
                    nums[0] == nums[1] && nums[2] == nums[3] -> {
                        orginal[i * 4 + 2] = nums[0] + nums[1]
                        orginal[i * 4 + 3] = nums[2] + nums[3]
                    }
                    nums[2] == nums[3] -> {
                        orginal[i * 4 + 1] = nums[0]
                        orginal[i * 4 + 2] = nums[1]
                        orginal[i * 4 + 3] = nums[2] + nums[3]
                    }
                    nums[1] == nums[2] -> {
                        orginal[i * 4 + 1] = nums[0]
                        orginal[i * 4 + 2] = nums[1] + nums[2]
                        orginal[i * 4 + 3] = nums[3]
                    }
                    nums[0] == nums[1] -> {
                        orginal[i * 4 + 1] = nums[0] + nums[1]
                        orginal[i * 4 + 2] = nums[2]
                        orginal[i * 4 + 3] = nums[3]
                    }
                    else -> {
                        orginal[i * 4] = nums[0]
                        orginal[i * 4 + 1] = nums[1]
                        orginal[i * 4 + 2] = nums[2]
                        orginal[i * 4 + 3] = nums[3]
                    }
                }
            }
        }
    }

    private fun slideLeft() {
        val nums = ArrayList<Int>()
        for (i in 0..3) {
            initRLNums(i, nums)
            when (nums.size) {
                1 -> orginal[i * 4] = nums[0]
                2 -> if (nums[0] == nums[1]) orginal[i * 4] = nums[0] + nums[1]
                else {
                    orginal[i * 4] = nums[0]
                    orginal[i * 4 + 1] = nums[1]
                }
                3 -> when {
                    nums[0] == nums[1] -> {
                        orginal[i * 4] = nums[0] + nums[1]
                        orginal[i * 4 + 1] = nums[2]
                    }
                    nums[1] == nums[2] -> {
                        orginal[i * 4] = nums[0]
                        orginal[i * 4 + 1] = nums[1] + nums[2]
                    }
                    else -> {
                        orginal[i * 4] = nums[0]
                        orginal[i * 4 + 1] = nums[1]
                        orginal[i * 4 + 2] = nums[2]
                    }
                }
                4 -> when {
                    nums[0] == nums[1] && nums[2] == nums[3] -> {
                        orginal[i * 4] = nums[0] + nums[1]
                        orginal[i * 4 + 1] = nums[2] + nums[3]
                    }
                    nums[0] == nums[1] -> {
                        orginal[i * 4 + 0] = nums[0] + nums[1]
                        orginal[i * 4 + 1] = nums[2]
                        orginal[i * 4 + 2] = nums[3]
                    }
                    nums[1] == nums[2] -> {
                        orginal[i * 4 + 0] = nums[0]
                        orginal[i * 4 + 1] = nums[1] + nums[2]
                        orginal[i * 4 + 2] = nums[3]
                    }
                    nums[2] == nums[3] -> {
                        orginal[i * 4 + 0] = nums[0]
                        orginal[i * 4 + 1] = nums[1]
                        orginal[i * 4 + 2] = nums[2] + nums[3]
                    }
                    else -> {
                        orginal[i * 4] = nums[0]
                        orginal[i * 4 + 1] = nums[1]
                        orginal[i * 4 + 2] = nums[2]
                        orginal[i * 4 + 3] = nums[3]
                    }
                }
            }
        }
    }

    private fun initDUNums(i: Int, nums: ArrayList<Int>) {
        nums.clear()
        if (orginal[i] !== 0) nums.add(orginal[i])
        if (orginal[i + 4] !== 0) nums.add(orginal[i + 4])
        if (orginal[i + 8] !== 0) nums.add(orginal[i + 8])
        if (orginal[i + 12] !== 0) nums.add(orginal[i + 12])
        orginal[i] = 0
        orginal[i + 4] = 0
        orginal[i + 8] = 0
        orginal[i + 12] = 0
    }

    private fun slideDown() {
        val nums = ArrayList<Int>()
        for (i in 0..3) {
            initDUNums(i, nums)
            when (nums.size) {
                1 -> orginal[i + 12] = nums[0]
                2 -> if (nums[0] == nums[1]) orginal[i + 12] = nums[0] + nums[1]
                else {
                    orginal[i + 8] = nums[0]
                    orginal[i + 12] = nums[1]
                }
                3 -> when {
                    nums[1] == nums[2] -> {
                        orginal[i + 8] = nums[0]
                        orginal[i + 12] = nums[1] + nums[2]
                    }
                    nums[0] == nums[1] -> {
                        orginal[i + 8] = nums[0] + nums[1]
                        orginal[i + 12] = nums[2]
                    }
                    else -> {
                        orginal[i + 4] = nums[0]
                        orginal[i + 8] = nums[1]
                        orginal[i + 12] = nums[2]
                    }
                }
                4 -> when {
                    nums[0] == nums[1] && nums[2] == nums[3] -> {
                        orginal[i + 8] = nums[0] + nums[1]
                        orginal[i + 12] = nums[2] + nums[3]
                    }
                    nums[2] == nums[3] -> {
                        orginal[i + 4] = nums[0]
                        orginal[i + 8] = nums[1]
                        orginal[i + 12] = nums[2] + nums[3]
                    }
                    nums[1] == nums[2] -> {
                        orginal[i + 4] = nums[0]
                        orginal[i + 8] = nums[1] + nums[2]
                        orginal[i + 12] = nums[3]
                    }
                    nums[0] == nums[1] -> {
                        orginal[i + 4] = nums[0] + nums[1]
                        orginal[i + 8] = nums[2]
                        orginal[i + 12] = nums[3]
                    }
                    else -> {
                        orginal[i] = nums[0]
                        orginal[i + 4] = nums[1]
                        orginal[i + 8] = nums[2]
                        orginal[i + 12] = nums[3]
                    }
                }
            }
        }
    }

    private fun slideUp() {
        val nums = ArrayList<Int>()
        for (i in 0..3) {
            initDUNums(i, nums)
            when (nums.size) {
                1 -> orginal[i] = nums[0]
                2 -> if (nums[0] == nums[1]) orginal[i] = nums[0] + nums[1]
                else {
                    orginal[i] = nums[0]
                    orginal[i + 4] = nums[1]
                }
                3 -> when {
                    nums[0] == nums[1] -> {
                        orginal[i] = nums[0] + nums[1]
                        orginal[i + 4] = nums[2]
                    }
                    nums[1] == nums[2] -> {
                        orginal[i] = nums[0]
                        orginal[i + 4] = nums[1] + nums[2]
                    }
                    else -> {
                        orginal[i] = nums[0]
                        orginal[i + 4] = nums[1]
                        orginal[i + 8] = nums[2]
                    }
                }
                4 -> when {
                    nums[0] == nums[1] && nums[2] == nums[3] -> {
                        orginal[i] = nums[0] + nums[1]
                        orginal[i + 4] = nums[2] + nums[3]
                    }
                    nums[0] == nums[1] -> {
                        orginal[i] = nums[0] + nums[1]
                        orginal[i + 4] = nums[2]
                        orginal[i + 8] = nums[3]
                    }
                    nums[1] == nums[2] -> {
                        orginal[i] = nums[0]
                        orginal[i + 4] = nums[1] + nums[2]
                        orginal[i + 8] = nums[3]
                    }
                    nums[2] == nums[3] -> {
                        orginal[i] = nums[0]
                        orginal[i + 4] = nums[1]
                        orginal[i + 8] = nums[2] + nums[3]
                    }
                    else -> {
                        orginal[i] = nums[0]
                        orginal[i + 4] = nums[1]
                        orginal[i + 8] = nums[2]
                        orginal[i + 12] = nums[3]
                    }
                }
            }
        }
    }
}