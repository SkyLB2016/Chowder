package com.sky.chowder.utils.http

import com.sky.chowder.model.LoginEntity
import com.sky.chowder.model.params.LoginParams
import com.sky.http.BaseHttp
import com.sky.model.ObjectEntity

import io.reactivex.Observable

/**
 * Created by SKY on 2017/6/1.
 * 网络请求类
 */
class HttpUtils private constructor() : BaseHttp() {

    init {
        initClient()
        initRetrofit()
    }

    fun login(params: LoginParams): Observable<ObjectEntity<LoginEntity>> {
        return retrofit.create(IHttpUrl::class.java).login(params)
    }

    companion object {
        private var utils: HttpUtils? = null

        val instance: HttpUtils
            get() {
                if (utils == null)
                    synchronized(HttpUtils::class.java) {
                        if (utils == null) utils = HttpUtils()
                    }
                return utils!!
            }
    }


}
