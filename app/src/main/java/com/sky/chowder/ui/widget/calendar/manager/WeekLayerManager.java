package com.sky.chowder.ui.widget.calendar.manager;


import android.graphics.Rect;

import com.sky.chowder.ui.widget.calendar.CalendarView;
import com.sky.chowder.ui.widget.calendar.common.CalendarInfo;
import com.sky.chowder.ui.widget.calendar.common.CalendarMode;
import com.sky.chowder.ui.widget.calendar.common.WeekInfo;
import com.sky.chowder.ui.widget.calendar.impl.OnPageChangeListener;
import com.sky.chowder.ui.widget.calendar.impl.OnTimeChange;
import com.sky.chowder.ui.widget.calendar.layer.CalendarLayer;
import com.sky.chowder.ui.widget.calendar.layer.WeekLayer;

import java.util.Calendar;

public class WeekLayerManager extends BaseLayerManager implements OnPageChangeListener {

    private WeekLayer mLayer;
    private int mYear, mMonth, mDay;
    private OnTimeChange mTimeChangeListener;

    public WeekLayerManager(CalendarView view, Rect defRect, CalendarLayer layer) {
        super(view, defRect, layer);
        registerOnPageChangeListener(this);
        CalendarInfo info = layer.getModeInfo();
        mYear = info.getYear();
        mMonth = info.getMonth();
        mDay = info.getDay();
        mLayer = (WeekLayer) layer;
//        mLayer.setSelectedDay(mDay);
    }

    @Override
    public void setCurLayerMode(CalendarInfo curInfo) {
        super.setCurLayerMode(curInfo);
        mYear = curInfo.getYear();
        mMonth = curInfo.getMonth();
        mDay = curInfo.getDay();
        mLayer = (WeekLayer) getCurLayer();
        mLayer.setSelectedDay(mDay);
    }

    @Override
    public CalendarLayer createLayer(CalendarInfo info) {
        if (info.getMode() == CalendarMode.WEEK) {
            return new WeekLayer(info, getView().getResources());
        }
        return null;
    }

    @Override
    public CalendarInfo getNextModeInfo(CalendarInfo curInfo) {
        int year = curInfo.getYear();
        int month = curInfo.getMonth();
        int day = curInfo.getDay();
        WeekLayer curLayer = (WeekLayer) getCurLayer();
        int index = curLayer.getDayIndex();
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, day);
        calendar.add(Calendar.DAY_OF_MONTH, 7 - index);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        return new CalendarInfo(curInfo.getRect(), year, month, day, curInfo.getMode());
    }

    @Override
    public CalendarInfo getPreModeInfo(CalendarInfo curInfo) {
        int year = curInfo.getYear();
        int month = curInfo.getMonth();
        int day = curInfo.getDay();
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, day);
        WeekLayer curLayer = (WeekLayer) getCurLayer();
        int index = curLayer.getDayIndex();
        calendar.add(Calendar.DAY_OF_MONTH, -7 - index);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        return new CalendarInfo(curInfo.getRect(), year, month, day, curInfo.getMode());
    }

    @Override
    public void onClick(int x, int y) {
        WeekInfo info = mLayer.getYMDByLocation(x, y);
        if (info == null) {
            return;
        }
//        mYear = info.getYear();
//        mMonth = info.getMonth();
//        mDay = info.getDay();
        WeekLayer layer = (WeekLayer) getCurLayer();
        layer.setSelectedDay(info);
//        if (mTimeChangeListener != null) {
//            mTimeChangeListener.onTimeChange(info);
//        }
    }

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
        mLayer.setSelectedDay(-1);
        mDay = info.getDay();
        mYear = info.getYear();
        mMonth = info.getMonth();
        mLayer = (WeekLayer) getCurLayer();
        mLayer.setSelectedDay(mDay);
        if (mTimeChangeListener != null) {
            mTimeChangeListener.onTimeChange(info);
        }
    }

    public void setCurLayerTime(int year, int month, int day) {
        mDay = day;
        mYear = year;
        mMonth = month;
        mLayer.setSelectedDay(mDay);
        if (mTimeChangeListener != null) {
            mTimeChangeListener.onTimeChange(new CalendarInfo(year, month, day, CalendarMode.WEEK));
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

    public WeekLayer getLayer() {
        return mLayer;
    }

    public void setOnTimeChangeListener(OnTimeChange lis) {
        mTimeChangeListener = lis;
    }
}