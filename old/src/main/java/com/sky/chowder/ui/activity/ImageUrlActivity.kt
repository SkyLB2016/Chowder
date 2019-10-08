package com.sky.chowder.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.sky.SkyApp
import com.sky.chowder.R
import com.sky.chowder.model.CourseEntity
import com.sky.chowder.ui.adapter.LoaderURLAdapter
import com.sky.chowder.ui.presenter.ImageUrlP
import com.sky.chowder.utils.imageloader.ImageLoaderAsync
import com.sky.utils.BitmapUtils
import com.sky.utils.LogUtils
import com.sky.utils.PhotoUtils
import com.sky.widget.MyRecyclerView
import com.sky.design.app.RecyclerPActivity
import java.io.File
import java.util.*

/**
 * Created by SKY on 2015/11/28.
 * 加载网络图片
 */
class ImageUrlActivity : com.sky.design.app.RecyclerPActivity<CourseEntity, ImageUrlP>() {
    private var first: Boolean = false
    private var firstVisibleItem: Int = 0
    private var lastVisibleItem: Int = 0
    override fun getLayoutResId() = R.layout.navigation_content
    override fun creatPresenter() = ImageUrlP(this)
    override fun getRecyclerView(): MyRecyclerView = recycler
    override fun getSwipeView(): SwipeRefreshLayout = swipe

    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)
        first = true
        fab.setOnClickListener { v -> Snackbar.make(v, "正在加载，请稍后", Snackbar.LENGTH_LONG).setAction("cancel") { showToast("已取消") }.show() }
        adapter.setOnItemClickListener { view, position -> getPhoto(view, position) }
    }

    override fun creatAdapter() {
        val layoutIds = ArrayList<Int>()//主体布局
        layoutIds.add(R.layout.adapter_main_01)
        layoutIds.add(R.layout.adapter_main_02)
        layoutIds.add(R.layout.adapter_main_02)
        layoutIds.add(R.layout.adapter_main_01)
        adapter = LoaderURLAdapter(layoutIds)
    }

    override fun onRecyclerScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onRecyclerScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) setImageLoader(firstVisibleItem, lastVisibleItem, recycler)
        else cancelAlltasks()
    }

    override fun onRecyclerScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onRecyclerScrolled(recyclerView, dx, dy)
        val layoutManager = recycler.layoutManager as LinearLayoutManager
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
        lastVisibleItem = layoutManager.findLastVisibleItemPosition()
        if (lastVisibleItem > 0 && first) {
            first = false
            setImageLoader(firstVisibleItem, lastVisibleItem, recycler)
        }
    }

    private fun setImageLoader(start: Int, last: Int, recycle: RecyclerView) {
        for (i in start..last) {
            ImageLoaderAsync.showAsyncImage(recycle.findViewWithTag<View>(adapter.datas[i].picBig) as ImageView, adapter.datas[i].picBig!!)
//            imageLoader.showAsyncImage(image, adapter.datas[i].picBig);
        }
    }

    private fun cancelAlltasks() {
        ImageLoaderAsync.cancelAlltasks()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelAlltasks()
    }

    private lateinit var photoUtils: PhotoUtils

    private fun getPhoto(view: View, position: Int) {
        val photoPath = SkyApp.getInstance().picCacheDir + System.currentTimeMillis() + ".jpg"
        photoUtils = PhotoUtils(this, photoPath)
        photoUtils?.setUploadPicture { photoName, bitmap ->
            val image = view.findViewById<ImageView>(R.id.image)
            image.setImageBitmap(bitmap)
            adapter.datas[position].picBig = photoName
            LogUtils.i("photo==$photoName")
            LogUtils.i("压缩后所占内存大小==${bitmap.allocationByteCount / 1024}KB")
            LogUtils.i("原图所占内存大小==${BitmapUtils.getBitmapFromPath(photoName).allocationByteCount / 1024 / 1024}MB")

            val pathname = SkyApp.getInstance().picCacheDir + System.currentTimeMillis() % 1000 + ".jpg"
            BitmapUtils.saveBitmapToFile(bitmap, pathname)//保存照片到应用缓存文件目录下
            LogUtils.i("原图文件大小==${File(photoName).length() / 1024 / 1024}MB")
            LogUtils.i("压缩后文件大小==${File(pathname).length() / 1024}KB")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        photoUtils?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        photoUtils?.onActivityResult(requestCode, resultCode, data)
    }
}