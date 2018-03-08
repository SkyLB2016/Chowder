package com.sky.chowder.ui.activity.pend

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.sky.chowder.R
import com.sky.chowder.ui.widget.Text
import kotlinx.android.synthetic.main.activity_test.*

/**
 * Created by SKY on 2016/8/28.
 */
class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val value = ValueAnimator.ofFloat(0f, 1920f)
//        value.setTarget(image)
        value.duration = 5000
        value.addUpdateListener { animation ->
            val lp = image.layoutParams
            lp.height = animation!!.animatedValue.toString().toFloat().toInt()
            image.layoutParams = lp
        }
        value.start()
        value.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                (image.drawable as Animatable).start()
            }
        })

    }

    fun text() {
        val text = Text(this)
        text.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        text.text = "天地玄黄，宇宙洪荒，日月盈仄，辰宿列张。寒来暑往，秋收冬藏。闰余成岁，律吕调阳。云腾致雨，露结为霜。金生丽水，玉出昆冈。"
        setContentView(text)
    }
}