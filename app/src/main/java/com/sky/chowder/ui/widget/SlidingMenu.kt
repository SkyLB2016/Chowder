package com.sky.chowder.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import com.sky.utils.ScreenUtils
import com.sky.widget.State

/**
 * Created by SKY on 2015/8/17 15:56.
 * 横向的侧滑栏
 */
class SlidingMenu @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : HorizontalScrollView(context, attrs, defStyleAttr) {

    private val screenWidth = ScreenUtils.getWidthPX(context)
    private val screenHeight: Int = ScreenUtils.getHeightPX(context)
    private var once = true
    private var wallPaper: ViewGroup? = null
    private var menu: ViewGroup? = null
    private var content: ViewGroup? = null

    private val contentScale = DEFAULTSCALE//控制主布局缩放的大小
    private val mMenuScale = DEFAULTSCALE//控制菜单缩放的大小
    private var menuWidth: Int = 0
    private var downX: Float = 0f
    private var downY: Float = 0f
    private var downTime: Long = 0//按下时的时间，计算速度，展开或者关闭menu
    private var state = State.CLOSE//默认状态

    private val isClose: Boolean
        get() = state === State.CLOSE

    private val isOpen: Boolean
        get() = state === State.OPEN

    var onMenuScroll: OnMenuScroll? = null//
    var menuState: MenuState? = null//menu打开与关闭时的监听

    fun toggleMenu() = if (isClose) open() else close()

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
        state = State.CLOSE
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {//获取按下时的信息
                downX = event.rawX
                downY = event.rawY
                downTime = System.currentTimeMillis()
                if (isOpen && downX > menuWidth) return true//打开时，某些部分的touch事件不向下传递
            }
            MotionEvent.ACTION_MOVE ->//在范围内时滑动的x距离大于100的时候才开始滑动状态
                if (downY > screenHeight * 2 / 5 && event.rawX - downX > 100)
                    return true
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                //抬起时的XY以及时间
                val upX = event.rawX
                val upY = event.rawY
                val distance = upX - downX//抬起时x方向走过的总路程
                //在open状态下判断点击后抬起时，xy在content范围内，需要关闭menu
                if (isOpen) {
                    //获取content的顶部与底部位置
                    val top = content!!.height * (SCALE - contentScale) / 2
                    val bottom = content!!.height - top
                    //保证X点在主布局的范围内,同时移动的绝对距离不大于10
                    if (downX > menuWidth /*&& upX>menuWidth*/ && Math.abs(distance) < 10
                            //保证Y点落下与抬起时均在主布局的范围内（/*同时移动的绝对距离不大于10*/）
                            && downY > top && downY < bottom && upY > top && upY < bottom /*&& Math.abs(upY - downY) < 10*/)
                        return close()//不在向下传递
                }
                //计算速度,以及左右滑动时的操作
                val speed = distance / (System.currentTimeMillis() - downTime)
//                LogUtils.i("speed==$speed")
                if (distance < 0 /*左滑*/ && Math.abs(speed) > SPEED && isOpen) return close()
                else if (distance > 0 /*右滑*/ && Math.abs(speed) > SPEED && isClose) return open()
                //正常滑动时，menu滑出不到一半时关闭，否则打开,/*true事件拦截不再向下传递*/
//                return if (scrollX >= menuWidth / 2) close() else open()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        val percent = l * SCALE / menuWidth
        //打开时是1--0，关闭时是0--1
        //打开时menu的透明度及大小从0.7到1，content的大小从1到0.7
        //关闭时menu的透明度及大小从1到0.7，content的大小从0.7到1
        val mPercent = SCALE - (SCALE - mMenuScale) * percent//0.7--1
//        ObjectAnimator.ofFloat(menu!!, "translationX", menuWidth.toFloat() * percent * mMenuScale)//有从后边拉出来的感觉
//        menu?.translationX = menuWidth.toFloat() * percent * mMenuScale
        menu?.alpha = mPercent
        menu?.scaleX = mPercent
        menu?.scaleY = mPercent

        val cPercent = contentScale + (SCALE - contentScale) * percent
        content?.scaleX = cPercent
        content?.scaleY = cPercent
        //固定缩放时的中心
        content?.pivotX = 0f
        content?.pivotY = (content!!.height / 2).toFloat()
        if (onMenuScroll != null) onMenuScroll!!.onScrollChanged(l, t, oldl, oldt)
    }

    private fun open(): Boolean {
        smoothScrollTo(0, 0)
        state = State.OPEN
        if (menuState != null) menuState!!.onOpen()
        return true
    }

    private fun close(): Boolean {
        smoothScrollTo(menuWidth, 0)
        state = State.CLOSE
        if (menuState != null) menuState!!.onClose()
        return true
    }

    interface MenuState {
        fun onOpen()
        fun onClose()
    }

    companion object {
        private val SPEED = 2
        private val SCALE = 1f
        private val DEFAULTSCALE = 0.7f
    }

    interface OnMenuScroll {
        fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int)
    }
}