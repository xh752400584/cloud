package com.xh.cloud.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Copyright 2021
 *
 * @author LiLei
 * @date 2021/5/4 1:45:57
 */
public class DateTimeUtil {

    public static final String DATE_SLASH_YMD_PATTERN = "yyyy/MM/dd";
    public static final String DATE_YMD_PATTERN = "yyyy-MM-dd";
    public static final String DATE_MD_PATTERN = "MM-dd";
    public static final String DATE_YM_PATTERN = "yyyy-MM";
    public static final String DATE_YMD_EIGHT_PATTERN = "yyyyMMdd";
    public static final String DATE_YMDHMS_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_MS_PATTERN = "mm:ss";
    public static final String DATE_YMD_CHINESE_PATTERN = "yyyy年MM月dd日";
    public static final String DATE_ZERO_MS_PATTERN = "00:00";

    public static final DateTimeFormatter FORMATTER_0 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter FORMATTER_1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter FORMATTER_2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    public static final DateTimeFormatter FORMATTER_3 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    /**
     * 原子性时间戳值
     */
    private static AtomicLong atomicTimeMills = new AtomicLong(0);

    /**
     * 获取多线程下唯一时间戳标识
     *
     * @return 唯一时间戳
     */
    public static String getUniqueTimestamp() {
        while (true) {
            long currentMill = atomicTimeMills.get();
            long currentTimeMillis = System.currentTimeMillis();
            // 使用CAS自旋实现时间戳唯一性
            if (currentTimeMillis > currentMill && atomicTimeMills.compareAndSet(currentMill, currentTimeMillis)) {
                return Long.toString(currentTimeMillis);
            }
        }
    }

    /**
     * 获取两个时间的相差天数
     *
     * @param start 时间1
     * @param end   时间2
     * @return 相差天数
     */
    public static long getDaysDiffOfTwoTime(LocalDate start, LocalDate end) {
        Assert.notNull(start, "时间参数不能为NULL");
        Assert.notNull(end, "时间参数不能为NULL");
        // 两个时间相差几天
        return ChronoUnit.DAYS.between(start, end);
    }

    public static String dataConverterStr(LocalDateTime dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    public static LocalDateTime dataConverter(String dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateStr, formatter);
    }

    public static String dateToString(Date date, String pattern) {
        return date == null || StringUtils.isBlank(pattern) ? null : new SimpleDateFormat(pattern).format(date);
    }

    public static LocalDateTime dataToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    public static String timeZoneConversion(String dateTime, String srcTimeZone, String destTimeZone) throws ParseException {
        SimpleDateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        srcDf.setTimeZone(TimeZone.getTimeZone(srcTimeZone));
        Date scrDateTime = srcDf.parse(dateTime);
        SimpleDateFormat destDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        destDf.setTimeZone(TimeZone.getTimeZone(destTimeZone));
        String destFormat = destDf.format(scrDateTime);
        return destFormat;
    }

    public static Date strToDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }


    public static LocalDateTime getLocalDateTime(String dateStr, String pattern) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateStr, fmt);
    }

    public static String getLocalDateStr(LocalDate localDate, String pattern) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(pattern);
        return localDate.format(fmt);
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date      
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date      
     */
    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }


    public static Date stringToDate(String date, String pattern) throws ParseException {
        return date == null || StringUtils.isBlank(pattern) ? null : new SimpleDateFormat(pattern).parse(date);
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }


    public static String formatStartTime(String startTime) {
        return startTime + " 00:00:00";
    }

    public static String formatEndTime(String endTime) {
        return endTime + " 23:59:59";
    }

    public static LocalDateTime timeHourUpCeil(LocalDateTime time) {
        // 获取时分秒
        Boolean flag = false;
        String ms = dataConverterStr(time, DATE_MS_PATTERN);
        if (DATE_ZERO_MS_PATTERN.equals(ms)) {
            flag = true;
        }
        // 获取年月日
        String yearMonthDay = dataConverterStr(time, DATE_YMD_PATTERN + " HH");
        String allTime = yearMonthDay + ":00:00";
        // 转换时间加1小时
        LocalDateTime localDateTime = dataConverter(allTime, DATE_YMDHMS_PATTERN);
        return flag ? localDateTime : localDateTime.plusHours(1);
    }

}
