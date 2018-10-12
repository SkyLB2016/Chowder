package com.sky.chowder.ui.activity

import android.Manifest
import android.content.ActivityNotFoundException
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.annotation.LayoutRes
import android.support.v4.view.ViewCompat.animate
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.sky.SkyApp
import com.sky.chowder.R
import com.sky.chowder.api.view.IMainView
import com.sky.chowder.model.ActivityModel
import com.sky.chowder.ui.adapter.MainAdapter
import com.sky.chowder.ui.presenter.MainP
import com.sky.chowder.utils.http.HttpUrl
import com.sky.utils.AppUtils
import com.sky.utils.JumpAct
import com.sky.utils.LogUtils
import common.base.BasePActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.net.URL
import java.text.Collator
import java.util.*

/**
 * Created by SKY on 2015/12/6.
 * 主页
 */
class MainActivity : BasePActivity<MainP>(), Toolbar.OnMenuItemClickListener, IMainView, Observer {
//    override fun onCreate(savedInstanceState: Bundle?) {
//super.onCreate(savedInstanceState)
//Debug.startMethodTracing()
//    }
//    override fun onDestroy() {
//super.onDestroy()
//Debug.stopMethodTracing()
//    }

    private lateinit var adapter: MainAdapter
    public override fun getLayoutResId(): Int = R.layout.activity_main
    override fun creatPresenter() = MainP(this)
    public override fun initialize(savedInstanceState: Bundle?) {
        //在应用内时，屏幕常亮
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        baseTitle.setLeftButton(-1)
        recycler?.setHasFixedSize(true)
        adapter = MainAdapter(R.layout.adapter_main_delete)
        recycler.adapter = adapter

        adapter.setOnItemClickListener { _, position -> JumpAct.jumpActivity(this, adapter.datas[position].componentName) }
//        adapter?.setOnItemLongClickListener { _, _ ->
//            showToast("长按监听已处理")
//            true
//        }
        AppUtils.isPermissions(this,
                arrayOf(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE),
                intArrayOf(0, 0, 0)
        )
        tts = TextToSpeech(this, TextToSpeech.OnInitListener { status -> checkTTS(status) })
        val rect = Rect(4, 4, 4, 4)
        fab.setOnClickListener {
            //var text = getString(R.string.cezi).trim().replace(" ", "")
            //LogUtils.i("总长==${text.length}")
            //SingletonDemo.INSTANCE.otherMethods()
            //SingletonTest.test()
//            calendar()
//            val array1 =assets.list("Documents")
//            for (i in array1){
//                LogUtils.i(i)
//            }
            //Rw2 B2 U2 Lw U2 Rw' U2 Rw U2 F2 Rw F2 Lw' B2 Rw2
            var dir = "Documents"//路径
            var array: Array<String>? = null
            val poetry = ArrayList<String>();
            val link = LinkedList<String>();
//            val stack = Stack<String>();
//            stack.add(dir)
            link.add(dir)
            while (link.isNotEmpty()) {
                dir = link.removeFirst()
                array = assets.list(dir)
                for (i in array) {
                    if (i.endsWith(".txt")) {
                        poetry.add("$dir/$i")
                    } else {
                        link.add("$dir/$i")
                    }
                }
            }
//            poetry.sort()
//            for (i in poetry) LogUtils.i(i)
            val collator = Collator.getInstance(Locale.CHINA)
            poetry.sortWith(Comparator { o1, o2 -> collator.compare(o1, o2) })
            for (i in poetry){
//                LogUtils.i(i.substringAfterLast("/", "").substringBefore(".",""))
                LogUtils.i(i)
            }
        }
    }

    fun calendar() {
        val calendar = Calendar.getInstance()
        calendar.set(2018, 8, 1)
        LogUtils.i("年周==${calendar.get(Calendar.WEEK_OF_MONTH)}")
        calendar.set(2018, 8, 2)
        LogUtils.i("年周==${calendar.get(Calendar.WEEK_OF_MONTH)}")
        calendar.set(2018, 8, 9)
        LogUtils.i("年周==${calendar.get(Calendar.WEEK_OF_MONTH)}")
        calendar.set(2018, 8, 16)
        LogUtils.i("年周==${calendar.get(Calendar.WEEK_OF_MONTH)}")
        calendar.set(2018, 8, 23)
        LogUtils.i("年周==${calendar.get(Calendar.WEEK_OF_MONTH)}")
        calendar.set(2018, 8, 30)
        LogUtils.i("年周==${calendar.get(Calendar.WEEK_OF_MONTH)}")

//        LogUtils.i("本日==${calendar.get(Calendar.DAY_OF_YEAR)}")
//        LogUtils.i("周几==${calendar.get(Calendar.DAY_OF_WEEK)}")
//        LogUtils.i("日期==${calendar.get(Calendar.MONTH) + 1}月${calendar.get(Calendar.DAY_OF_MONTH)}")
//        LogUtils.i("年周==${calendar.get(Calendar.WEEK_OF_YEAR)}")
//        LogUtils.i("月周==${calendar.get(Calendar.WEEK_OF_MONTH)}")
    }

