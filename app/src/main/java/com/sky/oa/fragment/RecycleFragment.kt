package com.sky.oa.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sky.design.app.BaseFragment
import com.sky.oa.R
import com.sky.oa.adapter.CollapsingAdapter
import com.sky.oa.presenter.BaseP
import kotlinx.android.synthetic.main.include_recycler.*

/**
 * Created by SKY on 2015/11/28 14:52.
 */
class RecycleFragment : BaseFragment<BaseP>() {
    override fun getLayoutResId(): Int = R.layout.include_recycler
    override fun creatPresenter() =BaseP(activity!!)

    override fun initialize(savedInstanceState: Bundle?) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recycler.layoutManager = LinearLayoutManager(recycler.context)
        recycler.adapter = CollapsingAdapter(R.layout.item_recycle_card_main)
    }
}
