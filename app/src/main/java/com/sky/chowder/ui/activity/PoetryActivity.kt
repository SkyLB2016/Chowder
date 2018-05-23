package com.sky.chowder.ui.activity

import android.content.ClipboardManager
import android.graphics.RectF
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.TextView
import com.sky.adapter.RecyclerAdapter
import com.sky.adapter.RecyclerHolder
import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import com.sky.chowder.model.ChapterEntity
import com.sky.chowder.ui.pop.CatalogPop
import com.sky.utils.ScreenUtils
import com.sky.widget.BaseTitle
import kotlinx.android.synthetic.main.activity_poetry.*
import kotlinx.android.synthetic.main.item_tvdisplay.view.*
import java.util.*

/**
 * Created by SKY on 2018/3/16.
 */
class PoetryActivity : BaseNoPActivity(), View.OnClickListener {
    private var gravity = Gravity.LEFT
    private lateinit var adapter: RecyclerAdapter<ChapterEntity>
    private val resId = ArrayList<Int>()
    private lateinit var clipM: ClipboardManager

    override fun getLayoutResId(): Int = R.layout.activity_poetry
    override fun initialize() {
        baseTitle.setLeftButton(R.mipmap.ic_menu)
        baseTitle.onLeftImgClick = BaseTitle.OnClickListener { sliding.toggleMenu() }
        baseTitle.setRightText("3.16建")
        adapter = object : RecyclerAdapter<ChapterEntity>(R.layout.item_tvdisplay) {
            override fun onAchieveHolder(holder: RecyclerHolder?, position: Int) {
                with(holder!!.itemView) {
                    tvDisplay.text = getDatas()[position].content
                    tvDisplay.gravity = gravity
                    if (position == 0) tvDisplay.setPadding(resources.getDimensionPixelSize(R.dimen.wh_32), resources.getDimensionPixelSize(R.dimen.wh_16), resources.getDimensionPixelSize(R.dimen.wh_32), resources.getDimensionPixelSize(R.dimen.wh_4))
                }
            }
        }
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = adapter
        clipM = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        val controller = AnimationUtils.loadLayoutAnimation(this, R.anim.anim_layout)
        controller.delay = 0.5f
        controller.order = LayoutAnimationController.ORDER_NORMAL
        recycler.layoutAnimation = controller

    }

    override fun loadData() {
        val poetry = resources.getStringArray(R.array.poetry)
        var tv: TextView
        var array: List<String>
        for ((index, text) in poetry.withIndex()) {
            array = text.split(",")
            tv = LayoutInflater.from(this).inflate(R.layout.item_tv, flow, false) as TextView
            tv.width = resources.getDimensionPixelSize(R.dimen.wh_96)
            tv.textSize = 18f
            tv.text = array[0]
//            item_tv.tag = array[0]
            tv.id = index
            flow.addView(tv)
            tv.setOnClickListener(this)
            resId.add(resources.getIdentifier(array[1], "string", packageName))
        }
        val text = getString(resId[6])
//        gravity = Gravity.CENTER
        setToolbarTitle(text.lines()[0])
        llBottomBar.visibility = View.GONE
        registerOnClick(tvLast, tvCatalog, tvNext)
        getCatalog(text)
    }

    override fun registerView(v: View) = v.setOnClickListener(catalog)

    override fun onClick(v: View?) {
        gravity = when (v?.id) {
            in 2..5 -> Gravity.CENTER
            else -> Gravity.LEFT
        }
        val text = getString(resId[v!!.id])
//        clipM.primaryClip = ClipData.newPlainText("",text.lines()[0])
        setToolbarTitle(text.lines()[0])
        getCatalog(text)
//        LogUtils.i("行数==${text.lines().size}")
    }

    private val catalog = View.OnClickListener { v ->
        when (v?.id) {
            R.id.tvLast -> upToChapter()
            R.id.tvCatalog -> showCatalogPop(adapter?.datas)
            R.id.tvNext -> nextChapter()
        }
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
        val list = text.lines()
        val catalog = ArrayList<ChapterEntity>()
        var chapter = ChapterEntity()
        chapter.chapter = list[0]
        for (i in list) {
            val text = if (i.startsWith(" ")) i.substring(1) else i
            if (text.startsWith("第") && text.endsWith("章")
                    || text.startsWith("卷")
                    || text.startsWith("【")
                    || text.startsWith("●")
                    || text.contains("第") && !text.contains("。")) {
                chapter.content.setLength(chapter.content.length - 1)
                catalog.add(chapter)
                chapter = ChapterEntity()
                chapter.chapter = text
//            } else if (i.contains(".")) {
//                chapter.content.setLength(chapter.content.length - 1)
//                catalog.add(chapter)
//                chapter = ChapterEntity()
//                chapter.chapter = i.substring(2, i.indexOf("."))
            }
            chapter.content.append("$text\n")
        }
        var sign = -1
        if (catalog.isEmpty() && list.size > 30) {
            chapter.content = StringBuilder()
            for (i in list.indices) {
                val text = if (list[i].startsWith(" ")) list[i].substring(1) else list[i]
                if (sign === -1 && text.contains("，")) sign = i % 10
                if (i % 10 === sign) {
                    chapter.content.setLength(chapter.content.length - 1)
                    catalog.add(chapter)
                    chapter = ChapterEntity()
                    chapter.chapter = text
                }
                chapter.content.append("$text\n")
            }
        }
        chapter.content.setLength(chapter.content.length - 1)
        catalog.add(chapter)
        adapter?.datas = catalog
    }

    private fun showCatalogPop(floders: List<ChapterEntity>?) {
        val floderPop = CatalogPop(LayoutInflater.from(this).inflate(R.layout.include_recycler, null),
                ScreenUtils.getWidthPX(this), (ScreenUtils.getHeightPX(this) * 0.7).toInt())
        floderPop?.datas = floders
        floderPop?.setOnItemClickListener { _, position -> moveToChapter(position) }
        floderPop?.showAtLocation(window.decorView, Gravity.CENTER, 0, 0)
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        val yy = getObject("scrollX", 0)
//        scroll?.scrollTo(0, yy)
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
            llBottomBar.visibility == View.VISIBLE -> {
                llBottomBar.visibility = View.GONE
                return
            }
            else -> super.onBackPressed()
        }
    }
}