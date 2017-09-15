package com.sky.api;

import java.util.List;

/**
 * Created by SKY on 2017/8/22.
 */
public interface IRefreshV<T> extends IBaseView {
    void setRefreshing(boolean isrefresh);

    void setSwipeEnable(boolean isrefresh);

    void setAdapterList(List<T> entities);

    void addAdapterList(List<T> entities);
}