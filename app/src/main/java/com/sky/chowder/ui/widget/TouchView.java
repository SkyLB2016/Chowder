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

/**
 * Created by SKY on 2018/2/24 14:49.
 */
public class TouchView extends View {
    private final static int INVALID_ID = -1;
    private int mActivePointerId = INVALID_ID;
    private int mSecondaryPointerId = INVALID_ID;
    private float mPrimaryLastX = -1;
    private float mPrimaryLastY = -1;
    private float mSecondaryLastX = -1;
    private float mSecondaryLastY = -1;

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
                int index = event.getActionIndex();
                mActivePointerId = event.getPointerId(index);
                mPrimaryLastX = MotionEventCompat.getX(event, index);
                mPrimaryLastY = MotionEventCompat.getY(event, index);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                index = event.getActionIndex();
                mSecondaryPointerId = event.getPointerId(index);
                mSecondaryLastX = event.getX(index);
                mSecondaryLastY = event.getY(index);
                break;
            case MotionEvent.ACTION_MOVE:
                index = event.findPointerIndex(mActivePointerId);
                int secondaryIndex = MotionEventCompat.findPointerIndex(event, mSecondaryPointerId);
                final float x = MotionEventCompat.getX(event, index);
                final float y = MotionEventCompat.getY(event, index);
//                final float secondX = MotionEventCompat.getX(event, secondaryIndex);
//                final float secondY = MotionEventCompat.getY(event, secondaryIndex);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //xxxxxx(涉及pointer id的转换，之后的文章会讲解)
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_ID;
                mPrimaryLastX = -1;
                mPrimaryLastY = -1;
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Color.BLUE);
        if (mPrimaryLastX != -1)
            canvas.drawCircle(mPrimaryLastX, mPrimaryLastY, 88, paint);
        if (mSecondaryLastX != -1)
            canvas.drawCircle(mSecondaryLastX, mSecondaryLastY, 88, paint);
    }
}
