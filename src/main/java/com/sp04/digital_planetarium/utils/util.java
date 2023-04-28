package com.sp04.digital_planetarium.utils;

public class util {
    /**
     * 生成在[min, max]范围内的随机整数
     * @param min 随机数最小值(含)
     * @param max 随机数最大值(含)
     * @return 随机整数
     */
    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }
}
