package com.sky.base;

import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.sky.api.IBaseView;
import com.sky.utils.ToastUtils;
import com.sky.widget.BaseTitle;
import com.sky.widget.DialogManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * activity 的基类
 * Created by SKY on 2017/5/27.
 */
public abstract class SkyActivity extends AppCompatActivity implements IBaseView {

    protected BaseTitle baseTitle;//公共标题
    protected DialogManager dialogManager;//公共弹窗
    protected Unbinder unbinder;

    protected <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        unbinder = ButterKnife.bind(this);
        baseTitle = new BaseTitle(this);
        //在应用内时，屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void setToolbarTitle(@NonNull String title, @NonNull String rightText) {
        baseTitle.setCenterTitle(title);
        baseTitle.setRightText(rightText);
    }

    public void setToolbarTitle(@NonNull String title) {
        baseTitle.setCenterTitle(title);
    }

    @Override
    public void setToolbarRightTitle(@NonNull String rightText) {
        baseTitle.setRightText(rightText);
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
    public void showLoading() {
        if (dialogManager == null) dialogManager = new DialogManager(this);
        dialogManager.showDialog(this);
    }

    @Override
    public void disLoading() {
        if (dialogManager != null)
            dialogManager.disDialog();
    }

    @Override
    public void sendMainMessage(int mainhandler) {

    }

}
