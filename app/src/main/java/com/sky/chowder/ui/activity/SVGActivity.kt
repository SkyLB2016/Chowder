package com.sky.chowder.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.webkit.WebView
import com.sky.chowder.R
import com.sky.utils.FileUtils

/**
 * Created by SKY on 2015/3/26.
 * SVG动画
 */
class SVGActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)//去掉信息栏
        super.onCreate(savedInstanceState)
        val view = WebView(this)
//        view.loadUrl("file:///android_asset/svg_page_loading.html");
        loadAssets(view)
        setContentView(view)
        //加载动画
        val animation = AnimationUtils.loadAnimation(this, R.anim.alpha)
        view.startAnimation(animation)
    }

    /**
     * 加载本地html，并显示图片。
     * 待研究
     * @param view
     */
    private fun loadAssets(view: WebView) {
        val settings = view.settings
        settings.javaScriptEnabled = true
        settings.useWideViewPort = false//图形：false小，true大
        settings.loadWithOverviewMode = true
        settings.setSupportZoom(false)// 允许放大缩小
        settings.builtInZoomControls = false// 显示放大缩小按钮
        settings.displayZoomControls = false// api 11以上
        settings.setSupportMultipleWindows(true)
        settings.setGeolocationEnabled(true)// 启用地理定位

        view.isHorizontalScrollBarEnabled = false
        view.setBackgroundColor(Color.TRANSPARENT) // 设置背景色
        var html = FileUtils.readInput(assets.open("html/svg_page_loading.html"))

        html = html.replace("@image_assets", "html/logo.png")
        val baseurl = "file:///android_asset/"
        view.loadDataWithBaseURL(baseurl, html, "text/html", "UTF-8", null)
    }
}