package com.sky.oa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.sky.sdk.utils.LogUtils;

/**
 * Created by libin on 2019/12/17 17:56 Tuesday.
 */
public class TouchB extends LinearLayout {
    public TouchB(Context context) {
        super(context);
    }

    public TouchB(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchB(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.i("B的dispatch的ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.i("B的dispatch的ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.i("B的dispatch的ACTION_UP");
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.i("B的intercept的ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.i("B的intercept的ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.i("B的intercept的ACTION_UP");
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.i("B的touch的ACTION_DOWN");
                return true;
//                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.i("B的touch的ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.i("B的touch的ACTION_UP");
                break;
        }
        return super.onTouchEvent(event);
    }
}
