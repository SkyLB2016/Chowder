package com.sky.chowder.ui.activity

import android.graphics.BlurMaskFilter
import android.graphics.EmbossMaskFilter
import android.view.View
import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import kotlinx.android.synthetic.main.activity_draw.*

/**
 * Created by SKY on 16/5/10 下午3:50.
 * 双缓冲实验
 */
class DrawActivity : BaseNoPActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_draw

    private lateinit var blurMaskFilter: BlurMaskFilter
    private lateinit var filter: EmbossMaskFilter
    override fun initialize() {
        filter = EmbossMaskFilter(floatArrayOf(1.5f, 1.5f, 1.5f), 0.6f, 6f, 4.2f)
        blurMaskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)
        //        drawView.cachePaint.setMaskFilter(filter);
//        btnChange.setOnClickListener { _ -> draw!!.cachePaint!!.maskFilter = blurMaskFilter }
    }

    fun changeOnClick(view: View) {
        draw!!.cachePaint!!.maskFilter = blurMaskFilter
    }

}