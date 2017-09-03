package com.sky.demo.api;

import com.sky.demo.BuildConfig;
import com.sky.demo.ui.activity.ErrorLogActivity;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;

public abstract class RequestCallBack<ResultType> implements Callback.CommonCallback<ResultType> {
    private final IDataResult resultCB;

    public RequestCallBack(IDataResult callback) {
        this.resultCB = callback;
    }

    @Override
    public abstract void onSuccess(ResultType result);

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        if (ex instanceof HttpException) { // 网络错误
            HttpException e = (HttpException) ex;
            int responseCode = e.getCode();
            String responseMsg = e.getMessage();
            String errorResult = e.getResult();
            resultCB.onFailure(e, e.getCode());
            if (BuildConfig.DEBUG)
                ErrorLogActivity.startThis(ex.getMessage() + "");
        } else if (ex instanceof NullPointerException) {
            
        }
    }

    @Override
    public void onCancelled(CancelledException cex) {
        resultCB.onCancel();
    }

    @Override
    public void onFinished() {
        resultCB.onFinish();
    }
}
