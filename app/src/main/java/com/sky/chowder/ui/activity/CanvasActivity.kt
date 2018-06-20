package com.sky.chowder.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.MediaController
import android.widget.VideoView
import com.sky.chowder.R
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
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        touch.onKeyDown(keyCode, event)
        return super.onKeyDown(keyCode, event)
    }


    private fun setVideo() {
        val video = findViewById<VideoView>(0)
        val mediaController = MediaController(this)
        val file = File("/storage/emulated/0/QQBrowser/视频/时光.mp4")
        if (file.exists()) {
            video?.setVideoPath(file.absolutePath)
            video?.setMediaController(mediaController)
            mediaController.setMediaPlayer(video)
            video?.requestFocus()
            video?.start()
        }
    }
}
