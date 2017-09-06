package com.sky.chowder.ui.adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

/**
 * Created by 李彬 on 2017/3/6.
 */
internal class GuideAdapter(private val views: List<View>) : PagerAdapter() {

    override fun getCount(): Int {
        return views.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = views[position]
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(views[position])
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}