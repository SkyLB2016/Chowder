package com.sky.chowder.ui.activity

import android.support.design.widget.Snackbar
import android.view.View
import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import kotlinx.android.synthetic.main.activity_detail.*

/**
 * Created by SKY on 15/11/28 下午2:22.
 */
class DetailActivity : BaseNoPActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_detail

    override fun initialize() {
        toolbarv7.setNavigationIcon(R.mipmap.ic_back)
        setSupportActionBar(toolbarv7)
        toolbarv7.setNavigationOnClickListener { finish() }

        collapsing.title = "我的课程"
    }

    fun checkin(view: View) =
            Snackbar.make(view, "checkin success!", Snackbar.LENGTH_SHORT).show()
}