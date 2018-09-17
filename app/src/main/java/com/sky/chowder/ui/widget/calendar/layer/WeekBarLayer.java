package com.sky.chowder.ui.widget.calendar.layer;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;

import com.sky.chowder.ui.widget.calendar.CalendarView;
import com.sky.chowder.ui.widget.calendar.common.CalendarColor;
import com.sky.chowder.ui.widget.calendar.common.CalendarInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeekBarLayer implements CalendarLayer {

    private static final int COL_COUNT = 7;
    private static final String[] BASE_WEEK = {"一", "二", "三", "四", "五", "六", "日"};
    private static String[] WEEK = new String[COL_COUNT];

    private CalendarInfo mModeInfo;
    private Paint mPaint;

    private List<Rect> mWeekRectList = new ArrayList<>();
    private HashMap<String, Float> mTextWidthMap;

    private Rect mRect;              //允许的绘制区域
    private Rect mBorderRect;        //绘制的边界
    private boolean mIsShow;

    private float mWeekTextSize = 12;                       //星期的字体大小,单位 sp
    private int mWeekBarHeight = 30;                        //星期高度,单位 dp
    private int mWeekAreaColor = CalendarColor.WHITE;       //星期导航条的背景色
    private int mWeekTextColor = CalendarColor.TITLE_GRAY;  //星期的文字颜色
    private float mWeekTextHeight;                          //代码自动计算
    private float mWeekTextAscent;                          //代码自动计算
    private int mWeekItemWidth;                             //代码自动计算

    public WeekBarLayer(CalendarInfo info, Resources resources) {
        mModeInfo = info;
        DisplayMetrics metrics = resources.getDisplayMetrics();
        mWeekTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mWeekTextSize, metrics);
        mWeekBarHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mWeekBarHeight, metrics);

        //动态设置第一天开始的星期
        int weekStart = (CalendarView.FIRST_DAY + COL_COUNT - 2) % 7;
        for (int i = 0; i < COL_COUNT; i++) {
            int index = (i + weekStart) % COL_COUNT;
            WEEK[i] = BASE_WEEK[index];
        }

        mRect = info.getRect();
        mWeekItemWidth = mRect.width() / COL_COUNT;
        mBorderRect = info.getBorderRect();
        //每星期的数字显示区域
        for (int i = 0; i < COL_COUNT; i++) {
            Rect item = new Rect();
            item.left = mRect.left + i * mWeekItemWidth;
            item.top = mRect.top;
            item.right = item.left + mWeekItemWidth;
            item.bottom = mRect.top + mWeekBarHeight;
            mWeekRectList.add(item);
        }
        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //星期信息的初始化
        mPaint.setTextSize(mWeekTextSize);
        mTextWidthMap = new HashMap<>();
        FontMetrics textFontMetrics = mPaint.getFontMetrics();
        mWeekTextHeight = textFontMetrics.descent - textFontMetrics.ascent;
        mWeekTextAscent = textFontMetrics.ascent;
        for (int i = 0; i < COL_COUNT; i++) {
            float textWidth = mPaint.measureText(WEEK[i]);
            mTextWidthMap.put(WEEK[i], textWidth);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (!isShow()) {
            return;
        }
        //绘制星期导航条背景
        mPaint.setColor(mWeekAreaColor);
        canvas.drawRect(mRect, mPaint);
        //绘制星期导航条文字
        mPaint.setColor(mWeekTextColor);
        mPaint.setTextSize(mWeekTextSize);
        for (int i = 0; i < mWeekRectList.size(); i++) {
            Rect rect = mWeekRectList.get(i);
            String text = WEEK[i];
            float x = rect.left + (rect.width() - mTextWidthMap.get(text)) / 2;
            float y = rect.top + (rect.height() - mWeekTextHeight) / 2 - mWeekTextAscent;
            canvas.drawText(text, x, y, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    private boolean isOutOfBorder(Rect a) {
        return mBorderRect != null && (a.left > mBorderRect.right || a.right < mBorderRect.left || a.top > mBorderRect.bottom || a.bottom < mBorderRect.top);
    }

    @Override
    public void scrollBy(int dx, int dy) {
        mRect.offset(dx, dy);
        mBorderRect.offset(dx, dy);
        for (int i = 0; i < mWeekRectList.size(); i++) {
            mWeekRectList.get(i).offset(dx, dy);
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

    public void setShow(boolean show) {
        mIsShow = show;
    }

    public boolean isShow() {
        return mIsShow;
    }
}