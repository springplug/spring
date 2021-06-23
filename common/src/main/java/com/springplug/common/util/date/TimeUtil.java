package com.springplug.common.util.date;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * 
 * Copyright © 2019 zhangpanxiang All rights reserved.
 * 
 * 功能描述：
 * @Package: com.zzmg.common.util.date
 * @author: zhangpanxiang  
 * @date: 2019年5月16日 上午9:55:04
 */
public class TimeUtil extends LocalDateUtil {

	/**
	 * 不允许实例化
	 */
	private TimeUtil() {
	}
	
	/**
	 * 获取当前时分秒
	 * 
	 * @return 字符串时间 格式：HH:mm:ss
	 */
	public static String getNow() {
		return localTimeToString(getLocalTime(), timeformatter);
	}

	/**
	 * 时间戳转时分秒字符串
	 * 
	 * @param timeStamp 时间戳
	 * @return 返回格式 HH:mm:ss
	 */
	public static String timeStampToString(Object timeStamp) {
		return timeStampToString(timeStamp, timeformatter);
	}

	/**
	 * 指定时间增加hour小时
	 * 
	 * @param arg  指定时间 格式 HH:mm:ss
	 * @param hour 增加的小时数
	 * @return 计算后的时分秒字符串
	 */
	public static String parseHours(String arg, int hour) {
		return parse(arg,hour, ChronoUnit.HOURS);
	}

	/**
	 * 指定时间增加minute分钟
	 * 
	 * @param arg    指定时间 格式 HH:mm:ss
	 * @param minute 增加的分钟数
	 * @return 计算后的时分秒字符串
	 */
	public static String parseMinutes(String arg, int minute) {
		return parse(arg,minute, ChronoUnit.MINUTES);
	}
	
	/**
	 * 指定时间增加
	 * @param arg 指定时间 格式 HH:mm:ss
	 * @param time 增加的时间
	 * @param unit 类型 时/分/秒
	 * @return 计算后的时分秒字符串
	 */
	public static String parse(String arg,int time,TemporalUnit unit) {
		return localTimeToString(parse2LocalTime(arg,time,unit), timeformatter);
	}
	
	/**
	 * 指定时间增加
	 * @param arg 指定时间 格式 HH:mm:ss
	 * @param time 增加的时间
	 * @param unit 类型 时/分/秒
	 * @return 计算后的LocalTime
	 */
	public static LocalTime parse2LocalTime(String arg,int time,TemporalUnit unit) {
		return stringToLocalTime(arg, timeformatter).plus(time,unit);
	}

	/**
	 * 判断指定时分秒是否在当前时分秒之后
	 * 
	 * @param arg 指定时分秒
	 * @return 是返回true 否返回false
	 */
	public static boolean isAfter(String arg) {
		return stringToLocalTime(arg, timeformatter).isAfter(getLocalTime());
	}

	/**
	 * 判断指定时分秒是否在当前时分秒之前
	 * 
	 * @param arg 指定时分秒
	 * @return 是返回true 否或者相等返回false
	 */
	public static boolean isBefore(String arg) {
		return stringToLocalTime(arg, timeformatter).isBefore(getLocalTime());
	}

	/**
	 * 判断arg0时分秒是否在arg1时分秒之后
	 * 
	 * @param arg0 时分秒 格式HH:mm:ss
	 * @param arg1 时分秒 格式HH:mm:ss
	 * @return 是返回true 否返回false
	 */
	public static boolean isAfter(String arg0, String arg1) {
		return stringToLocalTime(arg0, timeformatter).isAfter(stringToLocalTime(arg1, timeformatter));
	}

	/**
	 * 判断arg0时分秒是否在arg1时分秒之后
	 * 
	 * @param arg0 时分秒 格式HH:mm:ss
	 * @param arg1 时分秒 格式HH:mm:ss
	 * @return 是返回true 否返回false
	 */
	public static boolean isBefore(String arg0, String arg1) {
		return stringToLocalTime(arg0, timeformatter).isBefore(stringToLocalTime(arg1, timeformatter));
	}

	/**
	 * 判断arg0时间戳是否在arg1时间戳之后(只判断时分秒)
	 * 
	 * @param arg0 时间戳
	 * @param arg1 时间戳
	 * @return 是返回true 否返回false
	 */
	public static boolean isAfter(Long arg0, Long arg1) {
		return timeStampToLocalTime(arg0).isAfter(timeStampToLocalTime(arg1));
	}

	/**
	 * 判断arg0时间戳是否在arg1时间戳之前(只判断时分秒)
	 * 
	 * @param arg0 时间戳
	 * @param arg1 时间戳
	 * @return 是返回true 否返回false
	 */
	public static boolean isBefore(Long arg0, Long arg1) {
		return timeStampToLocalTime(arg0).isBefore(timeStampToLocalTime(arg1));
	}

	/**
	 * 判断指定时分秒与当前时分秒相差多少秒(当前时分秒-指定时分秒)
	 * 
	 * @param arg 指定时分秒 格式HH:mm:ss
	 * @return 返回相差秒数 可能为负数
	 */
	public static Long period(String arg) {
		return stringToLocalTime(arg, timeformatter).until(getLocalTime(), ChronoUnit.SECONDS);
	}

	/**
	 * 判断指定时分秒与当前时分秒相差多少秒(当前时分秒-指定时分秒)
	 * 
	 * @param arg 指定时分秒 格式HH:mm:ss
	 * @return 返回相差秒数 可能为负数
	 */
	public static Long period(int arg) {
		return period(arg * 1000L);
	}

	/**
	 * 判断指定时分秒与当前时分秒相差多少秒(当前时分秒-指定时分秒)
	 * 
	 * @param arg 指定时分秒 格式HH:mm:ss
	 * @return 返回相差秒数 可能为负数
	 */
	public static Long period(Long arg) {
		return timeStampToLocalTime(arg).until(getLocalTime(), ChronoUnit.SECONDS);
	}

	/**
	 * 判断arg0时分秒与arg1时分秒相差多少秒(arg1-arg0)
	 * 
	 * @param arg0 指定时分秒 格式HH:mm:ss
	 * @param arg1 指定时分秒 格式HH:mm:ss
	 * @return 返回相差秒数 可能为负数
	 */
	public static Long period(String arg0, String arg1) {
		return stringToLocalTime(arg0, timeformatter).until(stringToLocalTime(arg1, timeformatter), ChronoUnit.SECONDS);
	}

	/**
	 * 判断arg0时间戳与arg1时间戳相差多少秒(arg1-arg0)
	 * 
	 * @param arg0 指定时间戳
	 * @param arg1 指定时间戳
	 * @return 返回相差秒数 可能为负数
	 */
	public static Long period(Long arg0, Long arg1) {
		return timeStampToLocalTime(arg0).until(timeStampToLocalTime(arg1), ChronoUnit.SECONDS);
	}

}
