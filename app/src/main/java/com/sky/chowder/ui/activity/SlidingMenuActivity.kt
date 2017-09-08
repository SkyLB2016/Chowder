package com.sky.chowder.ui.activity

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import kotlinx.android.synthetic.main.activity_sliding.*
import java.util.*

/**
 * Created by sky on 15/12/9 下午8:54.
 * 横向侧滑栏  流式布局
 */
class SlidingMenuActivity : BaseNoPActivity() {

    private val mVals = arrayOf("start_progress", "stop_progress", "remove_allactions", "add_action", "remove_action", "remove_share_action", "bottomtabbar", "Android", "Weclome Hi ", "Button", "TextView", "Hello", "Android", "Weclome", "Button ImageView", "TextView", "Helloworld", "Android", "Weclome Hello", "Button Text", "TextView")

    override fun getLayoutResId(): Int {
        return R.layout.activity_sliding
    }

    override fun initialize() {
        super.initialize()
        initData()
    }

    private fun initData() {
        val mInflater = LayoutInflater.from(this)
        for (i in mVals.indices) {
            val tv = mInflater.inflate(R.layout.tv, flowlayout, false) as TextView
            val lp = tv.layoutParams
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT
            tv.layoutParams = lp
            tv.text = mVals[i]
            flowlayout?.addView(tv)
            //ObjectAnimator.ofFloat(tv, "rotation", 0F, 360F).setDuration(3000).start();
            tv.tag = mVals[i]
        }
        var i: Int = '!'.toInt()
        while (i <= 'z'.toInt()) {
            val tv = mInflater.inflate(R.layout.tv, flowlayout, false) as TextView
            tv.text = "${i.toChar()}"
            flowlayout!!.addView(tv)
            ObjectAnimator.ofFloat(tv, "rotation", 0f, 360f).setDuration(3000).start()
            setOnclicK(tv)
            i++
        }
        getNum()
    }

    //一组数据从1开始数，到13时移除此数，之后继续从1开始数
    private fun getNum() {
        val nums = (1..14).mapTo(LinkedList<String>()) { it.toString() + "" }
        var a = 1

        while (!nums.isEmpty()) {
            if (a != 13) {
                val temp = nums.first
                nums.removeFirst()
                nums.addLast(temp)
                a++
            } else {
                val tv = LayoutInflater.from(this).inflate(R.layout.tv, flowlayout, false) as TextView
                tv.text = nums.first + ""
                flowlayout!!.addView(tv)
                ObjectAnimator.ofFloat(tv, "rotation", 0f, 360f).setDuration(3000).start()
                setOnclicK(tv)
                nums.removeFirst()
                a = 1
            }
        }
    }

    private fun setOnclicK(tv: TextView) {
        tv.setOnClickListener {
            val c = tv.text.toString().trim { it <= ' ' }[0]
            showToast(c + "=" + c.toInt() + ";dialogid=" + c.toInt() % 6)
        }
    }
}