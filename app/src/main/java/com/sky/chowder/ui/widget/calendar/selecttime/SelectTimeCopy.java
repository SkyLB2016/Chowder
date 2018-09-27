package com.sky.chowder.ui.widget.calendar.selecttime;

import android.support.annotation.NonNull;

import com.sky.chowder.ui.widget.calendar.common.CalendarInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by libin on 2018/9/25 下午8:30.
 */
public class SelectTimeCopy {

    //    private List<DayInfo> selectMonthList = new ArrayList<DayInfo>();//选中天
    private Map<String, List<WeekInfo>> selectWeekMap = new HashMap<>();//选中的周
    private Map<String, List<DayInfo>> selectDayMap = new HashMap<>();//选中的日期

    private static class singletonHolder {
        private static SelectTimeCopy instance = new SelectTimeCopy();
    }

    public static SelectTimeCopy getInstance() {
        return singletonHolder.instance;
    }

    private SelectTimeCopy() {
    }

    public List<DayInfo> getSelectDayList(int year, int month) {
        return getSelectDayList(getKey(year, month));
    }

    public List<DayInfo> getSelectDayList(String key) {
        return selectDayMap.get(key);
    }

    public Map<String, List<DayInfo>> getSelectDayMap() {
        return selectDayMap;
    }

    public List<WeekInfo> getSelectWeekList(int year, int month) {
        return getSelectWeekList(getKey(year, month));
    }

    public List<WeekInfo> getSelectWeekList(String key) {
        return selectWeekMap.get(key);
    }

    public Map<String, List<WeekInfo>> getSelectWeekMap() {
        return selectWeekMap;
    }

    public void addDay(int year, int month, int day, int lastDayIndex) {
        String key = getKey(year, month);
        String lable = key + setDouble(day) + "日";
        boolean contains = false;
        List<DayInfo> selects = getSelectDayList(key);
        if (selects == null) {
            selects = new ArrayList<>();
            selectDayMap.put(key, selects);
        }
        for (int i = 0; i < selects.size(); i++) {
            if (selects.get(i).getLable().equals(lable)) {
                contains = true;
                selects.remove(i);
                break;
            }
        }
        if (!contains) {
            selects.add(new DayInfo(new CalendarInfo(year, month, day), lable, lastDayIndex));
        }
    }

    public void addWeek(WeekInfo info) {
    }

    @NonNull
    private String getKey(int year, int month) {
        return year + "年" + setDouble(month+1) + "月";
    }

    public String setDouble(int value) {
        return String.format("%02d", value);
    }

    public void clear() {
        selectDayMap.clear();
    }
}
