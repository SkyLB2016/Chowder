package com.sky.chowder.ui.activity

import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import butterknife.OnClick
import com.sky.base.RecyclerPActivity
import com.sky.chowder.R
import com.sky.chowder.model.ImageFloder
import com.sky.chowder.ui.adapter.LoaderUriAdapter
import com.sky.chowder.ui.pop.FloderPop
import com.sky.chowder.ui.pop.URIPop
import com.sky.chowder.ui.presenter.ImageUriP
import com.sky.utils.SDCardUtils
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
class ImageUriActivity : RecyclerPActivity<String, ImageUriP>() {
    //瀑布流布局
    private var layoutManager: StaggeredGridLayoutManager? = null
    private var firstVisibleItem: Int = 0//初始可见item
    private var lastVisibleItem: Int = 0//最后一个可见item
    private var first = true

    private var totalCount = 0
    private val floders = ArrayList<ImageFloder>()
    private var parent: File? = null
    private var maxCount = 0//图片数量最多的文件
    private var floderPop: BasePop<*>? = null
    private var imagePop: URIPop? = null
    var adapter: LoaderUriAdapter? = null

    override fun getLayoutResId(): Int = R.layout.activity_uri

    override fun creatPresenter() = ImageUriP(this)

    override fun initialize() {
        super.initialize()
        initView()
        setdata()
    }

    override fun creatAdapter() = LoaderUriAdapter(R.layout.adapter_uri)

    private fun initView() {
        setSwipeEnable(false)
//        //瀑布流布局
        layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        setRecyclerLayout(layoutManager)
        //        recycler.setLayoutManager(new StaggeredGridLayoutManager(this,null,0,0));
        //        recycler.setLayoutManager(new GridLayoutManager(this,3));
        adapter = getAdapter();
        adapter!!.setOnItemClickListener { view, position ->
            createPopShowImage(position, parent)
            if (!imagePop!!.isShowing) imagePop!!.showAtLocation(recycler, Gravity.CENTER, 0, 0)
        }
    }

    override fun onRecyclerScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onRecyclerScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            adapter?.setImageLoader(firstVisibleItem, lastVisibleItem, recyclerView)
        }
    }

    override fun onRecyclerScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onRecyclerScrolled(recyclerView, dx, dy)
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

    private fun setdata() {
        if (!SDCardUtils.isSDCardEnable()) {
            showToast("暂无外部存储")
            return
        }
        object : Thread() {
            override fun run() {
                super.run()
                checkDiskImage()
                handler.sendEmptyMessage(0x99)
            }
        }.start()
    }

    fun checkDiskImage() {
        //临时的辅助类，用于防止同一个文件夹的多次扫描
        val parentPaths = HashSet<String>()

        val imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val resolver = contentResolver
        val cursor = resolver.query(imageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?", arrayOf("image/jpeg", "image/png"), MediaStore.Images.Media.DATE_MODIFIED)
        while (cursor!!.moveToNext()) {
            val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            val parentFile = File(path).parentFile ?: continue
            //parent如果为空跳出此次循环
            val parentPath = parentFile.absolutePath
            //开始收集父级文件夹得信息
            val floder: ImageFloder
            //每个文件夹只添加一次，不能重复添加
            if (parentPaths.contains(parentPath)) {
                continue
            } else {
                parentPaths.add(parentPath)
                floder = ImageFloder()
                //路径
                floder.dirPath = parentPath
                //第一张图片
                floder.firstImagePath = path
            }
            val count = parentFile.list(filter).size
            //收集图片的总数量
            totalCount += count
            floder.count = count
            floders.add(floder)
            //选出图片数量最多的floder
            if (count > maxCount) {
                maxCount = count
                parent = parentFile
            }
        }
        cursor.close()
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
        createPopShowFloder()
        if (!floderPop!!.isShowing)
            floderPop!!.showAtLocation(window.decorView, Gravity.BOTTOM, 0, 0)
    }

    private fun createPopShowFloder() {
        if (floderPop == null)
            floderPop = FloderPop(LayoutInflater.from(this).inflate(R.layout.include_recycler, null),
                    ScreenUtils.getWidthPX(this), (ScreenUtils.getHeightPX(this) * 0.7).toInt())
        floderPop!!.datas = floders
        floderPop!!.setOnItemClickListener { v, position ->
            val floder = floders[position]
            parent = File(floder.dirPath!!)
            handler.sendEmptyMessage(0x99)

            floderPop!!.dismiss()
        }
    }

    private fun createPopShowImage(position: Int, parent: File?) {
        if (imagePop == null) imagePop = URIPop(LayoutInflater.from(this).inflate(R.layout.viewpager, null))
        val imageNames = Arrays.asList(*parent!!.list(filter))
        imagePop!!.parentPath = parent.absolutePath
        imagePop!!.datas = imageNames
        imagePop!!.setCurrentItem(position)
    }

    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0x99 -> setAdapterData()
            }
        }
    }

    private fun setAdapterData() {
        val imageNames = Arrays.asList(*parent!!.list(filter))
        adapter!!.setParentPath(parent!!.absolutePath)
        adapter!!.datas = imageNames
        val path = parent!!.absolutePath
        flodername!!.text = path.substring(path.lastIndexOf("/") + 1)
        number!!.text = Integer.toString(imageNames.size)
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter!!.interruptExecutors()
    }

    private var filter: FilenameFilter = FilenameFilter { dir, filename ->
        val type = arrayOf(".jpg", ".JPG", ".jpeg", ".JPEG", ".png", ".PNG")
        type.any { filename.endsWith(it) }
    }
}