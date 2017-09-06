package com.sky.chowder.ui.activity

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import com.sky.Common
import com.sky.chowder.MyApplication
import com.sky.chowder.R
import com.sky.utils.JumpAct

/**
 * Created by 李彬 on 2017/3/6.
 */

class WelcomeActivity : AppCompatActivity() {
    //    public Boolean flag;//是否首次运行，true代表首次
    private val TIME = 1000//handler延迟时间

    override fun onCreate(savedInstanceState: Bundle?) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏,在v7中AppCompatActivity下无用
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)//去掉信息栏
        super.onCreate(savedInstanceState)
        val view = ImageView(this)
        view.setBackgroundResource(R.mipmap.guide01)
        setContentView(view)
        //SPUtils.put(WelcomeActivity.this, "isfirst", true);
        //        flag = (Boolean) SPUtils.getInstance().get(Common.ISFIRST, true);
        //加载动画
        val animation = AlphaAnimation(0.5f, 1f)
        animation.duration = TIME.toLong()
        view.startAnimation(animation)
        animation.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationRepeat(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                Handler().postDelayed({
                    // 判断程序与第几次运行，如果是第一次运行引导页面
                    JumpAct()
                }, TIME.toLong())
            }
        })
    }

    private fun JumpAct() {
        if (MyApplication.instance.getObject(Common.ISFIRST, true)) {
            JumpAct.jumpActivity(this, GuideActivity::class.java)
        } else {
            JumpAct.jumpActivity(this, MainActivity::class.java)
        }
        finish()
    }
}
