package com.sky.chowder.ui.activity

import android.os.Bundle
import com.sky.chowder.R
import com.sky.design.app.BaseActivity

/**
 * Created by SKY on 2017/3/6.
 * 下拉动画
 */
class PullDownActivity : BaseActivity() {

    override fun getLayoutResId(): Int = R.layout.activity_pulldown

    override fun initialize(savedInstanceState: Bundle?) = showToast(intent.getStringExtra("message") ?: "")
    override fun loadData() = Unit

}
