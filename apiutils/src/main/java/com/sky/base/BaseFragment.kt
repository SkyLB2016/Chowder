package com.sky.base

import android.os.Bundle
import android.support.annotation.CheckResult
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baidu.mobstat.StatService
import com.sky.api.IBaseView

/**
 * Created by SKY on 2017/6/7.
 */
abstract class BaseFragment<P : BasePresenter<*>> : Fragment(), IBaseView {
    protected var presenter: P? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (presenter == null) presenter = creatPresenter()
        checkPresenterIsNull()
        presenter?.onCreateView(savedInstanceState)
        return inflater.inflate(getLayoutResId(), null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        presenter?.onViewCreate(savedInstanceState)
    }

    /**
     * 获取布局的resId
     */
    @CheckResult
    protected abstract fun getLayoutResId(): Int

    /**
     * 初始化presenter
     */
    protected abstract fun creatPresenter(): P

    /**
     * 初始化
     */
    protected abstract fun initialize()

    /**
     * 检查mPresenter是否为空
     */
    protected fun checkPresenterIsNull() {
        if (presenter == null) {
            throw IllegalStateException("please init presenter at creatPresenter() method...")
        }
    }

    override fun setToolbarTitle(title: String, rightText: String) {
        (activity as SkyActivity).setToolbarTitle(title, rightText)
    }

    override fun setToolbarTitle(title: String) {
        (activity as SkyActivity).setToolbarTitle(title)
    }

    override fun setToolbarRightTitle(rightText: String) {
        (activity as SkyActivity).setToolbarRightTitle(rightText)
    }

    override fun showToast(@StringRes resId: Int) {
        (activity as SkyActivity).showToast(resId)
    }

    /**
     * 显示信息提示条
     *
     * @param text 提示信息字符串
     */
    override fun showToast(text: String) {
        (activity as SkyActivity).showToast(text)
    }

    override fun showLoading() {
        (activity as SkyActivity).showLoading()
    }

    override fun disLoading() {
        (activity as SkyActivity).disLoading()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter?.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        presenter?.onStart()
    }

    override fun onResume() {
        super.onResume()
        // 页面埋点
        StatService.onPageStart(activity, javaClass.name)
        presenter?.onResume()
    }

    override fun onPause() {
        super.onPause()
        // 页面埋点
        StatService.onPageEnd(activity, javaClass.name)
        presenter?.onPause()
    }

    override fun onStop() {
        super.onStop()
        presenter?.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
        presenter?.onDetach()
    }

    override fun finish() {
        if (activity != null)
            activity?.finish()
    }
    fun registerOnClick(vararg views: View) {
        for (v in views) registerView(v)
    }

    open fun registerView(v: View) = Unit
}
