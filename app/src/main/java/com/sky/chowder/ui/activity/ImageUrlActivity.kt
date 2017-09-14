package com.sky.chowder.ui.activity

import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.sky.base.RecyclerPActivity
import com.sky.chowder.R
import com.sky.chowder.model.CourseEntity
import com.sky.chowder.ui.adapter.LoaderURLAdapter
import com.sky.chowder.ui.presenter.ImageUrlP
import com.sky.chowder.utils.ImageLoaderAsync
import kotlinx.android.synthetic.main.navigation_content.*

/**
 * Created by SKY on 2015/11/28.
 * 加载网络图片
 */
class ImageUrlActivity : RecyclerPActivity<CourseEntity, ImageUrlP>() {
    private var first: Boolean = false
    private var firstVisibleItem: Int = 0
    private var lastVisibleItem: Int = 0

    override fun getLayoutResId() = R.layout.navigation_content

    override fun creatPresenter() = ImageUrlP(this)

    private lateinit var imageLoader: ImageLoaderAsync

    override fun initialize() {
        first = true
        imageLoader = ImageLoaderAsync()
        super.initialize()
        fab.setOnClickListener { v -> Snackbar.make(v, "正在加载，请稍后", Snackbar.LENGTH_LONG).setAction("cancel") { showToast("已取消") }.show() }
    }

    override fun creatAdapter() = LoaderURLAdapter(R.layout.adapter_main)

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
            imageLoader.showAsyncImage(recycle.findViewWithTag<View>(adapter.datas[i].picBig) as ImageView, adapter.datas[i].picBig)
        }
    }

    private fun cancelAlltasks() {
        imageLoader.cancelAlltasks()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelAlltasks()
    }
}