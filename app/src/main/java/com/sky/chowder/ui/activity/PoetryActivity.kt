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
import com.sky.utils.LogUtils
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
                        tvDisplay.setPadding(resources.getDimensionPixelSize(R.dimen.wh_32), resources.getDimensionPixelSize(R.dimen.wh_16), resources.getDimensionPixelSize(R.dimen.wh_32), resources.getDimensionPixelSize(R.dimen.wh_4))
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
        LogUtils.i("行数==${text.lines().size}")
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
                    || i.startsWith("【")
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
// 像这种话啊，你也别问员工，没意义，员工要是说实话呢，那叫挑拨离间，就如某某，公司内的人基本都讨厌ta,要是不说实话呢，又觉着过意不去，所以说话就支支吾吾。
// 说到底呢，我也只是名员工，说的再具体点我只是一名专精技术的员工，我也没那么强的事业心，用不着去事事迎合你们，行就是行，不行就是不行（或者换个说法：
// 我曾经对你们说过，在程序上只要你能想到，在我这基本都是能实现的，但是，请注意这里有个但是，你想到的在程序逻辑上不一定能很好的兼容，往往这样就会产生连锁bug，
// 又或者会让本来很简单的一个功能，因为加了很多限制，变成一个臃肿的逻辑，无形中就限制了后期维护与改动）或许在你们看来这后期维护与改动没什么，但是像这种问题多了，
// 所要耗费的时间可不是说抬抬手就能解决的了，往往是改这，别的地也会出bug，程序是一个整体，但局部应该就是局部，一件事只做一件事。
// 程序要上线了，这边页面又变了，这就是你们那位口中喊着这是为公司写的又不是为我写的的人常干的事，或许变就变呗，这在你们看着很正常，因为需求有改动了嘛，
// 但那只是因为你们不了解也不想了解，我记着我们说过，上线前就不要改动的话吧，你这改一个小改动，我这可能就需要大改，可能就需要涉及底层的更改，这样牵扯的地方就多了，
// 很容易产生bug，这项目还上不上线，况且需求变了，那时间工期变不变？。？。都说无规矩不成方圆，打我们开始接手这个项目到现在，这规矩也还是没立起来，其原因何在。？
//明明呢是自己的一家之言，就张口闭口为用户，用户需要。
// 给android提的bug，却是iPhone的页面，而我这查无此bug，你们说这bug是ios还是android，这种问题常犯的话，你们觉着作为一个产品他合格吗，连自己的产品都不了解的产品合格吗。
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