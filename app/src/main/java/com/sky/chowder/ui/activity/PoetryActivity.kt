package com.sky.chowder.ui.activity

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import kotlinx.android.synthetic.main.activity_poetry.*

/**
 * Created by SKY on 2016/8/28.
 */
class PoetryActivity : BaseNoPActivity(), View.OnClickListener {
    override fun getLayoutResId(): Int = R.layout.activity_poetry
    override fun initialize() {
        val method = arrayListOf("道德经"
                , "千字文"
                , "三字经"
                , "弟子规"
                , "沁园春雪"
                , "陋室铭"
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
        tvDisplay.gravity = Gravity.CENTER
        tvDisplay.text = getText(when (v?.tag) {
            "道德经" -> {
                tvDisplay.gravity = Gravity.LEFT
                R.string.daodejing
            }
            "千字文" -> R.string.qianziwen
            "三字经" -> R.string.sanzijing
            "弟子规" -> R.string.dizigui
            "沁园春雪" -> R.string.xue
            "陋室铭" -> R.string.loushiming
            else -> 0
        })
    }
}