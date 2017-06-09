package com.glimmer.carrybport.api.presenter;

import com.sky.api.IBasePresenter;

/**
 * Created by sky on 2017/6/7.
 */
public interface ILoginP extends IBasePresenter{

    void login(String name ,String pwd);
    void register();
    void forget();
}
