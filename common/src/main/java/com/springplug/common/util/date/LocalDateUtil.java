package com.springplug.common.util.date;

import com.springplug.common.util.Util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 
 * Copyright © 2019 zhangpanxiang All rights reserved.
 * 
 * 功能描述：
 * @Package: com.zzmg.common.util.date
 * @author: zhangpanxiang  
 * @date: 2019年5月16日 上午9:54:59
 */
public class LocalDateUtil extends Util {

	public final static String yearformatter = "yyyy";
	
	public final static String dateTimeformatter = "yyyy-MM-dd HH:mm:ss";
	
	public final static String dateTimeNoSymbolformatter = "yyyyMMddHHmmss";

	public final static String dateformatter = "yyyy-MM-dd";

	public final static String timeformatter = "HH:mm:ss";

	public final static ZoneId zoneId = ZoneId.systemDefault();

	public final static ZoneOffset zoneOffset = ZoneOffset.of("+8");

	public final static String dateformatterYTD = "yyyy年MM月dd日";
	
	/**
	 * 不允许实例化
	 */
	protected LocalDateUtil() {
	}

	/**
	 * 获取当前时间10位的时间戳
	 * 
	 * @return 10位时间戳
	 */
	public static int getNowTimeStamp() {
		return (int) (operation(LocalDateTime::now).toEpochSecond(zoneOffset));
	}

	/**
	 * 获取当前时间的时分秒
	 * 
	 * @return LocalTime
	 */
	public static LocalTime getLocalTime() {
		return operation(LocalTime::now).withNano(0);
	}

	/**
	 * 获取当前时间的日期
	 * 
	 * @return LocalDate
	 */
	public static LocalDate getLocalDate() {
		return operation(LocalDate::now);
	}

	/**
	 * 获取当前时间
	 * 
	 * @return LocalDateTime
	 */
	public static LocalDateTime getLocalDateTime() {
		return operation(LocalDateTime::now);
	}

	/**
	 * 获取当前日期
	 * 
	 * @return 字符串时间格式
	 */
	public static String getNow(String formatter) {
		return localDateTimeToString(getLocalDateTime(), formatter);
	}

	/**
	 * 判断本年是否为闰年
	 * 
	 * @return 闰年返回true 不是闰年返回false
	 */
	public static boolean isLeapYear() {
		return operation(getLocalDate()::isLeapYear);
	}

	/**
	 * 通过日期格式返回 DateTimeFormatter
	 * 
	 * @param formatter 日期格式
	 * @return DateTimeFormatter
	 */
	public static DateTimeFormatter ofPattern(String formatter) {
		return DateTimeFormatter.ofPattern(formatter);
	}

	/**
	 * 时间戳转字符串
	 * 
	 * @param arg 需要转换的参数
	 * @return 时间字符串
	 */
	public static String timeStampToString(Object arg, String formatter) {
		long timeStamp = Long.parseLong(arg.toString());
		if (timeStamp <= Integer.MAX_VALUE) {
			timeStamp = timeStamp * 1000L;
		}
		return timeStampToLocalDateTime(timeStamp).format(ofPattern(formatter));
	}

	/**
	 * 时间戳转LocalDateTime
	 * 
	 * @param arg 需要转换的参数
	 * @return 返回LocalDateTime
	 */
	public static LocalDateTime timeStampToLocalDateTime(Long arg) {
		if (arg <= Integer.MAX_VALUE) {
			arg = arg * 1000L;
		}
		return instantToLocalDateTime(Instant.ofEpochMilli(arg));
	}

	/**
	 * 字符串转LocalDateTime
	 * 
	 * @param arg 需要转换的参数
	 * @return 返回LocalDateTime
	 */
	public static LocalDateTime stringToLocalDateTime(String arg, String formatter) {
		return LocalDateTime.parse(arg, ofPattern(formatter));
	}

	/**
	 * LocalDateTime 转字符串
	 * 
	 * @param arg       需要转换的参数
	 * @param formatter 时间格式
	 * @return 时间字符串
	 */
	public static String localDateTimeToString(LocalDateTime arg, String formatter) {
		return arg.format(ofPattern(formatter));
	}

	/**
	 * 字符串转LocalDate
	 * 
	 * @param arg 需要转换的参数
	 * @return 返回LocalDate
	 */
	public static LocalDate stringToLocalDate(String arg, String formatter) {
		return LocalDate.parse(arg, ofPattern(formatter));
	}

	/**
	 * 时间戳转LocalDate
	 * 
	 * @param arg 需要转换的参数
	 * @return 返回LocalDate
	 */
	public static LocalDate timeStampToLocalDate(Long arg) {
		if (arg <= Integer.MAX_VALUE) {
			arg = arg * 1000L;
		}
		return instantToLocalDate(Instant.ofEpochMilli(arg));
	}
	
	/**
	 * 时间戳转LocalTime
	 * 
	 * @param arg 需要转换的参数
	 * @return 返回LocalTime
	 */
	public static LocalTime timeStampToLocalTime(Long arg) {
		if (arg <= Integer.MAX_VALUE) {
			arg = arg * 1000L;
		}
		return timeStampToLocalDateTime(arg).toLocalTime();
	}

