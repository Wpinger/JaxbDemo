package com.fh.jaxbdemo;

import android.util.Patterns;

import java.util.UUID;

/**
 * Created by Jie on 2017/12/5.
 */

public class StringUtils {
    private final static String REGEX_NUMBER = "^[1-9]\\d*$"; // 正整数
    private final static String REGEX_DECIMAL = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"; // DECIMAL

    private StringUtils() {

    }

    /**
     * isEmpty
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        if (s == null || s.length() == 0 || s.trim().length() == 0)
            return true;
        else
            return false;
    }

    /**
     * isNotEmpty
     *
     * @param s
     * @return
     */
    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    /**
     * equals
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean equals(CharSequence s1, CharSequence s2) {
        if (s1 == s2)
            return true;
        if (s1 != null && s2 != null) {
            int length = s1.length();
            if (length == s2.length()) {
                if (s1 instanceof String && s2 instanceof String) {
                    return s1.equals(s2);
                } else {
                    for (int i = 0; i < length; i++) {
                        if (s1.charAt(i) != s2.charAt(i))
                            return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * trim
     *
     * @param s
     * @param defaultValue
     * @return if s is null, return defaultValue
     */
    public static String trim(String s, String defaultValue) {
        return s == null ? defaultValue : s.trim();
    }

    /**
     * length
     *
     * @param s
     * @return if s is null, return 0;
     */
    public static int length(CharSequence s) {
        return null == s ? 0 : s.length();
    }

    /**
     * isPhone
     *
     * @param s
     * @return
     */
    public static boolean isPhone(String s) {
        return Patterns.PHONE.matcher(s).matches();
    }

    /**
     * isNumber
     *
     * @param s
     * @return
     */
    public static boolean isNumber(String s) {
        return matches(s, REGEX_NUMBER);
    }

    /**
     * isEmail
     *
     * @param s
     * @return
     */
    public static boolean isEmail(String s) {
        return Patterns.EMAIL_ADDRESS.matcher(s).matches();
    }

    /**
     * matches
     *
     * @param s
     * @param regex
     * @return
     */
    public static boolean matches(String s, String regex) {
        if (null != s && null != regex) {
            return s.matches(regex);
        }
        return false;
    }

    /**
     * 填充全0字符串
     *
     * @param length
     * @return
     */
    public static String fillZeroString(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append("00");
        }
        return stringBuilder.toString();
    }

    /**
     * 判断是否全0包
     *
     * @param data
     * @return
     */
    public static boolean checkZeroString(String data) {
        if (StringUtils.isNotEmpty(data)) {
            char[] characters = data.toCharArray();
            int i = 0, length = characters.length;
            for (i = 0; i < length; i++) {
                if (characters[i] != '0')
                    return false;
            }
        }
        return true;
    }

    /**
     * 去掉字符串后面的空格
     *
     * @param contextString 待处理的字符串
     * @return
     */
    public static String delSpacing(String contextString) {
        String ntr = ("A" + contextString).trim().substring(1);
        contextString = ntr;
        return contextString;
    }

    /**
     * 获取UUID
     *
     * @return
     */
    public static String getUUIDString() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        return str.replaceAll("-", "").toUpperCase();
    }

    /**
     * 校验端口号是否合法
     *
     * @param port
     * @return
     */
    public static boolean checkPortValid(String port) {
        return port.matches("([1-9]|[1][0-2])-([1-9]|[1][0-6])-([1-9]|[1][0-9]|[2][0-4])");
    }

    /**
     * 校验字符串是否全为空格
     * @param s
     * @return
     */
    public static boolean checkSpaceString(String s) {
        if (null == s) {
            return true;
        }
        if (null == s.trim() || 0 == s.trim().length()) {
            return true;
        }
        return false;
    }
}
