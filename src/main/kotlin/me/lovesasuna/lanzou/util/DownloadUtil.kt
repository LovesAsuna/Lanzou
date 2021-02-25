package me.lovesasuna.lanzou.util

import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * @author LovesAsuna
 * @date 2020/8/22 13:44
 */
object DownloadUtil {
    fun download(urlString: String, fileName: String, savePath: String, vararg heads: Array<String>): Boolean {
        return try {
            val map = HashMap<String, String>()
            for (head in heads) {
                map[head[0]] = head[1]
            }
            val bytes = OkHttpUtil.getBytes(urlString, OkHttpUtil.addHeaders(map))
            download(bytes, fileName, savePath)
            true
        } catch (e: IOException) {
            false
        }
    }

    /**
     * @param byteArray 字节数组
     * @param fileName 不包含路径的文件名
     * @param savePath 不包含文件名的路径
     */
    @Throws(IOException::class)
    fun download(byteArray: ByteArray, fileName: String, savePath: String) {
        download(byteArray, File(savePath + File.separator + fileName))
    }

    /**
     * @param byteArray 字节数组
     * @param file 文件绝对路径
     */
    @Throws(IOException::class)
    fun download(byteArray: ByteArray, file: File) {
        val fout = FileOutputStream(file)
        fout.write(byteArray)
        fout.close()
    }
}