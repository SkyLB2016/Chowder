package com.sky.chowder.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sky.chowder.R;

/**
 * Created by libin on 2018/9/6 上午11:37.
 */
public class WorkProgress extends View {

    private double current = 60;//当前完成度
    private double total = 100;//正常总量
    private double beyond = 0;//超出

    private Paint paint;
    private float width = 0;//控件宽
    private float lineHeight = 0;//线宽
    private float radiusBig = 0;//大圆半径
    private float radiusSmall = 0;//小圆半径

    //中心，第一个中心点也是线的起始点
    private float centerX = 0;
    private float centerY = 0;

    private String textStart = "0%";
    private String textCurrent = "60%";
    private String textEnd = "100%";

    private int surplusColor = 0;


    public void setCurrent(double current) {
        this.current = current;
        textCurrent = current + "%";
        surplusColor = getResources().getColor(R.color.color_E6E6E6);
    }

    public void setBeyond(double beyond) {
//        this.beyond = beyond;
        current = total;
        total = beyond;

        textCurrent = "100%";
        textEnd = (int) total + "%";
        surplusColor = getResources().getColor(R.color.color_FAC36C);

    }

    public WorkProgress(Context context) {
        this(context, null);
    }

    public WorkProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WorkProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);

        setMeasuredDimension(layoutWidth, getResources().getDimensionPixelOffset(R.dimen.wh_48));
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        lineHeight = getResources().getDimension(R.dimen.wh_4);
        radiusBig = getResources().getDimension(R.dimen.wh_5half);
        radiusSmall = getResources().getDimension(R.dimen.wh_2half);

        surplusColor = getResources().getColor(R.color.color_E6E6E6);

        centerX = radiusBig;
        centerY = radiusBig;

        //如果要严格按照UI图来画view，则还需要计算起始文字的宽度的一半，与centerX作比，取大值。
//        paint.setTextSize(getResources().getDimension(R.dimen.text_12));
//        float start = paint.measureText(textStart);
//        float end = paint.measureText(textEnd);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getMeasuredWidth();
        float currentWidth = (float) (width * current / total);
        if (currentWidth < centerX) currentWidth += centerX;

        paint.setColor(getResources().getColor(R.color.color_1AB394));
        paint.setStrokeWidth(lineHeight);
        //当前任务线
        canvas.drawLine(centerY, centerY, currentWidth, centerY, paint);

        //剩余线
        paint.setColor(surplusColor);
        canvas.drawLine(currentWidth, centerY, width - centerX, centerY, paint);

        //最后一个圆与剩余线同色，先画，外圆
        canvas.drawCircle(width - centerX, centerY, radiusBig, paint);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(radiusSmall);
        //内圆
        canvas.drawCircle(width - centerX, centerY, radiusSmall, paint);

        //换色
        paint.setColor(getResources().getColor(R.color.color_1AB394));
        //起始圆，外圆
        canvas.drawCircle(centerX, centerY, radiusBig, paint);
        canvas.drawCircle(currentWidth, centerY, radiusBig, paint);

        //调整颜色，线宽
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(radiusSmall);
        //内圆
        canvas.drawCircle(centerX, centerY, radiusSmall, paint);
        canvas.drawCircle(currentWidth, centerY, radiusSmall, paint);

        //画虚线
        paint.setColor(getResources().getColor(R.color.color_CCCCCC));
        paint.setStrokeWidth(getResources().getDimension(R.dimen.wh_1));
        paint.setPathEffect(new DashPathEffect(new float[]{getResources().getDimension(R.dimen.wh_2), getResources().getDimension(R.dimen.wh_1)}, 0f));//虚线
        //虚线Y轴起始位置
        float dashStartY = centerY * 2 + radiusSmall;//没有为什么，我数着UI图坐标算得
        float dashEndY = dashStartY + getResources().getDimension(R.dimen.wh_12);
        setLayerType(LAYER_TYPE_SOFTWARE, null);//硬件加速，不然画不了虚线
        canvas.drawLine(centerX, dashStartY, centerX, dashEndY, paint);
        if (current != 0) {
            canvas.drawLine(currentWidth, dashStartY, currentWidth, dashEndY, paint);
        }
        canvas.drawLine(width - centerX, dashStartY, width - centerX, dashEndY, paint);

        //重制画笔
        paint.reset();

        paint.setTextSize(getResources().getDimension(R.dimen.text_12));
        paint.setColor(getResources().getColor(R.color.color_666666));

        //文字起始位置
        float textStartY = dashEndY + getResources().getDimension(R.dimen.wh_6);

//        float start = paint.measureText(textStart);
        canvas.drawText(textStart, 0, textStartY + paint.descent() - paint.ascent(), paint);
        if (current != 0) {
            float middle = paint.measureText(textCurrent);
            canvas.drawText(textCurrent, currentWidth - middle / 3 * 2, textStartY + paint.descent() - paint.ascent(), paint);
        }
        float end = paint.measureText(textEnd);
        canvas.drawText(textEnd, width - end, textStartY + paint.descent() - paint.ascent(), paint);
    }
}
