package com.sky.chowder.utils.http


import com.sky.chowder.model.CourseEntity
import com.sky.chowder.model.DataEntity
import com.sky.chowder.model.LoginEntity
import com.sky.chowder.model.params.LoginParams
import com.sky.model.ObjectEntity
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming

/**
 * Created by SKY on 2017/6/7.
 * retrofit的接口类
 */
interface IHttpUrl {
    @Streaming
    @POST(HttpUrl.URL_LOGIN)
    fun login(@Body loginParams: LoginParams): Observable<ObjectEntity<LoginEntity>>

    @GET(HttpUrl.URL_MUKE1)
    fun getMuKe(): Observable<DataEntity<CourseEntity>>

    //    @GET(HttpUrl.checkUpload)
    //    Observable<ObjectEntity<Object>> checkUpload();
}
