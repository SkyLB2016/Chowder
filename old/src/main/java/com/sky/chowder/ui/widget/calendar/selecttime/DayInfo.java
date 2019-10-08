package com.sky.chowder.ui.widget.calendar.selecttime;

import com.sky.chowder.ui.widget.calendar.common.CalendarInfo;

public class DayInfo {
    private CalendarInfo info;
    private String lable;//比较相等，排序
    private int index;

    public DayInfo() {
    }

    public DayInfo(CalendarInfo info, String lable, int index) {
        this.info = info;
        this.lable = lable;
        this.index = index;
    }

    public DayInfo(CalendarInfo info, int index) {
        this.info = info;
        this.index = index;
    }

    public CalendarInfo getInfo() {
        return info;
    }

    public void setInfo(CalendarInfo info) {
        this.info = info;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
