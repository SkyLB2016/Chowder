package com.sky.utils;

import android.text.TextUtils;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by SKY on 16/5/10 下午3:50.
 */
public class TextUtil {
    /**
     * @param text 控件
     * @return 获取控件中的内容
     */
    public static String getText(TextView text) {
        return text.getText().toString().trim();
    }

    /**
     * @param text  需要判断的文本
     * @param toast 提示内容
     * @return 空为true
     */
    public static boolean notNull(String text, String toast) {
        if (!TextUtils.isEmpty(text)) return false;
        ToastUtils.showShort(ActivityLifecycle.getInstance().getCurrentAct(), toast);
        return true;
    }

    public static boolean notNullObj(Object obj, String toast) {
        if (null != obj) return false;
        ToastUtils.showShort(ActivityLifecycle.getInstance().getCurrentAct(), toast);
        return true;
    }

    /**
     * 返回格式化成整数的数字
     */
    public static DecimalFormat formatInt() {
        return new DecimalFormat("#0");
    }

    /**
     * 格式化成两位小数的数字
     */
    public static DecimalFormat formatDou() {
        return new DecimalFormat("#0.00");
    }

    /**
     * 单位换算
     *
     * @param size 单位为B
     * @return 转换后的单位
     */
    public static String formatSize(long size) {
        if (size < 1024 && size > 0)
            return size + "B";
        else if (size < 1024 * 1024)
            return formatDou().format(size / 1024d) + "K";
        else if (size < 1024 * 1024 * 1024)
            return formatDou().format(size / (1024 * 1024d)) + "M";
        else if (size < 1024 * 1024 * 1024 * 1024)
            return formatDou().format(size / (1024 * 1024 * 1024d)) + "G";
        else
            return formatDou().format(size / (1024 * 1024 * 1024 * 1024d)) + "T";
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
