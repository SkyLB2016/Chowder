package com.sky.g2048

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import com.sky.SkyApp
import com.sky.utils.DateUtil
import com.sky.utils.FileUtils
import com.sky.utils.LogUtils
import common.base.BaseNoPActivity
import kotlinx.android.synthetic.main.activity_g2048.*

class Game2048Activity : BaseNoPActivity() {

    private val pathName = SkyApp.getInstance().fileCacheDir + "2048orginal.txt"
    var automatic: Boolean = false//自动AI

    val array = intArrayOf(KeyEvent.KEYCODE_W, KeyEvent.KEYCODE_S, KeyEvent.KEYCODE_A, KeyEvent.KEYCODE_D,
            KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT)
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            when (msg!!.what) {
                1009 -> {
                    tvTime.text = DateUtil.timeStampToDate(System.currentTimeMillis() - start, "mm:ss")
                    sendEmptyMessage(1009)
                }
                1110 -> {
//                    if (game.orginal[12] == 0)
//                        game.changeData(KeyEvent.KEYCODE_S)
//                    else
//                    game.changeData(array[Random().nextInt(array.size - 1)])
//                    sendEmptyMessage(1110)
                }
                2001 -> game.orginal = FileUtils.deserialize<IntArray>(pathName) ?: IntArray(0)
            }

        }
    }
    var start = 0L//开始时间
    override fun getLayoutResId(): Int = R.layout.activity_g2048
    override fun initialize(savedInstanceState: Bundle?) {
        LogUtils.isDebug = BuildConfig.DEBUG
        setToolbarRightTitle("重新开始")
//        if (BuildConfig.isModel)
//            baseTitle.setLeftButton(-1)
        when (BuildConfig.isModel) {
            true -> baseTitle.setLeftButton(-1)
        }
        game.checkIsEnd = { end -> imgEnd.visibility = if (end) View.VISIBLE else View.GONE }
        findViewById<View>(R.id.tvRight).setOnClickListener {
            game.restartGame()
            setTime()
        }
        btReturnOld.setOnClickListener { game.returnOld() }

        tvTime.text = "00:00"
        setTime()
        tvAutomatic.setOnClickListener { setAutomatic() }
//        handler.sendEmptyMessageDelayed(2001, 100)
        handler.postDelayed({ game.orginal = FileUtils.deserialize<IntArray>(pathName) ?: IntArray(0) }, 100)
    }

    private fun setAutomatic() {
//        if (!automatic) {
//            handler.sendEmptyMessage(1110)
//            tvAutomatic.text = "停止计算"
//        } else {
//            tvAutomatic.text = "自动计算"
//            handler.removeMessages(1110)
//        }
//        automatic = !automatic
    }


    private fun setTime() {
        start = System.currentTimeMillis()
        setObject("time", start)
        handler.sendEmptyMessage(1009)
    }

    override fun loadData() = Unit

    override fun onTouchEvent(event: MotionEvent?): Boolean = game.onTouchEvent(event!!)

    override fun onStop() {
        super.onStop()
        if (game.isEnd) FileUtils.deleteFile(pathName)
        else FileUtils.serialize(pathName, game.orginal)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_W,
            KeyEvent.KEYCODE_S,
            KeyEvent.KEYCODE_A,
            KeyEvent.KEYCODE_D,
            KeyEvent.KEYCODE_DPAD_UP,
            KeyEvent.KEYCODE_DPAD_DOWN,
            KeyEvent.KEYCODE_DPAD_LEFT,
            KeyEvent.KEYCODE_DPAD_RIGHT -> game.onKeyDown(keyCode,event)
            KeyEvent.KEYCODE_B -> game.returnOld()
            KeyEvent.KEYCODE_E -> setAutomatic()
            KeyEvent.KEYCODE_R -> {
                game.restartGame()
                setTime()
            }
        }
        return super.onKeyDown(keyCode, event!!)
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