package com.sky.chowder.ui.widget.calendar.lunar;

import java.util.HashMap;

/**
 * 农历所有信息的缓存
 */
public class LunarData {

    private HashMap<String, LunarInfo> mLunarInfo = new HashMap<>();

    private LunarData() {
    }

    public static synchronized LunarData getInstance() {
        return LunarDataHolder.holder;
    }

    private static class LunarDataHolder {
        private static LunarData holder = new LunarData();
    }

    public void clear() {
        mLunarInfo.clear();
    }

    /** 获取农历信息 */
    public synchronized LunarInfo getLunarInfo(int year, int month, int day) {
        String key = getLunarKey(year, month, day);
        if (mLunarInfo.containsKey(key)) {
            return mLunarInfo.get(key);
        } else {
            LunarInfo lunarCalendar = LunarUtil.createLunarInfo(year, month, day);
            mLunarInfo.put(key, lunarCalendar);
            return lunarCalendar;
        }
    }

    private String getLunarKey(int year, int month, int day) {
        return year + "_" + month + "_" + day;
    }
}