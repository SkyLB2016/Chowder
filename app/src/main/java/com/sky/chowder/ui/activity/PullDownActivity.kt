package com.sky.chowder.ui.activity

import com.sky.base.BaseNoPActivity
import com.sky.chowder.R

/**
 * Created by SKY on 2017/3/6.
 * 下拉动画
 */
class   PullDownActivity : BaseNoPActivity() {

    override fun getLayoutResId(): Int = R.layout.activity_pulldown

    override fun initialize() = showToast(intent.getStringExtra("message"))
}
