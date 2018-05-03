package com.fh.jaxbdemo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by zhaojie on 2017/4/26.
 */
public class CommonUtils {
    private static final List<Class> primitiveClasses = Arrays.asList(new Class[] {String.class, Integer.class
            , Long.class, Double.class, Float.class, Boolean.class
            , Date.class, BigDecimal.class});

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String toLowerCase(String str) {
        if (StringUtils.isNotEmpty(str)) {
            return str.substring(0, 1).toLowerCase() + str.substring(1);
        }
        return null;
    }

    public static String toUpperCase(String str) {
        if (StringUtils.isNotEmpty(str)) {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
        return null;
    }

    /**
     * 判断是否为基本数据类型和包装器类型以及String类型
     * @param typeClass
     * @return
     */
    public static boolean isPrimitiveType(Class<?> typeClass) {
        return typeClass.isPrimitive() || primitiveClasses.contains(typeClass);
    }

    public static boolean isListType(Class<?> typeClass) {
        return typeClass.getName().equals("java.util.List");
    }

    public static Object getValue(String value, Class<?> typeClass) throws Exception {
        if (typeClass == int.class || typeClass == Integer.class) {
            return StringUtils.isEmpty(value) ? 0 : Integer.parseInt(value);
        } else if (typeClass == long.class || typeClass == Long.class) {
            return StringUtils.isEmpty(value) ? 0 : Long.parseLong(value);
        } else if (typeClass == double.class || typeClass == Double.class) {
            return StringUtils.isEmpty(value) ? 0 : Double.parseDouble(value);
        } else if (typeClass == float.class || typeClass == Float.class) {
            return StringUtils.isEmpty(value) ? 0.0f : Float.parseFloat(value);
        } else if (typeClass == boolean.class || typeClass == Boolean.class) {
            return StringUtils.isEmpty(value) ? false : Boolean.parseBoolean(value);
        } else if (typeClass == BigDecimal.class) {
            return StringUtils.isEmpty(value) ? 0 : new BigDecimal(value);
        } else if (typeClass == Date.class) {
            return StringUtils.isEmpty(value) ? null : sdf.parse(value);
        }
        return StringUtils.isEmpty(value) ? "" : value;
    }

    public static String objectToString(Object object, Class<?> typeClass) throws Exception {
        if (null == object)
            return null;

        if (typeClass == int.class || typeClass == Integer.class) {
            return String.valueOf(object);
        } else if (typeClass == long.class || typeClass == Long.class) {
            return String.valueOf(object);
        } else if (typeClass == double.class || typeClass == Double.class) {
            return String.valueOf(object);
        } else if (typeClass == float.class || typeClass == Float.class) {
            return String.valueOf(object);
        } else if (typeClass == boolean.class || typeClass == Boolean.class) {
            return String.valueOf(object);
        } else if (typeClass == BigDecimal.class) {
            return String.valueOf(object);
        } else if (typeClass == Date.class) {
            return sdf.format(object);
        }
        return object.toString();
    }

    /**
     * 是否null字符串
     * @param s
     * @return
     */
    public static boolean isNullString(String s) {
        return null == s || "null".equalsIgnoreCase(s);
    }
}
