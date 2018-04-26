package com.sky.chowder.ui.activity

import android.graphics.RectF
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import com.sky.chowder.ui.pop.CatalogPop
import com.sky.utils.LogUtils
import com.sky.utils.ScreenUtils
import com.sky.widget.BaseTitle
import kotlinx.android.synthetic.main.activity_poetry.*
import java.util.*

/**
 * Created by SKY on 2018/3/16.
 */
class PoetryActivity : BaseNoPActivity(), View.OnClickListener {

    private val resId = ArrayList<Int>()
    override fun getLayoutResId(): Int = R.layout.activity_poetry
    override fun initialize() {
        baseTitle.setLeftButton(R.mipmap.ic_menu)
        baseTitle.onLeftImgClick = BaseTitle.OnClickListener { sliding.toggleMenu() }
        baseTitle.setRightText("3.16建")

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

        tvDisplay.text = getString(resId[3]).replace(" ", "")
        tvDisplay.gravity = Gravity.CENTER
        setToolbarTitle(tvDisplay.text.lines()[0])
        llBottomBar.visibility = View.GONE
        registerOnClick(tvLast, tvCatalog, tvNext)
    }

    override fun registerView(v: View) = v.setOnClickListener(catalog)

    override fun onClick(v: View?) {
        tvDisplay.gravity = when (v?.id) {
            in 2..5 -> Gravity.CENTER
            else -> Gravity.LEFT
        }
        tvDisplay.text = getString(resId[v!!.id]).replace(" ", "")
        setToolbarTitle(tvDisplay.text.lines()[0])
        LogUtils.i("总行数==${tvDisplay.lineCount}")
    }

    private val catalog = View.OnClickListener { v ->
        when (v?.id) {
            R.id.tvLast -> moveToLine("●三十")
            R.id.tvCatalog -> getCatalog()
            R.id.tvNext -> nextChapter()
        }
    }

    private fun nextChapter() {
        val yy = scroll.scrollY
        val line = (yy - tvDisplay.paddingTop) / tvDisplay.lineHeight
        val layout = tvDisplay.layout
        val start = layout.getLineStart(line+1)
        val end = layout.getLineEnd(line+1)

        val article = tvDisplay.text
        val text = article.substring(start, end)

        val list = article.lines()
        var position = 0
        for (i in list.indices) {
            if (list[i].contains(text))
                position = i
        }

        var catalog = ""
        //筛选章节第**章
        for (i in position until list.size) {
            val text = list[i]
            LogUtils.i("==$text")

            if (catalog.isNotEmpty()) break
            if (list[i].startsWith("第") && list[i].endsWith("章")) catalog = list[i]
            //筛选章节卷
            if (catalog.isNotEmpty()) break
            if (list[i].startsWith("卷")) catalog = list[i]
            //筛选章节第，结尾不含标点
            if (catalog.isNotEmpty()) break
            if (list[i].contains("第") && !list[i].contains("。")) catalog = list[i]
            //筛选章节
            if (catalog.isNotEmpty()) break
            if (list[i].contains(".")) catalog = list[i]
            //筛选章节10句为一章
        }
        if (catalog.isNotEmpty()) moveToLine(catalog) else showToast("已是最后一章")
    }

    private fun moveToLine(sign: String) {
        val text = tvDisplay.text
        val index = text.indexOf(sign)
        tvDisplay.text = text.substring(0, index)
        val line = tvDisplay.lineCount
        tvDisplay.text = text
        moveToLine(line - 1)
    }

    private fun moveToLine(line: Int) {
        scroll?.scrollTo(0, tvDisplay.lineHeight * line + tvDisplay.paddingTop)
//        val layout = tvDisplay.layout
//        val rect = Rect()
//        layout?.getLineBounds(line, rect)
//        scroll?.scrollTo(0, rect.height() * line + tvDisplay.paddingTop)
    }

    private fun getCatalog() {
        val list = tvDisplay.text.lines()
        val catalog = ArrayList<String>()
        //筛选章节第**章
        for (i in list) if (i.startsWith("第") && i.endsWith("章")) catalog.add(i)
        //筛选章节卷
        if (catalog.isEmpty()) for (i in list) if (i.startsWith("卷")) catalog.add(i)
        //筛选章节第，结尾不含标点
        if (catalog.isEmpty()) for (i in list) if (i.contains("第") && !i.contains("。")) catalog.add(i)
        //筛选章节
        if (catalog.isEmpty()) for (i in list) if (i.contains(".")) catalog.add(i.substring(2, i.indexOf(".")))
        //筛选章节10句为一章
        var sign = 0
        if (catalog.isEmpty() && list.size > 30) for (i in list.indices) {
            if (sign === 0 && list[i].contains("，")) sign = i % 10
            if (i % 10 === sign) catalog.add(list[i])
        }
        if (catalog.isNotEmpty()) showCatalogPop(catalog)
        else showToast("无目录")
    }

    private fun showCatalogPop(floders: List<String>) {
        val floderPop = CatalogPop(LayoutInflater.from(this).inflate(R.layout.include_recycler, null),
                ScreenUtils.getWidthPX(this), (ScreenUtils.getHeightPX(this) * 0.7).toInt())
        floderPop?.datas = floders
        floderPop?.setOnItemClickListener { _, position -> moveToLine(floders[position]) }
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
        super.onBackPressed()
        if (llBottomBar.visibility == View.VISIBLE) {
            llBottomBar.visibility = View.GONE
            return
        }

        val yy = scroll.scrollY
        val line = (yy - tvDisplay.paddingTop) / tvDisplay.lineHeight
        setObject("line", line)
        setObject("scrollX", scroll.scrollY)

    }

//    override fun onRestart() {
//        super.onRestart()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onStart() {
//        super.onStart()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onResume() {
//        super.onResume()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onPause() {
//        super.onPause()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
//    }

}
