package com.sky.chowder.utils

import android.content.Context
import android.os.Bundle
import com.sky.utils.JumpAct

/**
 * Created by SKY on 2017/6/19.
 */
object ActJump {
    private fun toActivity(context: Context, cls: Class<*>) {
        JumpAct.jumpActivity(context, cls)
    }

    private fun toActivity(context: Context, cls: Class<*>, bundle: Bundle) {
        JumpAct.jumpActivity(context, cls, bundle)
    }
}