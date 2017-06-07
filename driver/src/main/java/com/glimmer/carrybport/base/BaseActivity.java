package com.glimmer.carrybport.base;


import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.glimmer.carrybport.ui.widget.BaseTitle;
import com.glimmer.carrybport.ui.widget.DialogManager;
import com.sky.api.IBaseView;
import com.sky.utils.ToastUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * activity 的基类
 * Created by SKY on 2017/5/27.
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseView {

    protected BaseTitle baseTitle;//公共标题
    protected Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        unbinder = ButterKnife.bind(this);
        baseTitle = new BaseTitle(this);
//        DialogManager.showDialog(this);
    }

    /**
     * 获取布局的resId
     */
    @CheckResult
    protected abstract int getLayoutResId();

    /**
     * 初始化
     */
    protected abstract void initialize();

    protected void onRightTextClick() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void setToolbarTitle(@NonNull String title, @NonNull String rightText) {
        baseTitle.setCenterTitle(title);
        baseTitle.setRightImgId(rightText);
        baseTitle.setOnRightClick(new BaseTitle.OnClickListener() {
            @Override
            public void OnClick(View v) {
                onRightTextClick();
            }
        });
    }

    public void setToolbarTitle(@NonNull String title) {
        baseTitle.setCenterTitle(title);
    }

    @Override
    public void showToast(@StringRes int resId) {
        ToastUtils.showShort(this, resId);
    }

    @Override
    public void showToast(@NonNull String text) {
        ToastUtils.showShort(this, text);
    }

    @Override
    public void showDialog() {
        DialogManager.showDialog(this);
    }

    @Override
    public void dismissDialog() {
        DialogManager.disDialog();
    }
}
