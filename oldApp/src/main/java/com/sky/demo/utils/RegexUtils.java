package com.sky.demo.utils;

import java.util.regex.Pattern;

/**
 * @描述 TODO 正则检验
 * @创建者 Chi
 * @创建时间 2015年
 */
public final class RegexUtils {
    /**
     * 各种正则的规则
     **/
    public static final String STRING_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    //    public static final String STRING_EMAIL = "^([a-zA-Z0-9_\\-\\.\\+]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    //public static final String STRING_CHINESE_PHONE_NUMBER = "^1[3|4|5|7|8][0-9]\\d{4,8}$";
    public static final String STRING_CHINESE_PHONE_NUMBER = "^0?1[3|4|5|7|8][0-9]\\d{4,8}$";
    public static final String STRING_CHINESE_TEL_NUMBER = "^0[\\d]{2,3}-[\\d]{7,8}$";
    public static final String STRING_INTEGER_WITHOUT_SIGN = "^[0-9]+$";
    public static final String STRING_INTEGER = "^[-+]?[0-9]+$";
    public static final String STRING_DECIMAL = "^[-+]?[0-9]*\\.?[0-9]+$";
    public static final String STRING_ALPHABETIC = "^[A-Za-z]+$";
    public static final String STRING_IDENTIFIER = "^[A-Za-z0-9_]+$";

    public static final String CARDNUM = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$";


    /**
     * @描述：包括特殊字符匹配 (字符串中不包括符号 数学次方号^ 单引号' 双引号" 分号; 逗号, 帽号: 数学减号- 右尖括号> 左尖括号< 反斜杠/ 即空格, 制表符, 回车符等)
     */
    public static final String STRING_SPECIAL_CHAR = "^[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+$";
    /**
     * @描述：只包含中文
     */
    public static final String STRING_CHINESE = "^[\u4e00-\u9fa5]|[\ufe30-\uffa0]+$";
    /**
     * @描述：只包含中文、英文字母、数字或下划线
     */
    public static final String STRING_CHINESE_IDENTIFIER = "^[\u4e00-\u9fa5]|[\ufe30-\uffa0]|[a-zA-Z0-9_]+$";

    public static final Pattern PATTERN_EMAIL = Pattern.compile(STRING_EMAIL);
    public static final Pattern PATTERN_CHINESE_PHONE_NUMBER = Pattern.compile(STRING_CHINESE_PHONE_NUMBER);
    public static final Pattern PATTERN_CHINESE_TEL_NUMBER = Pattern.compile(STRING_CHINESE_TEL_NUMBER);
    public static final Pattern PATTERN_INTEGER_WITHOUT_SIGN = Pattern.compile(STRING_INTEGER_WITHOUT_SIGN);
    public static final Pattern PATTERN_INTEGER = Pattern.compile(STRING_INTEGER);
    public static final Pattern PATTERN_DECIMAL = Pattern.compile(STRING_DECIMAL);
    public static final Pattern PATTERN_ALPHABETIC = Pattern.compile(STRING_ALPHABETIC);
    public static final Pattern PATTERN_IDENTIFIER = Pattern.compile(STRING_IDENTIFIER);
    public static final Pattern PATTERN_SPECIAL_CHAR = Pattern.compile(STRING_SPECIAL_CHAR);
    public static final Pattern PATTERN_CHINESE = Pattern.compile(STRING_CHINESE);
    public static final Pattern PATTERN_CHINESE_IDENTIFIER = Pattern.compile(STRING_CHINESE_IDENTIFIER);

    public static final Pattern PATTERN_CARNUM = Pattern.compile(CARDNUM);

    /**
     * @param source  需要判断的字符串
     * @param pattern  正则规则
     * @return 符合返回true
     */
    public static boolean matches(String source, String pattern) {
        return Pattern.matches(pattern, source);
    }

    /**
     * @param source 需要判断的字符串
     * @param pattern  正则规则
     * @return  符合返回true
     */
    public static boolean matches(String source, Pattern pattern) {
        return pattern.matcher(source).matches();
    }

    /**
     * 判断是否是邮箱
     *
     * @param source
     * @return
     */
    public static boolean isEmail(String source) {
        return matches(source, PATTERN_EMAIL);
    }

    /**
     * @param source
     * @return 判断是否为车牌号
     */
    public static boolean isCarNum(String source) {
        return matches(source, PATTERN_CARNUM);
    }

    /**
     * 判断是否是中国手机号
     *
     * @param source
     * @return
     */
    public static boolean isChinesePhoneNumber(String source) {
        return matches(source, PATTERN_CHINESE_PHONE_NUMBER);
    }

    /**
     * 判断是否是中国电话
     *
     * @param source
     * @return
     */
    public static boolean isChineseTel(String source) {
        return matches(source, PATTERN_CHINESE_TEL_NUMBER);
    }

    /**
     * 判断是否是整数 (不包含+-.)
     *
     * @param source
     * @return
     */
    public static boolean isIntegerWithoutSign(String source) {
        return matches(source, PATTERN_INTEGER_WITHOUT_SIGN);
    }

    /**
     * 判断是否是整数 (可含有+-)
     *
     * @param source
     * @return
     */
    public static boolean isInteger(String source) {
        return matches(source, PATTERN_INTEGER);
    }

    /**
     * 判断是否是小数
     *
     * @param source
     * @return
     */
    public static boolean isDecimal(String source) {
        return matches(source, PATTERN_DECIMAL);
    }

    /**
     * 判断是否是只有字母
     *
     * @param source
     * @return
     */
    public static boolean isAlphabetic(String source) {
        return matches(source, PATTERN_ALPHABETIC);
    }

    /**
     * 判断是否是只有字母数字下滑线
     *
     * @param source
     * @return
     */
    public static boolean isIdentifier(String source) {
        return matches(source, PATTERN_IDENTIFIER);
    }

    /**
     * @param source
     * @return 不包含特殊字符false
     * @描述 判断是否有特殊字符
     */
    public static boolean hasSpecialChar(String source) {
        return matches(source, PATTERN_SPECIAL_CHAR);
    }

    /**
     * @param source
     * @return 有其他字符false
     * @描述 判断是否只包含中文
     */
    public static boolean isChinese(String source) {
        return matches(source, PATTERN_CHINESE);
    }

    /**
     * @param source
     * @return 有其他字符false
     * @描述 判断是否只包含中文英文字母数字或下划线
     */
    public static boolean isChineseIdentifier(String source) {
        return matches(source, PATTERN_CHINESE_IDENTIFIER);
    }
}