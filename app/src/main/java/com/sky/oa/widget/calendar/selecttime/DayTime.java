package com.sky.oa.widget.calendar.selecttime;

import com.sky.oa.widget.calendar.impl.ISelectTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by libin on 2018/9/26 下午8:06.
 */
public class DayTime extends ISelectTime<DayInfo> {

    private Map<String, List<DayInfo>> selectDayMap = new HashMap<>();//选中的日期

    @Override
    public void setSelectMap(Map<String, List<DayInfo>> selectDayMap) {
        this.selectDayMap = selectDayMap;
    }

    @Override
    public Map<String, List<DayInfo>> getSelectMap() {
        return selectDayMap;
    }

    @Override
    public List<DayInfo> getList(int year, int month) {
        return getList(getKey(year, month));
    }

    @Override
    public List<DayInfo> getList(String key) {
        return selectDayMap.get(key);
    }

    @Override
    public void add(DayInfo info) {
        String key = getKey(info.getInfo().getYear(), info.getInfo().getMonth());
        String lable = key + setDouble(info.getInfo().getDay());
        boolean contains = false;
        List<DayInfo> selects = getList(key);
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
            info.setLable(lable);
            selects.add(info);
        }
    }


    @Override
    public void clear() {
        selectDayMap.clear();
    }
}
