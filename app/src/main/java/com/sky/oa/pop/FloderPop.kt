package com.sky.oa.pop

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sky.oa.model.ImageFloder
import com.sky.design.adapter.RecyclerAdapter
import com.sky.design.adapter.RecyclerHolder
import com.sky.design.widget.BasePop
import com.sky.oa.R
import com.sky.sdk.utils.BitmapUtils

/**
 * Created by SKY on 2016/1/11 13:14.
 * 文件夹pop
 */
class FloderPop(view: View, width: Int, height: Int) : BasePop<ImageFloder>(view, width, height) {
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
