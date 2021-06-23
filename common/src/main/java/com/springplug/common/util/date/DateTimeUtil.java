package com.springplug.common.util.date;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 
 * Copyright © 2019 zhangpanxiang All rights reserved.
 * 
 * 功能描述：
 * @Package: com.zzmg.common.util.date
 * @author: zhangpanxiang  
 * @date: 2019年5月16日 上午9:54:45
 */
public class DateTimeUtil extends LocalDateUtil {
	
	/**
	 * 不允许实例化
	 */
	private DateTimeUtil() {
	}

	/**
	 * 获取当前时间
	 * 
	 * @return 字符串时间 格式：yyyy-mm-dd HH:mm:ss
	 */
	public static String getNow() {
		return getNow(dateTimeformatter);
	}

	/**
	 * 时间戳转时间字符串
	 * 
	 * @param arg 需要转换的参数
	 * @return 字符串时间 格式：yyyy-mm-dd HH:mm:ss
	 */
	public static String timeStampToString(Object arg) {
		return timeStampToString(arg, dateTimeformatter);
	}

	/**
	 * 字符串日期转时间戳10位
	 * 
	 * @param arg 格式 yyyy-MM-dd HH:mm:ss
	 * @return 返回10位时间戳
	 */
	public static int stringToTimeStamp10(String arg) {
		return stringToTimeStamp10(arg, dateTimeformatter);
	}

	/**
	 * 字符串日期转时间戳13位
	 * 
	 * @param arg 格式 yyyy-MM-dd HH:mm:ss
	 * @return 返回13位时间戳
	 */
	public static Long stringToTimeStamp13(String arg) {
		return stringToTimeStamp13(arg, dateTimeformatter);
	}

	/**
	 * 获取指定时间的当月第一天
	 * 
	 * @param arg 指定的时间字符串
	 * @return 返回第一天的字符串日期 格式 yyyy-MM-dd HH:mm:ss
	 */
	public static String withDayOfMonth(String arg) {
		return withDayOfMonth(arg, dateTimeformatter);
	}

	/**
	 * 对指定时间加day天
	 * 
	 * @param arg 指定的时间
	 * @param day 要加的天数
	 * @return 返回计算后的结果
	 */
	public static String plusDays(String arg, int day) {
		return plusDays(arg, day, dateTimeformatter);
	}

	/**
	 * 对当前时间加day天
	 * 
	 * @param day 要加的天数
	 * @return 返回计算后的结果
	 */
	public static String plusDays(int day) {
		return plusDays(getNow(), day, dateTimeformatter);
	}

	/**
	 * 对指定时间戳加day天
	 * 
	 * @param arg 指定的时间
	 * @param day 要加的天数
	 * @return 返回计算后的结果
	 */
	public static String plusDays(Integer arg, int day) {
		return plusDays(arg * 1000L, day);
	}

	/**
	 * 对指定时间戳加day天
	 * 
	 * @param arg 指定的时间
	 * @param day 要加的天数
	 * @return 返回计算后的结果
	 */
	public static String plusDays(Long arg, int day) {
		if (arg <= Integer.MAX_VALUE) {
			arg = arg * 1000L;
		}
		return localDateTimeToString(timeStampToLocalDateTime(arg).plusDays(day), dateTimeformatter);
	}

	/**
	 * 对当前时间加month月
	 * 
	 * @param month 要加的月数
	 * @return 返回计算后的结果
	 */
	public static String plusMonths(int month) {
		return plusMonths(getNow(), month, dateTimeformatter);
	}

	/**
	 * 对指定时间加month月
	 * 
	 * @param arg   指定的时间
	 * @param month 要加的月数
	 * @return 返回计算后的结果
	 */
	public static String plusMonths(String arg, int month) {
		return plusMonths(arg, month, dateTimeformatter);
	}

	/**
	 * 对指定时间戳加month月
	 * 
	 * @param arg   指定的时间
	 * @param month 要加的月数
	 * @return 返回计算后的结果
	 */
	public static String plusMonths(Integer arg, int month) {
		return plusMonths(arg * 1000L, month);
	}

