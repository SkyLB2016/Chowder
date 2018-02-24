package com.sky.chowder.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.sky.chowder.ui.widget.TouchView

/**
 * Created by SKY on 2016/8/28.
 */
class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val text = TouchView(this)
        text.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        text.text = "天地玄黄，宇宙洪荒，日月盈仄，辰宿列张。"
        setContentView(text)
//val lp=        text.layoutParams as LinearLayout.LayoutParams

//        setContentView(R.layout.activity_test)
    }
}