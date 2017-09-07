package com.sky.chowder.ui.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import com.sky.utils.ScreenUtils

/**
 * @author 彬 QQ 1136096189
 * 横向的侧滑栏
 * @date 2015/8/17 15:56
 */
class SlidingMenu @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : HorizontalScrollView(context, attrs, defStyleAttr) {

    private val screenWidth: Int
    private val screenHeight: Int
    private var once = true
    private var wallPaper: ViewGroup? = null
    private var menu: ViewGroup? = null
    private var content: ViewGroup? = null

    private val content_scale = DEFAULTSCALE//控制主布局缩放的大小
    private val mMenu_scale = DEFAULTSCALE//控制菜单缩放的大小
    private var menuWidth: Int = 0
    private var downX: Float = 0.toFloat()
    private var downY: Float = 0.toFloat()
    private var downTime: Long = 0//按下时的时间，计算速度，展开或者关闭menu
    private var state = State.CLOSE//默认状态

    private var onMenuListener: OnMenuListener? = null

    fun setOnMenuListener(onMenuListener: OnMenuListener) {
        this.onMenuListener = onMenuListener
    }

    interface OnMenuListener {
        fun OnScrollChangedListener(l: Int, t: Int, oldl: Int, oldt: Int)
    }

    init {
        val screen = ScreenUtils.getWH(context)
        screenWidth = screen[0]
        screenHeight = screen[1]
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (once) {//执行一次
            wallPaper = getChildAt(0) as ViewGroup
            menu = wallPaper!!.getChildAt(0) as ViewGroup
            content = wallPaper!!.getChildAt(1) as ViewGroup
            menu!!.layoutParams.width = screenWidth / 4 * 3
            menuWidth = menu!!.layoutParams.width
            content!!.layoutParams.width = screenWidth
            once = false
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        //默认关闭
        this.smoothScrollTo(menuWidth, 0)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {

        when (ev.action) {
            MotionEvent.ACTION_DOWN//获取按下时的信息
            -> {
                downX = ev.rawX
                downY = ev.rawY
                downTime = System.currentTimeMillis()
                if (isOpen && downX > menuWidth) return true//打开时，某些部分的touch事件不向下传递
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_UP -> {
                //抬起时的XY以及时间
                val upX = ev.rawX
                val upY = ev.rawY
                val upTime = System.currentTimeMillis()
                //在open状态下判断点击后就抬起时，xy在content范围内，需要关闭menu
                if (isOpen) {
                    val top = content!!.height * (SCALE - content_scale) / 2
                    val bottom = content!!.height - top
                    if (downX > menuWidth
                            //&& upX>menuWidth
                            && Math.abs(upX - downX) < 10//x方向不能移动超过10的距离

                            //&& Math.abs(upY - downY) < 10//Y方向不能移动超过10的距离
                            && downY > top && downY < bottom
                            && upY > top && upY < bottom) {
                        close()
                        return true//不在向下传递
                    }
                }
                //计算速度,以及左右滑动时的操作
                val speed = (upX - downX) / (upTime - downTime)
                if (upX - downX < 0 //左滑
                        && Math.abs(speed) > SPEED && isOpen) {
                    close()
                    return true
                } else if (upX - downX > 0 //右滑

                        && Math.abs(speed) > SPEED && isClose) {
                    open()
                    return true
                }
                //正常滑动时，menu滑出不到一半时关闭，否则打开
                val x = scrollX
                if (x >= menuWidth / 2) {
                    close()
                } else {
                    open()
                }
                return true//事件拦截不再向下传递
            }
        }
        return super.onTouchEvent(ev)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        val percent = l * SCALE / menuWidth
        //打开时是1--0，关闭时是0--1
        //打开时menu的透明度及大小从0.7到1，content的大小从1到0.7
        //关闭时menu的透明度及大小从1到0.7，content的大小从0.7到1
        val mPercent = SCALE - (SCALE - mMenu_scale) * percent//0.7--1

        ObjectAnimator.ofFloat(menu, "translationX", 0f, menuWidth.toFloat() * percent * mMenu_scale)
        menu!!.alpha = mPercent
        menu!!.scaleX = mPercent
        menu!!.scaleY = mPercent
//        ViewHelper.setTranslationX(menu!!, menuWidth.toFloat() * percent * mMenu_scale)//有从后边拉出来的感觉
//        ViewHelper.setAlpha(menu!!, mPercent)
//        ViewHelper.setScaleX(menu!!, mPercent)
//        ViewHelper.setScaleY(menu!!, mPercent)

        val cPercent = content_scale + (SCALE - content_scale) * percent
        content!!.scaleX = cPercent
        content!!.scaleY = cPercent
//        ViewHelper.setScaleX(content!!, cPercent)
//        ViewHelper.setScaleY(content!!, cPercent)
        //固定缩放时的中心
        content!!.pivotX = 0f
        content!!.pivotY = (content!!.height / 2).toFloat()
//        ViewHelper.setPivotX(content!!, 0f)
//        ViewHelper.setPivotY(content!!, (content!!.height / 2).toFloat())
        if (onMenuListener != null)
            onMenuListener!!.OnScrollChangedListener(l, t, oldl, oldt)
    }

    private fun open() {
        smoothScrollTo(0, 0)
        state = State.OPEN
        if (menuState != null) menuState!!.OnOpen()
    }

    private fun close() {
        smoothScrollTo(menuWidth, 0)
        state = State.CLOSE
        if (menuState != null) menuState!!.OnClose()
    }

    private val isClose: Boolean
        get() = state === State.CLOSE

    private val isOpen: Boolean
        get() = state === State.OPEN

    fun toggleMenu() {
        if (isClose) open()
        else close()
    }

    private var menuState: MenuState? = null

    //menu打开与关闭时的监听
    fun setOnMenuState(menuState: MenuState) {
        this.menuState = menuState
    }

    interface MenuState {
        fun OnOpen()
        fun OnClose()
    }

    companion object {
        private val SPEED = 2
        private val SCALE = 1f
        private val DEFAULTSCALE = 0.7f
    }
}