package com.sky.design.app

import android.os.Bundle
import androidx.annotation.CheckResult
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sky.design.api.IBaseView
import com.sky.design.app.BasePresenter

/**
 * Created by SKY on 2017/6/7.
 */
abstract class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResId(), null, false)
    }

    /**
     * 获取布局的resId
     */
    @CheckResult
    protected abstract fun getLayoutResId(): Int

//    override fun setToolbarTitle(title: String, rightText: String) {
//        (activity as SkyActivity).setToolbarTitle(title, rightText)
//    }
//
//    override fun setToolbarTitle(title: String) {
//        (activity as SkyActivity).setToolbarTitle(title)
//    }
//
//    override fun setToolbarRightTitle(rightText: String) {
//        (activity as SkyActivity).setToolbarRightTitle(rightText)
//    }
//
//    override fun showToast(@StringRes resId: Int) {
//        (activity as SkyActivity).showToast(resId)
//    }
//
//    override fun showToast(text: String) {
//        (activity as SkyActivity).showToast(text)
//    }
//
//    override fun showLoading() {
//        (activity as SkyActivity).showLoading()
//    }
//
//    override fun disLoading() {
//        (activity as SkyActivity).disLoading()
//    }
}
