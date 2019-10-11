package com.sky.oa.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.sky.oa.R;

/**
 * ${description}
 *
 * @author wangdongliang
 * @data 19/3/13
 */
public class ProgressView extends View {
    private Paint mCircleOutPaint;
    private Paint mCircleInPaint;
    private Paint mLinePaint;

    private Paint mDefaltCircleOutPaint;
    private Paint mDefaltCircleInPaint;

    private Paint mTvPaint;
    private int circleOutRadius = 25;
    private int circleInRadius = 8;
    private int margin = 300; //左右margin，从2开始每次减少100，最小100

    private String[] texts = null;//总个数
    private int mCurrentLevel;//当前进度,从0开始

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        mCircleOutPaint = new Paint();
        mCircleOutPaint.setStyle(Paint.Style.FILL);
        mCircleOutPaint.setColor(getResources().getColor(R.color.color_40A5FF));
        mCircleOutPaint.setAntiAlias(true);

        mCircleInPaint = new Paint();
        mCircleInPaint.setStyle(Paint.Style.FILL);
        mCircleInPaint.setColor(Color.WHITE);
        mCircleInPaint.setAntiAlias(true);

        mLinePaint = new Paint();
        mLinePaint.setColor(getResources().getColor(R.color.color_ECEEF0));
        mLinePaint.setStrokeWidth(4);

        mDefaltCircleOutPaint = new Paint();
        mDefaltCircleOutPaint.setStyle(Paint.Style.FILL);
        mDefaltCircleOutPaint.setColor(getResources().getColor(R.color.color_858585));
        mDefaltCircleOutPaint.setAntiAlias(true);

        mDefaltCircleInPaint = new Paint();
        mDefaltCircleInPaint.setStyle(Paint.Style.FILL);
        mDefaltCircleInPaint.setColor(getResources().getColor(R.color.color_3599f4));
        mDefaltCircleInPaint.setAntiAlias(true);

        mTvPaint = new Paint();
        mTvPaint.setColor(getResources().getColor(R.color.color_C2C2C1));
        mTvPaint.setTextSize(sp2px(getContext(), 12));
    }

    /**
     * 设置数据，并初始化
     *
     * @param texts
     */
    public void setTexts(String[] texts) {
        this.texts = texts;
        margin = 300;
        mCurrentLevel = 0;
    }

    /**
     * 设置进度
     *
     * @param currentLevel 当前进度，从0开始
     */
    public void setProgress(int currentLevel) {
        mCurrentLevel = currentLevel;
    }

    public int getProgress() {
        return mCurrentLevel;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        getSuggestedMinimumWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
//        int realWidth = (width - 2 * margin - (2 * mTotalLevel) * circleOutRadius) / (mTotalLevel - 1);
        twoLevel(canvas, height, width);
    }

    /**
     * 二级进度
     *
     * @param canvas
     * @param height
     * @param width
     */
    private void twoLevel(Canvas canvas, int height, int width) {
        int total = texts.length;
        margin = margin - 100 * (total - 2);
        if (margin < 100) margin = 100;//

        int realWidth = (width - 2 * margin - total * 2 * circleOutRadius) / (total - 1);
        canvas.drawLine(margin + circleOutRadius * 2, height / 3, width - margin - circleInRadius * 2, height / 3, mLinePaint);
        for (int i = 0; i < total; i++) {
            canvas.drawCircle(margin + circleOutRadius * (i * 2 + 1) + realWidth * i, height / 3, circleInRadius, mDefaltCircleInPaint);
            canvas.drawText(texts[i], margin + circleOutRadius * (2 * i + 1) + realWidth * i - getTextWidth(texts[i]), height / 3 + 80, mTvPaint);
            if (i <= mCurrentLevel) {
                mTvPaint.setColor(getResources().getColor(R.color.color_3599f4));
                canvas.drawCircle(margin + circleOutRadius * (2 * i + 1) + realWidth * i, height / 3, circleOutRadius, mCircleOutPaint);
                canvas.drawCircle(margin + circleOutRadius * (2 * i + 1) + realWidth * i, height / 3, circleInRadius, mCircleInPaint);
                canvas.drawText(texts[i], margin + circleOutRadius * (2 * i + 1) + realWidth * i - getTextWidth(texts[i]), height / 3 + 80, mTvPaint);
                mTvPaint.setColor(getResources().getColor(R.color.color_C2C2C1));
            }
        }
    }

    public float getTextWidth(String text) {
        return mTvPaint.measureText(text) / 2;
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