	/**
	 * LocalDate 转字符串
	 * 
	 * @param arg       需要转换的参数
	 * @param formatter 时间格式
	 * @return 时间字符串
	 */
	public static String localDateToString(LocalDate arg, String formatter) {
		return arg.format(ofPattern(formatter));
	}
	
	/**
	 * LocalTime 转字符串
	 * 
	 * @param arg       需要转换的参数
	 * @param formatter 时间格式
	 * @return 时分秒字符串
	 */
	public static String localTimeToString(LocalTime arg, String formatter) {
		return arg.format(ofPattern(formatter));
	}

	/**
	 * date 转字符串
	 *
	 * @return 时分秒字符串
	 */
	public static String dateToString(Date date) {
		return localDateTimeToString(dateToLocalDateTime(date),dateformatter);
	}
	
	/**
	 * date 转字符串
	 * 
	 * @param formatter 时间格式
	 * @return 时分秒字符串
	 */
	public static String dateToString(Date date, String formatter) {
		return localDateTimeToString(dateToLocalDateTime(date),formatter);
	}
	
	/**
	 * 字符串 转LocalTime
	 * 
	 * @param arg       需要转换的参数
	 * @param formatter 时间格式
	 * @return LocalTime
	 */
	public static LocalTime stringToLocalTime(String arg, String formatter) {
		return LocalTime.parse(arg, ofPattern(formatter));
	}
	
	/**
	 * 字符串 转Date
	 * 
	 * @param arg       需要转换的参数
	 * @param formatter 时间格式
	 * @return Date
	 */
	public static Date stringToDate(String arg, String formatter) {
		return localDateTimeToDate(stringToLocalDateTime(arg,formatter));
	}
	
	

	/**
	 * date日期类型转LocalDateTime
	 * 
	 * @param date 需要转换的date
	 * @return 转换后的LocalDateTime
	 */
	public static LocalDateTime dateToLocalDateTime(Date date) {
		return instantToLocalDateTime(dateToInstant(date));
	}

	/**
	 * date日期类型转LocalDate
	 * 
	 * @param date 需要转换的date
	 * @return 转换后的LocalDate
	 */
	public static LocalDate dateToLocalDate(Date date) {
		return dateToLocalDateTime(date).toLocalDate();
	}

	/**
	 * date日期类型转LocalTime
	 * 
	 * @param date 需要转换的date
	 * @return 转换后的LocalTime
	 */
	public static LocalTime dateToLocalTime(Date date) {
		return dateToLocalDateTime(date).toLocalTime();
	}

	/**
	 * LocalDateTime日期类型转date
	 * 
	 * @param localDateTime 需要转换的localDateTime
	 * @return 转换后的date
	 */
	public static Date localDateTimeToDate(LocalDateTime localDateTime) {
		return Date.from(localDateTimeToInstant(localDateTime));
	}

	/**
	 * localDate日期类型转date
	 * 
	 * @param localDate 需要转换的localDate
	 * @return 转换后的date
	 */
	public static Date localDateToDate(LocalDate localDate) {
		return Date.from(localDateToInstant(localDate));
	}

	/**
	 * localTime时分秒类型转date
	 * 
	 * @param localDate 需要转换的localDate(年月日)
	 * @param localTime 需要转换的localTime(时分秒)
	 * @return 转换后的date
	 */
	public static Date localTimeToDate(LocalDate localDate, LocalTime localTime) {
		return Date.from(localDateTimeToInstant(LocalDateTime.of(localDate, localTime)));
	}

	/**
	 * localDate日期类型转Instant
	 * 
	 * @param localDate 需要转换的localDate
	 * @return 转换后的Instant
	 */
	public static Instant localDateToInstant(LocalDate localDate) {
		return localDate.atStartOfDay(zoneId).toInstant();
	}

	/**
	 * localDateTime日期类型转Instant
	 * 
	 * @param localDateTime 需要转换的localDateTime
	 * @return 转换后的Instant
	 */
	public static Instant localDateTimeToInstant(LocalDateTime localDateTime) {
		return localDateTime.atZone(zoneId).toInstant();
	}

	/**
	 * Date日期类型转Instant
	 * 
	 * @param date 需要转换的date
	 * @return 转换后的Instant
	 */
	public static Instant dateToInstant(Date date) {
		return operation(date::toInstant);
	}

	/**
	 * Instant日期类型转localDate
	 * 
	 * @param instant 需要转换的instant
	 * @return 转换后的localDate
	 */
	public static LocalDate instantToLocalDate(Instant instant) {
		return instant.atZone(zoneId).toLocalDate();
	}

	/**
	 * Instant日期类型转localDateTime
	 * 
	 * @param instant 需要转换的instant
	 * @return 转换后的localDateTime
	 */
	public static LocalDateTime instantToLocalDateTime(Instant instant) {
		return LocalDateTime.ofInstant(instant, zoneId);
	}

	/**
	 * Instant日期类型转Date
	 * 
	 * @param instant 需要转换的instant
	 * @return 转换后的Date
	 */
	public static Date instantToDate(Instant instant) {
		return Date.from(instant);
	}

}
