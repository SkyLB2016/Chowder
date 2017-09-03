package com.sky.demo.ui.widget.pending;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 机械革命 on 2016/8/20.
 */

public class Ball extends View {
    private float centerX = 55;
    private float centerY = 66;
    private Paint paint = new Paint();

    public Ball(Context context) {
        super(context);
    }

    public Ball(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.GREEN);
        canvas.drawCircle(centerX, centerY, 66, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        centerX = event.getX();
        centerY = event.getY();
        invalidate();
        return true;
    }
}
