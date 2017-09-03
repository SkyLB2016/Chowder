package com.sky.chowder.utils.http;

import com.sky.chowder.model.LoginEntity;
import com.sky.chowder.model.params.LoginParams;
import com.sky.http.BaseHttp;
import com.sky.model.ObjectEntity;

import io.reactivex.Observable;

/**
 * Created by SKY on 2017/6/1.
 * 网络请求类
 */
public class HttpUtils extends BaseHttp{
    private static HttpUtils utils;

    public static HttpUtils getInstance() {
        if (utils == null)
            synchronized (HttpUtils.class) {
                if (utils == null) utils = new HttpUtils();
            }
        return utils;
    }

    private HttpUtils() {
        initClient();
        initRetrofit();
    }
    public Observable<ObjectEntity<LoginEntity>> login(LoginParams params) {
        return retrofit.create(IHttpUrl.class).login(params);
    }


}
