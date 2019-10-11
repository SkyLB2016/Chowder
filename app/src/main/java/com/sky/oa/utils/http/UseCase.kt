package com.sky.oa.utils.http

import com.sky.oa.api.OnRequestCallback

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by SKY on 2017/5/31.
 */
abstract class UseCase<T : com.sky.sdk.net.http.ApiResponse<*>> {
    protected abstract fun buildObservable(): Observable<T>

    fun subscribe(request: OnRequestCallback<T>) {
        buildObservable()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DefaultSubScriber(request))
    }
}
