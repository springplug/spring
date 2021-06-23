package com.springplug.common.util.date;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * Copyright © 2019 zhangpanxiang All rights reserved.
 * 
 * 功能描述：
 * @Package: com.zzmg.common.util.date
 * @author: zhangpanxiang  
 * @date: 2019年5月16日 上午9:54:51
 */
public class DateUtil extends LocalDateUtil {

	/**
	 * 不允许实例化
	 */
	private DateUtil() {
	}
	
	/**
	 * 获取当前日期
	 * 
	 * @return 字符串时间 格式：yyyy-mm-dd
	 */
	public static String getNow() {
		return getNow(dateformatter);
	}

	/**
	 * 时间戳转日期字符串
	 * 
	 * @param arg 需要转换的参数
	 * @return 字符串时间 格式：yyyy-mm-dd
	 */
	public static String timeStampToString(Object arg) {
		return timeStampToString(arg, dateformatter);
	}

	/**
	 * 字符串日期转时间戳10位
	 * 
	 * @param arg 格式 yyyy-MM-dd
	 * @return 返回10位时间戳
	 */
	public static int stringToTimeStamp10(String arg) {
		return stringToTimeStamp10(arg, dateformatter);
	}

	/**
	 * 字符串日期转时间戳13位
	 * 
	 * @param arg 格式 yyyy-MM-dd
	 * @return 返回13位时间戳
	 */
	public static Long stringToTimeStamp13(String arg) {
		return stringToTimeStamp13(arg, dateformatter);
	}

	/**
	 * 获取当月第一天
	 * 
	 * @return 返回第一天的字符串日期 格式 yyyy-MM-dd
	 */
	public static String withDayOfMonth() {
		return withDayOfMonth(getNow(), dateformatter);
	}

	/**
	 * 获取指定日期的当月第一天
	 * 
	 * @param arg 指定的时间字符串
	 * @return 返回第一天的字符串日期 格式 yyyy-MM-dd
	 */
	public static String withDayOfMonth(String arg) {
		return withDayOfMonth(arg, dateformatter);
	}

	/**
	 * 获取指定10位时间戳的当月第一天
	 * 
	 * @param arg 指定的时间戳
	 * @return 返回第一天的字符串日期 格式 yyyy-MM-dd
	 */
	public static String withDayOfMonth(Integer arg) {
		return withDayOfMonth(arg * 1000L);
	}

	/**
	 * 获取指定13位时间戳的当月第一天
	 * 
	 * @param arg 指定的时间戳
	 * @return 返回第一天的字符串日期 格式 yyyy-MM-dd
	 */
	public static String withDayOfMonth(Long arg) {
		return localDateToString(timeStampToLocalDate(arg).withDayOfMonth(1), dateformatter);
	}

	/**
	 * 获取当月最后一天
	 * 
	 * @return 返回第一天的字符串日期 格式 yyyy-MM-dd
	 */
	public static String lastDayOfMonth() {
		return lastDayOfMonth(getNow(), dateformatter);
	}

	/**
	 * 获取指定日期的当月最后一天
	 * 
	 * @param arg 指定的时间字符串
	 * @return 返回第一天的字符串日期 格式 yyyy-MM-dd
	 */
	public static String lastDayOfMonth(String arg) {
		return lastDayOfMonth(arg, dateformatter);
	}

	/**
	 * 获取指定10位时间戳的当月最后一天
	 * 
	 * @param arg 指定的时间戳
	 * @return 返回第一天的字符串日期 格式 yyyy-MM-dd
	 */
	public static String lastDayOfMonth(Integer arg) {
		return lastDayOfMonth(arg * 1000L);
	}

	/**
	 * 获取指定13位时间戳的当月最后一天
	 * 
	 * @param arg 指定的时间戳
	 * @return 返回第一天的字符串日期 格式 yyyy-MM-dd
	 */
	public static String lastDayOfMonth(Long arg) {
		return localDateToString(timeStampToLocalDate(arg).with(TemporalAdjusters.lastDayOfMonth()), dateformatter);
	}

	/**
	 * 对指定日期加day天
	 * 
	 * @param arg 指定的时间
	 * @param day 要加的天数
	 * @return 返回计算后的结果
	 */
	public static String plusDays(String arg, int day) {
		return plusDays(arg, day, dateformatter);
	}

