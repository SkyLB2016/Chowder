package com.sky.api;

public interface OnLoadingRequest<T> extends OnRequestCallback<T> {
    void loadingDialog(T t);
}