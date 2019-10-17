package com.sky.oa.fragment

import android.os.Bundle
import android.view.View
import com.sky.design.app.BaseFragment
import com.sky.oa.R

/**
 * Created by SKY on 2015/12/9 20:52.
 */
class MeshPFragment : BaseFragment() {

    override fun getLayoutResId(): Int = R.layout.fragment_meshview

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view!!, savedInstanceState)
        if (arguments != null) {
            val mTitle = arguments?.getString("title")
        }
    }

}
