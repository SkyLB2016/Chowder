package com.sky.chowder.ui.activity

import android.Manifest
import android.content.ActivityNotFoundException
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.sky.SkyApp
import com.sky.base.BasePActivity
import com.sky.chowder.R
import com.sky.chowder.api.view.IMainView
import com.sky.chowder.model.ActivityModel
import com.sky.chowder.ui.adapter.MainAdapter
import com.sky.chowder.ui.presenter.MainP
import com.sky.utils.AppUtils
import com.sky.utils.JumpAct
import com.sky.utils.LogUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File

/**
 * Created by SKY on 2015/12/6.
 * 主页
 */
class MainActivity : BasePActivity<MainP>(), Toolbar.OnMenuItemClickListener, IMainView {
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
    public override fun initialize() {
        baseTitle.setLeftButton(-1)
        recycler?.setHasFixedSize(true)
        adapter = MainAdapter(R.layout.adapter_main)
        recycler.adapter = adapter


        adapter?.setOnItemClickListener { _, position -> JumpAct.jumpActivity(this, adapter.datas[position].componentName) }
        adapter?.setOnItemLongClickListener { _, _ ->
            showToast("长按监听已处理")
            true
        }
        AppUtils.isPermissions(this,
                arrayOf(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE),
                intArrayOf(0, 0, 0)
        )
//        registerOnClick(fab,fab1)
        fab1.setOnClickListener {
            var text = getString(R.string.cezi).trim().replace(" ", "")
            LogUtils.i("总长==${text.length}")
        }
//        var num = Array(4, { Array(4) { 0 } })
        var num = Array(4) { IntArray(4) }
        fab.setOnClickListener {
            LogUtils.i("${window.decorView}")
        }

    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun setData(data: List<ActivityModel>) {
        adapter?.datas = data
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
        System.out.println(f.getParent())//返回此抽象路径名父目录的路径名字符串；如果此路径名没有指定父目录，则返回 null
        System.out.println(f.getName())//返回由此抽象路径名表示的文件或目录的名称
        System.out.println(f.exists())//测试此抽象路径名表示的文件或目录是否存在
        System.out.println(f.getAbsoluteFile())// 返回此抽象路径名的绝对路径名形式
        System.out.println(f.getAbsolutePath())//返回此抽象路径名的规范路径名字符串
        System.out.println(f.getPath())//将此抽象路径名转换为一个路径名字符串
        System.out.println(f.hashCode())//计算此抽象路径名的哈希码
        System.out.println(f.length())//返回由此抽象路径名表示的文件的长度
        System.out.println(f.list())// 返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中的文件和目录
        System.out.println(f.mkdir())//创建此抽象路径名指定的目录
    }

//    override fun onRestart() {
//        super.onRestart()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onStart() {
//        super.onStart()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onResume() {
//        super.onResume()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//    override fun onPause() {
//        super.onPause()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//    override fun onDestroy() {
//        super.onDestroy()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
}