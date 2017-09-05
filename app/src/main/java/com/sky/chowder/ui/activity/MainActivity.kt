package com.sky.chowder.ui.activity

import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.sky.base.BasePActivity
import com.sky.chowder.R
import com.sky.chowder.api.view.IMainView
import com.sky.chowder.model.ActivityModel
import com.sky.chowder.ui.adapter.MainAdapter
import com.sky.chowder.ui.presenter.MainPresenter
import com.sky.utils.JumpAct
import com.sky.utils.LogUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File

class MainActivity : BasePActivity<MainPresenter>(), Toolbar.OnMenuItemClickListener, IMainView {

    //        private val recycle: MyRecyclerView by bindView(R.id.recycle)
//    val fab: FloatingActionButton by bindView(R.id.fab)
    private var adapter: MainAdapter? = null

    public override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    public override fun initialize() {
        baseTitle.setLeftButton(-1)

        recycle!!.setHasFixedSize(true)
        adapter = MainAdapter(R.layout.adapter_main)
        recycle!!.adapter = adapter

        adapter!!.setOnItemClickListener { _, position -> JumpAct.jumpActivity(this, adapter!!.datas[position].componentName) }
        adapter!!.setOnItemLongClickListener { _, _ ->
            showToast("长按监听已处理")
            true
        }
        fab!!.setOnClickListener { v ->
            getMemory()
            getMemory1()
        }

        val s = "abc"
        val str = "$s.length is ${s.length}"
//结果为 abc.length is 3
        LogUtils.i(str)
        val i = 10
        val ss = "i = ${i + 1}"
        LogUtils.i(ss)
        val pow = pow1(3);

        val age = 99
        val typeOfPerson = when (age) {
            0 -> "New born"
            in 1..12 step 2 -> "hello"
            in 139 downTo 19 -> LogUtils.i("Teenager")
            else -> "Adult"
        }
        cases(typeOfPerson)
        loop@ for (i in 1..10) {
            for (j in 1..10) {
                if (i == 5) {
                    continue@loop
                }
                LogUtils.i("Teenager==" + j)
            }
        }
        vars(1, 2, 3, 4, 5)//输出12345
        findFixPoint(9.0)
        val files = File("Test").listFiles()
        LogUtils.i(files?.size.toString())
    }

    infix fun Int.shl(x: Int): Int {
        return 3
    }

    tailrec fun findFixPoint(x: Double = 1.0): Double = if (x == Math.cos(x)) x else findFixPoint(Math.cos(x))
    //等效于下面的代码:
//    private fun findFixPoint(): Double {
//        var x = 1.0
//        while (true) {
//            val y = Math.cos(x)
//            if (x == y) return y
//            x = y
//        }
//    }
    private fun cases(obj: Any) {
        when (obj) {
            1 -> print("one")
            "hello" -> LogUtils.i("Teenager")
            is Long -> LogUtils.i("Long")
            !is Long -> LogUtils.i("Not a string")
            else -> LogUtils.i("Unknown")
        }
    }

    fun max(a: Int, b: Int): Int {
        return if (a > b) a else b
    }

    //第一种形态
    fun pow(a: Int): Double {
        return Math.pow(a.toDouble(), 2.toDouble());
    }

    // 第二种形态，一个表达式函数体和一个可推断类型
    fun pow1(a: Int) = Math.pow(a.toDouble(), 2.toDouble())

    fun double(x: Int) = x * 2

    private fun vars(vararg v: Int) {
        for (vt in v) {
            print(vt)
        }
    }

    fun List<Int>.varss(vararg v: Int) {
        for (vt in v) {
            print(vt)
        }
    }

    fun MutableList<Int>.swap(x: Int, y: Int) {
        val temp = this[x] // this 对应 list
        this[x] = this[y]
        this[y] = temp
    }

    override fun creatPresenter() {
        presenter = MainPresenter(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //        menu.add(Menu.NONE,Menu.FIRST+1,1000,"dd").setIcon(R.mipmap.back).
        //                setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_01//跳转到指定的别的APP的activity
            -> startOther("com.cx.architecture", "com.cx.architecture.ui.activity.LoginActivity")
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
        val nowCurrent = System.currentTimeMillis()
        if (nowCurrent - lastBack > 3000) {
            showToast(resources.getString(R.string.toast_exit))
            lastBack = nowCurrent
        } else {
            super.onBackPressed()
        }
    }

    override fun setData(data: List<ActivityModel>) {
        adapter!!.datas = data
    }

    //获取电量百分比
    val battery: Int
        get() {
            val battery = applicationContext.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val currLevel = battery!!.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            val total = battery.getIntExtra(BatteryManager.EXTRA_SCALE, 1)
            return currLevel * 100 / total
        }

    private fun getMemory() {
        val rt = Runtime.getRuntime()
        val maxMemory = rt.maxMemory()
        val freeMemory = rt.freeMemory()
        val totalMemory = rt.totalMemory()
        showToast("maxMemory:" + java.lang.Double.toString((maxMemory / (1024 * 1024)).toDouble()))
        showToast("freeMemory:" + java.lang.Double.toString(freeMemory * 1.0 / (1024 * 1024)))
        showToast("totalMemory:" + java.lang.Double.toString(totalMemory * 1.0 / (1024 * 1024)))
    }

    private fun getMemory1() {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        showToast("memory==" + manager.memoryClass + "\n;large==" + manager.largeMemoryClass)
    }
}

