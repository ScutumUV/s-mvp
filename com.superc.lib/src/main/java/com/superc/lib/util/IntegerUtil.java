package com.superc.lib.util;

/**
 * Created by owner on 2017/9/12.
 */

public class IntegerUtil {

    /**
     * 判断是否是基数
     */
    public static boolean isCardinalNumber(int t) {
        if (t % 2 != 0) {
            return true;
        }
        return false;
    }
}
