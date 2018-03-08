package com.sky.chowder.ui.activity.pend

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
class ThreadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val text = TextView(this)
        text.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val tt = FileUtils.readCharFile(File(SkyApp.getInstance().fileCacheDir, "notappend"))
        text.text = tt
        setContentView(text)
//        syncThread()
//        testThread()
        val lock1 = DeadLock()
        val lock2 = DeadLock()
        lock1.flag = 1
        lock2.flag = 0
        val t1 = Thread(lock1)
        val t2 = Thread(lock2)
        t1.start()
        t2.start()
    }


    class DeadLock : Runnable {
        var flag = 0

        companion object {
            var num = 0
            val o1 = Any()
            val o2 = Any()
//            val o1 = TestModel("dd")
//            val o2 = TestModel("dd")
        }

        override fun run() {
            if (flag === 0) {
                synchronized(o1) {
                    LogUtils.i("00已锁定o1==${++num}")
                    try {
                        Thread.sleep(2000)
                    } catch (e: InterruptedException) {

                    }
                    synchronized(o2) {
                        LogUtils.i("00已锁定o2==${++num}")
                    }
                }
            }
            if (flag === 1) {
                synchronized(o2) {
                    LogUtils.i("11已锁定o2==${++num}")
                    try {
                        Thread.sleep(2000)
                    } catch (e: InterruptedException) {

                    }
                    synchronized(o1) {
                        LogUtils.i("11已锁定o1==${++num}")
                    }
                }
            }
        }
    }


    private fun syncThread() {
        val sync = Sync()
        val t1 = Thread(sync)
        val t2 = Thread(sync)
        t1.name = "线程1"
        t2.name = "线程2"
        t1.start()
        t2.start()
    }

    class Sync : Runnable {
        val timer = Timer()
        override fun run() {
            timer.add(Thread.currentThread().name)
        }
    }

    class Timer {
        var num = 0
        @Synchronized
        fun add(name: String) {
//        synchronized(this) {
            num++
            try {
                Thread.sleep(100)
            } catch (e: InterruptedException) {

            }
            LogUtils.i("$name == $num")
//        }
        }
    }

    private fun testThread() {
        val runner = Runner()
        //        Runner.run()
        val thread = Thread(runner)
        LogUtils.i("活着${thread.isAlive}")
        thread.priority = 1
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

    class Runner : Runnable {
        override fun run() {
            for (i in 1..100) {
                LogUtils.i("runner中==$i")
                Thread.sleep(100)
                if (i % 20 == 0) Thread.yield()
            }
        }
    }
}