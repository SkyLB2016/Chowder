package com.sky.chowder.ui.activity

import android.app.Activity
import android.graphics.BlurMaskFilter
import android.graphics.EmbossMaskFilter
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import com.sky.chowder.R
import com.sky.chowder.ui.widget.Text
import kotlinx.android.synthetic.main.activity_canvas.*
import java.io.File

/**
 * Created by SKY on 2017/3/10.
 */
class CanvasActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas)
//        setContentView(CanvasView(this))

        initialize()
    }

    fun text() {
        val text = Text(this)
        text.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        text.text = "天地玄黄，宇宙洪荒，日月盈仄，辰宿列张。寒来暑往，秋收冬藏。闰余成岁，律吕调阳。云腾致雨，露结为霜。金生丽水，玉出昆冈。"
        setContentView(text)
    }
    private fun setVideo() {
        val video = findViewById<VideoView>(0)
        val mediaController = MediaController(this)
        val file = File("/storage/emulated/0/QQBrowser/视频/隐世小龙女，她用颜值与功夫惊艳了时光.mp4")
        if (file.exists()) {
            video?.setVideoPath(file.absolutePath)
            video?.setMediaController(mediaController)
            mediaController.setMediaPlayer(video)
            video?.requestFocus()
            video?.start()
        }
    }

    private lateinit var blurMaskFilter: BlurMaskFilter
    private lateinit var filter: EmbossMaskFilter
    fun initialize() {
        filter = EmbossMaskFilter(floatArrayOf(1.5f, 1.5f, 1.5f), 0.6f, 6f, 4.2f)
        blurMaskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)
        //        drawView.cachePaint.setMaskFilter(filter);
//        btnChange.setOnClickListener { _ -> draw!!.cachePaint!!.maskFilter = blurMaskFilter }
    }

    fun changeOnClick(view: View) {
        draw!!.cachePaint!!.maskFilter = blurMaskFilter
    }
}
