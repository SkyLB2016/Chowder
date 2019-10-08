package com.sky.chowder.ui.activity

import android.os.Bundle
import android.view.View
import com.sky.chowder.R
import com.sky.design.app.BaseNoPActivity

/**
 * Created by SKY on 2018/5/2 10:49.
 */
class NinthPalaceActivity : com.sky.design.app.BaseNoPActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_ninthpalace
    override fun initialize(savedInstanceState: Bundle?) {
        setToolbarRightTitle("随机图案")
        findViewById<View>(R.id.tvRight).setOnClickListener { nine.shuffle() }
        nine.onSuccess = { s -> if (s) showToast("成功") else showToast("失败") }
//        LogUtils.i("${ProcessUtils().getRunningAppProcessInfo(this)}")
    }
    override fun loadData() = Unit

}
