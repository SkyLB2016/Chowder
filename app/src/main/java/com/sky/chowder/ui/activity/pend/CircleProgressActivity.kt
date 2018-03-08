package com.sky.chowder.ui.activity.pend

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import kotlinx.android.synthetic.main.activity_circleprogress.*

/**
 * Created by SKY on 15/12/9 下午8:54.
 * 自定义进度条布局,与textview字体大小颜色背景设置
 */
class CircleProgressActivity : BaseNoPActivity() {

    override fun getLayoutResId(): Int = R.layout.activity_circleprogress

    override fun initialize() {
        tvMoney.text = "金额(元)" + "\n" + "20,000"
        val span = SpannableString(tvMoney.text)
        span.setSpan(AbsoluteSizeSpan(resources.getDimension(R.dimen.text_XXXXlarge).toInt()), 6, tvMoney.text.toString().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(ForegroundColorSpan(resources.getColor(R.color.main_color)), 6, tvMoney.text.toString().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(BackgroundColorSpan(Color.YELLOW), 6, tvMoney.text.toString().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvMoney.text = span
    }
}
