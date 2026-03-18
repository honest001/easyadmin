package com.easyadmin.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {
    /**
     * 日期格式：yyyy-MM-dd
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    
    /**
     * 时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 月份格式：yyyy-MM
     */
    public static final String MONTH_FORMAT = "yyyy-MM";
    
    /**
     * 格式化日期
     * @param date 日期对象
     * @param format 格式
     * @return 格式化后的字符串
     */
    public static String format(LocalDate date, String format) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(format));
    }
    
    /**
     * 格式化日期时间
     * @param dateTime 日期时间对象
     * @param format 格式
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime dateTime, String format) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }
    
    /**
     * 解析日期字符串
     * @param dateStr 日期字符串
     * @param format 格式
     * @return 日期对象
     */
    public static LocalDate parseDate(String dateStr, String format) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(format));
    }
    
    /**
     * 解析日期时间字符串
     * @param dateTimeStr 日期时间字符串
     * @param format 格式
     * @return 日期时间对象
     */
    public static LocalDateTime parseDateTime(String dateTimeStr, String format) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(format));
    }
    
    /**
     * 获取当前日期
     * @return 当前日期
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }
    
    /**
     * 获取当前日期时间
     * @return 当前日期时间
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}