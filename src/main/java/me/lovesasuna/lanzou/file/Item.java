package me.lovesasuna.lanzou.file;

import me.lovesasuna.lanzou.utils.ReaderUtil;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author LovesAsuna
 * @date 2020/8/22 10:05
 **/
public abstract class Item {
    String id;

    public static boolean isFile(BufferedReader reader) {
        try {
            ReaderUtil.readAnyTime(18, reader);
            String line = reader.readLine();
            return !line.contains("登录");
        } catch (IOException e) {
            return false;
        }
    }

    public abstract void init(BufferedReader reader);
}
