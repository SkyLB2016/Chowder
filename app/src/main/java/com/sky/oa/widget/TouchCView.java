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
public class TouchCView extends TextView {
    public TouchCView(Context context) {
        super(context);
    }

    public TouchCView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchCView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
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
        return super.dispatchHoverEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.i("C的touch的ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.i("C的touch的ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.i("C的touch的ACTION_UP");
                break;
        }
        return super.onTouchEvent(event);
    }
}
