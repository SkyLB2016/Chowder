//package com.sky.chowder.ui.activity
//
//import android.Manifest
//import android.content.ActivityNotFoundException
//import android.os.Bundle
//import android.support.v7.widget.Toolbar
//import android.view.Menu
//import android.view.MenuItem
//import android.view.WindowManager
//import com.google.gson.GsonBuilder
//import com.google.gson.internal.bind.TypeAdapters
//import com.google.gson.reflect.TypeToken
//import com.sky.chowder.R
//import com.sky.chowder.api.view.IMainView
//import com.sky.chowder.model.ActivityEntity
//import com.sky.chowder.model.ActivityModel
//import com.sky.chowder.ui.adapter.MainAdapter
//import com.sky.chowder.ui.presenter.MainP
//import com.sky.chowder.model.gson.NullStringToEmptyAdapterFactory
//import com.sky.chowder.model.gson.StringNullAdapter
//import com.sky.utils.AppUtils
//import com.sky.utils.GsonUtils
//import com.sky.utils.JumpAct
//import com.sky.utils.LogUtils
//import common.base.BasePActivity
//import common.model.ApiResponse
//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.content_main.*
//
///**
// * Created by SKY on 2015/12/6.
// * 主页
// */
//class MainActivity : BasePActivity<MainP>(), Toolbar.OnMenuItemClickListener, IMainView {
//
//    private lateinit var adapter: MainAdapter
//    public override fun getLayoutResId(): Int = R.layout.activity_main
//    override fun creatPresenter() = MainP(this)
//    public override fun initialize(savedInstanceState: Bundle?) {
//        //在应用内时，屏幕常亮
//        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//
//        baseTitle.setLeftButton(-1)
//        recycler?.setHasFixedSize(true)
//        adapter = MainAdapter(R.layout.adapter_main_delete)
//        recycler.adapter = adapter
//
//        adapter.setOnItemClickListener { _, position ->
//            JumpAct.jumpActivity(this, adapter.datas[position].componentName) }
////        adapter?.setOnItemLongClickListener { _, _ ->
////            showToast("长按监听已处理")
////            true
////        }
//        AppUtils.isPermissions(this,
//                arrayOf(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE),
//                intArrayOf(0, 0, 0)
//        )
//        fab.setOnClickListener {
//            //Rw2 B2 U2 Lw U2 Rw' U2 Rw U2 F2 Rw F2 Lw' B2 Rw26
//
//            val entity = GsonUtils.jsonToEntity<ApiResponse<List<com.sky.chowder.model.ActivityEntity>>>(getString(R.string.jsonlist), object : TypeToken<ApiResponse<List<com.sky.chowder.model.ActivityEntity>>>() {}.type)
//            val entity1 = GsonUtils.jsonToEntity<ApiResponse<List<com.sky.chowder.model.ActivityEntity>>>(getString(R.string.jsonlistnull), object : TypeToken<ApiResponse<List<com.sky.chowder.model.ActivityEntity>>>() {}.type)
//            LogUtils.i(entity.objList.toString())
//            LogUtils.i(entity1.objList.toString())
//            val gsonBuilder = GsonBuilder()
//                    .excludeFieldsWithoutExposeAnnotation()
//                    .registerTypeAdapterFactory(NullStringToEmptyAdapterFactory<String>())
//            gsonBuilder.registerTypeAdapterFactory(NullStringToEmptyAdapterFactory.getInstance())
//            gsonBuilder.registerTypeAdapterFactory(TypeAdapters.newFactory(String::class.java, StringNullAdapter()))
//                    .registerTypeAdapter(String::class.java,StringNullAdapter())
//                    .serializeNulls()
//            val gson = gsonBuilder.create()
//            val entity2 = GsonBuilder().create().fromJson<ApiResponse<List<ActivityEntity>>>(getString(R.string.jsonlistnull), object : TypeToken<ApiResponse<List<ActivityEntity>>>() {}.type)
//            val entity3 = gson.fromJson<ApiResponse<List<ActivityEntity>>>(getString(R.string.jsonlistnull), object : TypeToken<ApiResponse<List<ActivityEntity>>>() {}.type)
//            LogUtils.i(entity2.objList.toString())
//            LogUtils.i(entity3.objList.toString())
//        }
//    }
//        //需要替换的图片
//        //drawable-hdpi notification_default_icon.png
//        //drawable-xhdpi notification_default_icon.png,landing_center
//        //drawable-xhdpi ic_splash_indicator_selected
//        //lib.account drawable ic_splash_indicator_selected
//    override fun setData(data: List<ActivityModel>) {
//        adapter.datas = data
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onMenuItemClick(item: MenuItem): Boolean {
//        when (item.itemId) {//跳转到指定的别的APP的activity
//            R.id.action_01 -> startOther("com.cx.architecture", "com.cx.architecture.ui.activity.LoginActivity")
//            R.id.action_02 -> startOther("com.gc.materialdesigndemo", "com.gc.materialdesigndemo.ui.MainActivity")
//        }
//        return false
//    }
//
//    private fun startOther(packageName: String, componentName: String) {
//        try {
//            JumpAct.jumpActivity(this@MainActivity, packageName, componentName)
//        } catch (e: ActivityNotFoundException) {
//            showToast("程序未安装")
//        }
//    }
//
//    private var lastBack: Long = 0
//    override fun onBackPressed() {
//        val now = System.currentTimeMillis()
//        if (now - lastBack > 3000) {
//            showToast(getString(R.string.toast_exit))
//            lastBack = now
//        } else super.onBackPressed()
//    }
//
////    override fun onRestart() {
////        super.onRestart()
////        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
////    }