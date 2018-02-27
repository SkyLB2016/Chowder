package com.sky.chowder.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sky.utils.LogUtils;

/**
 * Created by SKY on 2018/2/24 14:49.
 */
public class TouchView extends View {
    float lastX = -1, lastY = -1;
    float cX = -1, cY = -1;

    public TouchView(Context context) {
        super(context);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getRawX();
                lastY = event.getRawY();
                cX = event.getX();
                cY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (event.getRawX() - lastX);
                int dY = (int) (event.getRawY() - lastY);
                LogUtils.i("X=" + event.getRawX());

//                layout(getLeft() + dx, getTop() + dY, getRight() + dx, getBottom() + dY);
                offsetLeftAndRight(dx);
                offsetTopAndBottom(dY);
                cX = event.getX();
                cY = event.getY();
                lastX = event.getRawX();
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                lastX = -1;
                lastY = -1;
                cX = cY = -1;
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        LogUtils.i(getHeight() + "宽高" + getWidth());
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        if (lastX != -1) canvas.drawCircle(cX, cY, 88, paint);
    }
}
