package com.sky.oa.widget.calendar.selecttime;

import com.sky.oa.widget.calendar.common.CalendarInfo;

import java.util.List;

/**
 * Created by libin on 2018/9/21 下午5:32.
 */
public class WeekInfo {

    private List<CalendarInfo> infos;//本周所包含的日期
    private int weekOfYear;//
    private int weekOfMonth;//

    public WeekInfo(List<CalendarInfo> infos, int weekOfYear, int weekOfMonth) {
        this.infos = infos;
        this.weekOfYear = weekOfYear;
        this.weekOfMonth = weekOfMonth;
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

    //第33周（ 2018-08-20至2018-08-27 ）
    public String getFormatDate() {
        if (infos.size() < 7) return "";
        return "第" + weekOfYear + "周（ " + getDate(infos.get(0)) + "至" + getDate(infos.get(6)) + " ）";
    }

    private String getDate(CalendarInfo info) {
        return info.getYear() + "-" + setDouble(info.getMonth() + 1) + "-" + setDouble(info.getDay());
    }

    private String setDouble(int value) {
        return String.format("%02d", value);
    }
}
