package com.sky.oa.activity

import android.content.ClipboardManager
import android.graphics.RectF
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sky.oa.model.ChapterEntity
import com.sky.design.adapter.RecyclerAdapter
import com.sky.design.adapter.RecyclerHolder
import com.sky.oa.pop.CatalogPop
import com.sky.oa.utils.CatalogThread
import com.sky.design.app.BaseActivity
import com.sky.design.widget.BaseTitle
import com.sky.oa.R
import com.sky.sdk.utils.FileUtils
import com.sky.sdk.utils.ScreenUtils
import kotlinx.android.synthetic.main.activity_poetry.*
import kotlinx.android.synthetic.main.item_poetry.*
import java.text.Collator
import java.util.*

/**
 * Created by SKY on 2018/3/16.
 */
class PoetryActivity : BaseActivity() {
    private var gravity = Gravity.LEFT
    private lateinit var adapter: RecyclerAdapter<ChapterEntity>
    //    private var poetry = arrayOf<String>()
    private val poetry = ArrayList<String>();//
    private lateinit var clipM: ClipboardManager
    override fun getLayoutResId(): Int = R.layout.activity_poetry

    override fun initialize(savedInstanceState: Bundle?) {
        baseTitle.setLeftButton(R.mipmap.ic_menu)
        baseTitle.onLeftImgClick = BaseTitle.OnClickListener { sliding.toggleMenu() }
        baseTitle.setRightText("3.16建")
        adapter = object : RecyclerAdapter<ChapterEntity>(R.layout.item_poetry) {
            override fun onAchieveHolder(holder: RecyclerHolder?, position: Int) {
                with(holder!!.itemView) {
                    tvDisplay.text = getDatas()[position].content
//                    tvDisplay.gravity = gravity
                    if (position == 0) tvDisplay.setPadding(resources.getDimensionPixelSize(R.dimen.wh_32), resources.getDimensionPixelSize(R.dimen.wh_16), resources.getDimensionPixelSize(R.dimen.wh_32), resources.getDimensionPixelSize(R.dimen.wh_4))
                }
            }
        }
        recycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler.adapter = adapter
        clipM = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        initEvent()
        addAni()
    }

    private fun addAni() {
        val controller = AnimationUtils.loadLayoutAnimation(this, R.anim.anim_layout)
        controller.delay = 0.5f
        controller.order = LayoutAnimationController.ORDER_NORMAL
        recycler.layoutAnimation = controller
    }

    override fun loadData() {
        var dir = "Documents"//assets初始路径
        var array: Array<String>? = null//assets取出的目录名称
        val link = LinkedList<String>()
        link.add(dir)
//            val stack = Stack<String>();
//            stack.add(dir)
        //取出Documents下的所有文本文件
        while (link.isNotEmpty()) {
            dir = link.removeFirst()
            array = assets.list(dir)
            for (i in array!!) {
                if (i.endsWith(".txt")) {
                    poetry.add("$dir/$i")
                } else {
                    link.add("$dir/$i")
                }
            }
        }
        val collator = Collator.getInstance(Locale.CHINA)
        poetry.sortWith(Comparator { o1, o2 -> collator.compare(o1, o2) })

        var tv: TextView
        for ((index, text) in poetry.withIndex()) {
            tv = LayoutInflater.from(this).inflate(R.layout.item_tv, flow, false) as TextView
            tv.width = resources.getDimensionPixelSize(R.dimen.wh_96)
            tv.textSize = 18f
            tv.maxLines = 1
            tv.text = text.substringAfterLast("/", ".").substringBefore(".", "")
            tv.setPadding(10, 0, 10, 0)
            tv.id = index
            flow.addView(tv)
            tv.setOnClickListener(selectArticle)
        }
        val text = getDocument(poetry[0])
        setToolbarTitle(text.lines()[0])
        getCatalog(text)
    }

    private fun getDocument(sign: String): String {
        return FileUtils.readAssestToStr(this, sign)
    }

    private fun initEvent() {
        tvLast.setOnClickListener { upToChapter() }
        tvCatalog.setOnClickListener { showCatalogPop(adapter?.datas) }
        tvNext.setOnClickListener { nextChapter() }
    }

    private val selectArticle = View.OnClickListener { v ->
//        gravity = when (v?.id) {
//            in 2..5 -> Gravity.CENTER
//            else -> Gravity.LEFT
//        }
        val text = getDocument(poetry[v!!.id])
//        clipM.primaryClip = ClipData.newPlainText("",text.lines()[0])
        setToolbarTitle(text.lines()[0])
        getCatalog(text)
//        LogUtils.i("行数==${text.lines().size}")
    }

    private fun nextChapter() {
        val layoutManager = recycler.layoutManager as LinearLayoutManager
        val position = layoutManager.findFirstVisibleItemPosition()
        layoutManager.scrollToPositionWithOffset(position + 1, 0)
        layoutManager.stackFromEnd = true
    }

    private fun upToChapter() {
        val layoutManager = recycler.layoutManager as LinearLayoutManager
        val position = layoutManager.findFirstVisibleItemPosition()
        recycler.scrollToPosition(position - 1)
    }

    private fun moveToChapter(position: Int) {
        val layoutManager = recycler.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(position, 0)
        layoutManager.stackFromEnd = true
    }

    private fun getCatalog(text: String) {
        CatalogThread(text) { catalog -> adapter?.datas = catalog }
    }

    private fun showCatalogPop(floders: List<ChapterEntity>?) {
        val floderPop = CatalogPop(LayoutInflater.from(this).inflate(R.layout.include_recycler, null),
                ScreenUtils.getWidthPX(this), (ScreenUtils.getHeightPX(this) * 0.7).toInt())
        floderPop?.datas = floders
        floderPop?.setOnItemClickListener { _, position -> moveToChapter(position) }
        floderPop?.showAtLocation(window.decorView, Gravity.CENTER, 0, 0)
    }

    private var downX = 0f
    private var downY = 0f
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev!!.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
            }
            MotionEvent.ACTION_UP -> {
                val width = ScreenUtils.getWidthPX(this) / 3f
                val height = ScreenUtils.getHeightPX(this) / 3f
                val rect = RectF(width, height, width * 2, height * 2)
                if (Math.abs(ev.x - downX) < 5 && Math.abs(ev.y - downY) < 1 && rect.contains(ev.x, ev.y) && sliding.isClose)
                    llBottomBar.visibility = if (llBottomBar.visibility == View.GONE) View.VISIBLE else View.GONE
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() {
        when {
            sliding.isOpen -> sliding.toggleMenu()
            llBottomBar.visibility == View.VISIBLE -> llBottomBar.visibility = View.GONE
            else -> super.onBackPressed()
        }
    }
}