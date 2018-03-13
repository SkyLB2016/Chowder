package com.sky.chowder.ui.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.FrameLayout
import com.sky.chowder.R

/**
 * Created by SKY on 2015/4/9 21:10:39.
 * 卫星式菜单
 */
class SolarSystem @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private var tempX = 1
    private var tempY = 1

    private var position = CENTER// 默认位置
    private var mState = State.CLOSE// 默认状态
    private var menuItemClick: ((View, Int) -> Unit)? = null
    private var menuState: MenuState? = null
    /**
     * @return 判断是打开还是关闭状态
     */
    private val isOpen: Boolean get() = mState === State.OPEN

    private var radius: Int = 0
    /**
     * 中心按钮是否旋转，默认旋转
     */
    private var rotaFlag = true
    /**
     * 点击子view后是否回收,默认回收
     */
    var isRecoverChildView = true
    /**
     * true单次依次执行,false点击就执行，混乱效果很好玩。与isNowRotating绑定使用
     */
    private var isRotate = false
    /**
     * 是否正在旋转
     */
    private var isNowRotating = false

    /**
     * true单次依次执行,false点击就执行，混乱效果很好玩。与isNowRotating绑定使用
     */
    fun setIsRotate(isRotating: Boolean) {
        this.isRotate = isRotating
    }

    fun setPosition(position: Int) {
        if (this.position == position)
            return
        if (isOpen) toggleMenu(300)
        this.position = position
        setTempXY()
        requestLayout()
    }

    /**
     * 中心按钮是否旋转，默认旋转
     */
    fun setRotaMenu(flag: Boolean) {
        rotaFlag = flag
    }

    fun setRadius(radius: Int) {
        this.radius = radius
    }

    fun setOnMenuItemClickListener(menuItemClick: ((View, Int) -> Unit)) {
        this.menuItemClick = menuItemClick
    }

    fun setOnMenuState(MenuState: MenuState) {
        this.menuState = MenuState
    }

    init {
        // 半径默认值
        radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, RADIUS, resources.displayMetrics).toInt()
        // 获取自定义属性的值
        val style = context.theme.obtainStyledAttributes(attrs, R.styleable.SolarSystem, defStyleAttr, 0)
        position = style.getInt(R.styleable.SolarSystem_position, CENTER)//控件所处位置
        radius = style.getDimensionPixelSize(R.styleable.SolarSystem_radius, radius)//获取半径值
        style.recycle()// 释放
        setTempXY()
    }

    private fun setTempXY() {
        tempX = 1//重置
        tempY = 1
        if (position == RIGHT_TOP || position == RIGHT_BOTTOM || position == CENTER_RIGHT)
            tempX = -1
        if (position == LEFT_BOTTOM || position == RIGHT_BOTTOM || position == CENTER_BOTTOM || position == CENTER_RIGHT)
            tempY = -1
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 获取宽高与模式
        val layoutWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val layoutHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        val HeightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        // AT_MOST模式即wrap_content时测量父控件的宽高
        var width = 0
        var height = 0
        var childAt: View
        var lp: ViewGroup.MarginLayoutParams
        var childWidth: Int
        var childHeight: Int
        val childCount = childCount// 子view的数量
        for (i in 0 until childCount) {
            childAt = getChildAt(i)
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec)// 测量子view
            // 获取子view的margin
            lp = childAt.layoutParams as ViewGroup.MarginLayoutParams
            childWidth = childAt.measuredWidth + lp.leftMargin + lp.rightMargin
            childHeight = childAt.measuredHeight + lp.bottomMargin + lp.topMargin
            // 取出子view中最大的宽高
            width = Math.max(childWidth, width)
            height = Math.max(childHeight, height)
        }

        if (widthMode == View.MeasureSpec.EXACTLY && HeightMode == View.MeasureSpec.EXACTLY) {
            setMeasuredDimension(layoutWidth, layoutHeight)
            return
        }
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
        if (layoutWidth - paddingLeft - paddingRight < width) {
            width = layoutWidth - paddingLeft - paddingRight
        }
        if (layoutHeight - paddingTop - paddingBottom < height) {
            height = layoutHeight - paddingTop - paddingBottom
        }
        // 载入宽高
        setMeasuredDimension(width + paddingLeft + paddingRight, height + paddingTop + paddingBottom)
    }
    //测试ondraw与dispatchDraw的区别，定义view用ondraw，定义viewgroup用dispatchDraw
    //    @Override
    //    protected void dispatchDraw(Canvas canvas) {
    //        super.dispatchDraw(canvas);
    //        Paint paint =new Paint();
    //        paint.setColor(Color.BLACK);
    //        paint.setStrokeWidth(50);
    //        paint.setStyle(Paint.Style.FILL);
    //        canvas.drawLine(0,0,1000,1000,paint);
    //    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        // 获取宽高 padding 子view个数
        val childCount = childCount

        var childPadLeft: Int
        var childPadTop: Int
        // 定义centermenu的监听,中心按钮点击事件
        getChildAt(childCount - 1).setOnClickListener { v -> onMenuClick(v) }
        val menuWidth = getChildAt(childCount - 1).width
        val menuHeight = getChildAt(childCount - 1).height
        var childAt: View
        var lp: ViewGroup.MarginLayoutParams
        var childWidth: Int
        var childHeight: Int
        for (i in 0 until childCount) {
            childAt = getChildAt(i)
            childWidth = childAt.measuredWidth
            childHeight = childAt.measuredHeight
            lp = childAt.layoutParams as ViewGroup.MarginLayoutParams
            // 判断不同位置时所在的区域，
            childPadLeft = getChildPadLeft(menuWidth, lp, childWidth)
            childPadTop = getChildPadTop(menuHeight, lp, childHeight)
            childAt.layout(childPadLeft, childPadTop, childPadLeft + childWidth, childPadTop + childHeight)
        }
    }

    /**
     * 中心按钮点击事件
     */
    private fun onMenuClick(v: View) {
        //监测是否正在旋转
        if (isNowRotating) return
        //true单次依次执行,则isNowRotating也为true；false点击就执行，isNowRotating为false
        if (isRotate) isNowRotating = true
        // 菜单状态切换
        if (isOpen) menuState?.closeMenu() else menuState?.openMenu()
        // 主菜单是否旋转
        if (rotaFlag)
            rotationMenu(v, 0f, 720f, 1000)
        // 子view弹出与收回
        toggleMenu(TIME)
    }

    //九个位置三列
    private fun getChildPadLeft(menuWidth: Int, lp: ViewGroup.MarginLayoutParams, childWidth: Int): Int {
        return when (position) {
            CENTER, CENTER_TOP, CENTER_BOTTOM -> (width - childWidth) / 2 + lp.leftMargin + paddingLeft - paddingRight
            LEFT_TOP, LEFT_BOTTOM, CENTER_LEFT -> paddingLeft + lp.leftMargin + (menuWidth - childWidth) / 2
            RIGHT_TOP, RIGHT_BOTTOM, CENTER_RIGHT -> width - childWidth - lp.rightMargin - paddingRight - (menuWidth - childWidth) / 2
            else -> 0
        }
    }

    //九个位置三行
    private fun getChildPadTop(menuHeight: Int, lp: ViewGroup.MarginLayoutParams, childHeight: Int): Int = when (position) {
        CENTER, CENTER_LEFT, CENTER_RIGHT -> (height - childHeight) / 2 + lp.topMargin + paddingTop - paddingBottom
        LEFT_TOP, RIGHT_TOP, CENTER_TOP -> paddingTop + lp.topMargin + (menuHeight - childHeight) / 2
        LEFT_BOTTOM, CENTER_BOTTOM, RIGHT_BOTTOM -> height - childHeight - lp.bottomMargin - paddingBottom - (menuHeight - childHeight) / 2
        else -> 0
    }

    override fun generateLayoutParams(attrs: AttributeSet) = FrameLayout.LayoutParams(context, attrs)

    /**
     * 子view的弹出与收回
     *
     * @param Duration
     */
    fun toggleMenu(Duration: Int) {
        val childCount = childCount

        var animator01: ObjectAnimator
        var animator02: ObjectAnimator
        //旋转动画定义
        var rotation: ObjectAnimator
        var set: AnimatorSet
        // 弹出后子view的xy位置，去除最后一个子view
        var ix = 0f
        var iy = 0f
        var childAt: View

        for (i in 0 until childCount - 1) {
            childAt = getChildAt(i)
            //开始计算每个view的弹出位置
            when (position) {
                LEFT_TOP, //直角时的xy值，默认的位置为LEFT_TOP；默认cong从3点位置开始顺时针旋转;sin与cos互换则从六点位置开始逆时针旋转
                LEFT_BOTTOM, //与LEFT_TOP的Y轴相反，颠倒y轴方向即可；Y*-1
                RIGHT_TOP, //与LEFT_TOP的X轴相反，颠倒X轴方向即可；X*-1
                RIGHT_BOTTOM//与LEFT_TOP的XY轴都相反，颠倒X轴方向即可；X*-1,Y*-1
                -> {
                    ix = getCos(2.0, childCount - 2, i) * tempX
                    iy = getSin(2.0, childCount - 2, i) * tempY
                }
                CENTER//位于中间时的xy值，与LEFT_TOP一样，只是大了四倍而已,同时第一个与最后一个view连接，多了一个角度
                -> {
                    ix = getCos(1.0 / 2, childCount - 1, i)
                    iy = getSin(1.0 / 2, childCount - 1, i)
                }
                CENTER_TOP, //平角时的xy值；默认为CENTER_TOP；CENTER_BOTTOM,CENTER_LEFT,CENTER_RIGHT
                CENTER_BOTTOM//与LEFT_BOTTOM一样，只是大了两倍而已
                -> {
                    ix = getCos(1.0, childCount - 2, i)
                    iy = getSin(1.0, childCount - 2, i) * tempY
                }
                CENTER_LEFT//弹出位置改变，sin与cos互换
                -> {
                    ix = getSin(1.0, childCount - 2, i)
                    iy = getCos(1.0, childCount - 2, i)
                }
                CENTER_RIGHT//与CENTER_LEFT的X相反，X*-1
                -> {
                    ix = getSin(1.0, childCount - 2, i) * tempX
                    iy = getCos(1.0, childCount - 2, i)
                }
            }
            // 根据菜单的状态判断是弹出还是收回，平移的动画定义
            if (mState === State.CLOSE) {
                animator01 = ObjectAnimator.ofFloat(childAt, "translationX", 0f, ix)
                animator02 = ObjectAnimator.ofFloat(childAt, "translationY", 0f, iy)
            } else {
                animator01 = ObjectAnimator.ofFloat(childAt, "translationX", ix, 0f)
                animator02 = ObjectAnimator.ofFloat(childAt, "translationY", iy, 0f)
            }
            //旋转动画定义
            rotation = if (i % 2 == 1)
                ObjectAnimator.ofFloat(childAt, "pivotX", 0f, 360f)
            else ObjectAnimator.ofFloat(childAt, "pivotY", 0f, 360f)
            //把几个动画组合在一起
            set = AnimatorSet()
            set.interpolator = BounceInterpolator()//差值器
            set.playTogether(animator01, animator02, rotation)
            set.duration = Duration.toLong()
            set.startDelay = ((i + 1) * 100).toLong()// 每个的子view的延迟时间
            //子view的点击事件
            childAt.setOnClickListener { v ->
                menuItemClick?.invoke(v, i)
                // 子view点击之后的效果
                childAnimator(i)
                // 点击之后收回子view
                if (isRecoverChildView) toggleMenu(300)
            }
            // 执行动画
            set.start()
            //监听最后一个view的动画是否停止，
            if (isNowRotating && i == childCount - 2)
                set.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        isNowRotating = false
                    }
                })
        }
        // 执行完成后切换菜单状态
        mState = changeState()
    }

    private fun getCos(num: Double, count: Int, num3: Int) = (radius * Math.cos(Math.PI / num / count * num3)).toFloat()

    private fun getSin(num: Double, count: Int, num3: Int) = (radius * Math.sin(Math.PI / num / count * num3)).toFloat()

    /**
     * 子view的动画效果
     *
     * @param pos 选中的view
     */
    private fun childAnimator(pos: Int) {
        var scaleX: ObjectAnimator
        var childAt: View
        // 透明化效果
        var alpha: ObjectAnimator
        var set: AnimatorSet
        for (i in 0 until childCount - 1) {
            childAt = getChildAt(i)
            scaleX = // 让被选中的childat变大，其他变小
                    if (i == pos) ObjectAnimator.ofFloat(childAt, "scaleX", 1f, 2f, 1f)
                    else ObjectAnimator.ofFloat(childAt, "scaleX", 1f, 0f, 1f)

            // 透明化效果
            alpha = ObjectAnimator.ofFloat(childAt, "alpha", 1f, 0f, 1f)
            set = AnimatorSet()
            set.playTogether(alpha, scaleX)
            set.duration = (300 * (i + 1)).toLong()
//            set.duration = 1000
            set.start()
        }
    }

    /**
     * 切换菜单状态
     */
    private fun changeState() = if (mState === State.OPEN) State.CLOSE else State.OPEN


    /**
     * 主菜单旋转效果
     *
     * @param v 主菜单
     * @param first 起始值
     * @param end 结束时
     * @param duration 时间
     */
    private fun rotationMenu(v: View, first: Float, end: Float, duration: Int) {
        ObjectAnimator.ofFloat(v, "rotation", first, end).setDuration(duration.toLong()).start()
    }

    companion object {
        // 九个位置定义
        val LEFT_TOP = 0
        val LEFT_BOTTOM = 1
        val RIGHT_TOP = 2
        val RIGHT_BOTTOM = 3
        val CENTER_TOP = 4
        val CENTER_BOTTOM = 5
        val CENTER = 6
        val CENTER_LEFT = 7
        val CENTER_RIGHT = 8

        val RADIUS = 100f //默认的半径
        val TIME = 500  //子view弹出与收回的时间
    }

    /**
     * 菜单状态监听接口
     */
    interface MenuState {
        fun openMenu()
        fun closeMenu()
    }
}