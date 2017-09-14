package com.sky.chowder.utils.http

import com.google.gson.JsonSyntaxException
import com.sky.BuildConfig
import com.sky.Common
import com.sky.ErrorMes
import com.sky.SkyApp
import com.sky.api.OnRequestCallback
import com.sky.model.ApiResponse

import io.reactivex.annotations.NonNull
import io.reactivex.observers.DefaultObserver
import retrofit2.HttpException

/**
 * Created by SKY on 2017/5/29.
 */
class DefaultSubScriber<T : ApiResponse<*>>(private var request: OnRequestCallback<T>?) : DefaultObserver<T>() {

    override fun onNext(@NonNull data: T) {
        if (request == null) return
        when {
            data == null -> request!!.onFail(ErrorMes(Common.NET_NULL, Common.NET_EMPTY))
            data.isSuccess -> request!!.onSuccess(data)
            data.status -> request!!.onSuccess(data)
            else -> request!!.onFail(ErrorMes(data.code, data.msg))
        }
    }

    override fun onError(@NonNull e: Throwable) {
        if (request == null) return
        var error = when {
            BuildConfig.DEBUG -> e.message
            e is JsonSyntaxException -> "服务器数据格式异常"
            e is HttpException -> e.message!!
            else -> "网络故障"
        }
        request!!.onFail(ErrorMes(Common.NET_NOT_FOUND,  error))
        if (BuildConfig.DEBUG) SkyApp.getInstance().showToast(error)
    }
    override fun onComplete() {

    }
}
