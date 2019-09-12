package com.sky.chowder.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sky.chowder.R;
import com.sky.chowder.model.WorkerTypeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 性别,结婚比例
 * Created by libin on 2019/4/29 上午10:41.
 */
public class WorkerCircleView extends View {

    private Paint paint;
    private float startAngle = -90;//起始角度,12点位置
    private float sweepAngle;//需要旋转的角度，
    private int padding = getResources().getDimensionPixelOffset(R.dimen.wh_20);//外圆半径

    List<WorkerTypeModel> data = new ArrayList<>();

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

    private int maxPerson = 0;

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(layoutWidth, layoutWidth);
    }

    /**
     * 0-100
     *
     * @param progress
     */
    public void setProgress(int progress) {
        maxPerson = 0;
        data.clear();
        data.add(new WorkerTypeModel("未婚", 10));
        data.add(new WorkerTypeModel("未婚", 15));
        data.add(new WorkerTypeModel("未婚", 5));
        data.add(new WorkerTypeModel("未婚", 20));
        for (int i = 0; i < data.size(); i++) {
            maxPerson += data.get(i).getValue();
        }
//        postInvalidate();
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

//        paint.setColor(getResources().getColor(R.color.color_b1bdee));
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(android.R.color.white));
        canvas.drawCircle(centerX, centerY, radiusOut, paint);
//        canvas.drawCircle(centerX, centerY, radiusIn, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);
        //弧线的画笔中心在矩形的边边上，所以矩形大小应该在向内缩进半个圆线的宽度
        float offset = padding + lineWidth / 2;
        RectF rect = new RectF(offset, offset, width - offset, width - offset);
        for (int i = 0; i < data.size(); i++) {
            if (i % 2 == 0) {
                paint.setColor(getResources().getColor(R.color.color_7d91e2));
            } else {
                paint.setColor(getResources().getColor(R.color.color_b1bdee));
            }

            int value = data.get(i).getValue();
            sweepAngle = value * 1.0f / maxPerson * 360;
            canvas.drawArc(rect, startAngle, sweepAngle, false, paint);
            startAngle += sweepAngle;
        }
    }
}
