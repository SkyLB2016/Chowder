package com.sky.chowder.ui.fragment

import android.os.Bundle
import android.view.View

import com.sky.base.BaseFragment
import com.sky.chowder.R
import com.sky.chowder.ui.presenter.MainPresenter

/**
 * Created by SKY on 2015/12/9 20:52.
 */
class One : BaseFragment<MainPresenter>() {

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            val mTitle = arguments.getString("title")
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_first
    }

    override fun initPresenter() {
presenter = MainPresenter(activity)
    }

    override fun initialize() {

    }
}
