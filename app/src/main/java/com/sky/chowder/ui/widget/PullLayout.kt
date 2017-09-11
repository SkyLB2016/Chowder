package com.sky.chowder.ui.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import com.sky.chowder.R
import com.sky.utils.ScreenUtils

/**
 * Created by SKY on 2015/8/19.
 * 下拉菜单，待调整
 */
class PullLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ScrollView(context, attrs, defStyleAttr) {

    //private ViewGroup wallPaper;
    private var mMenu: ViewGroup? = null
    private var mContent: ViewGroup? = null
    private var titleBar: ViewGroup? = null
    private var back: Button? = null//返回键
    private var right: Button? = null
    private var action_title: TextView? = null//标题

    private var ll_weather: ViewGroup? = null
    private var ev: EyeView? = null

    private var animator: ObjectAnimator? = null

    private var once: Boolean? = true
    private val screenWidth: Int = ScreenUtils.getWidthPX(context)
    private val screenHeight: Int = ScreenUtils.getHeightPX(context)

    private var mState = State.OPEN
    private val downX: Float = 0.toFloat()
    private var downY: Float = 0.toFloat()//按下时的xy
    private var menuHeight: Int = 0

    override fun onFinishInflate() {
        super.onFinishInflate()
        isVerticalScrollBarEnabled = false
        titleBar = findViewById(R.id.titlebar) as ViewGroup
        action_title = findViewById(R.id.action_title) as TextView
        back = findViewById(R.id.action_back) as Button
        right = findViewById(R.id.action_menu) as Button
        setTitleBar(R.mipmap.ic_back, R.color.white)

        mMenu = findViewById(R.id.rl_menu) as ViewGroup
        ev = findViewById(R.id.ev) as EyeView
        ll_weather = findViewById(R.id.ll_weather) as ViewGroup
        ev!!.setOnClickListener { close() }

        mContent = findViewById(R.id.ll_content) as ViewGroup
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (once!!) {
            mMenu!!.layoutParams.height = screenHeight / 4 * 3
            menuHeight = mMenu!!.layoutParams.height
            once = false
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        titleBar!!.layout(0, 0, titleBar!!.width, titleBar!!.height)
        mContent!!.layout(0, menuHeight, mContent!!.width, mContent!!.height + menuHeight)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> downY = ev.y
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_MOVE -> {
                val moveY = (ev.y.toInt() - downY).toInt()
                //moveY>0下拉，同时改变menu的大小；<0上拉，无操作
                //                if (moveY > 0 && getScrollY() == 0) {
                if (moveY > 0 && isOpen!!) {
                    setT(-moveY / 4)
                    return true
                }
            }
            MotionEvent.ACTION_UP -> {
                //open状态下下拉后，恢复menu的状态
                if (ev.y - downY > 0 && isOpen!!) {
                    animatePull(0F)
                    return true
                }
                //menu的打开与关闭，getScrollY()必须小于menuHeight
                if (ev.y - downY > 20 && scrollY < menuHeight) {
                    open()
                } else if (ev.y - downY < -20 && scrollY < menuHeight) {
                    close()
                }
                return true
            }
        }
        return super.onTouchEvent(ev)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (t > menuHeight) {
            ObjectAnimator.ofFloat(titleBar, "translationY", t.toFloat())
            return
        } else {
            animateScroll(t.toFloat())
        }
    }

    //整个 view中的精华之所在
    private fun setT(t: Int) {
        scrollTo(0, t)
        if (t < 0) {
            animatePull(t.toFloat())
        }
    }

    private fun animateScroll(t: Float) {
        val percent = t.toFloat() / menuHeight
        ObjectAnimator.ofFloat(mMenu, "translationY", t)
        ObjectAnimator.ofFloat(mContent, "translationY", titleBar!!.height * percent)
        ObjectAnimator.ofFloat(ev, "translationY", -t / 2)
        ObjectAnimator.ofFloat(ll_weather, "translationY", -t / 2)

        //        ViewHelper.setTranslationY(mMenu, t);
        //        ViewHelper.setTranslationY(mContent, titleBar.getHeight() * percent);
        //        ViewHelper.setTranslationY(ev, -t / 2);
        //        ViewHelper.setTranslationY(ll_weather, -t / 2);
        ev!!.setRadius((menuHeight.toFloat() * 0.25f * (1 - percent)).toInt())
        action_title!!.setTextColor(evaluate(percent, Color.WHITE, Color.BLACK))
        ObjectAnimator.ofFloat(titleBar, "translationY", t)
        //        ViewHelper.setTranslationY(titleBar, t);
        titleBar!!.setBackgroundColor(evaluate(percent, Color.argb(0, 255, 255, 255), Color.argb(255, 255, 255, 255)))
        if (percent == 1f) {
            back!!.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.mipmap.ic_back), null, null, null)
        } else if (percent == 0f) {
            back!!.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.mipmap.ic_done), null, null, null)
        }
    }

    private fun animatePull(t: Float) {
        mMenu!!.layoutParams.height = menuHeight - t.toInt()
        mMenu!!.requestLayout()
        ObjectAnimator.ofFloat(mContent, "translationY", -t)
        //        ViewHelper.setTranslationY(mContent, -t);

        val percent = t.toFloat() / menuHeight
        ev!!.scaleX = 1 - percent
        ev!!.scaleY = 1 - percent
        ObjectAnimator.ofFloat(ll_weather, "translationY", -t / 2)
        //        ViewHelper.setScaleX(ev, 1 - percent);
        //        ViewHelper.setScaleY(ev, 1 - percent);
        //        ViewHelper.setTranslationY(ll_weather, -t / 2);

    }

    private fun evaluate(fraction: Float, startValue: Any, endValue: Int?): Int {
        val startInt = startValue as Int
        val startA = startInt shr 24 and 0xff
        val startR = startInt shr 16 and 0xff
        val startG = startInt shr 8 and 0xff
        val startB = startInt and 0xff
        val endInt = endValue!!
        val endA = endInt shr 24 and 0xff
        val endR = endInt shr 16 and 0xff
        val endG = endInt shr 8 and 0xff
        val endB = endInt and 0xff
        return startA + (fraction * (endA - startA)).toInt() shl 24 or (startR + (fraction * (endR - startR)).toInt() shl 16) or (startG + (fraction * (endG - startG)).toInt() shl 8) or startB + (fraction * (endB - startB)).toInt()
    }

    val isOpen: Boolean?
        get() = mState === State.OPEN

    fun toggleMenu() {
        if (isOpen!!) {
            close()
        } else {
            open()
        }
    }

    fun close() {
        if (animator != null && animator!!.isRunning) {
            return
        }
        val start = scrollY
        animator = ObjectAnimator.ofInt(this, "t", start, menuHeight)
        //        animator.setInterpolator(new DecelerateInterpolator());
        animator!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                mState = State.CLOSE
            }
        })
        animator!!.duration = 250
        animator!!.start()
    }

    fun open() {
        if (animator != null && animator!!.isRunning) {
            return
        }
        val start = scrollY
        val center = (-scrollY / 2.2f).toInt()
        animator = ObjectAnimator.ofInt(this, "t", start, center, 0)
        //        animator.setInterpolator(new DecelerateInterpolator());
        animator!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                mState = State.OPEN
            }
        })
        animator!!.duration = 400
        animator!!.start()
    }

    /**
     * 设置titlebar
     */
    private fun setTitleBar(backImgId: Int, titleColorId: Int) {
        back!!.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(backImgId), null, null, null)
        titleBar!!.setBackgroundResource(R.color.dark_orange)
        action_title!!.setTextColor(resources.getColor(titleColorId))
    }

}