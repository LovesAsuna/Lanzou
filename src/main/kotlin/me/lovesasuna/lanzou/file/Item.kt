package me.lovesasuna.lanzou.file

import me.lovesasuna.lanzou.network.Downloadable
import me.lovesasuna.lanzou.util.ReaderUtil
import java.io.BufferedReader
import java.io.IOException

/**
 * @author LovesAsuna
 * @date 2020/8/22 10:05
 */
abstract class Item : Downloadable {
    lateinit var id: String
    abstract fun init(reader: BufferedReader)

    companion object {
        /**
         * 通过reader判断此链接包含的内容
         *
         * @param reader 通过蓝奏云网址获取的reader
         * @return boolean
         */
        fun isFile(reader: BufferedReader): Boolean {
            return try {
                ReaderUtil.readAnyTime(5, reader)
                var line = reader.readLine()
                val isFile = line.contains("- 蓝奏云")
                line = reader.readLine()
                val n = if (line.contains("description")) {
                    13
                } else {
                    12
                }
                ReaderUtil.readAnyTime(n, reader)
                isFile
            } catch (e: IOException) {
                false
            }
        }
    }
}