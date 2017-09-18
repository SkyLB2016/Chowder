package com.sky.chowder.ui.fragment


import com.sky.base.BaseFragment
import com.sky.chowder.R
import com.sky.chowder.ui.presenter.SolarP

/**
 * Created by SKY on 2015/12/9 20:52.
 */
class Two : BaseFragment<SolarP>() {
    override fun getLayoutResId(): Int {
        return R.layout.fragment_two
    }

    override fun creatPresenter() = SolarP(activity)

    override fun initialize() {

    }
}