	/**
	 * 对指定时间戳加month月
	 * 
	 * @param arg   指定的时间
	 * @param month 要加的月数
	 * @return 返回计算后的结果
	 */
	public static String plusMonths(Long arg, int month) {
		if (arg <= Integer.MAX_VALUE) {
			arg = arg * 1000L;
		}
		return localDateTimeToString(timeStampToLocalDateTime(arg).plusMonths(month), dateformatter);
	}

	/**
	 * 判断指定时间是否为闰年
	 * 
	 * @param arg 指定时间
	 * @return 闰年返回true 不是闰年返回false
	 */
	public static boolean isLeapYear(String arg) {
		return LocalDate.parse(arg, ofPattern(dateTimeformatter)).isLeapYear();
	}

	/**
	 * 判断指定时间戳是否为闰年
	 * 
	 * @param arg 指定时间戳
	 * @return 闰年返回true 不是闰年返回false
	 */
	public static boolean isLeapYear(Integer arg) {
		return isLeapYear(arg * 1000L);
	}

	/**
	 * 判断指定时间戳是否为闰年
	 * 
	 * @param arg 指定时间戳
	 * @return 闰年返回true 不是闰年返回false
	 */
	public static boolean isLeapYear(Long arg) {
		if (arg <= Integer.MAX_VALUE) {
			arg = arg * 1000L;
		}
		return timeStampToLocalDate(arg).isLeapYear();
	}

	/**
	 * 判断指定时间是否在当前时间之后
	 * 
	 * @param arg 指定时间
	 * @return 是返回true 否或者相等返回false
	 */
	public static boolean isAfter(String arg) {
		return stringToLocalDateTime(arg, dateTimeformatter).isAfter(getLocalDateTime());
	}
	
	/**
	 * 判断指定时间是否在当前时间之后
	 * 
	 * @return 是返回true 否或者相等返回false
	 */
	public static boolean isAfter(Date date) {
		return dateToLocalDateTime(date).isAfter(getLocalDateTime());
	}

	/**
	 * 判断指定时间是否在当前时间之前
	 * 
	 * @param arg 指定时间
	 * @return 是返回true 否返回false
	 */
	public static boolean isBefore(String arg) {
		return stringToLocalDateTime(arg, dateTimeformatter).isBefore(getLocalDateTime());
	}
	
	/**
	 * 判断指定时间是否在当前时间之前
	 * 
	 * @return 是返回true 否返回false
	 */
	public static boolean isBefore(Date date) {
		return dateToLocalDateTime(date).isBefore(getLocalDateTime());
	}

	/**
	 * 判断arg0时间是否在arg1时间之后
	 * 
	 * @param arg0 时间 格式yyyy-MM-dd HH:mm:ss
	 * @param arg1 时间 格式yyyy-MM-dd HH:mm:ss
	 * @return 是返回true 否返回false
	 */
	public static boolean isAfter(String arg0, String arg1) {
		return stringToLocalDateTime(arg0, dateTimeformatter).isAfter(stringToLocalDateTime(arg1, dateTimeformatter));
	}

	/**
	 * 判断arg0时间是否在arg1时间之前
	 * 
	 * @param arg0 时间 格式yyyy-MM-dd HH:mm:ss
	 * @param arg1 时间 格式yyyy-MM-dd HH:mm:ss
	 * @return 是返回true 否返回false
	 */
	public static boolean isBefore(String arg0, String arg1) {
		return stringToLocalDateTime(arg0, dateTimeformatter).isBefore(stringToLocalDateTime(arg1, dateTimeformatter));
	}

	/**
	 * 判断arg0时间戳是否在arg1时间戳之后
	 * 
	 * @param arg0 时间戳
	 * @param arg1 时间戳
	 * @return 是返回true 否返回false
	 */
	public static boolean isAfter(Long arg0, Long arg1) {
		return timeStampToLocalDateTime(arg0).isAfter(timeStampToLocalDateTime(arg1));
	}

	/**
	 * 判断arg0时间戳是否在arg1时间戳之前
	 * 
	 * @param arg0 时间戳
	 * @param arg1 时间戳
	 * @return 是返回true 否返回false
	 */
	public static boolean isBefore(Long arg0, Long arg1) {
		return timeStampToLocalDateTime(arg0).isBefore(timeStampToLocalDateTime(arg1));
	}

