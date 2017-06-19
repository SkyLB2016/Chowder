package com.glimmer.carrybport.ui.activity;

import android.view.View;

import com.glimmer.carrybport.R;
import com.glimmer.carrybport.api.view.IMainView;
import com.glimmer.carrybport.base.BasePActivity;
import com.glimmer.carrybport.ui.presenter.MainPresenter;

public class MainActivity extends BasePActivity<MainPresenter> implements IMainView {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initialize() {
        //隐藏左侧图标
        baseTitle.setLeftButton(-1);
//        new UseCase<Object>(){
//            @Override
//            protected Observable<Object> buildObservable() {
//                return null;
//            }
//        }.subscribe(new OnRequestCallback<Object>() {
//            @Override
//            public void onFail(ErrorMes error) {
//
//            }
//
//            @Override
//            public void onSuccess(Object o) {
//
//            }
//        });
        showDialog();
        dismissDialog();
    }

    @Override
    protected void creatPresenter() {
        presenter = new MainPresenter(this);
    }

    public void tvonclick(View v) {
        presenter.test();
    }

    public void tvonclick2(View v) {
        presenter.test1();
    }
}