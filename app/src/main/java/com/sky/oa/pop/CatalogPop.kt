package com.sky.oa.pop

import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sky.oa.model.ChapterEntity
import com.sky.design.adapter.RecyclerAdapter
import com.sky.design.adapter.RecyclerHolder
import com.sky.design.widget.BasePop
import com.sky.oa.R
import kotlinx.android.synthetic.main.item_tv.view.*

/**
 * Created by SKY on 2016/1/11 13:14.
 * 文件夹pop
 */
class CatalogPop(view: View, width: Int, height: Int) : BasePop<ChapterEntity>(view, width, height) {
    private var recycle: RecyclerView? = null

    private var adapter: RecyclerAdapter<ChapterEntity>? = null

    override fun initView() {
        super.initView()
        var swipe: SwipeRefreshLayout = view.findViewById(R.id.swipe)
        swipe.isEnabled = false
        recycle = view.findViewById(R.id.recycler)
        adapter = object : RecyclerAdapter<ChapterEntity>(R.layout.item_tv) {
            override fun onAchieveHolder(holder: RecyclerHolder, position: Int) {
                with(holder!!.itemView) {
                    tv.text = datas[position].chapter
                    tv.setBackgroundResource(R.drawable.sel_rect_yellow_ddc29f07)
                    tv.textSize = 18f
                    tv.gravity = Gravity.LEFT
                }
            }
        }
        recycle?.adapter = adapter
    }

    override fun initEvent() {
        adapter?.setOnItemClickListener { view, position ->
            itemClickListener?.onItemClick(view, position)
            dismiss()
        }
    }

    override fun initDatas() {
        adapter?.datas = popDatas
    }
}
