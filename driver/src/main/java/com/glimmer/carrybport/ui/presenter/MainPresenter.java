package com.glimmer.carrybport.ui.presenter;

import android.content.Context;

import com.glimmer.carrybport.api.presenter.IMainPresenter;
import com.glimmer.carrybport.base.BasePresenter;
import com.sky.Common;
import com.sky.rxbus.DefaultBus;
import com.sky.utils.LogUtils;

/**
 * Created by SKY on 2017/5/29.
 */
public class MainPresenter extends BasePresenter implements IMainPresenter {

    public MainPresenter(Context mContext) {
        super(mContext);
    }

    @Override
    public void loadData() {
    }

    @Override
    public void receiveEvent(DefaultBus event) {
        super.receiveEvent(event);
//        JumpAct.jumpActivity(mContext, LoginActivity.class);
        LogUtils.i("MainPresenter");
    }

    @Override
    public void test1() {
        sendEvent(1002);
    }

    @Override
    public void test() {
        sendEvent(Common.LOGIN);
    }
}
