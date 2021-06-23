package com.springplug.common.util;

import java.util.Random;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Copyright © 2019 zhangpanxiang All rights reserved.
 * <p>
 * 功能描述：工具类 所有工具类的父类方法
 *
 * @Package: com.test.Util
 * @author: zhangpanxiang
 * @date: 2019年5月6日 下午1:42:02
 */
public class Util {

    /**
     * 不允许实例化
     */
    protected Util() {
    }

    /**
     * 根据传入参数创建实体
     *
     * @param <T>
     * @param args 构造方法需要传入的参数
     * @param t    函数表达式
     * @return 返回实体
     */
    public static <T> T get(Object args, Function<String, T> t) {
        return t.apply(args.toString());
    }

    /**
     * 创建无参实体
     *
     * @param <T>
     * @param t   函数表达式
     * @return 返回实体
     */
    public static <T> T get(Supplier<T> t) {
        return t.get();
    }

    /**
     * 判断是否为string类型
     *
     * @param args 需要判断的参数
     * @return 是返回true 否返回false
     */
    public static boolean isString(Object args) {
        return operation(args, x -> x instanceof String);
    }

    /**
     * 判断是否为double类型
     *
     * @param args 需要判断的参数
     * @return 是返回true 否返回false
     */
    public static boolean isDouble(Object args) {
        return operation(args, x -> x instanceof Double);
    }

    /**
     * 判断是否为float类型
     *
     * @param args 需要判断的参数
     * @return 是返回true 否返回false
     */
    public static boolean isFloat(Object args) {
        return operation(args, x -> x instanceof Float);
    }

    /**
     * 判断是否为Integer类型
     *
     * @param args 需要判断的参数
     * @return 是返回true 否返回false
     */
    public static boolean isInteger(Object args) {
        return operation(args, x -> x instanceof Integer);
    }

    /**
     * 执行函数
     *
     * @param supplier 执行函数
     * @return 操作后返回
     */
    public static <T> T operation(Supplier<T> supplier) {
        return supplier.get();
    }

    /**
     * 根据函数对一个参数进行操作
     *
     * @param args     需要进行操作的参数
     * @param function 执行函数
     * @return 操作后返回
     */
    public static <T> T operation(Object args, Function<Object, T> function) {
        return function.apply(args);
    }

    /**
     * 根据函数对两个参数进行操作
     *
     * @param args0    需要进行操作的参数1
     * @param args1    需要进行操作的参数2
     * @param function 执行函数
     * @return 操作后返回
     */
    public static <T> T operation(Object args0, Object args1, BiFunction<Object, Object, T> function) {
        return function.apply(args0, args1);
    }

    /**
     * 生成UUID
     *
     * @return 字符串
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    /**
     * 生成随机数
     *
     * @param charCount 随机数位数
     * @return 字符串
     */
    public static String getRandNum(int charCount) {
        String charValue = "";
        for (int i = 0; i < charCount; i++) {
            char c = (char) (randomInt(0, 10) + '0');
            charValue += String.valueOf(c);
        }
        return charValue;
    }

    /**
     * 指定范围内生成一个随机数
     *
     * @param from 开始数
     * @param to   结束数
     * @return 随机数
     */
    public static int randomInt(int from, int to) {
        Random r = new Random();
        return from + r.nextInt(to - from);
    }
}
