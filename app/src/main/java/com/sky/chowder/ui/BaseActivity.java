package com.sky.chowder.ui;


import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sky.api.IBaseView;
import com.sky.chowder.C;
import com.sky.utils.ToastUtils;
import com.sky.utils.UIHandler;
import com.sky.widget.DialogManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * activity 的基类
 * Created by SKY on 2017/5/27.
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseView {

    protected <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    protected BaseTitle baseTitle;//公共标题
    protected DialogManager dialogManager;//公共标题
    protected Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        unbinder = ButterKnife.bind(this);
        baseTitle = new BaseTitle(this);
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
    public void setToolbarRightTitle(@NonNull String rightText) {
        baseTitle.setRightImgId(rightText);
        baseTitle.setOnRightClick(new BaseTitle.OnClickListener() {
            @Override
            public void OnClick(View v) {
                onRightTextClick();
            }
        });
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



    //handler部分
    protected UIHandler handler = new UIHandler(Looper.getMainLooper());

    //设置handler监听
    private void setHandler() {
        handler.setHandler(new UIHandler.IHandler() {
            public void handleMessage(Message msg) {
                handler(msg);//有消息就提交给子类实现的方法
            }
        });
    }

    //让子类处理消息
    protected void handler(Message msg) {
        if (msg.what == C.handler_0x001)
            showToast(msg.getData().getString("data") + "地势坤，君子以厚德载物。");
    }
    //handler 完
}
