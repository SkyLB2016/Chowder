package com.sky.chowder.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import com.sky.base.BaseFragment
import com.sky.chowder.R
import com.sky.chowder.ui.presenter.SolarP
import com.sky.utils.LogUtils

/**
 * Created by SKY on 2015/12/9 20:52.
 */
class XferModeF : BaseFragment<SolarP>() {
    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    LogUtils.d("msg=0")
                    val math1 = (Math.random() * 10).toInt()
                    this.sendEmptyMessageDelayed(math1, 5000)
                }
                1 -> {
                    LogUtils.d("msg=1")

                    val math2 = (Math.random() * 10).toInt()
                    this.sendEmptyMessageDelayed(math2, 10000)
                }
                else -> {
                    LogUtils.d("msg=" + msg.what)
                    val math3 = (Math.random() * 10).toInt()
                    this.sendEmptyMessageDelayed(math3, 100)
                }
            }

        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //        handler.post(new Runnable() {
        //            public void run() {
        //                //通知错误消息
        //            }
        //        });
        Animator().run()
    }

    override fun getLayoutResId(): Int = R.layout.fragment_three

    override fun creatPresenter() = SolarP(activity)

    override fun initialize() {

    }

    internal inner class Animator : Runnable {

        override fun run() {
            //            while (!Thread.currentThread().isInterrupted()) {
            val message = Message()
            message.what = 0
            handler.sendMessage(message)
            try {
                Thread.sleep(0)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                e.printStackTrace()
            }

            //            }
        }
    }
}
