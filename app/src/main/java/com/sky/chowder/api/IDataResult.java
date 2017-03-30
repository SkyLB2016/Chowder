package com.sky.chowder.api;


/**
 * Created by sky on 15/12/9 下午8:54.
 * 网络请求时使用的接口
 */
public interface IDataResult<T> {
    void onFailure(Exception exception, int Code);

    /**
     * 可加载progress
     *
     * @param total
     * @param current
     * @param isUploading
     */
    void onLoading(long total, long current, boolean isUploading);

    /**
     * 根据请求返回data
     *
     * @param data
     */
    void onSuccessData(T data);

    void onStart();

    void onCancel();

    void onFinish();
}
