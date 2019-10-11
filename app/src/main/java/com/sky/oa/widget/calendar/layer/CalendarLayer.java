package com.sky.oa.widget.calendar.layer;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.sky.oa.widget.calendar.common.CalendarInfo;

public interface CalendarLayer {
    Rect getBorderRect();

    CalendarInfo getModeInfo();

    void onDraw(Canvas canvas);

    boolean onTouchEvent(MotionEvent event);

    void scrollBy(int dx, int dy);
}