	/**
	 * 对当前时间加day天
	 * 
	 * @param day 要加的天数
	 * @return 返回计算后的结果
	 */
	public static String plusDays(int day) {
		return plusDays(getNow(), day, dateformatter);
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
		return localDateToString(timeStampToLocalDate(arg).plusDays(day), dateformatter);
	}

	/**
	 * 对当前日期加month月
	 * 
	 * @param month 要加的月数
	 * @return 返回计算后的结果
	 */
	public static String plusMonths(int month) {
		return plusMonths(getNow(), month, dateformatter);
	}

	/**
	 * 对指定日期加month月
	 * 
	 * @param arg   指定的时间
	 * @param month 要加的月数
	 * @return 返回计算后的结果
	 */
	public static String plusMonths(String arg, int month) {
		return plusMonths(arg, month, dateformatter);
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
		return localDateToString(timeStampToLocalDate(arg).plusMonths(month), dateformatter);
	}

	/**
	 * 判断指定日期是否为闰年
	 * 
	 * @param arg 指定日期
	 * @return 闰年返回true 不是闰年返回false
	 */
	public static boolean isLeapYear(String arg) {
		return stringToLocalDate(arg, dateformatter).isLeapYear();
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
		return timeStampToLocalDate(arg).isLeapYear();
	}

	/**
	 * 判断指定日期是否在当前日期之后
	 * 
	 * @param arg 指定日期
	 * @return 是返回true 否返回false
	 */
	public static boolean isAfter(String arg) {
		return stringToLocalDate(arg, dateformatter).isAfter(getLocalDate());
	}

	/**
	 * 判断指定日期是否在当前日期之前
	 * 
	 * @param arg 指定日期
	 * @return 是返回true 否或者相等返回false
	 */
	public static boolean isBefore(String arg) {
		return stringToLocalDate(arg, dateformatter).isBefore(getLocalDate());
	}

	/**
	 * 判断arg0日期是否在arg1日期之后
	 * 
	 * @param arg0 日期 格式yyyy-MM-dd
	 * @param arg1 日期 格式yyyy-MM-dd
	 * @return 是返回true 否返回false
	 */
	public static boolean isAfter(String arg0, String arg1) {
		return stringToLocalDate(arg0, dateformatter).isAfter(stringToLocalDate(arg1, dateformatter));
	}

	/**
	 * 判断arg0日期是否在arg1日期之后
	 * 
	 * @param arg0 日期 格式yyyy-MM-dd
	 * @param arg1 日期 格式yyyy-MM-dd
	 * @return 是返回true 否返回false
	 */
	public static boolean isBefore(String arg0, String arg1) {
		return stringToLocalDate(arg0, dateformatter).isBefore(stringToLocalDate(arg1, dateformatter));
	}

	/**
	 * 判断arg0时间戳是否在arg1时间戳之后
	 * 
	 * @param arg0 时间戳
	 * @param arg1 时间戳
	 * @return 是返回true 否返回false
	 */
	public static boolean isAfter(Long arg0, Long arg1) {
		return timeStampToLocalDate(arg0).isAfter(timeStampToLocalDate(arg1));
	}

	/**
	 * 判断arg0时间戳是否在arg1时间戳之前
	 * 
	 * @param arg0 时间戳
	 * @param arg1 时间戳
	 * @return 是返回true 否返回false
	 */
	public static boolean isBefore(Long arg0, Long arg1) {
		return timeStampToLocalDate(arg0).isBefore(timeStampToLocalDate(arg1));
	}

	/**
	 * 判断指定日期与当前日期相差多少天(当前日期-指定日期)
	 * 
	 * @param arg 指定日期 格式yyyy-MM-dd
	 * @return 返回相差天数 可能为负数
	 */
	public static Long period(String arg) {
		return stringToLocalDate(arg, dateformatter).until(getLocalDate(), ChronoUnit.DAYS);
	}

	/**
	 * 判断指定日期与当前日期相差多少天(当前日期-指定日期)
	 * 
	 * @param arg 指定日期 格式yyyy-MM-dd
	 * @return 返回相差天数 可能为负数
	 */
	public static Long period(int arg) {
		return period(arg * 1000L);
	}

	/**
	 * 判断指定日期与当前日期相差多少天(当前日期-指定日期)
	 * 
	 * @param arg 指定日期 格式yyyy-MM-dd
	 * @return 返回相差天数 可能为负数
	 */
	public static Long period(Long arg) {
		return timeStampToLocalDate(arg).until(getLocalDate(), ChronoUnit.DAYS);
	}

