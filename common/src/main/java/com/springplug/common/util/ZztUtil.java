package com.springplug.common.util;

import java.math.BigDecimal;

/**
 * @author jwt
 * @version 1.0
 * @date 2021-5-17 16:35
 */
public class ZztUtil {

    /**
     * 通过feil计算zzt
     * @param salePrice
     * @param rate
     * @return
     */
    public static BigDecimal getZztByRate(BigDecimal salePrice, BigDecimal rate) {
        return salePrice.multiply(
                rate.divide(new BigDecimal(100)).subtract(new BigDecimal("0.035"))).multiply(new BigDecimal(16)).multiply(new BigDecimal("0.625")).setScale(2, BigDecimal.ROUND_DOWN);
    }
}
