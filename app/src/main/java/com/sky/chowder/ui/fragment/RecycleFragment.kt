package com.sky.chowder.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.sky.base.BaseFragment
import com.sky.chowder.R
import com.sky.chowder.ui.adapter.CollapsingAdapter
import com.sky.chowder.ui.presenter.SolarP
import kotlinx.android.synthetic.main.include_recycler.*

/**
 * Created by SKY on 2015/11/28 14:52.
 */
class RecycleFragment : BaseFragment<SolarP>() {
    override fun getLayoutResId(): Int = R.layout.include_recycler
    override fun creatPresenter() =SolarP(activity!!)

    override fun initialize(savedInstanceState: Bundle?) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recycler.layoutManager = LinearLayoutManager(recycler.context)
        recycler.adapter = CollapsingAdapter(R.layout.item_recycle_card_main)
    }
}
