package me.lovesasuna.lanzou.util

import java.io.BufferedReader
import java.io.IOException

/**
 * @author LovesAsuna
 * @date 2020/8/22 12:44
 */
object ReaderUtil {
    fun readAnyTime(times: Int, reader: BufferedReader) {
        BasicUtil.runAnyTimes(times, reader, { r: BufferedReader ->
            try {
                r.readLine()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        })
    }
}