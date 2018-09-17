package com.sky.chowder.ui.widget.calendar.manager;

import android.graphics.Rect;

import com.sky.chowder.ui.widget.calendar.CalendarView;
import com.sky.chowder.ui.widget.calendar.common.CalendarInfo;
import com.sky.chowder.ui.widget.calendar.common.CalendarMode;
import com.sky.chowder.ui.widget.calendar.impl.OnListLayerScrollListener;
import com.sky.chowder.ui.widget.calendar.impl.OnPageChangeListener;
import com.sky.chowder.ui.widget.calendar.layer.CalendarLayer;
import com.sky.chowder.ui.widget.calendar.layer.ListLayer;

import java.util.Calendar;

public class ListLayerManager extends BaseLayerManager implements OnPageChangeListener, OnListLayerScrollListener {

    private int mYear, mMonth, mDay;
    private ListLayer mLayer;

    public ListLayerManager(CalendarView view, Rect defRect, CalendarLayer layer) {
        super(view, defRect, layer);
        registerOnPageChangeListener(this);
        CalendarInfo info = layer.getModeInfo();
        mYear = info.getYear();
        mMonth = info.getMonth();
        mDay = info.getDay();
        mLayer = (ListLayer) layer;
        mLayer.setOnListLayerScrollListener(this);
        mLayer.refreshData();
    }

    @Override
    public CalendarLayer createLayer(CalendarInfo info) {
        if (info.getMode() == CalendarMode.DAY) {
            return new ListLayer(getView(), info);
        }
        return null;
    }

    @Override
    public CalendarInfo getNextModeInfo(CalendarInfo curInfo) {
        int year = curInfo.getYear();
        int month = curInfo.getMonth();
        int day = curInfo.getDay();
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, day);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
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
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        return new CalendarInfo(curInfo.getRect(), year, month, day, curInfo.getMode());
    }

    @Override
    public void setCurLayerMode(CalendarInfo curInfo) {
        super.setCurLayerMode(curInfo);
        mYear = curInfo.getYear();
        mMonth = curInfo.getMonth();
        mDay = curInfo.getDay();
        mLayer.setOnListLayerScrollListener(null);
        mLayer = (ListLayer) getCurLayer();
        mLayer.setOnListLayerScrollListener(this);
        mLayer.refreshData();
        mLayer.setIsCanDrag(false);

        int dy = curInfo.getRect().top - mLayer.getBorderRect().top;
        mLayer.scrollBy(0, dy);
        mLayer.getBorderRect().bottom = curInfo.getRect().bottom;
        mLayer.getRect().bottom = curInfo.getRect().bottom;
    }

    @Override
    public void onClick(int x, int y) {
    }

    // 周改变的时候,月根据情况变化
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
        mLayer.setOnListLayerScrollListener(null);
        mLayer = (ListLayer) getCurLayer();
        mLayer.setOnListLayerScrollListener(this);
        mLayer.refreshData();
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

    public ListLayer getLayer() {
        return mLayer;
    }

    @Override
    public void onScrollY(int dy) {
    }

    @Override
    public void onScrollEnd() {
        setCanScrollLeftOfRight(true);
    }

    @Override
    public void onScrollStart() {
        setCanScrollLeftOfRight(false);
    }

    public void setCurLayerCanDrag(boolean flag) {
        mLayer.setIsCanDrag(flag);
    }

    public boolean isCanPullUp() {
        return mLayer.isCanPullUp();
    }

    public boolean isCanPullDown() {
        return mLayer.isCanPullDown();
    }
}