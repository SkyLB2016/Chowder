package com.sky.chowder.ui.activity

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import butterknife.OnClick
import com.sky.base.RecyclerPActivity
import com.sky.chowder.R
import com.sky.chowder.api.view.ImageUriV
import com.sky.chowder.model.ImageFloder
import com.sky.chowder.ui.adapter.LoaderUriAdapter
import com.sky.chowder.ui.pop.FloderPop
import com.sky.chowder.ui.pop.URIPop
import com.sky.chowder.ui.presenter.ImageUriP
import com.sky.utils.ScreenUtils
import com.sky.widget.BasePop
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

    //瀑布流布局
    private var layoutManager: StaggeredGridLayoutManager? = null
    private var firstVisibleItem: Int = 0//初始可见item
    private var lastVisibleItem: Int = 0//最后一个可见item
    private var first = true

    var adapter: LoaderUriAdapter? = null
    private var floderPop: BasePop<*>? = null

    override fun getLayoutResId(): Int = R.layout.activity_uri

    override fun creatPresenter() = ImageUriP(this)

    override fun initialize() {
        super.initialize()
        setSwipeEnable(false)
//        //瀑布流布局
        layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        setRecyclerLayout(layoutManager)
        //        recycler.setLayoutManager(new StaggeredGridLayoutManager(this,null,0,0));
        //        recycler.setLayoutManager(new GridLayoutManager(this,3));
        adapter = getAdapter();
        adapter!!.setOnItemClickListener { view, position -> ShowImagePop(position) }
    }

    private fun ShowImagePop(position: Int) {
        val imagePop = URIPop(LayoutInflater.from(this).inflate(R.layout.viewpager, null))
        imagePop!!.parentPath = adapter?.parentPath
        imagePop!!.datas = adapter?.datas
        imagePop!!.setCurrentItem(position)
        if (!imagePop!!.isShowing) imagePop!!.showAtLocation(recycler, Gravity.CENTER, 0, 0)
    }

    override fun creatAdapter() = LoaderUriAdapter(R.layout.adapter_uri)

    override fun onRecyclerScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE)
            adapter?.setImageLoader(firstVisibleItem, lastVisibleItem, recyclerView!!)
    }

    override fun onRecyclerScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        //获取可见的第一个与最后一个item
        val firstPositions = layoutManager!!.findFirstVisibleItemPositions(IntArray(layoutManager!!.spanCount))
        firstVisibleItem = getMinPositions(firstPositions)
        val lastPositions = layoutManager!!.findLastVisibleItemPositions(IntArray(layoutManager!!.spanCount))
        lastVisibleItem = getMaxPositions(lastPositions)
        //首次加载执行
        if (lastVisibleItem > 0 && first) {
            first = false
            adapter?.setImageLoader(firstVisibleItem, lastVisibleItem, recycler)
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

    @OnClick(R.id.layout)
    fun layoutEvent(view: View) {
        if (!floderPop!!.isShowing) floderPop!!.showAtLocation(window.decorView, Gravity.BOTTOM, 0, 0)
    }

    override fun showFloderPop(floders: List<ImageFloder>) {
        floderPop = FloderPop(LayoutInflater.from(this).inflate(R.layout.include_recycler, null),
                ScreenUtils.getWidthPX(this), (ScreenUtils.getHeightPX(this) * 0.7).toInt())
        floderPop!!.datas = floders
        floderPop!!.setOnItemClickListener { v, position ->
            setAdapterData(File(floders[position].dirPath!!))
            floderPop!!.dismiss()
        }
    }

    override fun setAdapterData(parent: File?) {
        val imageNames = Arrays.asList(*parent!!.list(filter))
        adapter!!.parentPath = parent!!.absolutePath
        adapter!!.datas = imageNames

        flodername!!.text = parent?.name
        number!!.text = "共${imageNames.size}张图片"
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter!!.interruptExecutors()
    }

    private var filter: FilenameFilter = FilenameFilter { _, filename ->
        val type = arrayOf(".jpg", ".JPG", ".jpeg", ".JPEG", ".png", ".PNG")
        type.any { filename.endsWith(it) }
    }
}