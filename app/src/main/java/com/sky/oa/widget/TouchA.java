package com.sky.oa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.sky.sdk.utils.LogUtils;

/**
 * Created by libin on 2019/12/17 17:56 Tuesday.
 */
public class TouchA extends LinearLayout {
    public TouchA(Context context) {
        super(context);
    }

    public TouchA(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchA(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.i("A的dis==ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.i("A的dis==ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.i("A的dis==ACTION_UP");
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.i("A的拦截==ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.i("A的拦截==ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.i("A的拦截==ACTION_UP");
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.i("A的touch==ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.i("A的touch==ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.i("A的touch==ACTION_UP");
                break;
        }
        return super.onTouchEvent(event);
    }
}
