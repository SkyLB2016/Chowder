package com.sky.chowder.ui.activity

import android.view.MotionEvent
import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import kotlinx.android.synthetic.main.activity_game.*

/**
 * Created by SKY on 2018/4/20 11:17.
 */
class Game2048Activity : BaseNoPActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_game
    override fun initialize() {
        super.initialize()
        btRestart.setOnClickListener { game.restart() }
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean = game.onTouchEvent(event!!)
}