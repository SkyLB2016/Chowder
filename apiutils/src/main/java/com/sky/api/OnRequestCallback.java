package com.sky.api;

import com.sky.ErrorMes;

/**
 * Created by sky on 16/5/10 下午3:50.
 * 请求回掉接口
 *
 * @param <T> 泛型类
 */
public interface OnRequestCallback<T> {
    void onFail(ErrorMes error);

    void onSuccess(T data);
}