package com.sky.chowder.ui.activity

import com.sky.adapter.RecyclerAdapter
import com.sky.base.BasePActivity
import com.sky.chowder.R
import com.sky.chowder.ui.presenter.GameP

/**
 * Created by SKY on 2018/4/20 11:17.
 */
class Game2048Activity : BasePActivity<GameP>() {
    var adapter: RecyclerAdapter<Int>? = null

    override fun getLayoutResId(): Int = R.layout.activity_game
    override fun creatPresenter(): GameP = GameP(this)

    override fun initialize() {

    }
}
