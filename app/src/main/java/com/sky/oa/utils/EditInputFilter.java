package com.sky.oa.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 长度为12为，包含两位小数以及小数点
 */
public class EditInputFilter implements InputFilter {

    Pattern mPattern;
    //输入的最大数字
    private static final double MAX_VALUE = Double.MAX_VALUE;
    //小数点后的位数
    private static final int POINTER_LENGTH = 2;
    private static final String POINTER = ".";
    private static final String ZERO = "0";

    public EditInputFilter() {
        mPattern = Pattern.compile("([0-9]|\\.)*");
    }

    /**
     * @param source 新输入的字符串
     * @param start  新输入的字符串起始下标，一般为0
     * @param end    新输入的字符串终点下标，一般为source长度-1
     * @param dest   输入之前文本框内容
     * @param dstart 原内容起始坐标，一般为0
     * @param dend   原内容终点坐标，一般为dest长度-1
     * @return 输入内容
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String sourceText = source.toString();
        String destText = dest.toString();
        //验证删除等按键
        if (TextUtils.isEmpty(sourceText)) {
            return "";
        }
        Matcher matcher = mPattern.matcher(source);
        //已经输入小数点的情况下，只能输入数字
        if (destText.contains(POINTER)) {
            if (!matcher.matches()) {
                return "";
            } else {
                if (POINTER.equals(source)) { //只能输入一个小数点
                    return "";
                }
            }
            //验证小数点精度，保证小数点后只能输入两位
            int index = destText.indexOf(POINTER);
            if (index < dend) {
                int length = destText.length()-index;
                if (length > POINTER_LENGTH) {
                    return dest.subSequence(dstart, dend);
                }
            }
        } else {
            //没有输入小数点的情况下，只能输入小数点和数字，但首位不能输入小数点和0
            if (!matcher.matches()) {
                return "";
            } else {
                if ((POINTER.equals(source) || ZERO.equals(source)) && TextUtils.isEmpty(destText)) {
                    return "";
                }
            }
        }
        //验证输入熟悉的大小
        double sumText = Double.parseDouble(destText + sourceText);
        if (sumText > MAX_VALUE) {
            return dest.subSequence(dstart, dend);
        }
        if (dest.toString().length() == 12) {
            return "";
        }
        return dest.subSequence(dstart, dend) + sourceText;
    }

    private String getText(String text) {
        if (!TextUtils.isEmpty(text) && text.contains(".")) {
            String last = text.substring(text.indexOf(".") + 1);
            if (last.length() > 1) {
                DecimalFormat format = new DecimalFormat("#0.00");
                return format.format(Double.parseDouble(text));
            }
        }
        return text;
    }
}