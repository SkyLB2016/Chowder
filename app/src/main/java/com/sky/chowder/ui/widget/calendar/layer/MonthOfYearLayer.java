package com.sky.chowder.ui.widget.calendar.layer;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.sky.chowder.R;
import com.sky.chowder.ui.widget.calendar.common.CalendarInfo;
import com.sky.chowder.ui.widget.calendar.common.CalendarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MonthOfYearLayer implements CalendarLayer {

    private static final String[] MONTH_HANZI = new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
    private static final boolean IS_SHOW_OUT_MONTH = false;   //是否显示当前月以外的天
    private static final int MONTH_OF_DAY_ROW = 6;
    private static final int MONTH_OF_DAY_COL = 7;

    private CalendarInfo mModeInfo;
    private Rect mRect;                //当前的区域
    private Rect mBorderRect;          //绘制的边界
    private int mYear;                 //当前的年
    private int mMonth;                //当前的月
    private int[] mCurYMD;             //当前的年月日数组
    private Paint mPaint;

    private Rect mMonthRect;                //月份文字的显示区域
    private float mMonthTextSize;           //月份的文字大小
    private float mMonthTextDescent;        //月份的文字下坡值
    private int mMonthColor;                //月份的颜色
    private Rect mDayAreaRect;              //1-31日的显示区域
    private float mDayTextHeight;           //每天的文字高度
    private float mDayTextAscent;           //每天的文字上坡值
    private float mDayTextSize;             //日的文字大小
    private int mDayColor;                  //每天的颜色
    private int mDaySelectedBgColor;        //每天选中的背景色
    private int mDaySelectedTextColor;      //每天选中的文字颜色

    private int[] mDayArray;                //当前月的填充数据 7*6 的数组
    private List<Rect> mDayRectList = new ArrayList<>();                //6*7 的数组
    private HashMap<String, Float> mDayTextWidth = new HashMap<>();     //每天的数字测量宽度

    public MonthOfYearLayer(CalendarInfo info, Resources resources) {
        mCurYMD = CalendarUtil.getYMD(System.currentTimeMillis());
        mModeInfo = info;
        mRect = mModeInfo.getRect();
        mYear = mModeInfo.getYear();
        mMonth = mModeInfo.getMonth();
        mBorderRect = mModeInfo.getBorderRect();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //资源初始化
        mMonthTextSize = resources.getDimensionPixelOffset(R.dimen.text_16);
        mMonthColor = resources.getColor(R.color.schedule_year_month_color);
        mDayTextSize = resources.getDimensionPixelOffset(R.dimen.text_10);
        mDayColor = resources.getColor(R.color.schedule_default_color);
        mDaySelectedBgColor = resources.getColor(R.color.schedule_year_selected_day_color);
        mDaySelectedTextColor = resources.getColor(R.color.white);
        //月份初始化
        mPaint.setTextSize(mMonthTextSize);
        FontMetrics textFontMetrics = mPaint.getFontMetrics();
        int monthTextHeight = (int) (textFontMetrics.descent - textFontMetrics.ascent);
        mMonthTextDescent = textFontMetrics.descent;
        mMonthRect = new Rect(mRect.left, mRect.top, mRect.right, mRect.top + monthTextHeight);
        mDayAreaRect = new Rect(mRect.left, mMonthRect.bottom, mRect.right, mRect.bottom);
        //每天相关
        int dayWidth = mDayAreaRect.width() / MONTH_OF_DAY_COL;
        int dayHeight = mDayAreaRect.height() / MONTH_OF_DAY_ROW;
        mDayArray = CalendarUtil.getDayOfMonth(mYear, mMonth, IS_SHOW_OUT_MONTH, null);
        mPaint.setTextSize(mDayTextSize);
        for (int i = 0; i <= 31; i++) {
            String text = "" + i;
            float textWidth = mPaint.measureText(text);
            mDayTextWidth.put(text, textWidth);
        }
        textFontMetrics = mPaint.getFontMetrics();
        mDayTextHeight = textFontMetrics.descent - textFontMetrics.ascent;
        mDayTextAscent = textFontMetrics.ascent;
        //每天的显示区域
        for (int i = 0; i < 42; i++) {
            Rect day = new Rect();
            day.left = mDayAreaRect.left + (i % MONTH_OF_DAY_COL) * dayWidth;
            day.top = mDayAreaRect.top + (i / MONTH_OF_DAY_COL) * dayHeight;
            day.right = day.left + dayWidth;
            day.bottom = day.top + dayHeight;
            mDayRectList.add(day);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawMonth(canvas, mPaint);
        drawDay(canvas, mPaint);
    }

    private void drawMonth(Canvas canvas, Paint paint) {
        paint.setTextSize(mMonthTextSize);
        paint.setColor(mMonthColor);
        float x = mMonthRect.left;
        float y = mMonthRect.bottom - mMonthTextDescent;
        canvas.drawText(MONTH_HANZI[mMonth], x, y, paint);
    }

    private void drawDay(Canvas canvas, Paint paint) {
        paint.setStyle(Style.FILL_AND_STROKE);
        paint.setTextSize(mDayTextSize);
        for (int i = 0; i < mDayRectList.size(); i++) {
            Rect rect = mDayRectList.get(i);
            int day = mDayArray[i];
            if (day <= 0 || isOutOfBorder(rect)) {
                continue;
            }
            String text = String.valueOf(mDayArray[i]);
            float x = rect.left + (rect.width() - mDayTextWidth.get(text)) / 2;
            float y = rect.top + (rect.height() - mDayTextHeight) / 2 - mDayTextAscent;
            paint.setColor(mDayColor);
            //绘制当天背景
            if (mCurYMD[0] == mYear && mCurYMD[1] == mMonth && mCurYMD[2] == day) {
                paint.setColor(mDaySelectedBgColor);
                canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, paint);
                paint.setColor(mDaySelectedTextColor);
            }
            canvas.drawText(text, x, y, paint);
        }
    }

    @Override
    public void scrollBy(int dx, int dy) {
        mRect.offset(dx, dy);
        mBorderRect.offset(dx, dy);
        mMonthRect.offset(dx, dy);
        mDayAreaRect.offset(dx, dy);
        for (int i = 0; i < mDayRectList.size(); i++) {
            mDayRectList.get(i).offset(dx, dy);
        }
    }

    @Override
    public Rect getBorderRect() {
        return mRect;
    }

    @Override
    public CalendarInfo getModeInfo() {
        return mModeInfo;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    private boolean isOutOfBorder(Rect a) {
        return mBorderRect != null && (a.left > mBorderRect.right || a.right < mBorderRect.left || a.top > mBorderRect.bottom || a.bottom < mBorderRect.top);
    }
}
