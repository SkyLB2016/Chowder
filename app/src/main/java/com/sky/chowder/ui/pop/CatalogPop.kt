package com.sky.chowder.ui.pop

import android.support.v7.widget.RecyclerView
import android.view.View
import com.sky.adapter.RecyclerAdapter
import com.sky.adapter.RecyclerHolder
import com.sky.chowder.R
import com.sky.chowder.model.ChapterEntity
import com.sky.widget.BasePop
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
        recycle = view.findViewById(R.id.recycler)
        adapter = object : RecyclerAdapter<ChapterEntity>(R.layout.item_tv) {
            override fun onAchieveHolder(holder: RecyclerHolder, position: Int) {
                with(holder!!.itemView) {
                    tv.text = datas[position].chapter
                    tv.setBackgroundResource(R.drawable.sel_yellow)
                    tv.textSize=18f
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
