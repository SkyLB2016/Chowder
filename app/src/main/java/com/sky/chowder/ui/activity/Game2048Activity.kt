package com.sky.chowder.ui.activity

import android.view.MotionEvent
import android.view.View
import com.sky.SkyApp
import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import com.sky.utils.FileUtils
import kotlinx.android.synthetic.main.activity_game.*

/**
 * Created by SKY on 2018/4/20 11:17.
 */
class Game2048Activity : BaseNoPActivity() {
    private val pathName = SkyApp.getInstance().fileCacheDir + "2048orginal.txt"
    override fun getLayoutResId(): Int = R.layout.activity_game
    override fun initialize() {
        setToolbarRightTitle("重新开始")
        game.orginal = FileUtils.deserialize<IntArray>(pathName) ?: IntArray(0)
        getView<View>(R.id.tvRight).setOnClickListener { game.restartGame() }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean = game.onTouchEvent(event!!)

    override fun onStop() {
        super.onStop()
        if (game.isEnd) FileUtils.deleteFile(pathName)
        else FileUtils.serialize(pathName, game.orginal)
    }
}