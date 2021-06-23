package com.springplug.common.util.bigdecimal;

import com.springplug.common.util.Util;

import java.math.BigDecimal;
import java.util.function.Predicate;

/**
 * 
 * Copyright © 2019 zhangpanxiang All rights reserved.
 * 
 * 功能描述：
 * @Package: com.zzmg.common.util.bigdecimal
 * @author: zhangpanxiang  
 * @date: 2019年5月16日 上午9:54:31
 */
public class BigDecimalUtils extends Util {
	
	private final static int _scale=2;
	
	/**
	 * 不允许实例化
	 */
	private BigDecimalUtils() {
	}

	/**
	 * 判断是否相等 是则返回true 否则返回false
	 * 
	 * @param args0 判断相等的参数1
	 * @param args1 判断相等的参数2
	 * @return true或者false
	 */
	public static boolean equals(Object args0, Object args1) {
		Predicate<Object> predicate = x -> BigDecimalNewAndScale2Down(x).equals(BigDecimalNewAndScale2Down(args1));
		return predicate.test(args0);
	}

	/**
	 * 数字相加
	 * 
	 * @param <T>   泛型
	 * @param args0 相加的参数1
	 * @param args1 相加的参数2
	 * @return 根据第二个参数的类型返回对应类型
	 */
	public static <T> T add(Object args0, T args1) {
		BigDecimal bigDecimal = operation(args0, args1, (x, y) -> BigDecimalNewAndScale2Down(x)
				.add(BigDecimalNewAndScale2Down(y)).setScale(_scale, BigDecimal.ROUND_DOWN));
		return ReturnObject(bigDecimal, args1);
	}

	/**
	 * 数字相减
	 *
	 * @param <T>   泛型
	 * @param args0 相加的参数1
	 * @param args1 相加的参数2
	 * @return 根据第二个参数的类型返回对应类型
	 */
	public static <T> T subtract(Object args0, T args1,int scale) {
		BigDecimal bigDecimal = operation(args0, args1, (x, y) -> BigDecimalNewAndScale2Down(x)
				.subtract(BigDecimalNewAndScale2Down(y)).setScale(scale, BigDecimal.ROUND_DOWN));
		return ReturnObject(bigDecimal, args1);
	}

	/**
	 * 数字相减
	 * 
	 * @param <T>   泛型
	 * @param args0 相加的参数1
	 * @param args1 相加的参数2
	 * @return 根据第二个参数的类型返回对应类型
	 */
	public static <T> T subtract(Object args0, T args1) {
		return subtract(args0, args1,_scale);
	}

	/**
	 * 数字相乘
	 *
	 * @param <T>   泛型
	 * @param args0 相加的参数1
	 * @param args1 相加的参数2
	 * @return 根据第二个参数的类型返回对应类型
	 */
	public static <T> T multiply(Object args0, T args1,int scale) {
		BigDecimal bigDecimal = operation(args0, args1, (x, y) -> BigDecimalNewAndScale2Down(x)
				.multiply(BigDecimalNewAndScale2Down(y)).setScale(scale, BigDecimal.ROUND_DOWN));
		return ReturnObject(bigDecimal, args1);
	}

	/**
	 * 数字相乘
	 * 
	 * @param <T>   泛型
	 * @param args0 相加的参数1
	 * @param args1 相加的参数2
	 * @return 根据第二个参数的类型返回对应类型
	 */
	public static <T> T multiply(Object args0, T args1) {
		return multiply(args0, args1,_scale);
	}

	/**
	 * 数字相除
	 * 
	 * @param <T>   泛型
	 * @param args0 相加的参数1
	 * @param args1 相加的参数2
	 * @return 根据第二个参数的类型返回对应类型
	 */
	public static <T> T divide(Object args0, T args1) {
		BigDecimal bigDecimal = operation(args0, args1, (x, y) -> BigDecimalNewAndScale2Down(x)
				.divide(BigDecimalNewAndScale2Down(y), _scale, BigDecimal.ROUND_DOWN));
		return ReturnObject(bigDecimal, args1);
	}

	/**
	 * 数字相除
	 *
	 * @param <T>   泛型
	 * @param args0 相加的参数1
	 * @param args1 相加的参数2
	 * @return 根据第二个参数的类型返回对应类型
	 */
	public static <T> T divide(Object args0, T args1,int scale) {
		BigDecimal bigDecimal = operation(args0, args1, (x, y) -> BigDecimalNewAndScale2Down(x)
				.divide(BigDecimalNewAndScale2Down(y), scale, BigDecimal.ROUND_DOWN));
		return ReturnObject(bigDecimal, args1);
	}

	/**
	 * 字符串转换为数字
	 * @param num 字符串数字
	 * @param label 标签名称
	 * @return BigDecimal
	 */
	public static BigDecimal parseBigDecimal(String num, String label) throws Exception {
		try {
			return new BigDecimal(num);
		}catch (Exception e){
			throw new Exception(String.format("[%s]字符串转换数字格式错误",label));
		}
	}

	/**
	 * object 转 BigDecimal
	 * 
	 * @param args 需要转换的参数
	 * @return 转换后的BigDecimal
	 */
	private static BigDecimal BigDecimalNewAndScale2Down(Object args) {
		return get(args, BigDecimal::new).setScale(_scale, BigDecimal.ROUND_DOWN);
	}

	/**
	 * 根据传入第二个参数的类型返回对应类型
	 * @param <T> 泛型
	 * @param bigDecimal 需要转换类型的BigDecimal
	 * @param arg1 需要返回的类型参数
	 * @return 返回对应类型
	 */
	@SuppressWarnings("unchecked")
	private static <T> T ReturnObject(BigDecimal bigDecimal, T arg1) {
		if (isString(arg1)) {
			return (T) bigDecimal.toString();
		} else if (isInteger(arg1)) {
			return (T) Integer.valueOf(bigDecimal.intValue());
		} else if (isDouble(arg1)) {
			return (T) Double.valueOf(bigDecimal.doubleValue());
		} else if (isFloat(arg1)) {
			return (T) Float.valueOf(bigDecimal.floatValue());
		} else {
			return (T) bigDecimal;
		}
	}
}
