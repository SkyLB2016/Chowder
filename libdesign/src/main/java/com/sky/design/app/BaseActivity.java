package com.sky.design.app;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.CheckResult;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.sky.R;
import com.sky.design.api.IBasePresenter;
import com.sky.design.widget.BaseTitle;
import com.sky.design.widget.DialogManager;
import com.sky.rxbus.DefaultBus;
import com.sky.rxbus.RxBus;
import com.sky.sdk.utils.NetworkUtils;
import com.sky.sdk.utils.SPUtils;
import com.sky.sdk.utils.ToastUtils;

import io.reactivex.functions.Consumer;

/**
 * activity 的基类
 * Created by SKY on 2017/5/27.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public BaseTitle baseTitle;
    public DialogManager dialogManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        baseTitle = new BaseTitle(this);
    }

    /**
     * 获取布局的resId
     */
    @CheckResult
    protected abstract int getLayoutResId();

    public void setToolbarTitle(String title, String rightText) {
        baseTitle.setCenterTitle(title);
        baseTitle.setRightText(rightText);
    }

    public void setToolbarTitle(String title) {
        baseTitle.setCenterTitle(title);
    }

    public void setToolbarRightTitle(String rightText) {
        baseTitle.setRightText(rightText);
    }

    public void showToast(@StringRes int resId) {
        ToastUtils.showShort(this, resId);
    }

    public void showToast(String text) {
        ToastUtils.showShort(this, text);
    }

    public void showLoading() {
        dialogManager = dialogManager == null ? new DialogManager(this) : dialogManager;
        dialogManager.showDialog(this);
    }

    public void disLoading() {
        dialogManager.disDialog();
    }

}