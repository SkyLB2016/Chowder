package com.sky.oa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.sky.sdk.utils.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
                LogUtils.i("B的dis==ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.i("B的dis==ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.i("B的dis==ACTION_UP");
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    float x = 0;
    float y = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = ev.getRawX();
                y = ev.getRawY();
                LogUtils.i("x=="+x);
                LogUtils.i("y=="+y);
                LogUtils.i("B的拦截==ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                move++;
                LogUtils.i("B的拦截==ACTION_MOVE");
//                if (move > 5)
//                    return true;
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.i("B的拦截==ACTION_UP");
//                if (move > 5)
//                    return false;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    int move = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getRawX();
                y = event.getRawY();
                LogUtils.i("x=="+x);
                LogUtils.i("y=="+y);
                LogUtils.i("B的touch==ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.i("B的touch==ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.i("B的touch==ACTION_UP");
                break;
        }
        return super.onTouchEvent(event);
    }
}
