package com.sky.g2048

import android.view.MotionEvent
import android.view.View
import com.sky.SkyApp
import com.sky.base.BaseNoPActivity
import com.sky.utils.FileUtils
import kotlinx.android.synthetic.main.activity_g2048.*

class Game2048Activity : BaseNoPActivity() {
    private val pathName = SkyApp.getInstance().fileCacheDir + "2048orginal.txt"
    override fun getLayoutResId(): Int = R.layout.activity_g2048
    override fun initialize() {
        setToolbarRightTitle("重新开始")
//        if (BuildConfig.isModel)
//            baseTitle.setLeftButton(-1)
        when (BuildConfig.isModel) {
            true -> baseTitle.setLeftButton(-1)
        }
        game.orginal = FileUtils.deserialize<IntArray>(pathName) ?: IntArray(0)
        game.checkIsEnd = { end -> imgEnd.visibility = if (end) View.VISIBLE else View.GONE }
        getView<View>(R.id.tvRight).setOnClickListener { game.restartGame() }
        imgEnd.setOnClickListener { game.restartGame() }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean = game.onTouchEvent(event!!)

    override fun onStop() {
        super.onStop()
        if (game.isEnd) FileUtils.deleteFile(pathName)
        else FileUtils.serialize(pathName, game.orginal)
    }

    private var lastBack: Long = 0
    override fun onBackPressed() {
        val now = System.currentTimeMillis()
        if (BuildConfig.isModel && now - lastBack > 3000) {
            showToast(getString(R.string.toast_exit))
            lastBack = now
        } else super.onBackPressed()
    }
}