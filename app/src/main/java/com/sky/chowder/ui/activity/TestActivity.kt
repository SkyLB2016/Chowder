package com.sky.chowder.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sky.chowder.R
import kotlinx.android.synthetic.main.activity_test.*

/**
 * Created by SKY on 2016/8/28.
 */
class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
//        civ_head.setImageBitmap(BitmapUtils.getBitmapFromId(this,R.mipmap.ic_puzzle))
//        civ_head.setImageResource(R.mipmap.ic_puzzle)
        civ_head.borderColor = Color.BLACK
        civ_head.borderWidth = 10f
    }

}