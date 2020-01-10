package com.sky.oa.widget.calendar.impl;


import com.sky.oa.widget.calendar.CalendarView;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2017/1/11
 * 描    述：
 * 修订历史：
 * ================================================
 */
public interface OnInitListener {
    /**
     * 初始化之前的准备工作
     *
     * @param view 自身
     */
    void onInitBefore(CalendarView view);

    /**
     * 初始化完成后，需要的操作
     * @param view 自身
     */
    void onInitFinished(CalendarView view);
}
