package com.sky.chowder.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sky.utils.LogUtils;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;


/**
 * Created by 李彬 on 2017/3/10.
 */

public class RxJAVAActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(new DrawView(this));
        observer();
    }

    private void observer() {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                LogUtils.i("observer==" + "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.i("observer==" + "onError==" + e.getMessage());

            }

            @Override
            public void onNext(String s) {
                LogUtils.i("observer==" + "onNext==" + s);

            }
        };
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                LogUtils.i("subscriber==" + "onCompleted");

            }

            @Override
            public void onError(Throwable e) {
                LogUtils.i("subscriber==" + "onError==" + e.getMessage());

            }

            @Override
            public void onNext(String s) {
                LogUtils.i("subscriber==" + "onNext==" + s);

            }
        };

        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("第一个");
                subscriber.onNext("第二个");
                subscriber.onNext("第三个");
                subscriber.onCompleted();
            }
        });
        observable.subscribe(observer);
        observable.subscribe(subscriber);


        Action1<String> onNextAction = new Action1<String>() {
            // onNext()
            @Override
            public void call(String s) {
                LogUtils.i("onNextAction==" + s);
            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {
                // Error handling
                LogUtils.i("onErrorAction==");
            }
        };
        Action0 onCompletedAction = new Action0() {
            // onCompleted()
            @Override
            public void call() {
                LogUtils.i("onCompletedAction==");
            }
        };

// 自动创建 Subscriber ，并使用 onNextAction 来定义 onNext()
        observable.subscribe(onNextAction);
// 自动创建 Subscriber ，并使用 onNextAction 和 onErrorAction 来定义 onNext() 和 onError()
        observable.subscribe(onNextAction, onErrorAction);
// 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
    }
}
