package com.sky.design.app;

import android.os.Bundle;

import androidx.annotation.CheckResult;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.sky.design.api.IBaseView;
import com.sky.design.widget.BaseTitle;
import com.sky.design.widget.DialogManager;
import com.sky.sdk.utils.ToastUtils;

import org.jetbrains.annotations.Nullable;

/**
 * MVP的基类
 * Created by SKY on 2017/5/31.
 */
public abstract class BasePActivity<P extends BasePresenter> extends AppCompatActivity implements IBaseView {
    public BaseTitle baseTitle;
    public DialogManager dialogManager;
    protected P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        baseTitle = new BaseTitle(this);
        if (presenter == null) presenter = creatPresenter();
        initialize(savedInstanceState);//需要初始化的成员变量
        presenter.onCreate(savedInstanceState);//请求数据
    }

    protected abstract void initialize(@Nullable Bundle savedInstanceState);

    /**
     * 获取布局的resId
     */
    @CheckResult
    protected abstract int getLayoutResId();

    @Override
    public void setToolbarTitle(String title, String rightText) {
        baseTitle.setCenterTitle(title);
        baseTitle.setRightText(rightText);
    }

    @Override
    public void setToolbarTitle(String title) {
        baseTitle.setCenterTitle(title);
    }

    @Override
    public void setToolbarRightTitle(String rightText) {
        baseTitle.setRightText(rightText);
    }

    @Override
    public void showToast(@StringRes int resId) {
        ToastUtils.showShort(this, resId);
    }

    @Override
    public void showToast(String text) {
        ToastUtils.showShort(this, text);
    }

    @Override
    public void showLoading() {
        dialogManager = dialogManager == null ? new DialogManager(this) : dialogManager;
        dialogManager.showDialog(this);
    }

    @Override
    public void disLoading() {
        dialogManager.disDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    protected abstract P creatPresenter();
}
