package com.sky.chowder.ui.widget.calendar.selecttime;

import com.sky.chowder.ui.widget.calendar.common.CalendarInfo;
import com.sky.chowder.ui.widget.calendar.impl.ISelectTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by libin on 2018/9/26 下午8:06.
 */
public class WeekTime extends ISelectTime<WeekInfo> {

    private Map<String, List<WeekInfo>> selectWeekMap = new HashMap<>();//选中的日期

    @Override
    public Map<String, List<WeekInfo>> getSelectMap() {
        return selectWeekMap;
    }

    @Override
    public void setSelectMap(Map<String, List<WeekInfo>> selectWeekMap) {
        this.selectWeekMap = selectWeekMap;
    }

    @Override
    public List<WeekInfo> getList(int year, int month) {
        return getList(getKey(year, month));
    }

    @Override
    public List<WeekInfo> getList(String key) {
        return selectWeekMap.get(key);
    }

    @Override
    public void add(WeekInfo info) {
        //一个月最多六周
        int index = info.getWeekOfMonth();
        String key = "";
        CalendarInfo day = null;
        //四周内取最后一天肯定是本月的，5，6周取第一天
        if (index < 4) {
            day = info.getInfos().get(6);
            key = getKey(day.getYear(), day.getMonth());
        } else {
            day = info.getInfos().get(0);
            key = getKey(day.getYear(), day.getMonth());
        }
        List<WeekInfo> selects = getList(key);
        boolean contains = false;
        if (selects == null) {
            selects = new ArrayList<>();
            selectWeekMap.put(key, selects);
        }
        for (int i = 0; i < selects.size(); i++) {
            if (selects.contains(info)) {
                selects.remove(info);
                contains = true;
                break;
            }
        }
        if (!contains) {
            selects.add(info);
        }
    }

    @Override
    public void clear() {
        selectWeekMap.clear();
    }
}
