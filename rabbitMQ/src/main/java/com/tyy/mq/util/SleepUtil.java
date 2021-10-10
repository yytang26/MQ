package com.tyy.mq.util;

/**
 * @author:tyy
 * @date:2021/10/10
 */
public class SleepUtil {

    public static void sleep(int time) {
        try {
            Thread.sleep(time*1000);
        } catch (InterruptedException ignored) {
            Thread.interrupted();
        }
    }
}
