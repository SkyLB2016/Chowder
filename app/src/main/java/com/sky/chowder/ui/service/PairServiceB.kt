package com.sky.chowder.ui.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder

class PairServiceB : Service() {

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {

        }

        override fun onServiceDisconnected(name: ComponentName) {
            bindService(Intent(this@PairServiceB, PairServiceA::class.java), this, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return AliveBinder()
    }

    override fun onCreate() {
        super.onCreate()
        bindService(Intent(this@PairServiceB, PairServiceA::class.java), mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mServiceConnection)
    }
}