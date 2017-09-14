package com.sky.chowder.utils;

import com.sky.api.OnRequestCallback;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by SKY on 2017/5/31.
 */
public abstract class UseCase<T> {
    protected abstract Observable<T> buildObservable();

    public void subscribe(OnRequestCallback<T> request) {
        buildObservable()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubScriber(request));
    }
}
