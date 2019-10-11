package com.sky.oa.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.sky.oa.R
import com.sky.oa.fragment.*
import com.sky.design.app.BaseActivity
import kotlinx.android.synthetic.main.activity_tab_vp.*
import java.util.*

/**
 * Created by SKY on 2015/8/31 14：25.
 * 标签栏
 */
class TabLayoutActivity : BaseActivity() {

    override fun getLayoutResId(): Int = R.layout.activity_tab_vp

    override fun initialize(savedInstanceState: Bundle?) {
        setUpViewPager()
//        val mParams = findViewById<AppBarLayout>(R.id.appbar).getChildAt(0).layoutParams as AppBarLayout.LayoutParams
//        mParams.scrollFlags = 0//的时候AppBarLayout下的toolbar就不会随着滚动条折叠
        //        mParams.setScrollFlags(5); //的时候AppBarLayout下的toolbar会随着滚动条折叠
    }

    override fun loadData() = Unit

    private fun setUpViewPager() {
        val tabs = findViewById<TabLayout>(R.id.tabs)
        tabs.visibility = View.VISIBLE
        val titles = ArrayList<String>()
        titles.add("card")
        titles.add("MeshView")
//        titles.add("Shader")
//        titles.add("XferModeView")
//        titles.add("通讯录")
        for (i in titles) {
            tabs.addTab(tabs.newTab().setText(i))
        }
        val fragments = ArrayList<Fragment>()
        fragments.add(RecycleFragment())
        fragments.add(MeshFragment())
//        fragments.add(ShaderFragment())
//        fragments.add(XferModeFragment())
//        fragments.add(CursorLoaderListFragment())
        val adapter = object : FragmentPagerAdapter(supportFragmentManager, 1) {
            override fun getCount(): Int = fragments.size

            override fun getItem(position: Int): Fragment = fragments[position]

            override fun getPageTitle(position: Int): CharSequence = titles[position]
        }
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
//        tabs.setTabsFromPagerAdapter(adapter)
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                showToast(tab.text.toString())
//                tab.select();
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
//        tabs.tabMode = TabLayout.MODE_SCROLLABLE;
    }
}