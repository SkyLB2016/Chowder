package com.sky.chowder.ui.widget.calendar.selecttime;

import com.sky.chowder.ui.widget.calendar.common.CalendarMode;
import com.sky.chowder.ui.widget.calendar.impl.ISelectTime;

/**
 * 设计模式之策略模式
 * Created by libin on 2018/9/25 下午8:30.
 */
public class SelectTime {
    private ISelectTime select = new DayTime();//默认月视图

    private static class singletonHolder {
        private static SelectTime instance = new SelectTime();
    }

    public static SelectTime getInstance() {
        return singletonHolder.instance;
    }

    private SelectTime() {
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
