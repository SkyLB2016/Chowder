package com.sky.chowder.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.WindowManager
import com.sky.Common
import com.sky.chowder.R
import com.sky.chowder.ui.adapter.GuideContoler
import com.sky.utils.SPUtils

/**
 * Created by 李彬 on 2017/3/6.
 * 引导页面
 */
class GuideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)//去掉信息栏
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
        initData()
    }

    /**
     * 使用写好的库初始化引导页面
     */
    private fun initData() {
        val contoler = GuideContoler(this)
        val imgIds = intArrayOf(R.mipmap.guide01, R.mipmap.guide02, R.mipmap.guide03)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.pager_end, null)
        contoler.init(imgIds, view)
        view.findViewById(R.id.bt_begin).setOnClickListener {
            SPUtils.getInstance(Common.ISFIRST).put(Common.ISFIRST, false)
            startActivity(Intent(this@GuideActivity, MainActivity::class.java))
//            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            finish()
        }
    }
}
