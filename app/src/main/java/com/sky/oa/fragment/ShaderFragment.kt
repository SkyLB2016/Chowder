package com.sky.oa.fragment

import android.os.Bundle
import com.sky.design.app.BaseFragment
import com.sky.oa.R
import com.sky.oa.presenter.BaseP

/**
 * Created by SKY on 2015/12/9 20:52.
 */
class ShaderFragment : BaseFragment<BaseP>() {
    override fun getLayoutResId(): Int {
        return R.layout.fragment_two
    }

    override fun creatPresenter() = BaseP(activity!!)

    override fun initialize(savedInstanceState: Bundle?) {

    }
}
