package com.sky.demo.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.sky.demo.R;

/**
 * @author LiBin
 * @ClassName: TabTextView
 * @Description: 自定义选项卡标签
 * @date 2015年3月26日 下午4:19:07
 */
public class TabTextView extends View {

    private Bitmap mIconBitmap;//图标
    private int mColor;//底色
    private String mText;//标题
    private int mTextColor = getResources().getColor(R.color.black);//字体颜色
    //字体大小
    private int mTextSize = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            getResources().getDimension(R.dimen.small),
            getResources().getDisplayMetrics());

    private Canvas mCanvas;//画布
    private Paint mPaint;//画笔
    private Bitmap mBitmap;//图像

    private float mAlpha = 0.0f;//透明度
    private Rect mIconRect;//图标所占空间大小
    private Rect mTextBound;//字体所占空间
    private Paint mTextPaint;//字体的画笔

    public TabTextView(Context context) {
        this(context, null);
    }

    public TabTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray style = context.obtainStyledAttributes(attrs, R.styleable.TabTextView);
        int count = style.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = style.getIndex(i);
            switch (attr) {
                case R.styleable.TabTextView_tab_icon:
                    BitmapDrawable drawable = (BitmapDrawable) style.getDrawable(attr);
                    mIconBitmap = drawable.getBitmap();
                    break;
                case R.styleable.TabTextView_tab_color:
                    mColor = style.getColor(attr, getResources().getColor(R.color.green));
                    break;
                case R.styleable.TabTextView_tab_text:
                    mText = (String) style.getText(attr);
                    break;
                case R.styleable.TabTextView_tab_textsize:
                    mTextSize = (int) style.getDimension(attr, TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP,
                            getResources().getDimension(R.dimen.small),
                            getResources().getDisplayMetrics()));
                    break;
                case R.styleable.TabTextView_tab_textcolor:
                    mTextColor = style.getColor(attr, getResources().getColor(R.color.black));

                    break;
            }
        }
        style.recycle();//回收

        mTextBound = new Rect();
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);//测量文字所占空间大小
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量icon所占空间大小
        int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - mTextBound.height());
        int left = (getMeasuredWidth() - iconWidth) / 2;
        int top = (getMeasuredHeight() - iconWidth - mTextBound.height()) / 2;
        mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制图标
        canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
        int alpha = (int) Math.ceil(mAlpha * 255);

        //内存去准备mBitmap，setAlpha，纯色，xfermode，图标
        setUpTargetBitmap(alpha);
        //绘制原文本，变色文本
        drawSourceText(canvas, alpha);
        drawTargetText(canvas, alpha);
        //覆盖图标
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    /**
     * 绘制变的文字
     *
     * @param canvas
     * @param alpha
     */
    private void drawTargetText(Canvas canvas, int alpha) {
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);
        int left = (getMeasuredWidth() - mTextBound.width()) / 2;
        int bottom = mIconRect.bottom + mTextBound.height();
        canvas.drawText(mText, left, bottom, mTextPaint);
    }

    /**
     * 绘制原文字
     *
     * @param canvas
     * @param alpha
     */
    private void drawSourceText(Canvas canvas, int alpha) {
        mTextPaint.setColor(mTextColor);
        mTextPaint.setAlpha(255 - alpha);
        int left = (getMeasuredWidth() - mTextBound.width()) / 2;
        int bottom = mIconRect.bottom + mTextBound.height();
        canvas.drawText(mText, left, bottom, mTextPaint);
    }

    /**
     * 绘制底色图标
     *
     * @param alpha
     */
    private void setUpTargetBitmap(int alpha) {
        //初始化图像，画布，画笔
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setAlpha(alpha);

        mCanvas.drawRect(mIconRect, mPaint);//dst层
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(255);
        mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
        mPaint.setXfermode(null);
    }

    /**
     * 设置透明度
     *
     * @param alpha
     */
    public void setIconAlpha(float alpha) {
        this.mAlpha = alpha;
        invalidateView();
    }

    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    //保存模式
    private static final String INSTANCE_STATE = "instance_state";
    private static final String STATE_ALPHA = "state_alpha";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putFloat(STATE_ALPHA, mAlpha);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mAlpha = bundle.getFloat(STATE_ALPHA);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
        } else {
            super.onRestoreInstanceState(state);
        }
    }
}
