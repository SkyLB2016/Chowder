package com.sky.chowder.utils;


import com.google.gson.JsonSyntaxException;
import com.sky.BuildConfig;
import com.sky.Common;
import com.sky.ErrorMes;
import com.sky.SkyApp;
import com.sky.api.OnRequestCallback;
import com.sky.model.ApiResponse;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DefaultObserver;
import retrofit2.HttpException;

/**
 * Created by SKY on 2017/5/29.
 */
public class DefaultSubScriber<T extends ApiResponse> extends DefaultObserver<T> {
    OnRequestCallback<T> request;

    public DefaultSubScriber(OnRequestCallback<T> request) {
        this.request = request;
    }

    @Override
    public void onNext(@NonNull T data) {
        if (request == null) return;
        if (data == null) request.onFail(new ErrorMes(Common.NET_NULL, Common.NET_EMPTY));
        else if (data.isSuccess()) request.onSuccess(data);
        else if (data.getStatus()) request.onSuccess(data);
        else request.onFail(new ErrorMes(data.getCode(), data.getMsg()));
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (request == null) return;
        String error = "网络故障";
        if (e instanceof JsonSyntaxException) error = "服务器数据格式异常";
        else if (e instanceof HttpException) error = e.getMessage();

        request.onFail(new ErrorMes(Common.NET_NOT_FOUND, BuildConfig.DEBUG ? e.getMessage() : error));
        if (BuildConfig.DEBUG) SkyApp.getInstance().showToast(error);
    }

    @Override
    public void onComplete() {

    }
}
