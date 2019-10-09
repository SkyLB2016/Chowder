package com.sky.oa.widget;

import android.text.SpannableStringBuilder;

import com.sky.sdk.utils.LogUtils;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by libin on 2019/10/09 14:28 Wednesday.
 */
public class Test {
    private void checkCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        LogUtils.i("时间==" + calendar.getTimeInMillis());
        calendar.set(year, month, 1, 0, 0, 0);
        LogUtils.i("时间==" + calendar.getTimeInMillis());
        calendar.set(year, month + 1, 1, 0, 0, 0);
        LogUtils.i("时间==" + calendar.getTimeInMillis());
        LogUtils.i("时间==" + calendar.get(Calendar.MONTH));
        LogUtils.i("时间==" + calendar.get(Calendar.DATE));

        calendar.setTimeInMillis(calendar.getTimeInMillis() - 1000);

        LogUtils.i("时间==" + calendar.get(Calendar.MONTH));
        LogUtils.i("时间==" + calendar.get(Calendar.DATE));
    }

    SpannableStringBuilder builder = new SpannableStringBuilder();

    private static final String EMAIL = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";

    private void gsonBuilder() {
//        pattern("1136096189@qq.com",EMAIL);
//        pattern("n.(/","n\\.\\(\\/");
//        presenter.getActi();
//        video();
        long start = System.currentTimeMillis();
        LogUtils.i("值为==" + getNumber(40));
        long end = System.currentTimeMillis();
        LogUtils.i("所耗时间==" + (end - start));

        start = System.currentTimeMillis();
        LogUtils.i("40值为==" + getNumber1(40));
        LogUtils.i("50值为==" + getNumber1(50));
        end = System.currentTimeMillis();
        LogUtils.i("所耗时间==" + (end - start));

    }

    public int getNumber(int num) {
        if (num == 1 || num == 2) {
            return 1;
        } else {
            return getNumber(num - 1) + getNumber(num - 2);
        }
    }

    public int getNumber1(int num) {
        if (num == 1 || num == 2) {
            return 1;
        }
        int temp = 0;
        int n_1 = 1;//前一个
        int n_2 = 1;//前二个
        for (int i = 3; i <= num; i++) {
            temp = n_1 + n_2;
            n_2 = n_1;
            n_1 = temp;
        }
        return temp;
    }

//    String text = "aaa";
//        LogUtils.i("aaa==" + isNumerEX("aaa"));
//        LogUtils.i("-a=" + isNumerEX("-a"));
//        LogUtils.i("12a==" + isNumerEX("12a"));
//        LogUtils.i("a12==" + isNumerEX("a12"));
//        LogUtils.i("111==" + isNumerEX("111"));
//        LogUtils.i("1.11==" + isNumerEX("1.11"));
//        LogUtils.i("1.11342==" + isNumerEX("1.11342"));
//        LogUtils.i("-2==" + isNumerEX("-2"));
//        LogUtils.i("-1==" + isNumerEX("-"));
//        LogUtils.i("-1.22222==" + isNumerEX("-1.22222"));

    /**
     * 判断字符串是否为数字
     * 包括负数
     *
     * @param str
     * @return
     */
    public static boolean isNumerEX(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
        Pattern pattern1 = Pattern.compile("-?[0-9]*");
        return pattern.matcher(str).matches() || pattern1.matcher(str).matches();
    }

    private void pattern(String text, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        if (m.find()) {
//            int size = m.groupCount();
//            LogUtils.i("正则==" + m.group(0));
            LogUtils.i("正则==" + m.matches());
//            LogUtils.i("正则==" + size);
//            for (int i = 0; i < size; i++) {
//                LogUtils.i("正则==" + m.group(i));
//            }
        }
    }

}
