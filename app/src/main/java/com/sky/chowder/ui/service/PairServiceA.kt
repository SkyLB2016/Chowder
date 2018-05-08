package com.sky.chowder.ui.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Message
import com.sky.utils.LogUtils

/**
 * 半成品，待深入细化
 */
class PairServiceA : Service() {

    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
        }
    }

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            IBookManager.Stub.asInterface(service)
            service.linkToDeath(deathRecipient, 0)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            bindService(Intent(this@PairServiceA, PairServiceB::class.java), this, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onBind(intent: Intent): IBinder = stub

    override fun onCreate() {
        super.onCreate()
        bindService(Intent(this@PairServiceA, PairServiceB::class.java), mServiceConnection, Context.BIND_AUTO_CREATE)
        handler.sendEmptyMessageDelayed(1, 1000)
        stub.bookList
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mServiceConnection)
    }

    private val stub = object : IBookManager.Stub() {
        override fun getBookList(): MutableList<Book> {
            LogUtils.i("getBookList")
            return ArrayList()
        }

        override fun addBook(book: Book?) {
            LogUtils.i("addBook")
        }

        override fun linkToDeath(recipient: IBinder.DeathRecipient?, flags: Int) {
            super.linkToDeath(recipient, flags)
        }

        override fun unlinkToDeath(recipient: IBinder.DeathRecipient?, flags: Int): Boolean {

//            asBinder().unlinkToDeath(deathRecipient, 0)
            return super.unlinkToDeath(recipient, flags)
        }
    }
    val deathRecipient = IBinder.DeathRecipient { bindDeath() }

    private fun bindDeath() {
        stub.asBinder().unlinkToDeath(deathRecipient, 0)
    }
}