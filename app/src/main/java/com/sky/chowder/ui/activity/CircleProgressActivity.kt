package com.sky.chowder.ui.activity

import android.graphics.Color
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
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
        //字体大小
        span.setSpan(AbsoluteSizeSpan(resources.getDimension(R.dimen.text_XXXXlarge).toInt()), 6, tvMoney.text.toString().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        //字体颜色
        span.setSpan(ForegroundColorSpan(resources.getColor(R.color.main_color)), 6, tvMoney.text.toString().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        //字体背景
        span.setSpan(BackgroundColorSpan(Color.YELLOW), 6, tvMoney.text.toString().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvMoney.text = span

        val text = "1.天行健，君子以自强不息；"
        val builder = SpannableStringBuilder(text)
        builder.setSpan(ForegroundColorSpan(Color.RED), 6, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setSpan(AbsoluteSizeSpan(89), 6, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv1.text = builder

        //两次加大字体，设置字体为红色（big会加大字号，font可以定义颜色）
        tv1.append("\n")
        tv1.append(Html.fromHtml("<font color='#00ff00'><medium>地势坤，君子以厚德载物。</medium></font>"))
        //设置字体大小为3级标题，设置字体为红色
        tv2.text = Html.fromHtml("2.天行健，君子以自强不息；" + "<h3><font color='#ff0000'>地势坤，君子以厚德载物。</font></h3>")

        //设置字体大小为58（单位为物理像素），设置字体为红色，字体背景为黄色
        tv3.text = "3.天行健，君子以自强不息；地势坤，君子以厚德载物。"
        val span3 = SpannableString(tv3.text)
        span3.setSpan(AbsoluteSizeSpan(resources.getDimension(R.dimen.text_medium).toInt()), 11, tv3.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span3.setSpan(ForegroundColorSpan(Color.GREEN), 11, tv3.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span3.setSpan(BackgroundColorSpan(Color.rgb(0, 255, 255)), 11, tv3.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv3.text = span3

        //两次缩小字体，设置字体为红色（small可以减小字号）
        tv4.text = Html.fromHtml("4.<>天行健，君子以自强不息；<font color='#00ffff'><medium>地势坤，君子以厚德载物。</medium></font>")
        tv5.setText(R.string.test)
    }
}
