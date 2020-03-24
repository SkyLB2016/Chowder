package com.sky.oa.activity

import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import com.sky.design.app.BaseActivity
import com.sky.oa.R
import kotlinx.android.synthetic.main.activity_web.*


/**
 * Created by libin on 2020/3/18 21:10.
 */
class WebActivity : BaseActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_web

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val setting: WebSettings = webView.getSettings()
        // 格式规定为:file:///android_asset/文件名.html
        // 格式规定为:file:///android_asset/文件名.html
//        webView.loadUrl("file:///android_asset/javascript.html")

        // 设置与Js交互的权限
        setting.javaScriptEnabled = true
        // 设置允许JS弹窗
        setting.javaScriptCanOpenWindowsAutomatically = true
        setting.defaultTextEncodingName = "utf-8"
        setting.cacheMode = WebSettings.LOAD_NO_CACHE//不使用缓存，只从网络获取数据.
        webView.loadUrl("file:///android_asset/jsapi.html")
        webView.addJavascriptInterface(JS(), "android")
        webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String?) {
                super.onReceivedTitle(view, title)
                if (title != null) {
                    showToast("title==$title")
//                    firstTitle.text = title
                }
            }
        }
    }

    internal inner class JS {
        @JavascriptInterface
        fun closeWebView() {
            runOnUiThread(Runnable {
                showToast("webview")
            });
        }
    }
}