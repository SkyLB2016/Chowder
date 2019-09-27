package com.sky.chowder.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sky.chowder.R;
import com.sky.chowder.model.PCommon;

import java.util.ArrayList;
import java.util.List;

/**
 * 员工柱状图，司龄，年龄，学历
 * Created by libin on 2019/08/30 16:47 Friday.
 */
public class WorkerColumnView extends View {

    private int width;
    private int height;
    private Paint paint;
    private List<PCommon> data = new ArrayList<>();//员工集合
    private int color = getResources().getColor(R.color.color_7D91E2);

    private int LinePad = getResources().getDimensionPixelOffset(R.dimen.wh_14);//横线X的起始位置
    private int columnPad = getResources().getDimensionPixelOffset(R.dimen.wh_19);//柱状图X的起始位置
    private int columnWidth = getResources().getDimensionPixelOffset(R.dimen.wh_12);//柱状图的宽

    //坐标系计算，7条线，分六份，每份的大小
    private int number = 6;//每个柱状图默认分成多少份
    private int scale = 0;//柱状图每份的数量，也就是刻度的数量
    private int scaleHeight;//柱状图竖直方向上每份的高度
    private int interval;//横向方向上柱状图的间隔的宽度

    public WorkerColumnView(Context context) {
        this(context, null);
    }

    public WorkerColumnView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WorkerColumnView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(layoutWidth, layoutWidth / 5 * 3);
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setData(List<PCommon> data) {
        this.data = data;
        int max = data.get(0).getValue();
        for (int i = 1; i < data.size(); i++) {
            max = Math.max(max, data.get(i).getValue());
        }
        if (max % number == 0) {//没余值
            scale = max / number;
        } else {//有余值，需加1
            scale = max / number + 1;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getResources().getColor(R.color.white));
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        //默认分number份
        scaleHeight = height / number;
        int length = data.size();
        //算柱状图的的间隔
        interval = (width - columnPad * 2 - columnWidth * length) / (length - 1);

        paint.setColor(getResources().getColor(R.color.color_E6E6E6));
        paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.wh_1));
        canvas.drawLine(LinePad, scaleHeight * 6, width - LinePad, scaleHeight * 6, paint);

        float leftR;
        float rightR;
        float topR;
        float bottomR = scaleHeight * number;//所有柱状图的底线都是第7条线，也可以直接等于高度height
        int value;
        float valueHeight;
        RectF rectF;
        paint.setColor(color);
        for (int i = 0; i < length; i++) {
            value = data.get(i).getValue();
            valueHeight = 1.0f * value / scale * scaleHeight;

            leftR = columnPad + (columnWidth + interval) * i;//左距离等于左侧间隔+ 每份宽度与间隔之和*位置
            rightR = leftR + columnWidth;
            topR = bottomR - valueHeight;
            rectF = new RectF(leftR, topR, rightR, bottomR);
            canvas.drawRect(rectF, paint);
        }
    }
}
