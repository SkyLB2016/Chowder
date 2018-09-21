package com.sky.chowder.ui.widget.calendar.common;

import java.util.List;

/**
 * Created by libin on 2018/9/21 下午5:32.
 */
public class WeekInfo {

    List<CalendarInfo> infos;//本周所包含的日期
    int weekOfYear;//
    int weekOfMonth;//
    boolean isThisWeek = false;

    public WeekInfo(List<CalendarInfo> infos, int weekOfYear, int weekOfMonth) {
        this.infos = infos;
        this.weekOfYear = weekOfYear;
        this.weekOfMonth = weekOfMonth;
    }

    public WeekInfo(List<CalendarInfo> infos, int weekOfYear, int weekOfMonth, boolean isThisWeek) {
        this.infos = infos;
        this.weekOfYear = weekOfYear;
        this.weekOfMonth = weekOfMonth;
        this.isThisWeek = isThisWeek;
    }

    public List<CalendarInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<CalendarInfo> infos) {
        this.infos = infos;
    }

    public int getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(int weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    public int getWeekOfMonth() {
        return weekOfMonth;
    }

    public void setWeekOfMonth(int weekOfMonth) {
        this.weekOfMonth = weekOfMonth;
    }

    public boolean isThisWeek() {
        return isThisWeek;
    }

    public void setThisWeek(boolean thisWeek) {
        isThisWeek = thisWeek;
    }

    //第33周（ 2018-08-20至2018-08-27 ）
    public String getFormatDate() {
        if (infos.size() < 7) return "";
        return "第" + weekOfYear + "周（ " + getDate(infos.get(0)) + "至" + getDate(infos.get(6)) + " ）";
    }

    private String getDate(CalendarInfo info) {
        return info.getYear() + "-" + setDouble(info.getMonth()+1) + "-" + setDouble(info.getDay());
    }

    private String setDouble(int value) {
        return String.format("%02d", value);
    }
}
