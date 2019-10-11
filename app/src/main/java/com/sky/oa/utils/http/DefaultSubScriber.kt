package com.sky.oa.utils.http

import com.google.gson.JsonSyntaxException
import com.sky.BuildConfig
import com.sky.oa.AppConstants
import com.sky.oa.api.OnRequestCallback

import io.reactivex.annotations.NonNull
import io.reactivex.observers.DefaultObserver
import retrofit2.HttpException

/**
 * Created by SKY on 2017/5/29.
 */
class DefaultSubScriber<T : com.sky.sdk.net.http.ApiResponse<*>>(private var request: OnRequestCallback<T>?) :
    DefaultObserver<T>() {

    override fun onNext(@NonNull data: T) {
        request ?: return
        when {
            data == null -> request?.onFail(AppConstants.NET_NULL, AppConstants.NET_EMPTY)
            data.isSuccess -> request?.onSuccess(data)
            data.status -> request?.onSuccess(data)
            else -> request?.onFail(data.code, data.msg)
        }
    }

    override fun onError(@NonNull e: Throwable) {
        request ?: return
        var error = when {
            BuildConfig.DEBUG -> e.message
            e is JsonSyntaxException -> "服务器数据格式异常"
            e is HttpException -> e.message ?: "服务器错误信息异常"
            else -> "网络故障"
        }
        request?.onFail(AppConstants.NET_NOT_FOUND, error)
    }

    override fun onComplete() {

    }
}
