package com.sky.chowder.ui.activity

import android.view.View
import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import kotlinx.android.synthetic.main.activity_ninthpalace.*

/**
 * Created by SKY on 2018/5/2 10:49.
 */
class NinthPalaceActivity : BaseNoPActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_ninthpalace
    override fun initialize() {
        setToolbarRightTitle("随机图案")
        getView<View>(R.id.tvRight).setOnClickListener { nine.shuffle() }
        nine.onSuccess = { s -> if (s) showToast("成功") else showToast("失败") }
//        LogUtils.i("${ProcessUtils().getRunningAppProcessInfo(this)}")
    }

}
