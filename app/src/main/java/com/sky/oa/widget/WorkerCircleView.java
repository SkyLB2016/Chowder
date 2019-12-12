package com.sky.oa.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sky.oa.R;
import com.sky.oa.model.PCommon;

import java.util.ArrayList;
import java.util.List;

/**
 * 性别,结婚比例
 * Created by libin on 2019/4/29 上午10:41.
 */
public class WorkerCircleView extends View {

    private Paint paint;
    private Paint textP;
    private float startAngle = -90;//起始角度,12点位置
    private float sweepAngle;//需要旋转的角度，
    private int padding = getResources().getDimensionPixelOffset(R.dimen.wh_20);//外圆半径
    private List<Integer> colors;

    private List<PCommon> data = new ArrayList<>();
    private int maxPerson = 0;
    private List<Float> angles = new ArrayList<>();
    private int touchIndex = -1;//点击的位置

    public WorkerCircleView(Context context) {
        this(context, null);
    }

    public WorkerCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WorkerCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textP = new Paint();
        colors = new ArrayList<>();
        colors.add(R.color.color_7D91E2);
        colors.add(R.color.color_49F2D4);
        colors.add(R.color.color_35BEF8);
        colors.add(R.color.color_C3EBFF);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(layoutWidth, layoutWidth);
    }

    public void setData(List<PCommon> data) {
        this.data = data;
        maxPerson = 0;
        for (int i = 0; i < data.size(); i++) {
            maxPerson += data.get(i).getValue();
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int centerX = width / 2;
        int centerY = height / 2;

        int radiusOut = (width - padding * 2) / 2;//外圆半径
        int radiusIn = radiusOut / 2;//内圆半径
        int lineWidth = radiusIn;//圆线的半径

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(android.R.color.white));
        canvas.drawCircle(centerX, centerY, radiusOut, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);
        //弧线的画笔中心在矩形的边边上，所以矩形大小应该在向内缩进半个圆线的宽度
        float offset = padding + lineWidth / 2;
        RectF rect = new RectF(offset, offset, width - offset, width - offset);
        angles.clear();
        startAngle = -90;
        for (int i = 0; i < data.size(); i++) {
            paint.setColor(getResources().getColor(colors.get(i % 4)));//对4取余，省的越界了

            int value = data.get(i).getValue();
            sweepAngle = value * 1.0f / maxPerson * 360;
            canvas.drawArc(rect, startAngle, sweepAngle, false, paint);
            startAngle += sweepAngle;
            angles.add(startAngle);
        }
        drawMark(canvas);
    }

    private void drawMark(Canvas canvas) {
        if (touchIndex == -1) return;
        textP.reset();
        textP.setFlags(Paint.ANTI_ALIAS_FLAG);
        textP.setTextSize(getResources().getDimension(R.dimen.text_10));
        textP.setColor(getResources().getColor(R.color.color_333333));
        textP.setTextAlign(Paint.Align.LEFT);//左对齐

        //矩形背景
        Rect rectBounds = new Rect(10, 10, 10 + getWidth() / 2, 10 + getWidth() / 6);
        Drawable rectBg = getResources().getDrawable(R.drawable.rect_2_cc313131);
        rectBg.setBounds(rectBounds);
        rectBg.draw(canvas);//划出矩形背景
        //绘制文字，先计算三行文字所占高度，
        Paint.FontMetrics metrics = textP.getFontMetrics();
        float textHeight = metrics.descent - metrics.ascent;
        float baseline = rectBounds.top + rectBounds.height() / 2 + textHeight / 2 - metrics.descent;                                          //计算baseline

        int leftX = rectBounds.width() / 4;                                                                 //文字X的起始位置，默认设为矩形宽的四分之一
        textP.setColor(getResources().getColor(R.color.color_C8C8C8));
        canvas.drawText(data.get(touchIndex).getName(), leftX, baseline, textP);
        //具体人数
        textP.setTextSize(getResources().getDimension(R.dimen.text_12));
        textP.setColor(getResources().getColor(android.R.color.white));
        textP.setTextAlign(Paint.Align.RIGHT);//右对齐
        leftX = rectBounds.width() - 10;
        canvas.drawText(data.get(touchIndex).getValue() + "", leftX, baseline, textP);
    }

    /**
     * 获得两点之间的距离
     */
    public float getDistanceBetween2Points(int startX, int startY, float stopX, float stopY) {
        float distance = (float) Math.sqrt(Math.pow(startX - stopX, 2) + Math.pow(startY - stopY, 2));
        return distance;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();

                int width = getMeasuredWidth();
                int height = getMeasuredHeight();
                int centerX = width / 2;
                int centerY = height / 2;
                int radiusOut = (width - padding * 2) / 2;//外圆半径
                int radiusIn = radiusOut / 2;//内圆半径

                float distance = getDistanceBetween2Points(centerX, centerY, x, y);
                if (radiusIn - 10 <= distance && distance <= radiusOut + 10) {
                    float tempX = x - centerX;
                    float tempY = y - centerY;
                    double clickAngle;
                    if (tempX >= 0) {
                        clickAngle = Math.atan(tempY / tempX);
                    } else {
                        clickAngle = Math.atan(tempY / tempX) + Math.PI;
                    }
                    clickAngle = clickAngle / Math.PI * 180;
                    for (int i = 0; i < angles.size(); i++) {
                        if (clickAngle < angles.get(i)) {
                            touchIndex = i;
                            if (handler.hasMessages(1)) {
                                handler.removeMessages(1);
                            }
                            break;
                        }
                    }
                    invalidate();
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                touchIndex = -1;
                invalidate();
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (touchIndex != -1) {
                    handler.sendEmptyMessageDelayed(1, 3000);
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
