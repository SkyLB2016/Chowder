package com.sky.chowder.ui.activity

import android.app.Activity
import android.os.Bundle

import com.sky.chowder.ui.widget.BezierView

/**
 * Created by 李彬 on 2017/3/10.
 */

class BezierActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(BezierView(this))
    }
}
