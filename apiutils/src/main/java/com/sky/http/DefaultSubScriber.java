package com.sky.http;


import com.google.gson.JsonSyntaxException;
import com.sky.SkyApp;
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
        if (request == null) return;
//            mRequestCallback.onFail(new ErrBundle(Constants.NET_NULL, "回调参数不能为空"));
        if (data == null) {
            request.onFail(new ErrorMes(Common.NET_NULL, Common.NET_EMPTY));
        } else if (data.isSuccess()) {
            request.onSuccess(data);
        } else if (Common.LOGIN == data.getCode()) {
            Common.getRxBus().send(Common.LOGIN);
        } else {
            SkyApp.getInstance().showToast(data.getMsg());
            request.onFail(new ErrorMes(data.getCode(), data.getMsg()));
//        cancel();
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (request == null) return;
        String error = "网络故障";
        if (e instanceof JsonSyntaxException) error = "服务器数据格式异常";

        request.onFail(new ErrorMes(Common.NET_NOT_FOUND, Common.DEBUG
                ? e.getMessage()
                : error));
        if (Common.DEBUG)
            SkyApp.getInstance().showToast(error);
    }

    @Override
    public void onComplete() {

    }
}
