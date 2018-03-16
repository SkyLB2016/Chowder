package com.sky.chowder.ui.activity

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import com.sky.utils.LogUtils
import kotlinx.android.synthetic.main.activity_poetry.*

/**
 * Created by SKY on 2016/8/28.
 */
class PoetryActivity : BaseNoPActivity(), View.OnClickListener {
    override fun getLayoutResId(): Int = R.layout.activity_poetry
    override fun initialize() {
        val method = arrayListOf("千字文"
                , "道德经"
                , "三字经"
                , "弟子规"
        )
        for (i in method) {
            val tvText = LayoutInflater.from(this).inflate(R.layout.tv, flow, false) as TextView
            tvText.textSize = resources.getDimension(R.dimen.text_medium)
            tvText.textSize = 18f
            tvText.text = i
            tvText.tag = i
            flow.addView(tvText)
            tvText.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        tvDisplay.text = getText(when (v?.tag) {
            "千字文" -> R.string.qianziwen
            "道德经" -> R.string.daodejing
            "三字经" -> R.string.sanzijing
            "弟子规" -> R.string.dizigui
            else -> 0
        })


        LogUtils.i("${tvDisplay.lineCount * 12}")
    }
}