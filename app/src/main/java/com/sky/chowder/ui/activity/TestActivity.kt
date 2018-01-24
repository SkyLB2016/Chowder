package com.sky.chowder.ui.activity

import android.os.Bundle
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
        text.layoutParams= ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        val tt=FileUtils.readCharFile(File(SkyApp.getInstance().fileCacheDir , "notappend"))
        text.text = tt
        setContentView(text)
        LogUtils.i(tt)
    }
}