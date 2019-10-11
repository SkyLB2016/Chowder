package com.sky.oa.widget.calendar.manager;

import android.graphics.Rect;

import com.sky.oa.widget.calendar.CalendarView;
import com.sky.oa.widget.calendar.common.CalendarInfo;
import com.sky.oa.widget.calendar.common.CalendarMode;
import com.sky.oa.widget.calendar.impl.OnPageChangeListener;
import com.sky.oa.widget.calendar.impl.OnTimeChange;
import com.sky.oa.widget.calendar.layer.CalendarLayer;
import com.sky.oa.widget.calendar.layer.WeekLayer;

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
//        mLayer.setSelectedDay(mDay);
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
        if (month + 1 > Calendar.DECEMBER) {
            month = Calendar.JANUARY;
            year += 1;
        } else {
            month += 1;
        }
        return new CalendarInfo(curInfo.getRect(), year, month, curInfo.getDay(), curInfo.getMode());
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
        return new CalendarInfo(curInfo.getRect(), year, month, curInfo.getDay(), curInfo.getMode());
    }

    @Override
    public void onClick(int x, int y) {
        mLayer.setSelectedWeek(x, y);
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
        mDay = info.getDay();
        mYear = info.getYear();
        mMonth = info.getMonth();
        mLayer = (WeekLayer) getCurLayer();
//        Calendar cal = Calendar.getInstance();
//        cal.set(mYear, mMonth, mDay);
//        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
//        int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
//        if (dayOfWeek == 1) {
//            weekOfYear--;
//        }
//        List<WeekInfo> weeks = mLayer.getWeekList();
//        WeekInfo weekInfo=null;
//        for (int i = 0; i < weeks.size(); i++) {
//            if (weeks.get(i).getWeekOfYear() == weekOfYear) {
//                weekInfo = weeks.get(i);
//            }
//        }

//        mLayer.setSelectedDay(weekInfo);
        if (mTimeChangeListener != null) {
            mTimeChangeListener.onTimeChange(info);
        }
    }

    public void setCurLayerTime(int year, int month, int day) {
        mDay = day;
        mYear = year;
        mMonth = month;
//        mLayer.setSelectedDay(mDay);
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