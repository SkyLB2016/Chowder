package com.sky.chowder.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.sky.chowder.R;

/**
 * Created by SKY on 15/3/26 下午3:50.
 * 密码输入框
 */
public class PassWordView extends androidx.appcompat.widget.AppCompatEditText {
    int with;
    int height;
    private Paint textPaint;//字体的画笔
    private Paint whitePaint;//白背景
    int everyX;
    Rect mTextBound = new Rect();

    public PassWordView(Context context) {
        this(context, null);
    }

    public PassWordView(Context context, AttributeSet attrs) {

        this(context, attrs, R.attr.editTextStyle);
    }

    public PassWordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textPaint = new Paint();
        textPaint.setTextSize(getResources().getDimension(R.dimen.text_14));
        textPaint.setAntiAlias(true);

        whitePaint = new Paint();
        whitePaint.setColor(getResources().getColor(android.R.color.white));
        whitePaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        with = getMeasuredWidth();
        height = getMeasuredHeight();
        everyX = (with - 50) / 6;
//        setMeasuredDimension(width, everyX);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, with, height, whitePaint);
        String text = getText().toString();
        String[] s = text.split("");
        textPaint.setStrokeWidth(getResources().getDimension(R.dimen.wh_2));
        textPaint.setColor(getResources().getColor(R.color.color_e6e6e6));
        for (int i = 0; i < 6; i++) {
            canvas.drawLine(everyX * i + 10 * i, height, everyX * (i + 1) + 10 * i, height, textPaint);
        }
        textPaint.setColor(getResources().getColor(R.color.color_323232));
        textPaint.getTextBounds(".", 0, 1, mTextBound);//测量文字所占空间大小
        int left = (everyX - mTextBound.width()) / 2;
        int bottom = height - 30;
        for (int i = 1; i < s.length; i++) {
//            int left = (everyX - mTextBound.width()) / 2;
//            int bottom = (everyX + mTextBound.height()) / 2;
            int l = (everyX + 10) * (i - 1) + left;
            canvas.drawText(s[i], l, bottom, textPaint);
        }
    }
}
