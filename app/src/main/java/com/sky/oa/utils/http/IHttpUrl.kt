package com.sky.oa.utils.http


import com.sky.oa.model.CourseEntity
import com.sky.oa.model.LoginEntity
import com.sky.oa.model.params.LoginParams
import com.sky.sdk.net.http.ApiResponse
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
    fun login(@Body loginParams: LoginParams): Observable<ApiResponse<LoginEntity>>

    @GET(HttpUrl.URL_MUKE1)
    fun getMuKe(): Observable<ApiResponse<CourseEntity>>

    //    @GET(HttpUrl.checkUpload)
    //    Observable<ObjectEntity<Object>> checkUpload();
}
