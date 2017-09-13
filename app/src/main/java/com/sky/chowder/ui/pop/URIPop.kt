package com.sky.chowder.ui.pop

import android.support.v4.view.ViewPager
import android.view.View

import com.sky.chowder.R
import com.sky.chowder.ui.adapter.PopPagerAdapter
import com.sky.widget.BasePop

/**
 * Created by SKY on 2016/1/11 13:14.
 */
class URIPop(contentView: View) : BasePop<String>(contentView) {

    var parentPath: String? = null
    private var viewPager: ViewPager? = null
    private var adapter: PopPagerAdapter? = null

    override fun initEvent() {
        viewPager = view.findViewById<ViewPager>(R.id.id_viewpager)
        adapter = PopPagerAdapter()
        viewPager!!.adapter = adapter
    }

    override fun initDatas() {
        adapter!!.parentPath = parentPath
        adapter!!.strings = popDatas
        adapter!!.notifyDataSetChanged()

    }

    fun setCurrentItem(position: Int) {
        viewPager!!.setCurrentItem(position, false)
    }
}
