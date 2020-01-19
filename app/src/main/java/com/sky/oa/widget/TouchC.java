package com.sky.oa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sky.sdk.utils.LogUtils;

/**
 * Created by libin on 2019/12/17 17:56 Tuesday.
 */
public class TouchC extends TextView {
    public TouchC(Context context) {
        super(context);
    }

    public TouchC(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchC(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.i("C的dispatch的ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.i("C的dispatch的ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.i("C的dispatch的ACTION_UP");
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.i("C的touch的ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.i("C的touch的ACTION_MOVE");
                return true;
//            break;
            case MotionEvent.ACTION_UP:
                LogUtils.i("C的touch的ACTION_UP");
                break;
        }
        return super.onTouchEvent(event);
    }
}
