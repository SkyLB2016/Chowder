package com.sky.chowder.ui.activity

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import com.sky.widget.BaseTitle
import kotlinx.android.synthetic.main.activity_poetry.*
import java.util.*

/**
 * Created by SKY on 2018/3/16.
 */
class PoetryActivity : BaseNoPActivity(), View.OnClickListener {
    private val resId = ArrayList<Int>()
    override fun getLayoutResId(): Int = R.layout.activity_poetry
    override fun initialize() {
        baseTitle.setLeftButton(R.mipmap.ic_menu)
//        baseTitle.setLeftImgOnClick { sliding.toggleMenu() }
        baseTitle.onLeftImgClick = BaseTitle.OnClickListener { sliding.toggleMenu() }

        val poetry = resources.getStringArray(R.array.poetry)!!
        var tv: TextView
        for ((index, text) in poetry.withIndex()) {
            val array = text.split(",")
            tv = LayoutInflater.from(this).inflate(R.layout.tv, flow, false) as TextView
            tv.width = resources.getDimensionPixelSize(R.dimen.wh_96)
            tv.textSize = 18f
            tv.text = array[0]
//            tv.tag = array[0]
            tv.id = index
            flow.addView(tv)
            tv.setOnClickListener(this)
            resId.add(resources.getIdentifier(array[1], "string", packageName))
        }
        tvDisplay.text = getString(resId[2]).replace(" ", "")
        tvDisplay.gravity = Gravity.CENTER
    }

    override fun onClick(v: View?) {
        tvDisplay.gravity = when (v?.id) {
            in 2..5 -> Gravity.CENTER
            else -> Gravity.LEFT
        }
        tvDisplay.text = getString(resId[v!!.id]).replace(" ", "")
//        LogUtils.i("总行数==${tvDisplay.lineCount}")
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

}