	/**
	 * 判断指定日期与当前日期相差多少天(当前日期-指定日期)
	 * 
	 * @param arg 指定日期 格式yyyy-MM-dd
	 * @return 返回相差天数 可能为负数
	 */
	public static Long period(String arg) {
		return stringToLocalDateTime(arg, dateTimeformatter).until(getLocalDateTime(), ChronoUnit.DAYS);
	}

	/**
	 * 判断指定时间与当前时间相差多少天(当前日期-指定日期)
	 * 
	 * @param arg 指定时间戳
	 * @return 返回相差天数 可能为负数
	 */
	public static Long period(int arg) {
		return period(arg * 1000L);
	}

	/**
	 * 判断指定时间与当前时间相差多少天(当前日期-指定日期)
	 * 
	 * @param arg 指定时间戳
	 * @return 返回相差天数 可能为负数
	 */
	public static Long period(Long arg) {
		if (arg <= Integer.MAX_VALUE) {
			arg = arg * 1000L;
		}
		return timeStampToLocalDateTime(arg).until(getLocalDateTime(), ChronoUnit.DAYS);
	}

	/**
	 * 判断arg0时间与arg1时间相差多少天(arg1-arg0)
	 * 
	 * @param arg0 指定时间 格式yyyy-MM-dd HH:mm:ss
	 * @param arg1 指定时间 格式yyyy-MM-dd HH:mm:ss
	 * @return 返回相差天数 可能为负数
	 */
	public static Long period(String arg0, String arg1) {
		return stringToLocalDateTime(arg0, dateTimeformatter).until(stringToLocalDateTime(arg1, dateTimeformatter),
				ChronoUnit.DAYS);
	}

	/**
	 * 判断arg0时间戳与arg1时间戳相差多少天(arg1-arg0)
	 * 
	 * @param arg0 指定时间戳
	 * @param arg1 指定时间戳
	 * @return 返回相差天数 可能为负数
	 */
	public static Long period(Long arg0, Long arg1) {
		return timeStampToLocalDateTime(arg0).until(timeStampToLocalDateTime(arg1), ChronoUnit.DAYS);
	}

	/**
	 * 字符串日期转时间戳13位
	 * 
	 * @param arg       需要转换的参数
	 * @param formatter 时间格式
	 * @return 返回13位时间戳
	 */
	public static Long stringToTimeStamp13(String arg, String formatter) {
		return localDateTimeToInstant(stringToLocalDateTime(arg, formatter)).toEpochMilli();
	}

	/**
	 * 字符串日期转时间戳10位
	 * 
	 * @param arg       需要转换的参数
	 * @param formatter 时间格式
	 * @return 返回13位时间戳
	 */
	public static int stringToTimeStamp10(String arg, String formatter) {
		return (int) stringToLocalDateTime(arg, formatter).toEpochSecond(zoneOffset);
	}

	/**
	 * 获取指定时间的当月第一天
	 * 
	 * @param arg       指定的时间字符串
	 * @param formatter 指定的时间格式
	 * @return 返回第一天的字符串日期 格式 yyyy-MM-dd
	 */
	public static String withDayOfMonth(String arg, String formatter) {
		return localDateTimeToString(stringToLocalDateTime(arg, formatter).withDayOfMonth(1), dateTimeformatter);
	}

	/**
	 * 对指定时间加day天
	 * 
	 * @param arg       指定的时间
	 * @param day       要加的天数
	 * @param formatter 指定时间的格式
	 * @return 返回计算后的结果
	 */
	public static String plusDays(String arg, int day, String formatter) {
		return localDateTimeToString(stringToLocalDateTime(arg, formatter).plusDays(day), dateTimeformatter);
	}

	/**
	 * 对指定时间加month月
	 * 
	 * @param arg       指定的时间
	 * @param formatter 指定时间的格式
	 * @return 返回计算后的结果
	 */
	public static String plusMonths(String arg, int month, String formatter) {
		return localDateTimeToString(stringToLocalDateTime(arg, formatter).plusMonths(month), dateTimeformatter);
	}

}
