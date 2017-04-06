package com.sky.utils;

import android.text.TextUtils;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by sky on 16/5/10 下午3:50.
 */
public class TextUtil {
    /**
     * 获取控件中的内容
     *
     * @param text 控件
     * @return 控件中的内容
     */
    public static String getText(TextView text) {
        return text.getText().toString().trim();
    }

    /**
     * 非空判断,同时提示
     *
     * @param text  需要判断的文本
     * @param toast 提示内容
     * @return 空为true
     */
    public static boolean notNull(String text, String toast) {
        if (TextUtils.isEmpty(text)) {
            ToastUtils.showShort(ActivityLifecycle.getInstance().getCurrentActivity(), toast + "不能为空");
            return true;
        }
        return false;
    }

    public static boolean notNull2(String text, String toast) {
        if (TextUtils.isEmpty(text)) {
            ToastUtils.showShort(ActivityLifecycle.getInstance().getCurrentActivity(), toast);
            return true;
        }
        return false;
    }

    /**
     * 获取两位小数
     *
     * @param num 需要格式化的字符串数字
     * @return 返回格式化成两位小数的数字
     */
    public static String getDecimalFormat(String num) {
        return getDecimalFormat(Double.parseDouble(num));
    }

    /**
     * 获取两位小数
     *
     * @param num 需要格式化的数字
     * @return 返回格式化成两位小数的数字
     */
    public static String getDecimalFormat(double num) {
        return new DecimalFormat("######0.00").format(num);
    }

    /**
     * 清除HTML
     *
     * @param content 包含html字符串
     * @return 纯字符串
     */
    public static String stripHtml(String content) {
        // <p>段落替换为换行
        content = content.replaceAll("<p .*?>", "");
        // <br><br/>替换为换行
        content = content.replaceAll("<br\\s*/?>", "");
        // 去掉其它的<>之间的东西
        content = content.replaceAll("\\<.*?>", "");
        content = content.replaceAll("\r\n", "");
        content = content.replaceAll("&nbsp;", "");
        // 还原HTML
        // content = HTMLDecoder.decode(content);
        return content;
    }
}
