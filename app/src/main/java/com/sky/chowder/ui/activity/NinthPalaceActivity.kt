package com.sky.chowder.ui.activity

import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import kotlinx.android.synthetic.main.activity_ninthpalace.*

/**
 * Created by SKY on 2018/5/2 10:49.
 */
class NinthPalaceActivity : BaseNoPActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_ninthpalace
    override fun initialize() {
        nine.onSuccess = { s -> if (s) showToast("成功") else showToast("失败") }
    }

}
