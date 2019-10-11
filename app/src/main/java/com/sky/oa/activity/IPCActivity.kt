package com.sky.oa.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import com.sky.design.app.BaseActivity
import com.sky.oa.R
import com.sky.oa.service.Book
import com.sky.oa.service.IBookManager
import com.sky.oa.service.IOnNewBookArriveListener
import com.sky.oa.service.PairServiceA
import com.sky.sdk.utils.LogUtils
import kotlinx.android.synthetic.main.activity_ipc.*

/**
 * Created by SKY on 2018/5/9 19:07.
 */
class IPCActivity : BaseActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_ipc
    override fun initialize(savedInstanceState: Bundle?) {
        bindService(Intent(this, PairServiceA::class.java), connection, Context.BIND_AUTO_CREATE)
//        startService(Intent(this, PairServiceA::class.java))
//        val badge = BadgeView(this)
        badge.badgeCount = 10
        chro.base = SystemClock.elapsedRealtime()
        chro.start()
    }

    override fun loadData() = Unit
    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) = LogUtils.i("${msg.obj}")
    }
    var bookManager: IBookManager? = null
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            bookManager = IBookManager.Stub.asInterface(service)
            try {
                bookManager?.addBook(Book(4, "开发艺术"))
                val list = bookManager?.bookList
                LogUtils.i("${list?.toList()}")
                LogUtils.i("query book list,list type:${list?.javaClass?.canonicalName}")
                bookManager?.registerListener(binderListener)
            } catch (remo: RemoteException) {
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
        }
    }
    val binderListener = object : IOnNewBookArriveListener.Stub() {
        override fun onNewBookArrived(newBook: Book?) {
            handler.obtainMessage(333, newBook).sendToTarget()
        }

        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?
        ) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bookManager?.unRegisterListener(binderListener)
        unbindService(connection)
    }
}
