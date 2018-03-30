package com.sky.chowder.ui.activity

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import kotlinx.android.synthetic.main.activity_poetry.*

/**
 * Created by SKY on 2018/3/16.
 */
class PoetryActivity : BaseNoPActivity(), View.OnClickListener {
    override fun getLayoutResId(): Int = R.layout.activity_poetry
    override fun initialize() {
        var tv: TextView
        for ((index, text) in resources.getStringArray(R.array.poetry).withIndex()) {
            tv = LayoutInflater.from(this).inflate(R.layout.tv, flow, false) as TextView
            tv.textSize = 18f
            tv.text = text
//            tv.tag = i
            tv.id = index
            flow.addView(tv)
            tv.setOnClickListener(this)
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
        tvDisplay.gravity = when (v?.id) {
            1, 2, 3 -> Gravity.CENTER
            else -> Gravity.LEFT
        }
        tvDisplay.text = getString(when (v?.id) {
            0 -> R.string.daodejing
            1 -> R.string.qianziwen
            2 -> R.string.sanzijing
            3 -> R.string.dizigui
            4 -> R.string.loushiming
            5 -> R.string.xue
            6 -> R.string.caigentan
            7 -> R.string.daxue
            8 -> R.string.zhongyong
            9 -> R.string.lunyu
            10 -> R.string.mengzi
            11 -> R.string.biancheng
            else -> R.string.daodejing
        }).replace(" ", "")
//        LogUtils.i("总行数==${tvDisplay.lineCount}")
//        val method = resources.getStringArray(R.array.poetry)
//        tvDisplay.text = getString(when (v?.tag) {
//            method[11] -> R.string.biancheng
//        }).replace(" ", "")
    }
}
