package com.sky.oa.widget.calendar.layer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;

import com.sky.oa.R;
import com.sky.oa.widget.calendar.common.CalendarColor;
import com.sky.oa.widget.calendar.common.CalendarInfo;
import com.sky.oa.widget.calendar.common.CalendarMode;
import com.sky.oa.widget.calendar.impl.OnLayerClickListener;

import java.util.Calendar;
import java.util.Locale;

public class YearBarLayer implements CalendarLayer {

    private static final String YEAR = "年";
    private static final String MONTH = "月";
    private static final String TODAY = "今";
    private static final int MOTION_EVENT_DOWN_ID = 0;
    private static final int TOUCH_SLOP = 64;
    private static final int COL_COUNT = 7;

    private CalendarInfo mModeInfo;
    private Paint mPaint;
    private int mYear;                           // 当前的年
    private int mMonth;                          // 当前的月
    private CalendarMode mMode;
    private String mYearString;               // 年月的字符串
    private int mDownY, mDownX;                  // 手指按下的坐标
    private boolean mAlwaysInTapRegion = true;
    private boolean mIsShow;
    private boolean mTodayShow;

    private OnLayerClickListener mYearClick;
    private OnLayerClickListener mTodayClick;
    private OnLayerClickListener mLeftArrowClick;
    private OnLayerClickListener mRightArrowClick;

    private Rect mRect;              //允许的绘制区域
    private Rect mBorderRect;        //绘制的边界
    private Rect mYearRect;
    private Rect mTodayRect;
    private Rect mLeftArrowRect;
    private Rect mLeftBitmapArrowRect;
    private Rect mRightArrowRect;
    private Rect mRightBitmapArrowRect;

    private int mYearPadding = 10;                          //年与左右箭头的间距,单位 dp
    private int mYearAreaColor = CalendarColor.WHITE;       //年份导航条的背景色
    private int mYearTextColor = CalendarColor.TITLE_GRAY;  //年份的文字颜色
    private float mYearTextSize = 16;                       //年份的字体大小,单位 sp
    private float mYearTextHeight;                          //代码自动计算
    private float mYearTextAscent;                          //代码自动计算
    private float mYearItemWidth;                           //代码自动计算
    private int mTodayTextColor = CalendarColor.PRIMARY_COLOR;       //今的字体颜色
    private int mTodayBorderColor = CalendarColor.PRIMARY_COLOR;     //今的背景颜色
    private int mTodayBorderWidth = 1;                      //今的背景宽度
    private int mTodayRadius = 12;                          //今的边框半径,单位 dp
    private float mTodayTextSize = 14;                      //今的文字大小,单位 sp
    private float mTodayItemWidth;                          //代码自动计算
    private int mDividerColor = 0xFFE6E6E6;                 //分割线的颜色
    private float mDividerHeight = 0.5f;                    //分割线的高度,单位 dp
    private int mArrowRectSize = 20;                        //箭头的大小,单位 dp
    private Bitmap mLeftArrowBitmap;
    private Bitmap mRightArrowBitmap;

