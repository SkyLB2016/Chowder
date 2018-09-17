package com.sky.chowder.ui.widget.calendar.layer;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;

import com.sky.chowder.ui.widget.calendar.common.CalendarInfo;
import com.sky.chowder.ui.widget.calendar.common.CalendarMode;

import java.util.ArrayList;
import java.util.List;

public class YearLayer implements CalendarLayer {

    private static final String YEAR = "年";
    private static final int ROW_COUNT = 4;
    private static final int COL_COUNT = 3;

    private CalendarInfo mModeInfo;
    private Rect mRect;
    private Rect mBorderRect;
    private Resources mResources;
    private Paint mPaint;
    private int mYear;

    private int monthToLeftRightPadding = 8;
    private int monthToTopBottomPadding = 10;
    private int colSpace = 10;
    private int rowSpace = 15;

    private List<MonthOfYearLayer> mMonthLayerList = new ArrayList<>(); //年份中每月的显示层

    public YearLayer(CalendarInfo info, Resources resources) {
        mModeInfo = info;
        mRect = mModeInfo.getRect();
        mYear = mModeInfo.getYear();
        mBorderRect = info.getBorderRect();
        mResources = resources;
        DisplayMetrics metrics = resources.getDisplayMetrics();
        monthToLeftRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, monthToLeftRightPadding, metrics);
        monthToTopBottomPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, monthToTopBottomPadding, metrics);
        colSpace = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, colSpace, metrics);
        rowSpace = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rowSpace, metrics);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //年份中每月的显示区域
        int colWidth = (mRect.width() - monthToLeftRightPadding * 2 - colSpace * (COL_COUNT - 1)) / COL_COUNT;
        int rowHeight = (mRect.height() - monthToTopBottomPadding * 2 - rowSpace * (ROW_COUNT - 1)) / ROW_COUNT;
        for (int i = 0; i < 12; i++) {
            Rect rect = new Rect();
            rect.left = mRect.left + (i % COL_COUNT) * (colWidth + colSpace) + monthToLeftRightPadding;
            rect.top = mRect.top + (i / COL_COUNT) * (rowHeight + rowSpace) + monthToTopBottomPadding;
            rect.right = rect.left + colWidth;
            rect.bottom = rect.top + rowHeight;
            mMonthLayerList.add(new MonthOfYearLayer(new CalendarInfo(rect, rect, mYear, i, 1, CalendarMode.MONTH), mResources));
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (isOutOfBorder(mRect)) {
            return;
        }
        Paint paint = mPaint;
        paint.setColor(Color.WHITE);
        //绘制白色背景
        canvas.drawRect(mRect, paint);
        //绘制每月
        for (int i = 0; i < mMonthLayerList.size(); i++) {
            if (isOutOfBorder(mMonthLayerList.get(i).getBorderRect())) {
                continue;
            }
            mMonthLayerList.get(i).onDraw(canvas);
        }
    }

    @Override
    public void scrollBy(int dx, int dy) {
        mRect.offset(dx, dy);
        for (int i = 0; i < mMonthLayerList.size(); i++) {
            mMonthLayerList.get(i).scrollBy(dx, dy);
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

    private boolean isOutOfBorder(Rect rect) {
        return mBorderRect != null && (rect.left > mBorderRect.right || rect.right < mBorderRect.left || rect.top > mBorderRect.bottom || rect.bottom < mBorderRect.top);
    }

    public CalendarInfo getYMByLocation(int x, int y) {
        int index = -1;
        for (int i = 0; i < mMonthLayerList.size(); i++) {
            if (mMonthLayerList.get(i).getBorderRect().contains(x, y)) {
                index = i;
                break;
            }
        }
        if (index < 0) {
            return null;
        }
        int year = mMonthLayerList.get(index).getModeInfo().getYear();
        int month = mMonthLayerList.get(index).getModeInfo().getMonth();
        return new CalendarInfo(year, month, 1, CalendarMode.MONTH);
    }
}