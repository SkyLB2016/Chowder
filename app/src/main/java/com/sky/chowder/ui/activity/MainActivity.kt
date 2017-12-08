package com.sky.chowder.ui.activity

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import butterknife.OnClick
import com.google.gson.reflect.TypeToken
import com.sky.SkyApp
import com.sky.base.BasePActivity
import com.sky.chowder.R
import com.sky.chowder.api.view.IMainView
import com.sky.chowder.model.ActivityModel
import com.sky.chowder.ui.adapter.MainAdapter
import com.sky.chowder.ui.presenter.MainP
import com.sky.model.ApiResponse
import com.sky.utils.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.coroutines.experimental.buildSequence


/**
 * Created by SKY on 2015/12/6.
 * 主页
 */
class MainActivity : BasePActivity<MainP>(), Toolbar.OnMenuItemClickListener, IMainView {

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
    }

    private val f: (Int) -> Int = { a -> a * 2 }
    val sum = { x: Int, y: Int -> x + y }
    val sum1: (Int, Int) -> Int = { x, y -> x + y }
    val sum2: (Int, Int, Int) -> Int = { x, y, z -> x + y }
    private var test: String? = null
    private var test1 = ""
    private var test2 = "22"
    private var temp: List<String>? = null//计价详细信息

    @OnClick(R.id.fab)
    fun fabOnClick() {
        testGson()
//        showToast("width==${ScreenUtils.getHeightPX(this)}")
//        IntentTest.startIntent(this, Extra<String>(),"com.sky.action")
//        presenter.showToast("测试消息")
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
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = Phone.CONTENT_TYPE
//        startActivityForResult(intent, 101)

//        val flag = resources.getBoolean(R.bool.flag)
//        if (flag) showToast(getString(R.string.app_name))
//        adapter!!.datas[2].let { LogUtils.i(it.className) }
//        LogUtils.i(adapter!!.datas[2].let { it.componentName })
//        FileUtils.saveFile(SkyApp.getInstance().fileCacheDir, "name1.txt", "name")
//        FileUtils.saveFile(SkyApp.getInstance().fileCacheDir + "pass", "pass")
//        FileUtils.saveFile(SkyApp.getInstance().fileCacheDir + "pass.txt", "pass")
//        val map = FileUtils.get(this);
//        LogUtils.i(map["pass"])

//        val f = File(SkyApp.getInstance().fileCacheDir + "pass.txt")
//        System.out.println(f.getParent())//返回此抽象路径名父目录的路径名字符串；如果此路径名没有指定父目录，则返回 null
//        System.out.println(f.getName())//返回由此抽象路径名表示的文件或目录的名称
//        System.out.println(f.exists())//测试此抽象路径名表示的文件或目录是否存在
//        System.out.println(f.getAbsoluteFile())// 返回此抽象路径名的绝对路径名形式
//        System.out.println(f.getAbsolutePath())//返回此抽象路径名的规范路径名字符串
//        System.out.println(f.getPath())//将此抽象路径名转换为一个路径名字符串
//        System.out.println(f.hashCode())//计算此抽象路径名的哈希码
//        System.out.println(f.length())//返回由此抽象路径名表示的文件的长度
//        System.out.println(f.list())// 返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中的文件和目录
//        System.out.println(f.mkdir())//创建此抽象路径名指定的目录
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK) {
            val projection = arrayOf(Phone.NUMBER, Phone._ID, Phone.DISPLAY_NAME)
            val cursor = contentResolver.query(data?.data, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                val numberIndex = cursor.getColumnIndex(Phone.DISPLAY_NAME);
                val number = cursor.getString(numberIndex);
                showToast(number)
            }
            cursor.close()
        }
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
            val total = battery.getIntExtra(BatteryManager.EXTRA_SCALE, 0)
            return currLevel * 100 / total
        }

    private fun testGson() {
        val model = GsonUtils.jsonToEntity(getString(R.string.jsonobj), ActivityModel::class.java)
        val list = GsonUtils.jsonToList(getString(R.string.jsonarray), Array<ActivityModel>::class.java)
        val entity = GsonUtils.jsonToEntity<ApiResponse<List<ActivityModel>>>(getString(R.string.jsonlist), object : TypeToken<ApiResponse<List<ActivityModel>>>() {}.type)
        val array = GsonUtils.jsonToArray(getString(R.string.jsonarray), Array<ActivityModel>::class.java)
        val model4 = FileUtils.fileToSerialObj<ActivityModel>(SkyApp.getInstance().fileCacheDir, "model")
        val seq = buildSequence {
            for (i in 1..5) {
                // 产生一个 i 的平方
                yield(i * i)
            }
            // 产生一个区间
            yieldAll(26..28)
        }

        // 输出该序列
        println(seq.toList())

        var fruits = listOf("banana", "avocado", "apple", "kiwi")
        fruits
                .filter { it.startsWith("a") }
                .sortedBy { it }
                .map { it.toUpperCase() }
                .forEach { LogUtils.i(it) }
        array
                .sortedByDescending { it }
                .forEach { LogUtils.i(it.className) }

    }
}