package com.sky.chowder.ui.activity

import android.graphics.Rect
import android.graphics.RectF
import android.os.Handler
import android.os.Message
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import com.sky.utils.LogUtils
import com.sky.widget.BaseTitle
import kotlinx.android.synthetic.main.activity_poetry.*
import java.util.*

/**
 * Created by SKY on 2018/3/16.
 */
class PoetryActivity : BaseNoPActivity(), View.OnClickListener {

    val handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            llBottomBar.visibility = View.GONE
        }
    }

    private val resId = ArrayList<Int>()
    override fun getLayoutResId(): Int = R.layout.activity_poetry
    override fun initialize() {
        baseTitle.setLeftButton(R.mipmap.ic_menu)
        baseTitle.onLeftImgClick = BaseTitle.OnClickListener { sliding.toggleMenu() }
        baseTitle.setRightText("3.16建")

        val poetry = resources.getStringArray(R.array.poetry)!!
        var tv: TextView
        for ((index, text) in poetry.withIndex()) {
            val array = text.split(",")
            tv = LayoutInflater.from(this).inflate(R.layout.tv, flow, false) as TextView
            tv.width = resources.getDimensionPixelSize(R.dimen.wh_96)
            tv.textSize = 18f
            tv.text = array[0]
//            tv.tag = array[0]
            tv.id = index
            flow.addView(tv)
            tv.setOnClickListener(this)
            resId.add(resources.getIdentifier(array[1], "string", packageName))
        }
        tvDisplay.text = getString(resId[3]).replace(" ", "")
        tvDisplay.gravity = Gravity.CENTER
        setToolbarTitle(tvDisplay.text.lines()[0])
        llBottomBar.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        tvDisplay.gravity = when (v?.id) {
            in 2..5 -> Gravity.CENTER
            else -> Gravity.LEFT
        }
        tvDisplay.text = getString(resId[v!!.id]).replace(" ", "")
        setToolbarTitle(tvDisplay.text.lines()[0])
    }

    private fun moveToLine(line: Int) {
        val layout = tvDisplay.layout
        val rect = Rect()
        layout?.getLineBounds(line, rect)
        scroll?.scrollTo(0, rect.height() * line + tvDisplay.paddingTop)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
        return super.onTouchEvent(event)
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        val yy = getObject("scrollX", 0)
//        scroll?.scrollTo(0, yy)
    }

    var downX = 0f
    var downY = 0f
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev!!.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
            }
            MotionEvent.ACTION_UP -> {
                val rect = RectF(1080 / 3f, 1920 / 3f, 1080 / 3f * 2, 1920 / 3f * 2)
                if (Math.abs(ev.x - downX) < 5 && Math.abs(ev.y - downY) < 1 && rect.contains(ev.x, ev.y)) {
                    showToast("目录")
                    if (llBottomBar.visibility == View.GONE) {
                        llBottomBar.visibility = View.VISIBLE
                        handler.sendEmptyMessageDelayed(1, 5000)
                    } else {
                        llBottomBar.visibility = View.GONE
                        handler.removeMessages(1)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val layout = tvDisplay.layout
        val rect = Rect()
        layout?.getLineBounds(9, rect)
        val yy = scroll.scrollY
        val line = (yy - tvDisplay.paddingTop) / rect.height()
        LogUtils.i("line==$line")
        LogUtils.i("scrollX==${scroll.scrollY}")
        setObject("line", line)
        setObject("scrollX", scroll.scrollY)

    }

//    override fun onRestart() {
//        super.onRestart()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onStart() {
//        super.onStart()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onResume() {
//        super.onResume()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onPause() {
//        super.onPause()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }

}
