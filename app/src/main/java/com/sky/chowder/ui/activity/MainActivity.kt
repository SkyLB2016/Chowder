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
import butterknife.OnClick
import com.sky.base.BasePActivity
import com.sky.chowder.R
import com.sky.chowder.api.view.IMainView
import com.sky.chowder.model.ActivityModel
import com.sky.chowder.ui.adapter.MainAdapter
import com.sky.chowder.ui.presenter.MainPresenter
import com.sky.utils.JumpAct
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BasePActivity<MainPresenter>(), Toolbar.OnMenuItemClickListener, IMainView {

    private var adapter: MainAdapter? = null

    public override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun creatPresenter() {
        presenter = MainPresenter(this)
    }

    public override fun initialize() {
        baseTitle.setLeftButton(-1)
        recycle?.setHasFixedSize(true)
        adapter = MainAdapter(R.layout.adapter_main)
        recycle?.adapter = adapter

        adapter?.setOnItemClickListener { _, position -> JumpAct.jumpActivity(this, adapter!!.datas[position].componentName) }
        adapter?.setOnItemLongClickListener { _, _ ->
            showToast("长按监听已处理")
            true
        }
    }

    @OnClick(R.id.fab)
    fun obnClick() {
        getMemory()
        getMemory1()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menu.add(Menu.NONE, Menu.FIRST + 1, 1000, "dd").setIcon(R.mipmap.back).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
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
        adapter?.datas = data
    }

    //获取电量百分比
    val battery: Int
        get() {
            val battery = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val currLevel = battery!!.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            val total = battery.getIntExtra(BatteryManager.EXTRA_SCALE, 1)
            return currLevel * 100 / total
        }

    private fun getMemory() {
        val rt = Runtime.getRuntime()
        val maxMemory = rt.maxMemory()
        val freeMemory = rt.freeMemory()
        val totalMemory = rt.totalMemory()
        showToast("maxMemory: ${maxMemory / (1024 * 1024)}")
        showToast("freeMemory: ${freeMemory * 1.0 / (1024 * 1024)}")
        showToast("totalMemory: ${totalMemory * 1.0 / (1024 * 1024)}")
    }

    private fun getMemory1() {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        showToast("memory==${manager.memoryClass};large==${manager.largeMemoryClass}")
    }
}

