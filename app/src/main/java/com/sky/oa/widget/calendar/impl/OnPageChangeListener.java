package com.sky.oa.widget.calendar.impl;

import com.sky.oa.widget.calendar.common.CalendarInfo;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2017/1/11
 * 描    述：
 * 修订历史：
 * ================================================
 */
public interface OnPageChangeListener {

    void onPageScrolled(CalendarInfo info);         //在滑动

    void onPageSelectedChange(CalendarInfo info);   //当前layer变化

    void onPageScrollEnd(CalendarInfo info);        //滑动结束
}