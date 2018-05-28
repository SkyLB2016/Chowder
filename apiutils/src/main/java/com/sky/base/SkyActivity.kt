package com.sky.base

import android.os.Bundle
import android.support.annotation.CheckResult
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sky.api.IBaseView
import com.sky.utils.ToastUtils
import com.sky.widget.BaseTitle
import com.sky.widget.DialogManager

/**
 * activity 的基类
 * Created by SKY on 2017/5/27.
 */
abstract class SkyActivity : AppCompatActivity(), IBaseView {
    lateinit var baseTitle: BaseTitle//公共标题
    private var dialogManager: DialogManager? = null//公共弹窗

//    protected fun <T : View> getView(id: Int): T = findViewById<View>(id) as T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        baseTitle = BaseTitle(this)
    }

    /**
     * 获取布局的resId
     */
    @CheckResult
    protected abstract fun getLayoutResId(): Int

    override fun setToolbarTitle(title: String, rightText: String) {
        baseTitle.setCenterTitle(title)
        baseTitle.setRightText(rightText)
    }

    override fun setToolbarTitle(title: String) = baseTitle.setCenterTitle(title)
    override fun setToolbarRightTitle(rightText: String) = baseTitle.setRightText(rightText)
    override fun showToast(@StringRes resId: Int) = ToastUtils.showShort(this, resId)
    override fun showToast(text: String) = ToastUtils.showShort(this, text)
    override fun showLoading() {
        dialogManager = dialogManager ?: DialogManager(this)
        dialogManager?.showDialog(this)
    }

    override fun disLoading() {
        dialogManager?.disDialog()
    }

    fun registerOnClick(vararg views: View) {
        for (v in views) registerView(v)
    }

    open fun registerView(v: View) = Unit
}
