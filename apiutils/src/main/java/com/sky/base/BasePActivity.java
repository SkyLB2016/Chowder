package com.sky.base;

import android.os.Bundle;

/**
 * Created by SKY on 2017/5/31.
 */
public abstract class BasePActivity<P extends BasePresenter> extends SkyActivity {
    protected P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (presenter == null) presenter = creatPresenter();
        initialize();//需要初始化的成员变量
        presenter.onCreate(savedInstanceState);//请求数据
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
