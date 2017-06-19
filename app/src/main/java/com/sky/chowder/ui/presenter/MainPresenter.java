package com.sky.chowder.ui.presenter;

import android.content.Context;

import com.sky.Common;
import com.sky.chowder.api.presenter.IMainPresenter;
import com.sky.chowder.ui.BasePresenter;

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
    public void test1() {
        sendEvent(1002);
    }

    @Override
    public void test() {
        sendEvent(Common.LOGIN);
    }
}
