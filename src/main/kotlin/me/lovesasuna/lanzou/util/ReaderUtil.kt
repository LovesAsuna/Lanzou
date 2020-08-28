package me.lovesasuna.lanzou.util;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author LovesAsuna
 * @date 2020/8/22 12:44
 **/
public class ReaderUtil {
    public static void readAnyTime(int times, BufferedReader reader) {
        BasicUtil.runAnyTimes(times,reader,r -> {
            try {
                r.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
