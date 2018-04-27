package com.sky.chowder.ui.activity

import android.graphics.RectF
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
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
    var gravity = Gravity.LEFT
    var adapter: RecyclerAdapter<ChapterEntity>? = null
    private val resId = ArrayList<Int>()
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
                    if (position == 0) {
                        tvDisplay.setPadding(resources.getDimensionPixelSize(R.dimen.wh_32),
                                resources.getDimensionPixelSize(R.dimen.wh_16),
                                resources.getDimensionPixelSize(R.dimen.wh_32),
                                resources.getDimensionPixelSize(R.dimen.wh_4))
                    }
                }
            }
        }
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = adapter

        val poetry = resources.getStringArray(R.array.poetry)!!
        var tv: TextView
        for ((index, text) in poetry.withIndex()) {
            val array = text.split(",")
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


        val text = getString(resId[3]).replace(" ", "")
        gravity = Gravity.CENTER
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
        val text = getString(resId[v!!.id]).replace(" ", "")
        setToolbarTitle(text.lines()[0])
        getCatalog(text)
    }

    private val catalog = View.OnClickListener { v ->
        when (v?.id) {
            R.id.tvLast -> upToChapter()
            R.id.tvCatalog -> showCatalogPop(adapter?.datas!!)
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
            if (i.startsWith("第") && i.endsWith("章")
                    || i.startsWith("卷")
                    || i.contains("第") && !i.contains("。")) {
                chapter.content.setLength(chapter.content.length - 1)
                catalog.add(chapter)
                chapter = ChapterEntity()
                chapter.chapter = i
            } else if (i.contains(".")) {
                chapter.content.setLength(chapter.content.length - 1)
                catalog.add(chapter)
                chapter = ChapterEntity()
                chapter.chapter = i.substring(2, i.indexOf("."))
            }
            chapter.content.append("$i\n")
        }
        var sign = -1
        if (catalog.isEmpty() && list.size > 30) {
            chapter.content = StringBuilder()
            for (i in list.indices) {
                if (sign === -1 && list[i].contains("，")) sign = i % 10
                if (i % 10 === sign) {
                    chapter.content.setLength(chapter.content.length - 1)
                    catalog.add(chapter)
                    chapter = ChapterEntity()
                    chapter.chapter = list[i]
                }
                chapter.content.append("${list[i]}\n")
            }
        }
        chapter.content.setLength(chapter.content.length - 1)
        catalog.add(chapter)
        adapter?.datas = catalog
    }

    private fun showCatalogPop(floders: List<ChapterEntity>) {
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

    var downX = 0f
    var downY = 0f
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev!!.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
            }
            MotionEvent.ACTION_UP -> {
                val rect = RectF(1080 / 3f, 1920 / 3f, 1080 / 3f * 2, 1920 / 3f * 2)
                if (Math.abs(ev.x - downX) < 5 && Math.abs(ev.y - downY) < 1 && rect.contains(ev.x, ev.y) && sliding.isClose)
                    if (llBottomBar.visibility == View.GONE) llBottomBar.visibility = View.VISIBLE
                    else llBottomBar.visibility = View.GONE
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() {
        if (llBottomBar.visibility == View.VISIBLE) {
            llBottomBar.visibility = View.GONE
            return
        } else super.onBackPressed()
    }
}