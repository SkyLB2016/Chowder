package com.sky.design.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.sky.sdk.utils.ScreenUtils
import java.util.*

/**
 * Created by SKY on 2015/4/2 20:43:43.
 * 流式布局
 */
class FlowLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    // 所有控件，分行排列
    private val allViews = ArrayList<List<View>>()
    // 记录行高
    private val lineHeights = ArrayList<Int>()
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val layoutWidth = MeasureSpec.getSize(widthMeasureSpec)//match_parent时的宽
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)//获取测量模式，match与wrap
        val layoutHeight = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        // 模式为wrap_content时，测量框架的宽高
        // 先清空
        allViews.clear()
        lineHeights.clear()
        var lineViews: MutableList<View> = ArrayList()
        var width = 0
        var height = 0
        // 记录每一行的高度和宽度
        var lineWidth = 0
        var lineHeight = 0

        var child: View
        var lp: MarginLayoutParams
        var childWidth: Int
        var childHeight: Int
        val childCount = childCount
        val realWidth = layoutWidth - paddingLeft - paddingRight
        for (i in 0 until childCount) {
            child = getChildAt(i)
            measureChild(child, widthMeasureSpec, widthMeasureSpec)// 测量子view
            lp = child.layoutParams as ViewGroup.MarginLayoutParams
            childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin
            //当行宽加上一个childWidth大于父布局的实际宽时，切换下一行
            if (lineWidth + childWidth > realWidth) {
                //计算流布局的宽高
                width = Math.max(width, lineWidth)
                height += lineHeight
                //切换下一行，重置行高行宽
                lineWidth = childWidth
                lineHeight = childHeight

                allViews.add(lineViews)
                lineHeights.add(lineHeight)
                lineViews = ArrayList()
            } else {//不换行时，只计算行宽高
                lineWidth += childWidth
                lineHeight = Math.max(lineHeight, childHeight)
            }
            //判断最后一个，加载最后一行的宽高，获取最终宽高
            if (i === childCount - 1) {
                width = Math.max(width, lineWidth)
                height += lineHeight
            }
            lineViews.add(child)
        }
        allViews.add(lineViews)
        lineHeights.add(lineHeight)
        // 为框架父控件写入宽高
        setMeasuredDimension(
                if (widthMode == MeasureSpec.EXACTLY) layoutWidth else width + paddingLeft + paddingRight,
                if (heightMode == MeasureSpec.EXACTLY) layoutHeight else height + paddingTop + paddingBottom
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var lineViews: MutableList<View>
        var lineHeight: Int
        var left = paddingLeft
        var top = paddingTop
        for (i in allViews.indices) {
            lineViews = allViews[i] as MutableList<View>
            lineHeight = lineHeights[i]
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

    override fun generateLayoutParams(attrs: AttributeSet) = FrameLayout.LayoutParams(context, attrs)

    var lastY = 0f
    //滑动控件内容
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> lastY = event.rawY
            MotionEvent.ACTION_MOVE -> {
                var dy = lastY - event.rawY
                if (scrollY < 0 && dy < 0)
                    dy = 0f
                if (scrollY > height - ScreenUtils.getHeightPX(context) + ScreenUtils.getStatusHeight(context) + 10 && dy > 0)
                    dy = 0f
                scrollBy(0, dy.toInt())

//                var dy = event.rawY - lastY
//                if (scrollY < 0 && dy > 0)
//                    dy = 0f
//                if (scrollY > height - ScreenUtils.getHeightPX(context) + ScreenUtils.getStatusHeight(context) + 10 && dy < 0)
//                    dy = 0f
//                scrollBy(0, -dy.toInt())

                lastY = event.rawY
            }
        }
        postInvalidate()
        return true
    }
}