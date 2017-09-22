package com.sky.chowder.ui.activity

import android.Manifest
import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.provider.ContactsContract
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import butterknife.OnClick
import com.sky.base.BasePActivity
import com.sky.chowder.R
import com.sky.chowder.api.view.IMainView
import com.sky.chowder.model.ActivityModel
import com.sky.chowder.ui.adapter.MainAdapter
import com.sky.chowder.ui.presenter.MainP
import com.sky.chowder.ui.service.IntentTest
import com.sky.model.Extra
import com.sky.utils.AppUtils
import com.sky.utils.JumpAct
import kotlinx.android.synthetic.main.content_main.*


/**
 * Created by SKY on 2015/12/6.
 * 主页
 */
class MainActivity : BasePActivity<MainP>(), Toolbar.OnMenuItemClickListener, IMainView {

    private var adapter: MainAdapter? = null

    public override fun getLayoutResId(): Int = R.layout.activity_main

    override fun creatPresenter() = MainP(this)

    public override fun initialize() {
        baseTitle.setLeftButton(-1)
        recycler?.setHasFixedSize(true)
        adapter = MainAdapter(R.layout.adapter_main)
        recycler?.adapter = adapter

        adapter?.setOnItemClickListener { _, position -> JumpAct.jumpActivity(this, adapter!!.datas[position].componentName) }
        adapter?.setOnItemLongClickListener { _, _ ->
            showToast("长按监听已处理")
            true
        }
        if (!AppUtils.isPermission(this, Manifest.permission.WRITE_CONTACTS))
            AppUtils.requestPermission(this, arrayOf(Manifest.permission.WRITE_CONTACTS), 0)
        if (!AppUtils.isPermission(this, Manifest.permission.READ_CONTACTS))
            AppUtils.requestPermission(this, arrayOf(Manifest.permission.READ_CONTACTS), 0)
        if (!AppUtils.isPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE))
            AppUtils.requestPermission(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
    }

    @OnClick(R.id.fab)
    fun fabOnClick() {
//        getMemory()
//        getMemory1()
//        showToast("width==${ScreenUtils.getHeightPX(this)}")
        IntentTest.startIntent(this, Extra<String>(),"com.sky.action")
        presenter.showToast("测试消息")
//        val intent = Intent(AlarmClock.ACTION_SET_ALARM)
//        intent.putExtra(AlarmClock.EXTRA_HOUR, 15)
//        intent.putExtra(AlarmClock.EXTRA_MINUTES, 24)
//        intent.putExtra(AlarmClock.EXTRA_MESSAGE, "测试")
//        val intent = Intent(AlarmClock.ACTION_SET_TIMER)
//                .putExtra(AlarmClock.EXTRA_MESSAGE, "测试")
//                .putExtra(AlarmClock.EXTRA_LENGTH, 10)
//                .putExtra(AlarmClock.EXTRA_SKIP_UI, true)
//        val intent = Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA)
//
//        if (intent.resolveActivity(packageManager) != null) {
//            startActivity(intent);
//        }

        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE;
//        intent.type = ContactsContract.Contacts.CONTENT_TYPE
//        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        intent.data = ContactsContract.Contacts.CONTENT_URI
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, 101)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK) {
            val uri = data?.data
//            val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val projection = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
//            val projection = arrayOf(ContactsContract.Contacts.IS_USER_PROFILE)
            val cursor = contentResolver.query(uri, projection, null, null, null);
            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
//                val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                val numberIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//                val numberIndex = cursor.getColumnIndex(ContactsContract.Contacts.IS_USER_PROFILE);
                val number = cursor.getString(numberIndex);
                showToast(number)
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menu.add(Menu.NONE, Menu.FIRST + 1, 1000, "dd").setIcon(R.mipmap.back).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
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
        val nowCurrent = System.currentTimeMillis()
        if (nowCurrent - lastBack > 3000) {
            showToast(resources.getString(R.string.toast_exit))
            lastBack = nowCurrent
        } else super.onBackPressed()
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

