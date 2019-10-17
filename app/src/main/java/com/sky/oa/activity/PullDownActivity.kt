package com.sky.oa.activity

import android.os.Bundle
import com.sky.oa.R
import com.sky.design.app.BaseActivity

/**
 * Created by SKY on 2017/3/6.
 * 下拉动画
 */
class PullDownActivity : BaseActivity() {

    override fun getLayoutResId(): Int = R.layout.activity_pulldown
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showToast(intent.getStringExtra("message") ?: "")
    }
}
