package com.sky.chowder.ui.activity

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import kotlinx.android.synthetic.main.activity_sliding.*
import kotlinx.android.synthetic.main.activity_sliding_menu.*
import java.util.*

/**
 * Created by SKY on 15/12/9 下午8:54.
 * 横向侧滑栏  流式布局
 */
class SlidingMenuActivity : BaseNoPActivity(), View.OnClickListener {

    override fun getLayoutResId() = R.layout.activity_sliding
    override fun initialize() {
        super.initialize()
        initData()
        tvLogin.setOnClickListener { showToast("登录") }
        civ_head.setImageResource(R.mipmap.ic_puzzle)
    }

    private fun initData() {
        var i: Int = '!'.toInt()
        var tv: TextView
        while (i <= 'z'.toInt()) {
            tv = LayoutInflater.from(this).inflate(R.layout.tv, flow, false) as TextView
            tv.text = "${i.toChar()}AABBCC"
            flow.addView(tv)
            ObjectAnimator.ofFloat(tv, "rotation", 0f, 360f).setDuration(3000).start()
            tv.setOnClickListener(this)
            i++
        }
        getNum()
    }

    //一组数据从1开始数，到13时移除此数，之后继续从1开始数
    private fun getNum() {
        val nums = (1..14).mapTo(LinkedList<String>()) { it.toString() + "" }
        var a = 1

        var tv: TextView
        while (!nums.isEmpty()) {
            val temp = nums.first
            nums.removeFirst()
            if (a != 13) {
                nums.addLast(temp)
                a++
            } else {
                tv = LayoutInflater.from(this).inflate(R.layout.tv, flow, false) as TextView
                tv.text = "$temp"
                flow.addView(tv)
                ObjectAnimator.ofFloat(tv, "rotation", 0f, 360f).setDuration(3000).start()
                tv.setOnClickListener(this)
                a = 1
            }
        }
    }

    override fun onClick(v: View?) {
        val c = (v as TextView).text.toString().trim { it <= ' ' }[0]
        showToast(c + "=" + c.toInt() + ";dialogid=" + c.toInt() % 6)
    }
}