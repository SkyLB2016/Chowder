package com.sky.chowder.ui.adapter

import android.graphics.Typeface
import android.view.ViewGroup
import android.view.animation.LayoutAnimationController
import android.view.animation.ScaleAnimation
import com.sky.adapter.RecyclerAdapter
import com.sky.adapter.RecyclerHolder
import com.sky.chowder.R
import com.sky.chowder.model.ActivityModel
import kotlinx.android.synthetic.main.adapter_main.view.*

/**
 * Created by SKY on 2015/12/6.
 * 主页
 */
class MainAdapter(layoutId: Int) : RecyclerAdapter<ActivityModel>(layoutId) {
    var fontIcon = intArrayOf(R.string.font, R.string.font01, R.string.font02, R.string.font03,
            R.string.font04, R.string.font05, R.string.font06, R.string.font07, R.string.font08)

    override fun onAchieveHolder(holder: RecyclerHolder, position: Int) {
        //应写成异步调用
        val scale = ScaleAnimation(0f, 1f, 0f, 1f)
        scale.duration = 100
        if (position % 2 == 1) {
            val controller = LayoutAnimationController(scale, 0.5f)
            controller.order = LayoutAnimationController.ORDER_RANDOM
            (holder?.itemView as ViewGroup).layoutAnimation = controller
        } else holder?.itemView.startAnimation(scale)
        val face = Typeface.createFromAsset(context.assets, "font/icomoon.ttf")
        with(holder!!.itemView) {
            tvName.text = "${datas[position].className}" + resources.getString(fontIcon[position % 9])
            tvDescribe.text = resources.getString(fontIcon[8 - position % 9]) + datas[position].describe
            tvImage.text = resources.getString(fontIcon[position % 9])

            tvName.typeface = face
            tvDescribe.typeface = face
            tvImage.typeface = face
            tvImage.textSize=50f
        }
    }
}