package me.lovesasuna.lanzou.file;

import me.lovesasuna.lanzou.network.Downloadable;
import me.lovesasuna.lanzou.utils.ReaderUtil;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author LovesAsuna
 * @date 2020/8/22 10:05
 **/
public abstract class Item implements Downloadable {
    String id;

    /**
     * 通过reader判断此链接包含的内容
     *
     * @param reader 通过蓝奏云网址获取的reader
     * @return boolean
     **/
    public static boolean isFile(BufferedReader reader) {
        try {
            ReaderUtil.readAnyTime(18, reader);
            String line = reader.readLine();
            return !line.contains("登录");
        } catch (IOException e) {
            return false;
        }
    }

    public abstract void init(BufferedReader reader, boolean debug);
}
