package com.sky.chowder.ui.widget.calendar.manager;

import android.graphics.Rect;

import com.sky.chowder.ui.widget.calendar.CalendarView;
import com.sky.chowder.ui.widget.calendar.common.CalendarInfo;
import com.sky.chowder.ui.widget.calendar.common.CalendarMode;
import com.sky.chowder.ui.widget.calendar.impl.OnLayerClickListener;
import com.sky.chowder.ui.widget.calendar.impl.OnPageChangeListener;
import com.sky.chowder.ui.widget.calendar.layer.CalendarLayer;
import com.sky.chowder.ui.widget.calendar.layer.YearLayer;

public class YearLayerManager extends BaseLayerManager implements OnPageChangeListener {

    private YearLayer mLayer;
    private OnLayerClickListener mLayerClick;
    private OnPageChangeListener listener;

    public YearLayerManager(CalendarView view, Rect defRect, CalendarLayer layer) {
        super(view, defRect, layer);
        registerOnPageChangeListener(this);
        mLayer = (YearLayer) getCurLayer();
    }

    @Override
    public void setCurLayerMode(CalendarInfo curInfo) {
        super.setCurLayerMode(curInfo);
        mLayer = (YearLayer) getCurLayer();
    }

    @Override
    public CalendarLayer createLayer(CalendarInfo info) {
        if (info.getMode() == CalendarMode.YEAR) {
            return new YearLayer(info, getView().getResources());
        }
        return null;
    }

    @Override
    public CalendarInfo getPreModeInfo(CalendarInfo curInfo) {
        return new CalendarInfo(curInfo.getRect(), curInfo.getYear() - 1, 0, 1, curInfo.getMode());
    }

    @Override
    public CalendarInfo getNextModeInfo(CalendarInfo curInfo) {
        return new CalendarInfo(curInfo.getRect(), curInfo.getYear() + 1, 0, 1, curInfo.getMode());
    }

    @Override
    public void onClick(int x, int y) {
        mLayer.selectMonth(x,y);
    }

    public void setOnYearLayerClickListener(OnLayerClickListener click) {
        mLayerClick = click;
    }

    @Override
    public void onPageScrolled(CalendarInfo info) {
        if (listener != null) {
            listener.onPageScrolled(info);
        }
    }

    @Override
    public void onPageSelectedChange(CalendarInfo info) {
        if (listener != null) {
            listener.onPageSelectedChange(info);
        }
    }

    @Override
    public void onPageScrollEnd(CalendarInfo info) {
        if (mLayer == getCurLayer()) {
            return;
        }
        mLayer = (YearLayer) getCurLayer();
        if (listener != null) {
            listener.onPageScrollEnd(info);
        }
    }

    public YearLayer getLayer() {
        return mLayer;
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.listener = listener;
    }
}