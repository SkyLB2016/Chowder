package com.sky.chowder.ui.activity.pend

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.MediaController
import android.widget.VideoView

import com.sky.chowder.R

import java.io.File

/**
 * Created by SKY on 2016/8/28.
 */
class VideoActivity : AppCompatActivity() {
    private var video: VideoView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        video = findViewById<VideoView>(R.id.video)
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
}