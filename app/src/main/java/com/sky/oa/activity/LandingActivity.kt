package com.sky.oa.activity

import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.sky.oa.AppConstants
import com.sky.oa.R
import com.sky.sdk.utils.JumpAct
import com.sky.sdk.utils.SPUtils

/**
 * Created by SKY on 2017/3/6.
 * 欢迎页
 */
class LandingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏,在v7中AppCompatActivity下无用
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )//去掉信息栏
        super.onCreate(savedInstanceState)
        val view = ImageView(this)
        view.setBackgroundResource(R.mipmap.splash_00)
        setContentView(view)
        //加载动画
        val set = AnimationSet(true)
        val alpha = AlphaAnimation(0.1f, 1f)
        alpha.duration = 1000
        val rotate =
            RotateAnimation(0f, 360f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 1000
        val tran = TranslateAnimation(200f, 0f, 200f, 0f)
        tran.duration = 1000
        val scale = ScaleAnimation(0f, 1f, 0f, 1f)
//        val scale = Custom()
        scale.duration = 1000

        set.addAnimation(alpha)
        set.addAnimation(rotate)
        set.addAnimation(tran)
        set.addAnimation(scale)

        view.startAnimation(set)
        set.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                // 判断程序与第几次运行，如果是第一次运行引导页面
                Handler().postDelayed({ jumpAct() }, 1000)
            }
        })
    }

    private fun jumpAct() {
        if (SPUtils.getInstance(AppConstants.ISFIRST).getObject(AppConstants.ISFIRST, true)) {
            JumpAct.jumpActivity(this, SplashActivity::class.java)
        } else {
            JumpAct.jumpActivity(this, 
                MainActivity::class.java)
        }
        finish()
    }
}
