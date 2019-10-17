package com.sky.oa.activity

import android.os.Bundle
import android.view.View
import com.sky.design.app.BaseActivity
import com.sky.oa.R
import kotlinx.android.synthetic.main.activity_ninthpalace.*

/**
 * Created by SKY on 2018/5/2 10:49.
 */
class NinthPalaceActivity : BaseActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_ninthpalace
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarRightTitle("随机图案")
        findViewById<View>(R.id.tvRight).setOnClickListener { nine.shuffle() }
        nine.onSuccess = { s -> if (s) showToast("成功") else showToast("失败") }
//        LogUtils.i("${ProcessUtils().getRunningAppProcessInfo(this)}")
    }
}
