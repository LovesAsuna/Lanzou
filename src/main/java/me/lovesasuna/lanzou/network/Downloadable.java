package me.lovesasuna.lanzou.network;

import java.io.File;
import java.net.URL;

/**
 * @author LovesAsuna
 * @date 2020/8/22 11:35
 **/
public interface Downloadable {
    /**
     * 通过链接下载文件
     *
     * @param url 下载复制
     * @return 下载成功与否
     **/
    boolean download(URL url, File file);
}
