package com.sky.chowder.ui.adapter

import android.app.Activity
import android.content.Context
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.sky.chowder.R
import com.sky.chowder.ui.widget.DepthPageTransformer
import java.util.*

/**
 * Created by SKY on 2017/3/6.
 */
class GuideContoler(private val mContext: Context) {
    private var mViewPager: ViewPager? = null
    //ViewPager要显示的视图集合
    private var mViews: MutableList<View>? = null

    //点的集合
    private var pointGroup: LinearLayout? = null
    private var points: Array<View?>? = null
    //点的宽高
    private var pointW: Int = 0
    private var pointH: Int = 0

    private var pointSelect: Int = 0
    private var unPointSelect: Int = 0

    /***
     * 设置数据,适用于前面页面是图片，最后一个页面是一个layout布局
     * @param imgIds 图片的id数组
     * @param view
     */
    fun init(imgIds: IntArray, view: View) {
        mViews = ArrayList()
        for (i in imgIds.indices) {
            val iv = ImageView(mContext)
            iv.setBackgroundResource(imgIds[i])
            iv.scaleType = ImageView.ScaleType.FIT_XY
            mViews!!.add(iv)
        }
        mViews!!.add(view)
        setViewPager()
        setPoints()
    }

    /**
     * 设置ViewPager
     */
    private fun setViewPager() {
        mViewPager = (mContext as Activity).findViewById(R.id.viewpager) as ViewPager
        mViewPager!!.setPageTransformer(true, DepthPageTransformer())
        mViewPager!!.adapter = GuideAdapter(mViews!!)
        mViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                for (i in points!!.indices) {
                    if (i == position) {
                        points!![i]!!.setBackgroundResource(pointSelect)
                    } else {
                        points!![i]!!.setBackgroundResource(unPointSelect)
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    /**
     * 设置指示器
     */
    private fun setPoints() {
        setPointWH()
        pointGroup = (mContext as Activity).findViewById(R.id.layout_group) as LinearLayout
        points = arrayOfNulls(mViews!!.size)
        val params = LinearLayout.LayoutParams(pointW, pointH)
        params.setMargins(0, 0, 15, 0)
        for (i in points!!.indices) {
            points!![i] = View(mContext)
            if (i == 0) {
                points!![i]!!.setBackgroundResource(pointSelect)
            } else {
                points!![i]!!.setBackgroundResource(unPointSelect)
            }
            points!![i]!!.layoutParams = params
            pointGroup!!.addView(points!![i])
        }
    }

    private fun setPointWH() {
        pointW = if (pointW == 0) POINT_W else pointW
        pointH = if (pointH == 0) POINT_H else pointH
        pointSelect = R.drawable.shape_selected_oval
        unPointSelect = R.drawable.shape_unselected_oval
    }


    fun setPointW(pointW: Int) {
        this.pointW = pointW
    }

    fun setPointH(pointH: Int) {
        this.pointH = pointH
    }

    companion object {

        //默认点的宽高
        private val POINT_W = 25
        private val POINT_H = 25
    }
}
