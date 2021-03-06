package com.sky.oa.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import androidx.annotation.Nullable;

import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.sky.oa.R;
import com.sky.sdk.utils.LogUtils;

/**
 * 合同进度条
 * Created by libin on 2019/4/29 上午10:41.
 */
public class Progress extends View {

    private int layoutWidth = 80;//控件宽
    private int layoutHeight = 80;//控件高
    private Paint paint;

    private int circleLineIn = 4;//内线的宽度
    private int circleInColor;

    private int circleLineOut = 6;//外线的宽度
    private int circleOutColor;

    private float startAngle = -90;//起始角度
    private float sweepAngle;//需要旋转的角度
    private int progress;

    public Progress(Context context) {
        this(context, null);
    }

    public Progress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Progress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);

        circleLineIn = getResources().getDimensionPixelSize(R.dimen.wh_4);
        circleLineOut = getResources().getDimensionPixelSize(R.dimen.wh_6);
        circleInColor = getResources().getColor(R.color.color_D4D4D4);
        circleOutColor = getResources().getColor(R.color.red);
    }

    public void setLayoutWidth(int layoutWidth) {
        this.layoutWidth = layoutWidth;
    }

    public void setLayoutHeight(int layoutHeight) {
        this.layoutHeight = layoutHeight;
    }

    public void setCircleOutColor(int circleOutColor) {
        this.circleOutColor = circleOutColor;
    }

    /**
     * 0-100
     *
     * @param progress
     */
    public void setProgress(int progress) {
        this.progress = progress;
        this.sweepAngle = progress / 100f * 360f;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(layoutWidth, layoutHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (layoutHeight == 0 || layoutWidth == 0) {
            return;
        }
        int centerX = layoutWidth / 2;
        int centerY = layoutHeight / 2;

        paint.setStyle(Paint.Style.STROKE);
        //底层圆的半径
        int radius = centerX - circleLineOut / 2;
        paint.setColor(circleInColor);
        paint.setStrokeWidth(circleLineIn);
        canvas.drawCircle(centerX, centerY, radius, paint);

        //当前的进度
        paint.setColor(circleOutColor);
        paint.setStrokeWidth(circleLineOut);
        RectF rect = new RectF(circleLineOut / 2, circleLineOut / 2, layoutWidth - circleLineOut / 2, layoutHeight - circleLineOut / 2);
        canvas.drawArc(rect, startAngle, sweepAngle, false, paint);

        //起始位置的圆
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(layoutWidth / 2, circleLineOut / 2, circleLineOut / 2, paint);

        //计算重点位置的圆弧度
        double radians = (sweepAngle + startAngle) / 360.0 * Math.PI * 2;
        double ix = radius * Math.cos(radians);
        double iy = radius * Math.sin(radians);
        canvas.drawCircle(centerX + Float.parseFloat(ix + ""), centerY + Float.parseFloat(iy + ""), circleLineOut / 2, paint);

//        String text = progress + "%";

//        SpannableStringBuilder span = new SpannableStringBuilder();
//        span.append(progress + "");
//        span.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_18)), 0, span.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        span.append("%");
//        span.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.text_2)), span.length() - 3, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        String text = progress + "%";
        TextPaint textP = new TextPaint();
        textP.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_14));
        textP.setTextAlign(Paint.Align.LEFT);
        textP.setTypeface(Typeface.DEFAULT_BOLD);
        Rect textBound = new Rect();
        textP.getTextBounds(text, 0, text.length(), textBound);

        //计算的文字所在的背景框的左侧，顶部，右侧，底部
        int leftX = centerX - textBound.width() / 2;
        int topY = centerY - textBound.height() / 2;
        int rightX = leftX + textBound.width();
        int bottomY = topY + textBound.height();
        //文字背景所在矩形
        Rect textRect = new Rect(leftX, topY, rightX, bottomY);
//        Drawable textbg = ContextCompat.getDrawable(getContext(), R.drawable.sel_rect_green);
//        textbg.setBounds(textRect);//为文字设置背景
//        textbg.draw(canvas);//画入画布中
        //让文字居于背景中间，计算文字的左距离与底部距离
        float baseline = textRect.exactCenterY() + textBound.height() / 2 - textP.getFontMetricsInt().descent;
        canvas.drawText(text, 0, text.length() - 1, textRect.left, baseline, textP);//画入画布中

        LogUtils.i(textRect.centerY() + "XY" + textRect.exactCenterY());
        LogUtils.i("top==" + textP.getFontMetricsInt().top);
        LogUtils.i("ascent==" + textP.getFontMetricsInt().ascent);
        LogUtils.i("descent==" + textP.getFontMetricsInt().descent);
        LogUtils.i("bottom==" + textP.getFontMetricsInt().bottom);

        String textEnd = "%";
        Rect textBoundEnd = new Rect();
        textP.getTextBounds(textEnd, 0, textEnd.length(), textBoundEnd);
        textP.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_8));
        canvas.drawText("%", textRect.right - textBoundEnd.width() + 4, baseline, textP);

    }
}
