package com.sky.oa.widget.calendar.common;

import com.sky.oa.widget.calendar.CalendarView;
import com.sky.oa.widget.calendar.selecttime.WeekInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarUtil {

    /**
     * @param year           当前的年
     * @param month          当前的月
     * @param isShowOutMonth true，数组填满， false，非本月的用0填充
     * @param array          大小为2 ，记录当月的起始索引和结束索引
     * @return 返回大小为42的数组
     */
    public static int[] getDayOfMonth(int year, int month, boolean isShowOutMonth, int[] array) {
        int[] result = new int[42];
        if (array == null || array.length < 2) {
            array = new int[2];
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, 1);
        int monthStart = calendar.get(Calendar.DAY_OF_WEEK);

        //动态计算每周开始的星期
        monthStart = (monthStart + 7 - CalendarView.FIRST_DAY) % 7;

        //填充上月数据
        calendar.set(Calendar.DAY_OF_MONTH, 0);              //将日期设置为上一个月
        int dayInMonth = calendar.get(Calendar.DAY_OF_MONTH);//获取上一个月的最后一天
        for (int i = monthStart - 1; i >= 0; i--) {
            if (isShowOutMonth) {
                result[i] = dayInMonth;
                dayInMonth--;
            } else {
                result[i] = 0;
            }
        }

        //填充本月数据
        calendar.clear();
        calendar.set(year, month, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < monthDay; i++) {
            result[monthStart + i] = i + 1;
        }
        int end = monthStart + monthDay;

        //填充下月数据
        for (int i = end; i < 42; i++) {
            if (isShowOutMonth) {
                result[i] = i - end + 1;
            } else {
                result[i] = 0;
            }
        }

        array[0] = monthStart;
        array[1] = end - 1;
        return result;
    }

    /**
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static List<WeekInfo> getWeekOfMonth(int year, int month, int day) {
        List<WeekInfo> weeks = new ArrayList<>();//周数据
        int weekOfYear = 0;
        int weekOfMonth = 0;

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month, 1);
        //本月第一天是周几
        int monthStart = cal.get(Calendar.DAY_OF_WEEK);
        weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        //系统内以周日为一周的开始，值是1，APP内需要以周一开始，所以第一天是周日的话，年舟weekOfYear需要减1
        if (monthStart == 1) {
            weekOfYear--;
        }
        //动态计算每周开始的星期
        //计算在APP内本月1号在星期中所占的位置
        monthStart = (monthStart + 7 - CalendarView.FIRST_DAY) % 7;

        //一周所包含的日期
        List<CalendarInfo> info = new ArrayList<>();

        //第一周可能所包含上月的日期的日期
        cal.set(Calendar.DAY_OF_MONTH, 0);              //将日期设置为上一个月
        int dayInMonth = cal.get(Calendar.DAY_OF_MONTH);//获取上一个月的最后一天
        //填充上月数据
        for (int i = 0; i < monthStart; i++) {
            info.add(0, new CalendarInfo(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), dayInMonth));
            dayInMonth--;
        }
        //填充本月数据
        cal.clear();
        cal.set(year, month+1, 1);//设为下月一号
        cal.set(Calendar.DAY_OF_MONTH, 0);//设为本月最后一天
        int monthDay = cal.get(Calendar.DAY_OF_MONTH);//获取本月最后一天
        for (int i = 1; i <= monthDay; i++) {
            info.add(new CalendarInfo(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), i));

            //够七个数据里，填充，并初始化。
            if (info.size() == 7) {
                weeks.add(new WeekInfo(info, weekOfYear++, weekOfMonth++));
                info = new ArrayList<>();
            }
        }
        //不为空，填补下月数据
        if (!info.isEmpty()) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
//            for (int i = 1; i <= 7 - info.size(); i++) {
//                info.add(new CalendarInfo(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), i));
//            }
            int temp = 1;
            while (info.size() < 7) {
                info.add(new CalendarInfo(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), temp++));
            }
            weeks.add(new WeekInfo(info, weekOfYear++, weekOfMonth++));
        }
        return weeks;
    }

    /**
     * @param year  当前的年
     * @param month 当前的月
     * @param day   当前的日
     * @return 当前周的数据
     */
    public static int[] getDayOfWeek(int year, int month, int day) {
        int[] dayOfMonth = getDayOfMonth(year, month, true, null);
        int[] result = new int[7];
        int index = 0;
        boolean flag = false;
        for (int i = 0; i < dayOfMonth.length; i++) {
            if (dayOfMonth[i] == 1) {
                flag = true;
            }
            if (flag && day == dayOfMonth[i]) {
                index = i;
                break;
            }
        }
        int row = index / 7;
        System.arraycopy(dayOfMonth, row * 7, result, 0, 7);
        return result;
    }

    /**
     * 获取当前时间的年月日
     */
    public static int[] getYMD(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(time);
        int[] result = new int[3];
        result[0] = calendar.get(Calendar.YEAR);
        result[1] = calendar.get(Calendar.MONTH);
        result[2] = calendar.get(Calendar.DAY_OF_MONTH);
        return result;
    }

    public static long[] calSyncStartAndEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long start = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, 2);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        long end = calendar.getTimeInMillis();
        return new long[]{start, end};
    }

    /**
     * 首页拉取时间  8天
     */
    public static long[] calSyncIndexStartAndEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long start = calendar.getTimeInMillis();
        long end = start + 8 * 24 - 1;
        return new long[]{start, end};
    }

    public static long[] calPlanSyncStartAndEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long start = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, 2);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        long end = calendar.getTimeInMillis();
        return new long[]{start, end};
    }

    /**
     * 获取当前时间的 0点时间
     */
    public static long getTimeInMillisByYMD(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当前时间的 0点时间
     */
    public static long getTimeOfZeroInMillis(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当前时间的 24点时间
     */
    public static long getTomorrowTimeOfZeroInMillis(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当前时间所属月份的最后一天24:00:00的时间
     */
    public static long getCurrentMonthEndDay(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当前时间所属月份前一个月的第一天24:00:00的时间
     */
    public static long getCurrentMonthStartDay(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
}