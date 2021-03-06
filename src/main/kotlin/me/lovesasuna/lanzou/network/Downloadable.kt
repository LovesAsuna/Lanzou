package me.lovesasuna.lanzou.network

import java.nio.file.Path

/**
 * @author LovesAsuna
 * @date 2020/8/22 11:35
 */
interface Downloadable {
    /**
     * 下载文件
     *
     * @param path
     * @return 下载成功与否
     */
    fun download(path: Path): Boolean
}