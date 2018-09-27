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

    private int year, month, day;
    private MonthLayer mLayer;
    private OnTimeChange mTimeChangeListener;

    public MonthLayerManager(CalendarView view, Rect defRect, CalendarLayer layer) {
        super(view, defRect, layer);
        registerOnPageChangeListener(this);
        CalendarInfo info = layer.getModeInfo();
        year = info.getYear();
        month = info.getMonth();
        day = info.getDay();
        mLayer = (MonthLayer) layer;
    }

    @Override
    public void setCurLayerMode(CalendarInfo curInfo) {
        super.setCurLayerMode(curInfo);
        year = curInfo.getYear();
        month = curInfo.getMonth();
        day = curInfo.getDay();
        mLayer = (MonthLayer) getCurLayer();
//        mLayer.setSelectedDay(day);
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
        if (++month > Calendar.DECEMBER) {
            month = Calendar.JANUARY;
            year++;
//        } else {
//            month++;
        }
        return new CalendarInfo(curInfo.getRect(), year, month, 1, curInfo.getMode());
    }

    @Override
    public CalendarInfo getPreModeInfo(CalendarInfo curInfo) {
        int year = curInfo.getYear();
        int month = curInfo.getMonth();
        if (--month < Calendar.JANUARY) {
            month = Calendar.DECEMBER;
            year--;
//        } else {
//            month--;
        }
        return new CalendarInfo(curInfo.getRect(), year, month, 1, curInfo.getMode());
    }

    @Override
    public void onClick(int x, int y) {
        CalendarInfo info = mLayer.getYMDByLocation(x, y);
        if (info == null) {
            return;
        }
        year = info.getYear();
        month = info.getMonth();
        day = info.getDay();
        MonthLayer layer = (MonthLayer) getCurLayer();
        layer.setSelectedDay(day);

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
//        mLayer.setSelectedDay(1);
        MonthLayer layer = (MonthLayer) getCurLayer();

        year = info.getYear();
        month = info.getMonth();
        day = 1;
        info.setDay(day);
//        layer.setSelectedDay(day);//默认为第一天
        mLayer = layer;
        if (mTimeChangeListener != null) {
            mTimeChangeListener.onTimeChange(info);
        }
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public MonthLayer getLayer() {
        return mLayer;
    }

    public void setOnTimeChangeListener(OnTimeChange lis) {
        mTimeChangeListener = lis;
    }
}