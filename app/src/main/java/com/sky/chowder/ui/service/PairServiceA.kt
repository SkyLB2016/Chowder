package com.sky.chowder.ui.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.RemoteCallbackList
import com.sky.utils.LogUtils
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 半成品，待深入细化
 */
class PairServiceA : Service() {

    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            LogUtils.i("${books.toList()}")
            binder.addBook(book = Book(3, "tianxia"))

        }
    }
    val books = CopyOnWriteArrayList<Book>()
    val listeners = RemoteCallbackList<IOnNewBookArriveListener>()
    internal val isDestoryed = AtomicBoolean(false)

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            IBookManager.Stub.asInterface(service)
            service.linkToDeath(deathRecipient, 0)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            bindService(Intent(this@PairServiceA, PairServiceB::class.java), this, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onBind(intent: Intent): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        bindService(Intent(this@PairServiceA, PairServiceB::class.java), mServiceConnection, Context.BIND_AUTO_CREATE)
        handler.sendEmptyMessageDelayed(1, 1000)
        books.add(Book(1, "android"))
        books.add(Book(2, "ios"))
        Thread(Worker()).start()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mServiceConnection)
    }

    private val binder = object : IBookManager.Stub() {
        override fun registerListener(listener: IOnNewBookArriveListener?) {
            listeners.register(listener)

        }

        override fun unRegisterListener(listener: IOnNewBookArriveListener?) {
            listeners.unregister(listener)
            LogUtils.i("size==${listeners.beginBroadcast()}")
        }

        override fun getBookList(): MutableList<Book> {
            return books
        }

        override fun addBook(book: Book?) {
            books.add(book)
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
        binder.asBinder().unlinkToDeath(deathRecipient, 0)
    }

    internal fun onNewBookArrived(book: Book) {
        books.add(book)
        for (i in 0 until listeners.beginBroadcast()) {
            val listener = listeners.getBroadcastItem(i)
            listener.onNewBookArrived(book)
        }
        listeners.finishBroadcast()

    }

    internal inner class Worker : Runnable {
        override fun run() {
            while (!isDestoryed.get()) {
                try {
                    Thread.sleep(5000)
                } catch (e: InterruptedException) {

                }
                onNewBookArrived(Book(books.size + 1, "new${books.size + 1}"))
            }
        }

    }
}