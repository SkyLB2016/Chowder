package com.sky.oa.adapter

import com.sky.design.adapter.RecyclerAdapter
import com.sky.design.adapter.RecyclerHolder
import com.sky.oa.model.Inventory
import kotlinx.android.synthetic.main.item_inventroy.view.*

/**
 * Created by SKY on 2015/12/6.
 * 主页
 */
class TestAdapter(layoutId: Int) : RecyclerAdapter<Inventory>(layoutId) {

    override fun onAchieveHolder(holder: RecyclerHolder, position: Int) {
        var bean = datas.get(position)
        with(holder.itemView) {
            tv_item_desc.text = bean.getItemDesc()
            val quantity: String = bean.getQuantity().toString() + "箱"
            tv_quantity.text = quantity
            val detail: String = bean.getItemCode().toString() + "/" + bean.getDate()
            tv_detail.text = detail
            val volume: String = bean.getVolume().toString() + "方"
            tv_volume.text = volume
        }

    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }
}