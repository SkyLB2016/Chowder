package com.sky.g2048

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.RelativeLayout
import com.sky.SkyApp
import com.sky.utils.ScreenUtils
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by SKY on 2015/12/24 10:58.
 * 拼图游戏
 */
class Game2048Layout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr) {

    var orginal = IntArray(16)
        set(value) {
            if (value.isNotEmpty()) field = value
            else {
                field = IntArray(16)
                field[Random().nextInt(15)] = randomTwoOrFour()
                field[Random().nextInt(15)] = randomTwoOrFour()
            }
            val count = childCount
            for (i in 0 until count) (getChildAt(i) as ImageView).setImageDrawable(getImageDrawable(field[i]))
        }

    val oldOrginal = LinkedList<IntArray>()
    private var margin = resources.getDimensionPixelSize(R.dimen.wh_8)//分割后图片之间的间隔
    private val piece = 4//几行几列
    private var once = true
    private var pieceWidth: Int = 0
    private var width: Int? = 0
    private val nums = ArrayList<Int>()//临时判定数组
    var isEnd = false//是否结束
    var checkIsEnd: ((Boolean) -> Unit)? = null

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
            once = false
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
    }

    /**
     * 重新开始
     */
    fun restartGame() {
        for (i in 0..15) orginal[i] = 0
        orginal[Random().nextInt(15)] = randomTwoOrFour()
        resetView()
        isEnd = false
        oldOrginal.clear()
        checkIsEnd?.invoke(isEnd)
    }

    fun returnOld() {
        if (oldOrginal.isEmpty()) return
        orginal = oldOrginal.removeLast()
        val count = childCount
        for (i in 0 until count) (getChildAt(i) as ImageView).setImageDrawable(getImageDrawable(orginal[i]))
        isEnd = false
        checkIsEnd?.invoke(isEnd)
    }

    fun setAutomatic(automatic: IntArray) {
        orginal = automatic
        val count = childCount
        for (i in 0 until count) (getChildAt(i) as ImageView).setImageDrawable(getImageDrawable(orginal[i]))
        oldOrginal.add(orginal)
    }

    private fun getImageDrawable(num: Int): Drawable? =
            if (num in 2..16384) resources.getDrawable(resources.getIdentifier("i$num", "mipmap", context.packageName))
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
                if (Math.abs(diffX) < 200 && Math.abs(diffY) < 200) return true
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (diffX > 200) /*右滑*/ changeData(KeyEvent.KEYCODE_DPAD_RIGHT)
                    else if (diffX < -200) /*左滑*/ changeData(KeyEvent.KEYCODE_DPAD_LEFT)
                } else {
                    if (diffY > 200) /*下滑*/ changeData(KeyEvent.KEYCODE_DPAD_DOWN)
                    else if (diffY < -200) /*上滑*/ changeData(KeyEvent.KEYCODE_DPAD_UP)
                }
            }
        }
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        changeData(keyCode)
        return super.onKeyDown(keyCode, event)
    }

    private fun changeData(status: Int) {
        //复制原数据
        val old = orginal.clone()
        //开始移动数据
        when (status) {
            KeyEvent.KEYCODE_DPAD_UP,
            KeyEvent.KEYCODE_W -> slideUp()/*上滑*/
            KeyEvent.KEYCODE_DPAD_DOWN,
            KeyEvent.KEYCODE_S -> slideDown()/*下滑*/
            KeyEvent.KEYCODE_DPAD_LEFT,
            KeyEvent.KEYCODE_A -> slideLeft()/*左滑*/
            KeyEvent.KEYCODE_DPAD_RIGHT,
            KeyEvent.KEYCODE_D -> slideRight()/*右滑*/
        }
        if (!Arrays.equals(orginal, old)) {
            resetView()
            oldOrginal.add(old)//上一步数据
//            while (oldOrginal.size > 20) oldOrginal.removeFirst()
        }
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
            val position = if (random.size > 1) random[Random().nextInt(random.size - 1)] else random[0]
            orginal[position] = randomTwoOrFour()
            val image = getChildAt(position) as ImageView
            image.setImageDrawable(getImageDrawable(orginal[position]))
            addAni(image)
        }
        if (random.size < 2) {
            isEnd = true
            for (i in 0..3) {
                //横向对比
                if (contrast(i * 4, i * 4 + 1) || contrast(i * 4 + 1, i * 4 + 2) || contrast(i * 4 + 2, i * 4 + 3) ||
                        //纵向对比
                        contrast(i, i + 4) || contrast(i + 4, i + 8) || contrast(i + 8, i + 12)) {
                    isEnd = false
                    break
                }
            }
            if (isEnd) SkyApp.getInstance().showToast("游戏结束")
            checkIsEnd?.invoke(isEnd)
        }
    }

    private fun contrast(one: Int, two: Int) = orginal[one] == orginal[two]

    private fun addAni(image: ImageView) {
        val set = AnimatorSet()
        set.playTogether(ObjectAnimator.ofFloat(image, "scaleX", 0f, 1f), ObjectAnimator.ofFloat(image, "scaleY", 0f, 1f))
        set.duration = 300
        set.start()
    }

    private fun randomTwoOrFour(): Int {
        var flag = 0.25//50
        for (i in orginal) if (i > 512) flag = 0.5
        return if (Math.random() > flag) 2 else 4
    }


    //右滑，右边为第一个
    private fun slideRight() {
        for (i in 0..3) changeOrginal(i * 4 + 3, i * 4 + 2, i * 4 + 1, i * 4)
    }

    //左滑，左边为第一个
    private fun slideLeft() {
        for (i in 0..3) changeOrginal(i * 4, i * 4 + 1, i * 4 + 2, i * 4 + 3)
    }

    //下滑，下边为第一个
    private fun slideDown() {
        for (i in 0..3) changeOrginal(i + 12, i + 8, i + 4, i)
    }

    //上滑，上边为第一个
    private fun slideUp() {
        for (i in 0..3) changeOrginal(i, i + 4, i + 8, i + 12)
    }

    private fun changeOrginal(one: Int, two: Int, three: Int, four: Int) {
        resetNums(one, two, three, four)
        setOrg(one, 0, two, 0, three, 0, four, 0)
        when (nums.size) {
            1 -> setOrg(one, nums[0])
            2 -> if (nums[0] == nums[1]) setOrg(one, nums[0] + nums[1]) else setOrg(one, nums[0], two, nums[1])
            3 -> when {
                nums[0] == nums[1] -> setOrg(one, nums[0] + nums[1], two, nums[2])
                nums[1] == nums[2] -> setOrg(one, nums[0], two, nums[1] + nums[2])
                else -> setOrg(one, nums[0], two, nums[1], three, nums[2])
            }
            4 -> when {
                nums[0] == nums[1] && nums[2] == nums[3] -> setOrg(one, nums[0] + nums[1], two, nums[2] + nums[3])
                nums[0] == nums[1] -> setOrg(one, nums[0] + nums[1], two, nums[2], three, nums[3])
                nums[1] == nums[2] -> setOrg(one, nums[0], two, nums[1] + nums[2], three, nums[3])
                nums[2] == nums[3] -> setOrg(one, nums[0], two, nums[1], three, nums[2] + nums[3])
                else -> setOrg(one, nums[0], two, nums[1], three, nums[2], four, nums[3])
            }
        }
    }

    /**
     * 重置判断数组
     */
    private fun resetNums(vararg ps: Int) {
        nums.clear()
        var num: Int
        for (i in 0 until ps.size) {
            num = orginal[ps[i]]
            if (num !== 0) nums.add(num)
        }
    }

    /**
     * 给数据源赋值
     */
    private fun setOrg(vararg ns: Int) {
        for (i in 0 until ns.size / 2) {
            val index = ns[i * 2 + 0]
            val value = ns[i * 2 + 1]
            orginal[index] = value
        }
    }
}