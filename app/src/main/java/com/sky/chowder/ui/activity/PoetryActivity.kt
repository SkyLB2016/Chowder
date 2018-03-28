package com.sky.chowder.ui.activity

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import com.sky.utils.LogUtils
import kotlinx.android.synthetic.main.activity_poetry.*

/**
 * Created by SKY on 2018/3/16.
 */
class PoetryActivity : BaseNoPActivity(), View.OnClickListener {
    override fun getLayoutResId(): Int = R.layout.activity_poetry
    override fun initialize() {
//        val method = arrayListOf("道德经", "千字文", "三字经", "弟子规", "陋室铭", "沁园春雪")
        val method = resources.getStringArray(R.array.poetry)
        for (i in method) {
            val tvText = LayoutInflater.from(this).inflate(R.layout.tv, flow, false) as TextView
            tvText.textSize = 18f
            tvText.text = i
            tvText.tag = i
            flow.addView(tvText)
            tvText.setOnClickListener(this)
        }
        tvDisplay.text = getString(R.string.daodejing).replace(" ", "")
    }

//    override fun onRestart() {
//        super.onRestart()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onStart() {
//        super.onStart()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onResume() {
//        super.onResume()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onPause() {
//        super.onPause()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }

    override fun onClick(v: View?) {
        tvDisplay.gravity = when (v?.tag) {
            "千字文", "三字经", "弟子规" -> Gravity.CENTER
            else -> Gravity.LEFT
        }
        tvDisplay.text = getString(when (v?.tag) {
            "道德经" -> R.string.daodejing
            "论语" -> R.string.lunyu
            "大学" -> R.string.daxue
            "中庸" -> R.string.zhongyong
            "孟子" -> R.string.mengzi
            "菜根谭" -> R.string.caigentan
            "千字文" -> R.string.qianziwen
            "三字经" -> R.string.sanzijing
            "弟子规" -> R.string.dizigui
            "沁园春雪" -> R.string.xue
            "陋室铭" -> R.string.loushiming
            else -> R.string.daodejing
        }).replace(" ", "")
        LogUtils.i("总行数==${tvDisplay.lineCount}")
    }
}
