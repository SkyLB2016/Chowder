package com.sky.api;

/**
 * Created by sky on 2017/6/30.
 */
public interface IRefreshP extends IBasePresenter {
    void getPutExtra();

    void onRefresh();

    void onLoadMore(int ToitalItem);

    void getDataList();
}