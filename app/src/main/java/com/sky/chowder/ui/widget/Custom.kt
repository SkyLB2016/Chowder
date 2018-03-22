package com.sky.chowder.ui.widget

import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.Transformation

/**
 * Created by SKY on 2018/3/2 15:43.
 * 自定义动画效果
 */
class Custom : Animation() {

    private var centerX: Int = 0
    private var centerY: Int = 0
    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
        duration = 2000
        fillAfter = true
        interpolator = BounceInterpolator()
        centerX = width / 2
        centerY = height / 2
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        super.applyTransformation(interpolatedTime, t)
        val matrix = t.matrix
        matrix.postScale(1f, 1 - interpolatedTime, centerX.toFloat(), centerY.toFloat())
    }
}
