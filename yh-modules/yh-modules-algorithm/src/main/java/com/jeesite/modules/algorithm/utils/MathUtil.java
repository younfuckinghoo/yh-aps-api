package com.jeesite.modules.algorithm.utils;

import java.math.BigDecimal;

public class MathUtil {

    public static boolean isMoreThan(BigDecimal origin,long target){
        return origin.compareTo(BigDecimal.valueOf(target)) > 0;
    }

    public static boolean isMoreThan(BigDecimal origin,String target){
        return origin.compareTo(new BigDecimal(target)) > 0;
    }
    public static boolean isMoreThan(BigDecimal origin,BigDecimal target){
        return origin.compareTo(target) > 0;
    }
    public static boolean isMoreEqualThan(BigDecimal origin,long target){
        return origin.compareTo(BigDecimal.valueOf(target)) >= 0;
    }

    public static boolean isMoreEqualThan(BigDecimal origin,String target){
        return origin.compareTo(new BigDecimal(target)) >= 0;
    }
    public static boolean isMoreEqualThan(BigDecimal origin,BigDecimal target){
        return origin.compareTo(target) >= 0;
    }

    public static boolean isLessThan(BigDecimal origin,long target){
        return origin.compareTo(BigDecimal.valueOf(target)) < 0;
    }
    public static boolean isLessThan(BigDecimal origin,String target){
        return origin.compareTo(new BigDecimal(target)) < 0;
    }
    public static boolean isLessThan(BigDecimal origin,BigDecimal target){
        return origin.compareTo(target) < 0;
    }

    public static boolean isLessEqualThan(BigDecimal origin,long target){
        return origin.compareTo(BigDecimal.valueOf(target)) <= 0;
    }
    public static boolean isLessEqualThan(BigDecimal origin,String target){
        return origin.compareTo(new BigDecimal(target)) <= 0;
    }
    public static boolean isLessEqualThan(BigDecimal origin,BigDecimal target){
        return origin.compareTo(target) <= 0;
    }


}
