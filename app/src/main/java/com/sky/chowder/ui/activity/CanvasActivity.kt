package com.sky.chowder.ui.activity

import android.app.Activity
import android.os.Bundle

import com.sky.chowder.ui.widget.CanvasView

/**
 * Created by SKY on 2017/3/10.
 */
class CanvasActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(CanvasView(this))
    }
}