	/**
	 * 判断arg0日期与arg1日期相差多少天(arg1-arg0)
	 * 
	 * @param arg0 指定日期 格式yyyy-MM-dd
	 * @param arg1 指定日期 格式yyyy-MM-dd
	 * @return 返回相差天数 可能为负数
	 */
	public static Long period(String arg0, String arg1) {
		return stringToLocalDate(arg0, dateformatter).until(stringToLocalDate(arg1, dateformatter), ChronoUnit.DAYS);
	}

	/**
	 * 判断arg0时间戳与arg1时间戳相差多少天(arg1-arg0)
	 * 
	 * @param arg0 指定时间戳
	 * @param arg1 指定时间戳
	 * @return 返回相差天数 可能为负数
	 */
	public static Long period(Long arg0, Long arg1) {
		return timeStampToLocalDate(arg0).until(timeStampToLocalDate(arg1), ChronoUnit.DAYS);
	}

	/**
	 * 字符串日期转时间戳10位
	 * 
	 * @param arg       需要转换的参数
	 * @param formatter 时间格式
	 * @return 返回13位时间戳
	 */
	public static int stringToTimeStamp10(String arg, String formatter) {
		return (int) stringToLocalDate(arg, formatter).atStartOfDay(zoneId).toEpochSecond();
	}

	/**
	 * 字符串日期转时间戳13位
	 * 
	 * @param arg       需要转换的参数
	 * @param formatter 时间格式
	 * @return 返回13位时间戳
	 */
	public static Long stringToTimeStamp13(String arg, String formatter) {
		return localDateToInstant(stringToLocalDate(arg, formatter)).toEpochMilli();
	}

	/**
	 * 获取指定日期的当月第一天
	 * 
	 * @param arg       指定的时间字符串
	 * @param formatter 指定的时间格式
	 * @return 返回第一天的字符串日期 格式 yyyy-MM-dd
	 */
	public static String withDayOfMonth(String arg, String formatter) {
		return localDateToString(stringToLocalDate(arg, formatter).withDayOfMonth(1), dateformatter);
	}

	/**
	 * 获取指定时间的当月最后一天
	 * 
	 * @param arg       指定的时间字符串
	 * @param formatter 指定的时间格式
	 * @return 返回最后一天的字符串日期 格式 yyyy-MM-dd
	 */
	public static String lastDayOfMonth(String arg, String formatter) {
		return localDateToString(stringToLocalDate(arg, formatter).with(TemporalAdjusters.lastDayOfMonth()),
				dateformatter);
	}

	/**
	 * 对指定日期加day天
	 * 
	 * @param arg       指定的时间
	 * @param day       要加的天数
	 * @param formatter 指定时间的格式
	 * @return 返回计算后的结果
	 */
	public static String plusDays(String arg, int day, String formatter) {
		return localDateToString(stringToLocalDate(arg, formatter).plusDays(day), dateformatter);
	}

	/**
	 * 对指定时间加month月
	 * 
	 * @param arg       指定的时间
	 * @param month       要加的月数
	 * @param formatter 指定时间的格式
	 * @return 返回计算后的结果
	 */
	public static String plusMonths(String arg, int month, String formatter) {
		return localDateToString(stringToLocalDate(arg, formatter).plusMonths(month), dateformatter);
	}

	/**
	 * @author: jwt 返回时间格式:"yyyy年MM月dd日"
	 * @param arg
	 * @return
	 */
	public static String getDateYTD(Object arg){
		return timeStampToString(arg, dateformatterYTD);
	}

	//判断选择的日期是否是本周
	public static boolean isThisWeek(long time) {
		Calendar calendar = Calendar.getInstance();
		int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
		calendar.setTime(new Date(time));
		int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
		if (paramWeek == currentWeek) {
			return true;
		}
		return false;
	}

	//判断选择的日期是否是今天
	public static boolean isToday(long time) {
		return isThisTime(time, "yyyy-MM-dd");
	}

	//判断选择的日期是否是本月
	public static boolean isThisMonth(long time) {
		return isThisTime(time, "yyyy-MM");
	}

	private static boolean isThisTime(long time, String pattern) {
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String param = sdf.format(date);//参数时间
		String now = sdf.format(new Date());//当前时间
		if (param.equals(now)) {
			return true;
		}
		return false;
	}
}
