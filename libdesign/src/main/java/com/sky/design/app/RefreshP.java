package com.sky.design.app;

import android.content.Context;

import com.sky.design.api.IRefreshP;
import com.sky.design.api.IRefreshV;

/**
 * Created by SKY on 2017/8/22.
 */
public abstract class RefreshP<V extends IRefreshV> extends BasePresenter<V> implements IRefreshP {
    protected int page = 1;
    protected int totalCount = 0;

    public RefreshP(Context context) {
        super(context);
    }

    public RefreshP(Context context, V view) {
        super(context, view);
    }

    @Override
    public void loadData() {
        page = 1;
        getPutExtra();
        getDataList();
    }

    @Override
    public void getPutExtra() {
    }

    @Override
    public void onRefresh() {
        page = 1;
        getDataList();
    }

    @Override
    public void onLoadMore(int ToitalItem) {
        if (totalCount <= ToitalItem) {
            mView.showToast("已无更多");
        } else {
            page++;
            getDataList();
        }
    }
}
