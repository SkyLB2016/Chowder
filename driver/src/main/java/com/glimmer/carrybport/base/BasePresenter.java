package com.glimmer.carrybport.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.glimmer.carrybport.ui.activity.LoginActivity;
import com.glimmer.carrybport.utils.rox.UseCase;
import com.sky.Common;
import com.sky.api.IBasePresenter;
import com.sky.api.IBaseView;
import com.sky.rxbus.RxBus;
import com.sky.rxbus.Subscribe;
import com.sky.utils.JumpAct;
import com.sky.utils.NetworkJudgment;
import com.sky.utils.SPUtils;

import java.io.Serializable;

/**
 * Created by SKY on 2017/5/27.
 */
public abstract class BasePresenter<V extends IBaseView> implements IBasePresenter {
    protected Context mContext;
    protected V mView;
    protected UseCase useCase;

    public BasePresenter(Context mContext) {
        this.mContext = mContext;
        mView = (V) mContext;
        RxBus.getInstance().register(mContext);
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
        RxBus.getInstance().unRegister(mContext);
    }
    public void onDetach() {

    }

    public abstract void loadData();

    @Override
    public Serializable getExtras() {
        Bundle extras = ((AppCompatActivity) mContext).getIntent().getExtras();
        if (extras != null) {
            return extras.getSerializable(Common.EXTRA);
        }
        return null;
    }

    @Override
    public void sendEvent(int code) {
        RxBus.getInstance().send(code);
    }

    @Override
    public void sendEvent(int code, Object event) {
        RxBus.getInstance().send(code, event);
    }

    @Subscribe(code = Common.LOGIN)
    public void receiveToLogin() {
        JumpAct.jumpActivity(mContext, LoginActivity.class);
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
        return NetworkJudgment.isConnected(mContext);
    }


}
