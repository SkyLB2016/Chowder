package com.sky.chowder.ui.widget.calendar.lunar;

import android.text.TextUtils;

import java.util.Locale;

public class LunarInfo {
    private static final String[] lunarCalendarNumber = new String[]{"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
    private static final String[] lunarCalendarTen = new String[]{"初", "十", "廿", "三", "二"};
    private static final String[] year_of_birth = new String[]{"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
    private static final String[] lunarTerm = new String[]{"小寒", "大寒", "立春", "雨水",//
            "惊蛰", "春分", "清明节", "谷雨",//  清明节比较特殊,既是节气也是节日
            "立夏", "小满", "芒种", "夏至",//
            "小暑", "大暑", "立秋", "处暑",//
            "白露", "秋分", "寒露", "霜降",//
            "立冬", "小雪", "大雪", "冬至"};
    private static final String lunarLeapTag = "闰";
    private static final String lunarMonthTag = "月";
    private static final String zhengyueTag = "正";

    public int lunarYear = 0;                   //农历年
    public int lunarMonth = 0;                  //农历月
    public int lunarDay = 0;                    //农历天
    public int solarYear = 0;                   //阳历年
    public int solarMonth = 0;                  //阳历月
    public int solarDay = 0;                    //阳历天
    public boolean isLeapMonth = false;         //是否是闰月
    public boolean isFestival = false;          //是否是节假日
    public boolean isHoliday = false;           //是否放假

    public String lunarYearStr;                 //农历年字符串
    public String lunarMonthStr;                //农历月字符串
    public String lunarDayStr;                  //农历天字符串
    public String traditionFestivalStr;         //传统节假日
    public String internationalFestivalStr;     //国际节假日
    public String lunarSolarTermStr;            //24节气

    public String showStr;                      //日历需要显示的数据

    /**
     * 初始化农历数据
     */
    public void initLunarInfo() {
        if (lunarYear == 0 || lunarMonth == 0 || lunarDay == 0) {
            return;
        }

        lunarYearStr = getLunarYearString();
        lunarMonthStr = getLunarMonthString();
        lunarDayStr = getLunarDayString(true);
        lunarSolarTermStr = getLunarSolarTerms();
        traditionFestivalStr = getTraditionalFestival();
        internationalFestivalStr = getInternationalFestival();

        if (!TextUtils.isEmpty(traditionFestivalStr)) {
            //先显示国际节日
            isFestival = true;
            showStr = traditionFestivalStr;
        } else if (!TextUtils.isEmpty(internationalFestivalStr)) {
            //后显示中国节日
            isFestival = true;
            showStr = internationalFestivalStr;
        } else if (!TextUtils.isEmpty(lunarSolarTermStr)) {
            //再显示节气
            showStr = lunarSolarTermStr;
            //清明节特殊处理,既是节气也是节日
            if ("清明节".equals(showStr)) {
                isFestival = true;
            }
        } else {
            //最后显示默认农历
            showStr = lunarDayStr;
        }
    }

    /**
     * 获取今年属什么
     */
    public String getAnimalsYear() {
        return getAnimalsYear(lunarYear);
    }

    /**
     * 获取今年属什么
     */
    public String getAnimalsYear(int lunarYear) {
        return year_of_birth[(lunarYear - 4) % 12];
    }

    /**
     * 获取农历年
     */
    public String getLunarYearString() {
        return getLunarYearString(lunarYear);
    }

    /**
     * 获取农历年
     */
    public String getLunarYearString(int lunarYear) {
        return String.valueOf(lunarYear);
    }

    /**
     * 获取农历月
     */
    public String getLunarMonthString() {
        return getLunarMonthString(lunarMonth, isLeapMonth);
    }

    /**
     * 获取农历月
     */
    public String getLunarMonthString(int lunarMonth, boolean isLeapMonth) {
        return (isLeapMonth ? lunarLeapTag : "") + ((lunarMonth == 1) ? zhengyueTag : lunarCalendarNumber[lunarMonth - 1]) + lunarMonthTag;
    }

    /**
     * 获取农历天
     */
    public String getLunarDayString(boolean isDisplayLunarMonthForFirstDay) {
        return getLunarDayString(lunarMonth, lunarDay, isLeapMonth, isDisplayLunarMonthForFirstDay);
    }

    /**
     * 获取农历天
     */
    public String getLunarDayString(int lunarMonth, int lunarDay, boolean isLeapMonth, boolean isDisplayLunarMonthForFirstDay) {
        if (lunarDay > 30) {
            return "";
        }
        if (lunarDay == 1 && isDisplayLunarMonthForFirstDay) {
            return getLunarMonthString(lunarMonth, isLeapMonth);
        }
        if (lunarDay == 10) {
            return lunarCalendarTen[0] + lunarCalendarTen[1];
        }
        if (lunarDay == 20) {
            return lunarCalendarTen[4] + lunarCalendarTen[1];
        }

        return lunarCalendarTen[lunarDay / 10] + lunarCalendarNumber[(lunarDay + 9) % 10];
    }

    /**
     * 获取传统节假日
     */
    public String getTraditionalFestival() {
        return getTraditionalFestival(lunarYear, lunarMonth, lunarDay);
    }

    /**
     * 根据农历获取传统节假日
     */
    public String getTraditionalFestival(int lunarYear, int lunarMonth, int lunarDay) {
        if (lunarMonth == 1 && lunarDay == 1) {
            return "春节";
        }
        if (lunarMonth == 1 && lunarDay == 15) {
            return "元宵节";
        }
        if (lunarMonth == 5 && lunarDay == 5) {
            return "端午节";
        }
        if (lunarMonth == 7 && lunarDay == 7) {
            return "七夕";
        }
        if (lunarMonth == 8 && lunarDay == 15) {
            return "中秋节";
        }
        if (lunarMonth == 9 && lunarDay == 9) {
            return "重阳节";
        }
        if (lunarMonth == 12 && lunarDay == 8) {
            return "腊八";
        }
        if (lunarMonth == 12 && lunarDay == 23) {
            return "小年";
        }
        if (lunarMonth == 12 && lunarDay == LunarUtil.getLunarMonthDays(lunarYear, lunarMonth)) {
            return "除夕";
        }
        return "";
    }

    /**
     * 获取国际节假日
     */
    public String getInternationalFestival() {
        return getInternationalFestival(solarMonth, solarDay);
    }

    /**
     * 根据阳历获取国际节假日
     */
    public String getInternationalFestival(int solarMonth, int solarDay) {
        if (solarMonth == 0 && solarDay == 1) {
            return "元旦节";
        }
        if (solarMonth == 1 && solarDay == 14) {
            return "情人节";
        }
        if (solarMonth == 2 && solarDay == 8) {
            return "妇女节";
        }
        if (solarMonth == 2 && solarDay == 12) {
            return "植树节";
        }
        if (solarMonth == 4 && solarDay == 1) {
            return "劳动节";
        }
        if (solarMonth == 4 && solarDay == 4) {
            return "青年节";
        }
        if (solarMonth == 5 && solarDay == 1) {
            return "儿童节";
        }
        if (solarMonth == 7 && solarDay == 1) {
            return "建军节";
        }
        if (solarMonth == 8 && solarDay == 10) {
            return "教师节";
        }
        if (solarMonth == 9 && solarDay == 1) {
            return "国庆节";
        }
        if (solarMonth == 11 && solarDay == 25) {
            return "圣诞节";
        }
        return "";
    }

    /**
     * 根据阳历获取节气
     */
    public String getLunarSolarTerms() {
        int yy;
        double[] lunar_century_C;
        if (solarYear > 1999) {
            yy = solarYear - 2000;
            lunar_century_C = LunarUtil.lunar_21th_century_C;
        } else {
            yy = solarYear - 1900;
            lunar_century_C = LunarUtil.lunar_20th_century_C;
        }
        for (int i = 0; i < lunar_century_C.length; i++) {
            int day;
            if (i < 4) {
                day = (int) Math.floor((yy * LunarUtil.D_solar_terms + lunar_century_C[i]) - ((yy - 1) / 4));
            } else {
                day = (int) Math.floor((yy * LunarUtil.D_solar_terms + lunar_century_C[i]) - (yy / 4));
            }
            if (solarMonth == 0 && solarDay == day && i == 0) {
                return lunarTerm[0];
            }
            if (solarMonth == 0 && solarDay == day && i == 1) {
                return lunarTerm[1];
            }
            if (solarMonth == 1 && solarDay == day && i == 2) {
                return lunarTerm[2];
            }
            if (solarMonth == 1 && solarDay == day && i == 3) {
                return lunarTerm[3];
            }
            if (solarMonth == 2 && solarDay == day && i == 4) {
                return lunarTerm[4];
            }
            if (solarMonth == 2 && solarDay == day && i == 5) {
                return lunarTerm[5];
            }
            if (solarMonth == 3 && solarDay == day && i == 6) {
                return lunarTerm[6];
            }
            if (solarMonth == 3 && solarDay == day && i == 7) {
                return lunarTerm[7];
            }
            if (solarMonth == 4 && solarDay == day && i == 8) {
                return lunarTerm[8];
            }
            if (solarMonth == 4 && solarDay == day && i == 9) {
                return lunarTerm[9];
            }
            if (solarMonth == 5 && solarDay == day && i == 10) {
                return lunarTerm[10];
            }
            if (solarMonth == 5 && solarDay == day && i == 11) {
                return lunarTerm[11];
            }
            if (solarMonth == 6 && solarDay == day && i == 12) {
                return lunarTerm[12];
            }
            if (solarMonth == 6 && solarDay == day && i == 13) {
                return lunarTerm[13];
            }
            if (solarMonth == 7 && solarDay == day && i == 14) {
                return lunarTerm[14];
            }
            if (solarMonth == 7 && solarDay == day && i == 15) {
                return lunarTerm[15];
            }
            if (solarMonth == 8 && solarDay == day && i == 16) {
                return lunarTerm[16];
            }
            if (solarMonth == 8 && solarDay == day && i == 17) {
                return lunarTerm[17];
            }
            if (solarMonth == 9 && solarDay == day && i == 18) {
                return lunarTerm[18];
            }
            if (solarMonth == 9 && solarDay == day && i == 19) {
                return lunarTerm[19];
            }
            if (solarMonth == 10 && solarDay == day && i == 20) {
                return lunarTerm[20];
            }
            if (solarMonth == 10 && solarDay == day && i == 21) {
                return lunarTerm[21];
            }
            if (solarMonth == 11 && solarDay == day && i == 22) {
                return lunarTerm[22];
            }
            if (solarMonth == 11 && solarDay == day && i == 23) {
                return lunarTerm[23];
            }
        }
        return "";
    }

    public boolean isLunarSetting() {
        String language = getLanguageEnv();
        return language != null && ("zh-CN".equals(language.trim()) || "zh-TW".equals(language.trim()));
    }

    private String getLanguageEnv() {
        Locale l = Locale.getDefault();
        String language = l.getLanguage();
        String country = l.getCountry().toLowerCase();
        if ("zh".equals(language)) {
            if ("cn".equals(country)) {
                language = "zh-CN";
            } else if ("tw".equals(country)) {
                language = "zh-TW";
            }
        } else if ("pt".equals(language)) {
            if ("br".equals(country)) {
                language = "pt-BR";
            } else if ("pt".equals(country)) {
                language = "pt-PT";
            }
        }
        return language;
    }
}