package com.sky.chowder.ui;


import android.os.Bundle;
import android.text.TextUtils;

import com.sky.Common;
import com.sky.api.IBasePresenter;
import com.sky.chowder.MyApplication;
import com.sky.chowder.R;
import com.sky.chowder.utils.ActJump;
import com.sky.rxbus.DefaultBus;
import com.sky.utils.NetworkJudgment;
import com.sky.utils.SPUtils;

import java.io.Serializable;

import io.reactivex.functions.Consumer;

/**
 * activity 的基类
 * Created by SKY on 2017/5/27.
 */
public abstract class BaseNoPActivity extends BaseActivity implements IBasePresenter {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().getRxBus().add(this,
                MyApplication.getInstance().getRxBus().register(DefaultBus.class)
                        .subscribe(new Consumer<DefaultBus>() {
                            @Override
                            public void accept(DefaultBus o) throws Exception {
                                onReceiveEvent(o);
                            }
                        }));
        if (!hasInternetConnected()) showToast(getString(R.string.toast_isinternet));
        initialize();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().getRxBus().unregister(this);
    }

    @Override
    public Serializable getExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return extras.getSerializable(Common.EXTRA);
        }
        return null;
    }

    @Override
    public void sendEvent(int code) {
        MyApplication.getInstance().getRxBus().send(code);
    }

    @Override
    public <T> void sendEvent(int code, T event) {
        MyApplication.getInstance().getRxBus().send(code, event);
    }

    @Override
    public void onReceiveEvent(DefaultBus event) {
        if (event.getCode()== Common.LOGIN)  toLoginActivity();
    }

    @Override
    public <T> T getObject(String text, T value) {
        return (T) SPUtils.getInstance().get(text, value);
    }

    @Override
    public <T> void setObject(String text, T value) {
        SPUtils.getInstance().put(text, value);
    }

    @Override
    public boolean getUsertOnline() {
        return !TextUtils.isEmpty(getToken());
    }

    @Override
    public String getPhone() {
        return getObject(Common.PHONE, "");
    }

    @Override
    public String getToken() {
        return getObject(Common.TOKEN, "");
    }

    @Override
    public boolean hasInternetConnected() {
        return NetworkJudgment.isConnected(this);
    }

    public boolean toLoginActivity() {
        if (!getUsertOnline()) {
            ActJump.toLoginActivity(this);
            return true;
        }
        return false;
    }
}