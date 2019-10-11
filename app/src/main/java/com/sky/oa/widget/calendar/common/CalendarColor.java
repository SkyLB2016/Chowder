package com.sky.oa.widget.calendar.common;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：16/10/17
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class CalendarColor {

    public static int PRIMARY_COLOR = 0xFF289BF0;      //统一主色调

    public static final int BG = 0xFFF5F5F5;           //背景色
    public static final int BLUE = 0xFF289BF0;         //统一蓝色
    public static final int RED = 0xFFF44336;          //统一红色
    public static final int PROJECT = 0xFF1AB394;      //项目绿色
    public static final int TITLE_GRAY = 0xFF666666;   //深黑色
    public static final int STATUS_GRAY = 0xFF999999;  //深黑色
    public static final int FINISH_GRAY = 0xFFCCCCCC;  //深黑色
    public static final int FAFAFA = 0xFFFAFAFA;  //深黑色
    public static final int DARK_GRAY = 0xFF333333;    //深黑色
    public static final int LIGHT_GRAY = 0xFFB3B3B3;   //浅黑色
    public static final int D8D8D8 = 0xFFD8D8D8;   //浅黑色
    public static final int ORIGIN = 0xFFF5A623;       //橘黄色
    public static final int ORIGIN_BORDER = 0xFFE6A020; //橘黄色边框
    public static final int WHITE = 0xFFFFFFFF;        //白色

    public static final int PRIORITY_H = 0xFFFE7F7F;   //优先级-非常紧急 颜色
    public static final int PRIORITY_M = 0xFFFEDA89;   //优先级-紧急 颜色
    public static final int DUEDATE_COLOR = 0xFFFA4F4F;//逾期时间颜色

    public static void setCalendarPrimaryColor(int color) {
        PRIMARY_COLOR = color;
    }
}
