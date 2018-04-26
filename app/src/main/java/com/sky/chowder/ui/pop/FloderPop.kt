package com.sky.chowder.ui.pop

import android.support.v7.widget.RecyclerView
import android.view.View

import com.sky.adapter.RecyclerAdapter
import com.sky.adapter.RecyclerHolder
import com.sky.chowder.R
import com.sky.chowder.model.ImageFloder
import com.sky.utils.BitmapUtils
import com.sky.widget.BasePop

/**
 * Created by SKY on 2016/1/11 13:14.
 * 文件夹pop
 */
class FloderPop(view: View, width: Int, height: Int) : BasePop<ImageFloder>(view, width, height) {
    //看看pageradapter，FragmentStatePagerAdapter等三个的源码
    //看看pageradapter，FragmentStatePagerAdapter等三个的源码
    //看看pageradapter，FragmentStatePagerAdapter等三个的源码
    //看看pageradapter，FragmentStatePagerAdapter等三个的源码
    private var recycle: RecyclerView? = null

    private var adapter: RecyclerAdapter<ImageFloder>? = null

    override fun initView() {
        super.initView()
        recycle = view.findViewById<RecyclerView>(R.id.recycler)
        adapter = object : RecyclerAdapter<ImageFloder>(R.layout.pop_uri_item) {
            override fun onAchieveHolder(holder: RecyclerHolder, position: Int) {
                holder.setImageBitmap(R.id.image, BitmapUtils.getBitmapFromPath(datas[position].firstImagePath, 100, 100))
                holder.setText(R.id.tv_name, datas[position].name)
                holder.setText(R.id.tv_count, datas[position].count.toString() + "个")
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
