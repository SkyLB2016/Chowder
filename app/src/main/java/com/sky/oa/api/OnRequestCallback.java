package com.sky.oa.api;

/**
 * Created by SKY on 16/5/10 下午3:50.
 * 请求回掉接口
 *
 * @param <T> 泛型类
 */
public interface OnRequestCallback<T> {
    void onFail(String code, String message);

    void onSuccess(T data);
}