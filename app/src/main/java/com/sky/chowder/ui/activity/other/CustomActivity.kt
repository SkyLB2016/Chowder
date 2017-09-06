package com.sky.chowder.ui.activity.other

import android.app.Activity
import android.os.Bundle

import com.sky.chowder.ui.widget.pending.DrawView

/**
 * Created by 李彬 on 2017/3/10.
 */

class CustomActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(DrawView(this))
    }
}
