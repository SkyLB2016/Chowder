package com.sky.chowder.utils

import android.content.Context

import com.sky.Common
import com.sky.utils.JumpAct

import java.io.Serializable

/**
 * Created by SKY on 2017/6/19.
 */
object ActJump {
    private fun toActivity(context: Context, cls: Class<*>) {
        JumpAct.jumpActivity(context, cls)
    }

    private fun toActivity(context: Context, cls: Class<*>, serial: Serializable?) {
        JumpAct.jumpActivity(context, cls, Common.EXTRA, serial)
    }
}