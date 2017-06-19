package com.sky.chowder.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.sky.Common;
import com.sky.api.IBasePresenter;
import com.sky.api.IBaseView;
import com.sky.chowder.MyApplication;
import com.sky.chowder.R;
import com.sky.chowder.utils.ActJump;
import com.sky.chowder.utils.rox.UseCase;
import com.sky.rxbus.DefaultBus;
import com.sky.utils.NetworkJudgment;
import com.sky.utils.SPUtils;

import java.io.Serializable;

import io.reactivex.functions.Consumer;

/**
 * Created by SKY on 2017/5/27.
 */
public abstract class BasePresenter<V extends IBaseView> implements IBasePresenter {
    protected Context context;
    protected V mView;
    protected UseCase useCase;

    public BasePresenter(Context context) {
        this.context = context;
        mView = (V) context;
        MyApplication.getInstance().getRxBus().add(this,
                MyApplication.getInstance().getRxBus().register(DefaultBus.class)
                        .subscribe(new Consumer<DefaultBus>() {
                            @Override
                            public void accept(DefaultBus o) throws Exception {
                                onReceiveEvent(o);
                            }
                        }));
        if (!hasInternetConnected()) mView.showToast(context.getString(R.string.toast_isinternet));
    }


    public void onActivityCreated(Bundle savedInstanceState) {

    }

    public void onCreate(Bundle savedInstanceState) {
        loadData();
    }

    public void onCreateView(Bundle savedInstanceState) {
        loadData();
    }

    public void onStart() {
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onStop() {
    }

    public void onDestroyView() {
    }

    public void onDestroy() {
        MyApplication.getInstance().getRxBus().unregister(this);
    }

    public void onDetach() {

    }

    public abstract void loadData();

    @Override
    public Serializable getExtras() {
        Bundle extras = ((AppCompatActivity) context).getIntent().getExtras();
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
        if (event.getCode()==Common.LOGIN)  toLoginActivity();
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
        return NetworkJudgment.isConnected(context);
    }

    public boolean toLoginActivity() {
        if (!getUsertOnline()) {
            ActJump.toLoginActivity(context);
            return true;
        }
        return false;
    }

}
