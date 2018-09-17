package com.sky.chowder.ui.widget.calendar.manager;

import android.graphics.Rect;

import com.sky.chowder.ui.widget.calendar.CalendarView;
import com.sky.chowder.ui.widget.calendar.common.CalendarInfo;
import com.sky.chowder.ui.widget.calendar.common.CalendarMode;
import com.sky.chowder.ui.widget.calendar.impl.OnPageChangeListener;
import com.sky.chowder.ui.widget.calendar.impl.OnTimeChange;
import com.sky.chowder.ui.widget.calendar.layer.CalendarLayer;
import com.sky.chowder.ui.widget.calendar.layer.MonthLayer;

import java.util.Calendar;

public class MonthLayerManager extends BaseLayerManager implements OnPageChangeListener {

    private int mYear, mMonth, mDay;
    private MonthLayer mLayer;
    private OnTimeChange mTimeChangeListener;

    public MonthLayerManager(CalendarView view, Rect defRect, CalendarLayer layer) {
        super(view, defRect, layer);
        registerOnPageChangeListener(this);
        CalendarInfo info = layer.getModeInfo();
        mYear = info.getYear();
        mMonth = info.getMonth();
        mDay = info.getDay();
        mLayer = (MonthLayer) layer;
        mLayer.setSelectedDay(mDay);
    }

    public Rect getSelectedDayRect() {
        return mLayer.getSelectedRect();
    }

    @Override
    public void setCurLayerMode(CalendarInfo curInfo) {
        super.setCurLayerMode(curInfo);
        mYear = curInfo.getYear();
        mMonth = curInfo.getMonth();
        mDay = curInfo.getDay();
        mLayer = (MonthLayer) getCurLayer();
        mLayer.setSelectedDay(mDay);
    }

    @Override
    public CalendarLayer createLayer(CalendarInfo info) {
        if (info.getMode() == CalendarMode.MONTH) {
            return new MonthLayer(info, getView().getResources());
        }
        return null;
    }

    @Override
    public CalendarInfo getNextModeInfo(CalendarInfo curInfo) {
        int year = curInfo.getYear();
        int month = curInfo.getMonth();
        if (month + 1 > Calendar.DECEMBER) {
            month = Calendar.JANUARY;
            year += 1;
        } else {
            month += 1;
        }
        return new CalendarInfo(curInfo.getRect(), year, month, 1, curInfo.getMode());
    }

    @Override
    public CalendarInfo getPreModeInfo(CalendarInfo curInfo) {
        int year = curInfo.getYear();
        int month = curInfo.getMonth();
        if (month - 1 < Calendar.JANUARY) {
            month = Calendar.DECEMBER;
            year -= 1;
        } else {
            month -= 1;
        }
        return new CalendarInfo(curInfo.getRect(), year, month, 1, curInfo.getMode());
    }

    @Override
    public void onClick(int x, int y) {
        CalendarInfo info = mLayer.getYMDByLocation(x, y);
        if (info == null) {
            return;
        }
        mYear = info.getYear();
        mMonth = info.getMonth();
        mDay = info.getDay();
        MonthLayer layer = (MonthLayer) getCurLayer();
        layer.setSelectedDay(mDay);

        if (mTimeChangeListener != null) {
            mTimeChangeListener.onTimeChange(info);
        }
    }

    //周改变的时候,月根据情况变化
    @Override
    public void onPageScrolled(CalendarInfo info) {
    }

    @Override
    public void onPageSelectedChange(CalendarInfo info) {
    }

    @Override
    public void onPageScrollEnd(CalendarInfo info) {
        if (mLayer == getCurLayer()) {
            return;
        }
        mLayer.setSelectedDay(1);
        MonthLayer layer = (MonthLayer) getCurLayer();

        mYear = info.getYear();
        mMonth = info.getMonth();
        mDay = 1;
        info.setDay(mDay);
        layer.setSelectedDay(mDay);//默认为第一天
        mLayer = layer;
        if (mTimeChangeListener != null) {
            mTimeChangeListener.onTimeChange(info);
        }
    }

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getDay() {
        return mDay;
    }

    public MonthLayer getLayer() {
        return mLayer;
    }

    public void setOnTimeChangeListener(OnTimeChange lis) {
        mTimeChangeListener = lis;
    }
}