package com.sky.oa.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.sky.oa.AppConstants
import com.sky.oa.R
import com.sky.oa.adapter.GuideContoler
import com.sky.sdk.utils.SPUtils

/**
 * Created by SKY on 2017/3/6.
 * 引导页面
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)//去掉信息栏
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initData()
    }

    /**
     * 使用写好的库初始化引导页面
     */
    private fun initData() {
        val contoler = GuideContoler(this)
        val imgIds = intArrayOf(R.mipmap.ic_landing, R.mipmap.ic_bg_01, R.mipmap.ic_bg_02)
        val view = LayoutInflater.from(this).inflate(R.layout.pager_end, null)
        contoler.init(imgIds, view)
        view.findViewById<Button>(R.id.btBegin).setOnClickListener {
            SPUtils.getInstance(AppConstants.ISFIRST).put(AppConstants.ISFIRST, false)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
//            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            finish()
        }
    }
}