    public YearBarLayer(CalendarInfo info, Resources resources) {
        mModeInfo = info;
        DisplayMetrics metrics = resources.getDisplayMetrics();
        mYearPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mYearPadding, metrics);
        mTodayRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTodayRadius, metrics);
        mTodayBorderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTodayBorderWidth, metrics);
        mArrowRectSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mArrowRectSize, metrics);
        mDividerHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDividerHeight, metrics);
        mYearTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mYearTextSize, metrics);
        mTodayTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTodayTextSize, metrics);

        mLeftArrowBitmap = BitmapFactory.decodeResource(resources, R.mipmap.calendar_left_arrow);
        mRightArrowBitmap = BitmapFactory.decodeResource(resources, R.mipmap.calendar_right_arrow);
        mRect = info.getRect();
        mBorderRect = info.getBorderRect();
        mPaint = new Paint();
        mYearRect = new Rect();
        mLeftArrowRect = new Rect();
        mRightArrowRect = new Rect();
        mTodayRect = new Rect();
        init(info);
    }

    private void init(CalendarInfo info) {
        if (info == null) {
            return;
        }
        mYear = info.getYear();
        mMonth = info.getMonth();
        mModeInfo.setYear(mYear);
        mModeInfo.setMonth(mMonth);
        if (mMode == CalendarMode.YEAR) {
            mYearString = mYear + YEAR;
        } else {
            int month = mMonth + 1;
            String monthStr = String.format(Locale.getDefault(), "%02d", month);
            mYearString = mYear + YEAR + monthStr + MONTH;
        }
        //初始化画笔
        mPaint.setAntiAlias(true);
        //年份信息的初始化
        mPaint.setTextSize(mYearTextSize);
        FontMetrics textFontMetrics = mPaint.getFontMetrics();
        mYearTextHeight = textFontMetrics.descent - textFontMetrics.ascent;
        mYearTextAscent = textFontMetrics.ascent;
        mYearItemWidth = mPaint.measureText(mYearString);
        mPaint.setTextSize(mTodayTextSize);
        mTodayItemWidth = mPaint.measureText(TODAY);
        mYearRect.left = (int) (mRect.centerX() - mYearItemWidth / 2);
        mYearRect.right = (int) (mRect.centerX() + mYearItemWidth / 2);
        mYearRect.top = mRect.top;
        mYearRect.bottom = mRect.bottom;
        //左右箭头的区域计算
        mLeftArrowRect.left = mYearRect.left - mYearPadding - mArrowRectSize;
        mLeftArrowRect.right = mYearRect.left - mYearPadding;
        mLeftArrowRect.top = (mRect.top + mRect.bottom) / 2 - mArrowRectSize / 2;
        mLeftArrowRect.bottom = (mRect.top + mRect.bottom) / 2 + mArrowRectSize / 2;
        mRightArrowRect.left = mYearRect.right + mYearPadding;
        mRightArrowRect.right = mYearRect.right + mYearPadding + mArrowRectSize;
        mRightArrowRect.top = (mRect.top + mRect.bottom) / 2 - mArrowRectSize / 2;
        mRightArrowRect.bottom = (mRect.top + mRect.bottom) / 2 + mArrowRectSize / 2;
        mLeftBitmapArrowRect = new Rect(0, 0, mLeftArrowBitmap.getWidth(), mLeftArrowBitmap.getHeight());
        mRightBitmapArrowRect = new Rect(0, 0, mRightArrowBitmap.getWidth(), mRightArrowBitmap.getHeight());
        //今 的初始化
        mTodayRect.left = mRect.width() * (COL_COUNT - 1) / COL_COUNT;
        mTodayRect.right = mRect.right;
        mTodayRect.top = mRect.top;
        mTodayRect.bottom = mRect.bottom;
    }

    public void setYearMonth(CalendarInfo info, CalendarMode mode) {
        mMode = mode;
        init(info);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (!isShow()) {
            return;
        }
        //绘制年份导航条背景
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mYearAreaColor);
        canvas.drawRect(mRect, mPaint);
        //绘制年份导航条年月
        mPaint.setColor(mYearTextColor);
        float x = mYearRect.left + (mYearRect.width() - mYearItemWidth) / 2;
        float y = mYearRect.top + (mYearRect.height() - mYearTextHeight) / 2 - mYearTextAscent;
        mPaint.setTextSize(mYearTextSize);
        canvas.drawText(mYearString, x, y, mPaint);
        //绘制左右箭头
        canvas.drawBitmap(mLeftArrowBitmap, mLeftBitmapArrowRect, mLeftArrowRect, mPaint);
        canvas.drawBitmap(mRightArrowBitmap, mRightBitmapArrowRect, mRightArrowRect, mPaint);
        //绘制年份导航条 今
        drawToday(canvas, mPaint);
        //绘制年份与星期的分割线
        mPaint.setColor(mDividerColor);
        mPaint.setStrokeWidth(mDividerHeight);
        canvas.drawLine(mRect.left, mRect.bottom - mDividerHeight / 2, mRect.right, mRect.bottom - mDividerHeight / 2, mPaint);
    }

    private void drawToday(Canvas canvas, Paint paint) {
        if (!mTodayShow) {
            return;
        }
        float x = mTodayRect.left + (mTodayRect.width() - mTodayItemWidth) / 2;
        float y = mTodayRect.top + (mTodayRect.height() - mYearTextHeight) / 2 - mYearTextAscent;
        //几行无用代码
//        Rect bgRect = new Rect();
//        bgRect.left = (int) x;
//        bgRect.right = (int) (bgRect.left + mTodayItemWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mTodayBorderColor);
        paint.setStrokeWidth(mTodayBorderWidth);
        canvas.drawCircle(mTodayRect.centerX(), mTodayRect.centerY(), mTodayRadius, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mTodayTextColor);
        paint.setTextSize(mTodayTextSize);
        canvas.drawText(TODAY, x, y, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isShow()) {
            return true;
        }

        if (!mRect.contains((int) event.getX(), (int) event.getY())) {
            mAlwaysInTapRegion = true;
            return false;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                mDownX = (int) event.getX(MOTION_EVENT_DOWN_ID);
                mDownY = (int) event.getY(MOTION_EVENT_DOWN_ID);
                break;
            case MotionEvent.ACTION_MOVE:
                int distanceX = (int) (event.getX(MOTION_EVENT_DOWN_ID) - mDownX);
                mDownX = (int) event.getX(MOTION_EVENT_DOWN_ID);
                int distanceY = (int) (event.getY(MOTION_EVENT_DOWN_ID) - mDownY);
                mDownY = (int) event.getY(MOTION_EVENT_DOWN_ID);
                int distance = distanceX * distanceX + distanceY * distanceY;
                if (mAlwaysInTapRegion && distance > TOUCH_SLOP) {
                    mAlwaysInTapRegion = false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mAlwaysInTapRegion) {
                    onClick(mDownX, mDownY);
                }
                mAlwaysInTapRegion = true;
                break;
            default:
                break;
        }
        return true;
    }

    private boolean isOutOfBorder(Rect a) {
        return mBorderRect != null && (a.left > mBorderRect.right || a.right < mBorderRect.left || a.top > mBorderRect.bottom || a.bottom < mBorderRect.top);
    }

    private void onClick(int x, int y) {
        //同一时间点击一个位置
        if (mTodayRect.contains(x, y) && mTodayClick != null) {
            Calendar now = Calendar.getInstance();
            mTodayClick.onClick(new CalendarInfo(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)));
        } else if (mYearRect.contains(x, y) && mYearClick != null) {
            mYearClick.onClick(new CalendarInfo(mYear, mMonth, 1, CalendarMode.MONTH));
        } else if (mLeftArrowRect != null && mLeftArrowRect.contains(x, y)) {
            mLeftArrowClick.onClick(getCalendar(-1));
        } else if (mRightArrowRect != null && mRightArrowRect.contains(x, y)) {
            mRightArrowClick.onClick(getCalendar(1));
        }
    }

    private CalendarInfo getCalendar(int temp) {
        Calendar instance = Calendar.getInstance();
        instance.set(mYear, mMonth, 1);
        if (mMode == CalendarMode.MONTH || mMode == CalendarMode.WEEK) {
            instance.add(Calendar.MONTH, temp);
        } else {
            instance.add(Calendar.YEAR, temp);
        }
        return new CalendarInfo(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), 1, mMode);
    }

    @Override
    public void scrollBy(int dx, int dy) {
        mRect.offset(dx, dy);
        mBorderRect.offset(dx, dy);
    }

    @Override
    public Rect getBorderRect() {
        return mRect;
    }

    @Override
    public CalendarInfo getModeInfo() {
        return mModeInfo;
    }

    public void setYearBtnClick(OnLayerClickListener click) {
        mYearClick = click;
    }

    public void setTodayBtnClick(OnLayerClickListener click) {
        mTodayClick = click;
    }

    public void setLeftArrowClick(OnLayerClickListener click) {
        mLeftArrowClick = click;
    }

    public void setRightArrowClick(OnLayerClickListener click) {
        mRightArrowClick = click;
    }

    public void setShow(boolean show) {
        mIsShow = show;
    }

    public boolean isShow() {
        return mIsShow;
    }

    public void setTodayShow(boolean show) {
        mTodayShow = show;
    }

    public boolean isTodayShow() {
        return mTodayShow;
    }
}
