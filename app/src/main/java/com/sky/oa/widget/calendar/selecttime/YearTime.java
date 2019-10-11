package com.sky.oa.widget.calendar.selecttime;

import com.sky.oa.widget.calendar.impl.ISelectTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by libin on 2018/9/26 下午8:06.
 */
public class YearTime extends ISelectTime<YearInfo> {

    private Map<String, List<YearInfo>> selectYearMap = new HashMap<>();//选中的日期

    @Override
    public Map<String, List<YearInfo>> getSelectMap() {
        return selectYearMap;
    }

    @Override
    public void setSelectMap(Map<String, List<YearInfo>> selectYearMap) {
        this.selectYearMap = selectYearMap;
    }

    @Override
    public List<YearInfo> getList(int year, int month) {
        return getList(getKey(year, month));
    }

    @Override
    public List<YearInfo> getList(String key) {
        return selectYearMap.get(key);
    }

    @Override
    public void add(YearInfo info) {
        //一个月最多六周
        String key = getKey(info.getInfo().getYear(), 0);
        List<YearInfo> selects = getList(key);
        if (selects == null) {
            selects = new ArrayList<>();
            selectYearMap.put(key, selects);
        }
        String lable = setDouble(info.getInfo().getYear()) + "年" + setDouble(info.getInfo().getMonth()+1) + "月";
        info.setLable(lable);
        boolean contains = false;
        for (int i = 0; i < selects.size(); i++) {
            if (selects.get(i).getLable().equals(lable)) {
                contains = true;
                selects.remove(i);
                break;
            }
        }
        if (!contains) {
            selects.add(info);
        }
    }

    @Override
    public void clear() {
        selectYearMap.clear();
    }

    @Override
    protected String getKey(int year, int month) {
        return year + "年";
    }
}
