package com.sky.design.api;

import com.sky.design.api.IBaseView;

import java.util.List;

/**
 * Created by SKY on 2017/8/22.
 */
public interface IRefreshV<T> extends IBaseView {
    void setRefreshing(boolean isrefresh);

    void setSwipeEnable(boolean isrefresh);

    void setAdapterList(List<T> entities);

    void addAdapterList(List<T> entities);

    void addHintView();//无数据时的提示view

    void removeHintView();//移除view
}