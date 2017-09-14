package com.sky.chowder.ui.activity

import android.support.design.widget.AppBarLayout
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import com.sky.chowder.ui.fragment.One
import com.sky.chowder.ui.fragment.RecycleFragment
import com.sky.chowder.ui.fragment.Three
import com.sky.chowder.ui.fragment.Two
import kotlinx.android.synthetic.main.activity_tab_vp.*
import kotlinx.android.synthetic.main.include_toolbar.*
import java.util.*

/**
 * Created by SKY on 2015/8/31 14：25.
 * 标签栏
 */
class TabLayoutActivity : BaseNoPActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_tab_vp

    override fun initialize() {
        setUpViewPager()
        val mParams = appbar!!.getChildAt(0).layoutParams as AppBarLayout.LayoutParams
        mParams.scrollFlags = 0//的时候AppBarLayout下的toolbar就不会随着滚动条折叠
        //        mParams.setScrollFlags(5); //的时候AppBarLayout下的toolbar会随着滚动条折叠
    }

    private fun setUpViewPager() {
        tabs!!.visibility = View.VISIBLE
        val titles = ArrayList<String>()
        titles.add("card")
        titles.add("MeshView")
        titles.add("倒影")
        titles.add("shape")
        for (i in titles.indices) {
            tabs!!.addTab(tabs!!.newTab().setText(titles[i]))
        }
        val fragments = ArrayList<Fragment>()
        fragments.add(RecycleFragment())
        fragments.add(One())
        fragments.add(Two())
        fragments.add(Three())
        val adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getCount(): Int = fragments.size

            override fun getItem(position: Int): Fragment = fragments[position]

            override fun getPageTitle(position: Int): CharSequence = titles[position]
        }
        viewPager!!.adapter = adapter
        tabs!!.setupWithViewPager(viewPager)
        tabs!!.setTabsFromPagerAdapter(adapter)
        tabs!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                showToast(tab.text!!.toString())
//                tab.select();
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
//        tabs.tabMode = TabLayout.MODE_SCROLLABLE;
    }
}