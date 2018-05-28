package com.sky.chowder.utils.http

import com.sky.chowder.api.OnRequestCallback
import common.model.ApiResponse

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by SKY on 2017/5/31.
 */
abstract class UseCase<T : ApiResponse<*>> {
    protected abstract fun buildObservable(): Observable<T>

    fun subscribe(request: OnRequestCallback<T>) {
        buildObservable()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DefaultSubScriber(request))
    }
}
