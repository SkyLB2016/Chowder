package com.glimmer.carrybport.utils.rox;


import com.glimmer.carrybport.MyApplication;
import com.sky.Common;
import com.sky.ErrorMes;
import com.sky.api.OnRequestCallback;
import com.sky.model.BaseEntity;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DefaultObserver;

/**
 * Created by SKY on 2017/5/29.
 */
public class DefaultSubScriber<T extends BaseEntity> extends DefaultObserver<T> {
    OnRequestCallback<T> request;

    public DefaultSubScriber(OnRequestCallback<T> request) {
        this.request = request;
    }

    @Override
    public void onNext(@NonNull T data) {
        if (request == null) {
//            mRequestCallback.onFail(new ErrBundle(Constants.NET_REQUEST_ERR, "回调参数不能为空"));
        } else if (data == null) {
            request.onFail(new ErrorMes(Common.NET_REQUEST_ERR, "数据获取错误"));
        } else if (data.isSuccess()) {
            request.onSuccess(data);
        } else if (Common.LOGIN == data.getCode()) {
            MyApplication.getInstance().getRxBus().send(Common.LOGIN);
        } else
            request.onFail(new ErrorMes(data.getCode(), data.getMsg()));
//        cancel();

    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (request != null)
            request.onFail(new ErrorMes(Common.NET_REQUEST_NOT_FOUND, e.getMessage()));
    }

    @Override
    public void onComplete() {

    }
}
