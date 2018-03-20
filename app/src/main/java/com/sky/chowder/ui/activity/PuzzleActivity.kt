package com.sky.chowder.ui.activity

import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import kotlinx.android.synthetic.main.activity_puzzle.*

/**
 * Created by SKY on 2015/8/19 15:31.
 * 拼图游戏
 */
class PuzzleActivity : BaseNoPActivity() {

    override fun getLayoutResId(): Int = R.layout.activity_puzzle

    override fun initialize() {
        super.initialize()
        btUp.setOnClickListener { puzzle.piece = -1 }
        btNext.setOnClickListener { puzzle.piece = 1 }
    }
}