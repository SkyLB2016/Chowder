package com.sky.chowder.ui.activity

import android.os.Bundle
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.TextView
import com.sky.SkyApp
import com.sky.utils.FileUtils
import com.sky.utils.LogUtils
import java.io.File

/**
 * Created by SKY on 2016/8/28.
 */
class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val text = TextView(this)
        text.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val tt = FileUtils.readCharFile(File(SkyApp.getInstance().fileCacheDir, "notappend"))
        text.text = tt
        setContentView(text)
        val runner = Runner()
//        Runner.run()
        val thread = Thread(runner)
        LogUtils.i("活着${thread.isAlive}")
        thread.priority=1
        thread.start()
        LogUtils.i("活着${thread.isAlive}")
        LogUtils.i("优先级${Looper.getMainLooper().thread.priority}")
        LogUtils.i("优先级${thread.priority}")
//        thread.join()
        for (i in 1..100) {
            LogUtils.i("main中==$i")
            Thread.sleep(100)
            if (i % 20 == 0) Thread.yield()
        }
    }
}

class Runner : Runnable {
    override fun run() {
        for (i in 1..100) {
            LogUtils.i("runner中==$i")
            Thread.sleep(100)
            if (i % 20 == 0) Thread.yield()
        }
    }
}