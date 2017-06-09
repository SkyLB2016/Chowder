package com.glimmer.carrybport.utils.rox;


import com.glimmer.carrybport.model.LoginEntity;
import com.glimmer.carrybport.model.requestparams.LoginParams;
import com.sky.model.ObjectEntity;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

/**
 * Created by SKY on 2017/6/7.
 * retrofit的接口类
 */
public interface IHttpUrl {
    @Streaming
    @POST(HttpUrl.URL_LOGIN)
    Observable<ObjectEntity<LoginEntity>> login(@Body LoginParams loginParams);

//    @GET(HttpUrl.checkUpload)
//    Observable<ObjectEntity<Object>> checkUpload();
}
