package com.sky.chowder.ui.activity

import android.view.MotionEvent
import com.sky.base.BasePActivity
import com.sky.chowder.R
import com.sky.chowder.ui.presenter.GameP
import kotlinx.android.synthetic.main.activity_game.*

/**
 * Created by SKY on 2018/4/20 11:17.
 */
class Game2048Activity : BasePActivity<GameP>() {
    override fun getLayoutResId(): Int = R.layout.activity_game
    override fun creatPresenter(): GameP = GameP(this)
    override fun initialize() = Unit
    override fun onTouchEvent(event: MotionEvent?): Boolean = game.onTouchEvent(event!!)
}