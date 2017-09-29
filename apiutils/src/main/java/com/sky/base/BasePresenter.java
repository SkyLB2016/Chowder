package com.sky.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.sky.Common;
import com.sky.R;
import com.sky.api.IBasePresenter;
import com.sky.api.IBaseView;
import com.sky.rxbus.DefaultBus;
import com.sky.utils.NetworkUtils;
import com.sky.utils.SPUtils;

import java.io.Serializable;

import io.reactivex.functions.Consumer;

/**
 * Created by SKY on 2017/8/24.
 */
public abstract class BasePresenter<V extends IBaseView> implements IBasePresenter {
    protected Context context;
    protected V mView;

    public BasePresenter(Context context) {
        this(context, (V) context);
    }

    //fragment时直接使用此构造器
    public BasePresenter(Context context, V view) {
        this.context = context;
        mView = view;
        setRxBus();
    }

    private void setRxBus() {
        Common.getRxBus().add(this,
                Common.getRxBus().register(DefaultBus.class)
                        .subscribe(new Consumer<DefaultBus>() {
                            @Override
                            public void accept(DefaultBus o) throws Exception {
                                onReceiveEvent(o);
                            }
                        }));
        if (!hasInternetConnected()) mView.showToast(R.string.toast_isinternet);
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
        Common.getRxBus().unregister(this);
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
        Common.getRxBus().send(code);
    }

    @Override
    public <T> void sendEvent(int code, T event) {
        Common.getRxBus().send(code, event);
    }

    @Override
    public void onReceiveEvent(DefaultBus event) {

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
    public String getToken() {
        return getObject(Common.TOKEN, "");
    }

    @Override
    public boolean hasInternetConnected() {
        return NetworkUtils.isConnected(context);
    }

    @Override
    public String getStringArray(int array, int position) {
        return context.getResources().getStringArray(array)[position];
    }

    @Override
    public String getString(@StringRes int resId) {
        return context.getString(resId);
    }
}