package com.sky.demo.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.sky.demo.R;

/**
 * @author sky QQ:1136096189
 * @Description:  融资进度
 * @date 15/12/3 上午11:24
 */
public class CircleProgress extends View {
    private RectF area;//控件所占矩形
    private int radius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            100, getResources().getDisplayMetrics());//

    private Paint totalPaint;//总进度条画笔
    private Paint progressPaint;//进度条画笔
    private int totalColor = Color.BLACK;//总进度条颜色
    private int progressColor = Color.RED;//进度条颜色
    private int progressWidth = 5;//进度条宽度

    private Paint textPaint;//字体画笔
    private Rect textBound;//字体所占空间
    private String mText;
    private int textColor = Color.BLACK;//字体颜色
    private int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
            14, getResources().getDisplayMetrics());//字体大小
    private Drawable textbackground;//文字的背景

    private float value = 50;//进度条所占百分比
    private float startAngle = 140;//起始角度
    private float sweepAngle = 260;//旋转角度

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    public float getSweepAngle() {
        return sweepAngle;
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getTotalColor() {
        return totalColor;
    }

    public void setTotalColor(int totalColor) {
        this.totalColor = totalColor;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public int getProgressWidth() {
        return progressWidth;
    }

    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public Drawable getTextbackground() {
        return textbackground;
    }

    public void setTextbackground(Drawable textbackground) {
        this.textbackground = textbackground;
    }

    public float getValue() {
        return value;
    }

    /**
     * 按百分比计算
     *
     * @param value
     */
    public void setValue(float value) {
        this.value = value;
        invalidate();
    }

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray style = context.obtainStyledAttributes(attrs, R.styleable.CircleProgress);

        int count = style.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = style.getIndex(i);
            switch (attr) {
                case R.styleable.CircleProgress_radius:
                    radius = style.getDimensionPixelSize(attr, radius);
                    break;
                case R.styleable.CircleProgress_total_color:
                    totalColor = style.getColor(attr, totalColor);
                    break;
                case R.styleable.CircleProgress_pro_color:
                    progressColor = style.getColor(attr, progressColor);
                    break;
                case R.styleable.CircleProgress_pro_width:
                    progressWidth = style.getDimensionPixelSize(attr, progressWidth);
                    break;
                case R.styleable.CircleProgress_text:
                    mText = style.getString(attr);
                    break;
                case R.styleable.CircleProgress_textsize:
                    textSize = style.getDimensionPixelSize(attr, textSize);
                    break;
                case R.styleable.CircleProgress_textcolor:
                    textColor = style.getColor(attr, textColor);
                    break;
                case R.styleable.CircleProgress_text_background:
                    textbackground = style.getDrawable(attr);
                    break;
            }
        }
        style.recycle();

        setTextPaint();
        setProgressPaint();
    }

    private void setProgressPaint() {
        totalPaint = new Paint();
        totalPaint.setColor(totalColor);
        totalPaint.setStrokeWidth(progressWidth);
        totalPaint.setStyle(Paint.Style.STROKE);
        totalPaint.setAntiAlias(true);
        progressPaint = new Paint();
        progressPaint.setColor(progressColor);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setAntiAlias(true);
        LinearGradient shader = new LinearGradient(0, 0, 400, 0, new int[]{
                progressColor, Color.WHITE}, null,
                Shader.TileMode.CLAMP);
        progressPaint.setShader(shader);

    }

    /**
     * 设置画笔，并测量文件所占空间
     */
    private void setTextPaint() {
        textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        textBound = new Rect();
        textPaint.getTextBounds(mText, 0, mText.length(), textBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int areaWidth = radius * 2;//area所占空间
        areaWidth = Math.min(areaWidth, getMeasuredWidth());
        areaWidth = Math.min(areaWidth, getMeasuredHeight());

        radius = areaWidth / 2;
        int left = getMeasuredWidth() / 2 - radius;
        int top = getMeasuredHeight() / 2 - radius;
        area = new RectF(left, top, left + areaWidth, top + areaWidth);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画出底层进度条
        canvas.drawArc(area, startAngle, sweepAngle, false, totalPaint);
        //按百分比画出进度条
        canvas.drawArc(area, startAngle, sweepAngle * value / 100, false, progressPaint);

        //开始计算文字所占的位置
        //计算出每份所占PI
        double angle = Math.PI / 180;
        double endAngle = (startAngle + sweepAngle) % 360;//求出终点
        //计算起始点的XY的位置
        float startX = (float) (radius * Math.cos(angle * startAngle));
        float startY = (float) (radius * Math.sin(angle * startAngle));
        //计算终点的XY的位置
        float endX = (float) (radius * Math.cos(angle * endAngle));
        float endY = (float) (radius * Math.sin(angle * endAngle));
        //获取圆心XY
        float centerX = area.centerX();
        float centerY = area.centerY();
        //计算能分配给文字所占的最大空间，高设为文字自身高的三倍
        float textBoundy = centerY + startY - textBound.height() * 3 / 2;
        Rect textRect = new Rect((int) (centerX + startX + 20), (int) textBoundy,
                (int) (centerX + endX - 20), (int) (textBoundy + textBound.height() * 3));
        textbackground.setBounds(textRect);//为文字设置背景
        textbackground.draw(canvas);//画入画布中
        //让文字居于背景中间，计算文字的左距离与底部距离
        int left = textRect.left + (textRect.width() - textBound.width()) / 2;
        int bottom = textRect.top + textRect.height() - textBound.height();
        canvas.drawText(mText, left, bottom, textPaint);//画入画布中

//        for (int i = 0; i < 24; i++) {
//            float startX = (float) (radius * Math.cos(angle * i));
//            float startY = (float) (radius * Math.sin(angle * i));
//            canvas.drawText("b", centerX + startX, centerY + startY, textPaint);
//        }
    }
}
