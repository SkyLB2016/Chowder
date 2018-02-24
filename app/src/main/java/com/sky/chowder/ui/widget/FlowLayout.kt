package com.sky.chowder.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Scroller
import java.util.*

/**
 * Created by SKY on 2015/4/2 20:43:43.
 * 流式布局
 */
class FlowLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    var scroller: Scroller? = null

    init {
        scroller = Scroller(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val layoutWidth = View.MeasureSpec.getSize(widthMeasureSpec)//match_parent是的宽
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)//获取测量模式，match与wrap
        val layoutHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        // 模式为wrap_content时，测量框架的宽高
        var width = 0// 框架款起始值
        var height = 0// 框架高起始值
        // 记录每一行的高度和宽度
        var lineWidth = 0
        var lineHeight = 0

        val childCount = childCount// 获取框架内的子控件
        for (i in 0 until childCount) {

            val view = getChildAt(i)// 获取子view
            measureChild(view, widthMeasureSpec, widthMeasureSpec)// 测量子view

            val lp = view.layoutParams as ViewGroup.MarginLayoutParams
            val childWidth = view.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = view.measuredHeight + lp.topMargin + lp.bottomMargin

            if (lineWidth + childWidth > layoutWidth - paddingLeft - paddingRight) {
                width = Math.max(width, lineWidth)
                lineWidth = childWidth

                height += lineHeight
                lineHeight = childHeight
            } else {
                lineWidth += childWidth
                lineHeight = Math.max(lineHeight, childHeight)
            }
            // 判断最后一个，获取最终宽高
            if (i == childCount - 1) {
                width = Math.max(width, lineWidth)
                height += lineHeight
            }
        }
        // 为框架父控件写入宽高
        setMeasuredDimension(
                if (widthMode == View.MeasureSpec.EXACTLY) layoutWidth else width + paddingLeft + paddingRight,
                if (heightMode == View.MeasureSpec.EXACTLY) layoutHeight else height + paddingTop + paddingBottom
        )
    }

    // 所有控件，分行排列
    private val allViews = ArrayList<List<View>>()
    // 记录行高
    private val mLineHeight = ArrayList<Int>()

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // 先清空
        allViews.clear()
        mLineHeight.clear()

        var lineWidth = 0
        var lineHeight = 0
        var lineViews: MutableList<View> = ArrayList()
        val width = width
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            val lp = view.layoutParams as ViewGroup.MarginLayoutParams
            val childWidth = view.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = view.measuredHeight + lp.topMargin + lp.bottomMargin

            if (childWidth + lineWidth > width - paddingLeft - paddingRight) {
                allViews.add(lineViews)
                mLineHeight.add(lineHeight)

                lineWidth = 0
                lineHeight = childHeight
                lineViews = ArrayList()
            }
            lineWidth += childWidth
            lineHeight = Math.max(lineHeight, childHeight)
            lineViews.add(view)
        }

        allViews.add(lineViews)
        mLineHeight.add(lineHeight)
        // 框架的内边距
        var left = paddingLeft
        var top = paddingTop

        for (i in allViews.indices) {
            lineViews = allViews[i] as MutableList<View>
            lineHeight = mLineHeight[i]
            for (j in lineViews.indices) {
                val child = lineViews[j]
                if (child.visibility == View.GONE) continue
                val lp = child.layoutParams as ViewGroup.MarginLayoutParams

                val leftChild = left + lp.leftMargin
                val topChild = top + lp.topMargin
                val rightChild = leftChild + child.measuredWidth
                val bottomChild = topChild + child.measuredHeight

                child.layout(leftChild, topChild, rightChild, bottomChild)

                left += lp.leftMargin + lp.rightMargin + child.measuredWidth
            }
            left = paddingLeft
            top += lineHeight
        }
    }

    /**
     * 与当前ViewGroup对应的LayoutParams
     */
    override fun generateLayoutParams(attrs: AttributeSet): FrameLayout.LayoutParams {
        return FrameLayout.LayoutParams(context, attrs)
    }

//    var lastY = 0f
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        when (event?.action) {
//            MotionEvent.ACTION_DOWN -> lastY = event.rawY
//            MotionEvent.ACTION_MOVE -> {
//                if (scroller!!.isFinished) scroller?.abortAnimation()
//                var dy = lastY - event.rawY
////                if (scrollY < 0)
////                    dy = 0f
////                if (scrollY > height-ScreenUtils.getHeightPX(context))
////                    dy = 0f
//                scrollBy(0, dy.toInt())
//                lastY = event.rawY
//            }
//        }
//        postInvalidate()
//        return true
//    }

}
