package com.sky.oa.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.sky.oa.R
import com.sky.oa.widget.viewpager.DepthPageTransformer
import java.util.*

/**
 * Created by SKY on 2017/3/6.
 */
class SplashContoler(private val mContext: Context) {
    private lateinit var mViewPager: ViewPager
    //ViewPager要显示的视图集合
    private lateinit var mViews: MutableList<View>

    //点的集合
    private lateinit var pointGroup: LinearLayout
    private lateinit var points: Array<View?>
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
        mViewPager = (mContext as Activity).findViewById(R.id.viewpager)
        mViewPager.setPageTransformer(true, DepthPageTransformer())
        mViewPager.adapter = SplashAdapter(mViews)
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                for (i in points.indices) {
                    if (i == position) points[i]!!.setBackgroundResource(pointSelect)
                    else points[i]!!.setBackgroundResource(unPointSelect)
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
        pointGroup = (mContext as Activity).findViewById<LinearLayout>(R.id.layout_group)
        points = arrayOfNulls(mViews.size)
        val params = LinearLayout.LayoutParams(pointW, pointH)
        params.setMargins(0, 0, 15, 0)
        for (i in points.indices) {
            points[i] = View(mContext)
            points[i]?.setBackgroundResource(if (i == 0) pointSelect else unPointSelect)
            points[i]?.layoutParams = params
            pointGroup.addView(points[i])
        }
    }

    private fun setPointWH() {
        pointW = if (pointW == 0) POINT_W else pointW
        pointH = if (pointH == 0) POINT_H else pointH
        pointSelect = R.drawable.oval_ff4081
        unPointSelect = R.drawable.oval_white
    }


    fun setPointW(pointW: Int) {
        this.pointW = pointW
    }

    fun setPointH(pointH: Int) {
        this.pointH = pointH
    }

    companion object {

        //默认点的宽高
        private const val POINT_W = 25
        private const val POINT_H = 25
    }
}
