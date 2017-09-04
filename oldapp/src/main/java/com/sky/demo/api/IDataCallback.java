package com.sky.demo.api;

public interface IDataCallback<T> { //我们一样传入通用类型
    void onNewData(T data);

    void onError(String msg, int code);
}