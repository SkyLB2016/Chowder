package com.sky.oa.activity

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sky.oa.model.ImageFloder
import com.sky.design.app.RecyclerPActivity
import com.sky.design.widget.BasePop
import com.sky.design.widget.MyRecyclerView
import com.sky.oa.R
import com.sky.oa.adapter.LoaderUriAdapter
import com.sky.oa.api.view.ImageUriV
import com.sky.oa.pop.FloderPop
import com.sky.oa.pop.URIPop
import com.sky.oa.presenter.ImageUriP
import com.sky.sdk.utils.ScreenUtils
import kotlinx.android.synthetic.main.activity_uri.*
import kotlinx.android.synthetic.main.include_recycler.*
import java.io.File
import java.io.FilenameFilter
import java.util.*

/**
 * Created by SKY on 2015/11/28.
 * 加载本地图片
 */
class ImageUriActivity : RecyclerPActivity<String, ImageUriP>(), ImageUriV<String> {
    override fun getRecyclerView(): MyRecyclerView = recycler
    override fun getSwipeView(): SwipeRefreshLayout = swipe

    //瀑布流布局
    private var layoutManager: StaggeredGridLayoutManager? = null
    private var firstVisibleItem: Int = 0//初始可见item
    private var lastVisibleItem: Int = 0//最后一个可见item
    private var first = true

    private var loaderAdapter: LoaderUriAdapter? = null
    private var floderPop: BasePop<*>? = null

    override fun getLayoutResId(): Int = R.layout.activity_uri

    override fun creatPresenter() = ImageUriP(this)

    override fun initialize(savedInstanceState: Bundle?) {
        super.initialize(savedInstanceState)
        setSwipeEnable(false)
//        //瀑布流布局
        layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        setRecyclerLayout(layoutManager)
        //        recycler.setLayoutManager(new StaggeredGridLayoutManager(this,null,0,0));
        //        recycler.setLayoutManager(new GridLayoutManager(this,3));
        relative.setOnClickListener { if (!floderPop!!.isShowing) floderPop!!.showAtLocation(window.decorView, Gravity.BOTTOM, 0, 0) }
    }

    private fun showImagePop(position: Int) {
        val imagePop = URIPop(LayoutInflater.from(this).inflate(R.layout.viewpager, null))
        imagePop.parentPath = loaderAdapter?.parentPath
        imagePop.datas = adapter?.datas
        imagePop.setCurrentItem(position)
        if (!imagePop.isShowing) imagePop.showAtLocation(recycler, Gravity.CENTER, 0, 0)
    }

    override fun creatAdapter() {
        adapter = LoaderUriAdapter(R.layout.adapter_uri)
        loaderAdapter = adapter as LoaderUriAdapter
        adapter?.setOnItemClickListener { _, position -> showImagePop(position) }
    }

    override fun onRecyclerScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE)
            loaderAdapter?.setImageLoader(firstVisibleItem, lastVisibleItem, recyclerView!!)
    }

    override fun onRecyclerScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        //获取可见的第一个与最后一个item
        val firstPositions = layoutManager!!.findFirstVisibleItemPositions(IntArray(layoutManager!!.spanCount))
        firstVisibleItem = getMinPositions(firstPositions)
        val lastPositions = layoutManager!!.findLastVisibleItemPositions(IntArray(layoutManager!!.spanCount))
        lastVisibleItem = getMaxPositions(lastPositions)
        if (firstVisibleItem == -1) firstVisibleItem = 0
        //首次加载执行
        if (lastVisibleItem > -1 && first) {
            first = false
            loaderAdapter?.setImageLoader(firstVisibleItem, lastVisibleItem, recycler)
        }
    }

    private fun getMinPositions(firstPositions: IntArray): Int {
        return (0 until firstPositions.size)
                .map { firstPositions[it] }
                .min()
                ?: firstPositions[0]
    }

    private fun getMaxPositions(lastPositions: IntArray): Int {
        return (0 until lastPositions.size)
                .map { lastPositions[it] }
                .max()
                ?: lastPositions[0]
    }

    override fun showFloderPop(floders: List<ImageFloder>) {
        floderPop = FloderPop(LayoutInflater.from(this).inflate(R.layout.include_recycler, null),
                ScreenUtils.getWidthPX(this), (ScreenUtils.getHeightPX(this) * 0.7).toInt())
        floderPop?.datas = floders
        floderPop?.setOnItemClickListener { _, position ->
            setAdapterData(File(floders[position].dirPath!!))
            floderPop?.dismiss()
        }
    }

    override fun setAdapterData(parent: File?) {
        first = true
        val imageNames = Arrays.asList(*parent!!.list(filter))
        loaderAdapter?.parentPath = parent.absolutePath
        loaderAdapter?.datas = imageNames
        flodername?.text = parent.name
        number?.text = "共${imageNames.size}张图片"
    }

    override fun onDestroy() {
        super.onDestroy()
        loaderAdapter?.interruptExecutors()
    }

    private var filter: FilenameFilter = FilenameFilter { _, filename ->
        arrayOf(".jpg", ".JPG", ".jpeg", ".JPEG", ".png", ".PNG").any { filename.endsWith(it) }
    }
}