    fun changeRect(rect: Rect) {
        val rr = Rect(rect)
        rr.left = 108
        LogUtils.i("==${rr.left}")
        LogUtils.i("==${rect.left}")
        rect.left = 8
        rect.right = 8
        LogUtils.i("==${rect.right}")
        LogUtils.i("==${rect.left}")
        LogUtils.i("==${rr.left}")
        LogUtils.i("==${rr.right}")
    }

    private fun generateTag(): String {
        //        StackTraceElement stack = Thread.currentThread().getStackTrace()[4];//此方法取得的栈的前两个分别为vm和Thread
        val stacks = Throwable().stackTrace
        for (i in stacks.indices) {
            val stack = stacks[i]
            val tag = "%s.%s(L:%d)"
            var className = stack.className
            Log.i("name", "name==" + String.format(tag, className, stack.methodName, stack.lineNumber))
        }
        //        tag = TextUtils.isEmpty(TAG) ? tag : TAG + ":" + tag;
        return ""
    }

    private fun setObserver(@LayoutRes item: Int?) {
        //观察者
        val observer = Observer { o, arg -> LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}==$arg") }

        //被观察者
        val observable = object : Observable() {
            fun send(content: Any) {
                setChanged()
                //通知观察者
                notifyObservers(content)
            }
        }
        //把观察者注册到被观察者中
        observable.addObserver(observer)
        observable.addObserver(this)

        //被观察者发送消息
        observable.send("观察者模式")
    }

    override fun update(o: Observable?, arg: Any?) {
        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}==$arg")
    }

    private fun setAnimator() {
        animate(fab).alpha(1f)
                .rotation(180f)
                .translationX(-500f)
                .setDuration(3000)
                .withStartAction { }
                .withEndAction { }
                .start()
    }

    private fun getURL() {
        val url = URL(HttpUrl.URL_MUKE + HttpUrl.URL_MUKE1)
        LogUtils.i("资源名==${url.file}")
        LogUtils.i("主机名==${url.host}")
        LogUtils.i("路径==${url.path}")
        LogUtils.i("端口==${url.port}")
        LogUtils.i("协议名称==${url.protocol}")
        LogUtils.i("查询字符串==${url.query}")
    }

    private fun ttsTest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts?.speak("gesture", TextToSpeech.QUEUE_ADD, null, "speech")
            //                tts?.synthesizeToFile("gesture",null,File(""),"record")
        }
    }

    var tts: TextToSpeech? = null
    private fun checkTTS(status: Int) {

    }

    override fun setData(data: List<ActivityModel>) {
        adapter.datas = data
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {//跳转到指定的别的APP的activity
            R.id.action_01 -> startOther("com.cx.architecture", "com.cx.architecture.ui.activity.LoginActivity")
            R.id.action_02 -> startOther("com.gc.materialdesigndemo", "com.gc.materialdesigndemo.ui.MainActivity")
        }
        return false
    }

    private fun startOther(packageName: String, componentName: String) {
        try {
            JumpAct.jumpActivity(this@MainActivity, packageName, componentName)
        } catch (e: ActivityNotFoundException) {
            showToast("程序未安装")
        }
    }

    private var lastBack: Long = 0
    override fun onBackPressed() {
        val now = System.currentTimeMillis()
        if (now - lastBack > 3000) {
            showToast(getString(R.string.toast_exit))
            lastBack = now
        } else super.onBackPressed()
    }

    /**
     * 文件属性
     */
    private fun fileTest() {
        val f = File(SkyApp.getInstance().fileCacheDir + "pass.txt")
        LogUtils.i(f.getParent())//返回此抽象路径名父目录的路径名字符串；如果此路径名没有指定父目录，则返回 null
        LogUtils.i(f.getName())//返回由此抽象路径名表示的文件或目录的名称
        LogUtils.i("${f.exists()}")//测试此抽象路径名表示的文件或目录是否存在
        LogUtils.i("${f.getAbsoluteFile()}")// 返回此抽象路径名的绝对路径名形式
        LogUtils.i(f.getAbsolutePath())//返回此抽象路径名的规范路径名字符串
        LogUtils.i(f.getPath())//将此抽象路径名转换为一个路径名字符串
        LogUtils.i("${f.hashCode()}")//计算此抽象路径名的哈希码
        LogUtils.i("${f.length()}")//返回由此抽象路径名表示的文件的长度
        LogUtils.i("${f.list()}")// 返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中的文件和目录
        LogUtils.i("${f.mkdir()}")//创建此抽象路径名指定的目录
    }

    override fun onDestroy() {
        super.onDestroy()
        tts?.shutdown()
    }
//    override fun onRestart() {
//        super.onRestart()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
}