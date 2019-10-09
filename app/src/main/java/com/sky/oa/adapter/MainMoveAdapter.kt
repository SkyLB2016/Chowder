package com.sky.oa.adapter

import android.animation.ValueAnimator
import android.graphics.Typeface
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.animation.LayoutAnimationController
import android.view.animation.ScaleAnimation
import android.widget.LinearLayout
import com.sky.design.adapter.RecyclerAdapter
import com.sky.design.adapter.RecyclerHolder
import com.sky.oa.R
import com.sky.oa.model.ActivityModel
import kotlinx.android.synthetic.main.adapter_main_delete.view.*

/**
 * Created by SKY on 2015/12/6.
 * 主页
 */
class MainMoveAdapter(layoutId: Int) : RecyclerAdapter<ActivityModel>(layoutId) {
    private val fontIcon = intArrayOf(R.string.font, R.string.font01, R.string.font02, R.string.font03, R.string.font04, R.string.font05, R.string.font06, R.string.font07, R.string.font08)


    var isLeft = true//是否在左边
    var upDown = false//是否已进入上下滑动
    var leftRight = false//是否已进入左右滑动，唯一
    var moveX = 0f//移动的距离
    var downX = 0f
    var downY = 0f
    var slop = 0
    override fun onAchieveHolder(holder: RecyclerHolder, position: Int) {

        slop = ViewConfiguration.get(context).scaledTouchSlop
        moveX = context.resources.getDimension(R.dimen.wh_64)

        //应写成异步调用
        val scale = ScaleAnimation(0f, 1f, 0f, 1f)
        scale.duration = 100
        var face = Typeface.createFromAsset(context.assets, "font/icomoon.ttf")//字体，icomoon对应fonticon
        if (position % 2 == 1) {
            val controller = LayoutAnimationController(scale, 0.5f)
            controller.order = LayoutAnimationController.ORDER_RANDOM
            (holder?.itemView as ViewGroup).layoutAnimation = controller
            face = Typeface.createFromAsset(context.assets, "font/Lobster-Regular.ttf")//不对应fonticon
        } else holder?.itemView.startAnimation(scale)
        with(holder!!.itemView) {
            tvName.text = "${datas[position].className}" + resources.getString(fontIcon[position % 9])
            tvDescribe.text = resources.getString(fontIcon[8 - position % 9]) + datas[position].describe
            tvImage.text = resources.getString(fontIcon[position % 9])

            tvName.typeface = face
            tvDescribe.typeface = face
            tvImage.typeface = face
            tvImage.textSize = 50f
            layout.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        downX = event.rawX
                        downY = event.rawY

                        upDown = false
                        leftRight = false
                        isLeft = layout.scrollX > 10
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val tempY = downY - event.rawY
                        val tempX = downX - event.rawX

                        //处理上下左右的滑动分发时间,同时也处理了不大于slop不开始滑动
                        if (upDown) return@setOnTouchListener false
                        else if (!upDown && !leftRight) {
                            if (Math.abs(tempY) > slop) {
                                upDown = true
                                parent.requestDisallowInterceptTouchEvent(false)
                                return@setOnTouchListener false
                            } else if (Math.abs(tempX) > slop) {
                                leftRight = true
                                parent.requestDisallowInterceptTouchEvent(true)
                            } else {
                                return@setOnTouchListener false
                            }
                        }

                        //左滑
                        if (tempX > 0 && tempX <= moveX && !isLeft) {
                            layout.scrollTo(tempX.toInt(), 0)
                            return@setOnTouchListener true
                        } else if (tempX < 0 && tempX >= -moveX && isLeft) {//右滑
                            layout.scrollTo((moveX + tempX).toInt(), 0)
                            return@setOnTouchListener true
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        parent.requestDisallowInterceptTouchEvent(false)

                        val tempX = event.rawX - downX
                        val tempY = event.rawY - downY
                        if (Math.abs(tempX) < slop && Math.abs(tempY) < slop && isLeft) {
                            startAnimator(layout.scrollX.toFloat(), 0f, layout)
//                            layout.scrollTo(0, 0)
                        } else if (Math.abs(tempX) < slop && Math.abs(tempY) < slop) {
                            return@setOnTouchListener false
                        } else
                        //移动距离大于一半显示删除按钮，否则隐藏
                            if (layout.scrollX >= moveX / 2) {
//                                layout.scrollTo(moveX.toInt(), 0)
                                startAnimator(layout.scrollX.toFloat(), moveX, layout)
                            } else {
                                startAnimator(layout.scrollX.toFloat(), 0f, layout)
//                                layout.scrollTo(0, 0)
                            }
                        return@setOnTouchListener true
                    }
                }
                false
            }
            img_delete.setOnClickListener {
                datas.removeAt(position)
                layout.scrollTo(0, 0)
                notifyDataSetChanged()
            }
        }
    }

    private fun startAnimator(start: Float, end: Float, view: LinearLayout?) {
        val animator = ValueAnimator.ofFloat(start, end)
        animator.duration = 300
        animator.addUpdateListener { animation -> view?.scrollTo(animation!!.animatedValue.toString().toFloat().toInt(), 0) }
        animator.start()
    }
}