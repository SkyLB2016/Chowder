package com.sky.oa.widget.calendar.selecttime;

import com.sky.oa.widget.calendar.common.CalendarInfo;
import com.sky.oa.widget.calendar.common.CalendarMode;
import com.sky.oa.widget.calendar.impl.ISelectTime;

/**
 * 设计模式之策略模式
 * Created by libin on 2018/9/25 下午8:30.
 */
public class SelectTime {
    private ISelectTime select = new DayTime();//默认月视图
    private CalendarInfo today;
    private int limit=0;//限制时间

    private static class singletonHolder {
        private static SelectTime instance = new SelectTime();
    }

    public static SelectTime getInstance() {
        return singletonHolder.instance;
    }

    private SelectTime() {
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public CalendarInfo getToday() {
        return today;
    }

    public void setToday(CalendarInfo today) {
        this.today = today;
    }

    public ISelectTime getSelectTime() {
        return select;
    }

//    public void setSelectTime(ISelectTime select) {
//        this.select = select;
//    }

    public void setSelectTime(CalendarMode mode) {
        if (mode == CalendarMode.MONTH) {
            if (select instanceof DayTime) {
                select.clear();
            } else {
                select = new DayTime();
            }
        } else if (mode == CalendarMode.WEEK) {
            if (select instanceof WeekTime) {
                select.clear();
            } else {
                select = new WeekTime();
            }
        } else if (mode == CalendarMode.YEAR) {
            if (select instanceof YearTime) {
                select.clear();
            } else {
                select = new YearTime();
            }
        }
    }
}
