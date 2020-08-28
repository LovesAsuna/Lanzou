package me.lovesasuna.lanzou.util

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author LovesAsuna
 * @date 2020/8/22 13:44
 */
object DownloadUtil {
    fun download(urlString: String, fileName: String, savePath: String, vararg heads: Array<String>): Boolean {
        return try {
            val url = URL(urlString)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            for (head in heads) {
                conn.setRequestProperty(head[0], head[1])
            }
            conn.connect()
            download(conn, fileName, savePath)
            true
        } catch (e: IOException) {
            false
        }
    }

    /**
     * @param conn     已经设置好属性并已经连接的HttpURLConnection
     * @param fileName 不包含路径的文件名
     * @param savePath 不包含文件名的路径
     */
    @Throws(IOException::class)
    fun download(conn: HttpURLConnection, fileName: String, savePath: String) {
        download(conn, File(savePath + File.separator + fileName))
    }

    /**
     * @param conn     已经设置好属性并已经连接的HttpURLConnection
     * @param file     文件绝对路径
     */
    @Throws(IOException::class)
    fun download(conn: HttpURLConnection, file: File) {
        val inputStream = conn.inputStream
        var length: Int
        val bytes = ByteArray(2048)
        val fout = FileOutputStream(file)
        while (inputStream.read(bytes).also { length = it } != -1) {
            fout.write(bytes, 0, length)
        }
        fout.close()
        conn.disconnect()
    }
}