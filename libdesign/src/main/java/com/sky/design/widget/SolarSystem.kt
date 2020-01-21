package com.sky.design.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.BounceInterpolator
import android.widget.FrameLayout
import com.sky.R
import com.sky.sdk.utils.LogUtils

/**
 * Created by SKY on 2015/4/9 21:10:39.
 * 卫星式菜单
 * Math的三角函数算法：
var angle = 30.0//角度
val radians = Math.PI * angle / 180//转换成弧度
val ix = 200 * Math.cos(radians)
val iy = 200 * Math.sin(radians)
0-360 度，Cos取值范围1..0..-1..0..1，即+- -+，Sin 取值范围0..1..0..-1..0,即++- -
以x=Cos,y=Sin算为++，-+，- -，+-，即一二三四象限，以X正轴1为起点，逆时针方向画圆，画布上Y轴正负颠倒，所以是顺时针方向画圆
以x=Sin,y=Cos算为++，+-，- -，-+，即一四三二象限，以Y正轴1为起点，顺时针方向画圆，画布上Y轴正负颠倒，所以是逆时针方向画圆
val ix = 200 * Math.sin(radians)
val iy = 200 * Math.cos(radians)
 */
class SolarSystem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var radius: Int = RADIUS
    var position = CENTER
        // 默认位置
        set(value) {
            if (value === field) return
            if (isOpen) toggleMenu(TIME)
            field = value
            requestLayout()
        }

    private var mState = State.CLOSE// 默认状态
    var menuItemOnClick: ((View, Int) -> Unit)? = null
    var menuState: ((Boolean) -> Unit)? = null
    /**
     * @return 判断是打开还是关闭状态
     */
    private val isOpen: Boolean get() = mState === State.OPEN

    /**
     * 中心按钮是否旋转，默认旋转
     */
    var rotateMenu = true
    /**
     * 点击子vew后是否回收,默认回收
     */
    var isRecoverChild = true
    /**
     * 是否正在执行动画
     */
    private var isRotating = false

    init {
        val style =
            context.theme.obtainStyledAttributes(attrs, R.styleable.SolarSystem, defStyleAttr, 0)
        position = style.getInt(R.styleable.SolarSystem_position, CENTER)//控件所处位置
        radius = style.getDimensionPixelSize(R.styleable.SolarSystem_radius, radius)//获取半径值
        style.recycle()// 释放
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 获取宽高与模式
        val layoutWidth = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val layoutHeight = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        LogUtils.i("layoutWidth==${layoutWidth}")
        LogUtils.i("layoutHeight==${layoutHeight}")
        // AT_MOST模式即wrap_content时测量父控件的宽高
        var width = 0
        var height = 0
        var child: View
        var lp: MarginLayoutParams
        var childWidth: Int
        var childHeight: Int
        val childCount = childCount/*子view的数量*/
        for (i in 0 until childCount) {
            child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)// 测量子view
            // 获取子view的margin
            lp = child.layoutParams as MarginLayoutParams
            childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            childHeight = child.measuredHeight + lp.bottomMargin + lp.topMargin
            // 取出子view中最大的宽高
            width = childWidth.coerceAtLeast(width)
            height = childHeight.coerceAtLeast(height)
        }

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY)
            return setMeasuredDimension(layoutWidth, layoutHeight)

        // 居于四个角时父控件的宽高
        width += radius
        height += radius
        when (position) {
            CENTER -> {// 居中时宽高翻倍
                width += width
                height += height
            }
            CENTER_BOTTOM, CENTER_TOP -> width += width     //居中且在顶部或底部时，宽翻倍
            CENTER_LEFT, CENTER_RIGHT -> height += height   //居中且在左或右边时高翻倍
        }
        // 判断宽高是否超越EXACTLY即match模式下的宽高
        if (layoutWidth - paddingLeft - paddingRight < width) width =
            layoutWidth - paddingLeft - paddingRight
        if (layoutHeight - paddingTop - paddingBottom < height) height =
            layoutHeight - paddingTop - paddingBottom
        // 为框架父控件写入宽高
        setMeasuredDimension(
            if (widthMode == MeasureSpec.EXACTLY) layoutWidth else width + paddingLeft + paddingRight,
            if (heightMode == MeasureSpec.EXACTLY) layoutHeight else height + paddingTop + paddingBottom
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val childCount = childCount
        var childPadLeft: Int
        var childPadTop: Int
        // 定义centermenu的监听,中心按钮点击事件
        getChildAt(childCount - 1).setOnClickListener { v -> onMenuClick(v) }
        val menuWidth = getChildAt(childCount - 1).width
        val menuHeight = getChildAt(childCount - 1).height
        var childAt: View
        var lp: MarginLayoutParams
        var childWidth: Int
        var childHeight: Int
        for (i in 0 until childCount) {
            childAt = getChildAt(i)
            childWidth = childAt.measuredWidth
            childHeight = childAt.measuredHeight
            lp = childAt.layoutParams as MarginLayoutParams
            childPadLeft = getChildPadLeft(menuWidth, lp, childWidth)
            childPadTop = getChildPadTop(menuHeight, lp, childHeight)
            childAt.layout(
                childPadLeft,
                childPadTop,
                childPadLeft + childWidth,
                childPadTop + childHeight
            )
        }
    }

    /**
     * 中心按钮点击事件
     */
    private fun onMenuClick(v: View) {
        if (isRotating) return
        if (isOpen) menuState?.invoke(false)
        else menuState?.invoke(true)
        if (rotateMenu) ObjectAnimator.ofFloat(v, "rotation", 0f, 720f).setDuration(1000).start()
        toggleMenu(TIME)//子view弹出与收回
    }

    //九个位置三列
    private fun getChildPadLeft(menuWidth: Int, lp: MarginLayoutParams, childWidth: Int): Int {
        return when (position) {
            CENTER, CENTER_TOP, CENTER_BOTTOM -> (width - childWidth) / 2 + lp.leftMargin + paddingLeft - paddingRight
            LEFT_TOP, LEFT_BOTTOM, CENTER_LEFT -> paddingLeft + lp.leftMargin + (menuWidth - childWidth) / 2
            RIGHT_TOP, RIGHT_BOTTOM, CENTER_RIGHT -> width - childWidth - lp.rightMargin - paddingRight - (menuWidth - childWidth) / 2
            else -> 0
        }
    }

    //九个位置三行
    private fun getChildPadTop(menuHeight: Int, lp: MarginLayoutParams, childHeight: Int): Int =
        when (position) {
            CENTER, CENTER_LEFT, CENTER_RIGHT -> (height - childHeight) / 2 + lp.topMargin + paddingTop - paddingBottom
            LEFT_TOP, RIGHT_TOP, CENTER_TOP -> paddingTop + lp.topMargin + (menuHeight - childHeight) / 2
            LEFT_BOTTOM, CENTER_BOTTOM, RIGHT_BOTTOM -> height - childHeight - lp.bottomMargin - paddingBottom - (menuHeight - childHeight) / 2
            else -> 0
        }

    override fun generateLayoutParams(attrs: AttributeSet) = LayoutParams(context, attrs)

    /**
     * 子view的弹出与收回
     */
    fun toggleMenu(Duration: Long) {
        isRotating = true//不写这句话的动画会呈现一种混乱效果
        val childCount = childCount
        var tranX: ObjectAnimator
        var tranY: ObjectAnimator
        var set: AnimatorSet
        var childAt: View
        var num = childCount - 2//分成多少份弧度
        //每份的弧度
        var radians = when (position) {
            LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM -> Math.PI / 2 / num
            CENTER_TOP, CENTER_BOTTOM, CENTER_LEFT, CENTER_RIGHT -> Math.PI / num
            else -> Math.PI * 2 / ++num//居中时，相当于画了一个圆，份数多一份
        }
        //偏移弧度+起始角度
        var offsetR = when (position) {
            LEFT_BOTTOM, CENTER_LEFT -> Math.PI * 3 / 2
            RIGHT_TOP, CENTER_RIGHT -> Math.PI / 2
            RIGHT_BOTTOM, CENTER_BOTTOM -> Math.PI
            else -> 0.0//其他时候为0
        }
        for (i in 0 until childCount - 1) {
            childAt = getChildAt(i)
            childAt.setOnClickListener { v ->
                menuItemOnClick?.invoke(v, i)
                childAnimator(i)//子view点击之后的效果
                if (isRecoverChild) toggleMenu(TIME)
            }
            //开始计算每个view的弹出位置
            val ix = radius * Math.cos(offsetR + radians * i).toFloat()
            val iy = radius * Math.sin(offsetR + radians * i).toFloat()
            // 根据菜单的状态判断是弹出还是收回，平移的动画定义
            if (mState === State.CLOSE) {
                tranX = ObjectAnimator.ofFloat(childAt, "translationX", 0f, ix)
                tranY = ObjectAnimator.ofFloat(childAt, "translationY", 0f, iy)
            } else {
                tranX = ObjectAnimator.ofFloat(childAt, "translationX", ix, 0f)
                tranY = ObjectAnimator.ofFloat(childAt, "translationY", iy, 0f)
            }
            //把几个动画组合在一起
            set = AnimatorSet()
            set.interpolator = BounceInterpolator()//差值器
            set.playTogether(tranX, tranY, ObjectAnimator.ofFloat(childAt, "rotation", 0f, 360f))
            set.duration = Duration
            set.startDelay = (i + 1) * 100L//每个的子view的延迟时间
            set.start()
            if (isRotating && i === childCount - 2)
                set.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        isRotating = false//动画停止至为false
                    }
                })
        }
        mState = changeState()// 执行完成后切换菜单状态
    }

    /**
     * 子view的动画效果
     *
     * @param pos 选中的view
     */
    private fun childAnimator(pos: Int) {
        var scale: ObjectAnimator
        var childAt: View
        var set: AnimatorSet
        for (i in 0 until childCount - 1) {
            childAt = getChildAt(i)
            scale = when (i) {
                pos -> ObjectAnimator.ofFloat(childAt, "scaleX", 1f, 2f, 1f)
                else -> ObjectAnimator.ofFloat(childAt, "scaleY", 1f, 0f, 1f)
            }
            set = AnimatorSet()
            set.playTogether(scale, ObjectAnimator.ofFloat(childAt, "alpha", 1f, 0f, 1f))
            set.duration = 300 * (i + 1L)
            set.start()
        }
    }

    /**
     * 切换菜单状态
     */
    private fun changeState() = if (mState === State.OPEN) State.CLOSE else State.OPEN

    companion object {
        // 九个位置定义
        const val LEFT_TOP = 0
        const val LEFT_BOTTOM = 1
        const val RIGHT_TOP = 2
        const val RIGHT_BOTTOM = 3
        const val CENTER_TOP = 4
        const val CENTER_BOTTOM = 5
        const val CENTER = 6
        const val CENTER_LEFT = 7
        const val CENTER_RIGHT = 8

        const val RADIUS = 300 //默认的半径
        const val TIME = 500L  //子view弹出与收回的时间
    }
}