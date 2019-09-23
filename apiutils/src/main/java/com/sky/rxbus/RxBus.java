package com.sky.rxbus;


import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Set;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * Created by SKY on 2017/6/9.
 */
public class RxBus {
    private final FlowableProcessor<Object> mBus;
    private HashMap<Object, Disposable> map = new HashMap<>();

    private RxBus() {
        mBus = PublishProcessor.create().toSerialized();
    }

    private static class Holder {
        private static RxBus instance = new RxBus();
    }

    public static RxBus getInstance() {
        return Holder.instance;
    }

    public <T> Flowable<T> register(Class<T> clz) {
        return mBus.ofType(clz);
    }

    public void add(Object o, Disposable subscribe) {
        map.put(o, subscribe);
    }

    public void send(@NonNull int code) {
        mBus.onNext(new DefaultBus(code, new DefaultBus()));
    }

    public <T> void send(@NonNull int code, @NonNull T obj) {
        mBus.onNext(new DefaultBus(code, obj));
    }

    public void unregister(Object o) {
        Disposable disposable = map.get(o);
        if (disposable != null)
            disposable.dispose();
    }

    public void unregisterAll() {
        Set<Object> objects = map.keySet();
        for (Object object : objects) {
            map.get(object).dispose();
        }
        //解除注册
        mBus.onComplete();
    }

    public boolean hasSubscribers() {
        return mBus.hasSubscribers();
    }
}
