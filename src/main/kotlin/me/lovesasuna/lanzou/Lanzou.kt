package me.lovesasuna.lanzou

import me.lovesasuna.lanzou.file.FileImpl
import me.lovesasuna.lanzou.file.FileItem
import me.lovesasuna.lanzou.file.FolderImpl
import me.lovesasuna.lanzou.file.Item
import me.lovesasuna.lanzou.util.NetWorkUtil
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.util.*

/**
 * @author LovesAsuna
 * @date 2020/8/22 9:08
 */
class Lanzou {
    private var item: Item? = null
    fun parseSuffix(suffix: String): Item? {
        item = getItem(suffix)
        return item
    }

    fun parseUrl(url: URL): Item? {
        item = getItem(url.path.replaceFirst("/".toRegex(), ""))
        return item
    }

    @Throws(NullPointerException::class)
    private fun getItem(suffix: String): Item {
        val lanzousUrl = "https://wwa.lanzous.com/$suffix"
        val result = NetWorkUtil.get(lanzousUrl)
        Objects.requireNonNull(result)
        val reader = BufferedReader(InputStreamReader(result!!.second))
        return try {
            val item: Item
            item = if (Item.isFile(reader)) {
                // 文件
                val file = FileImpl(suffix)
                file
            } else {
                // 文件夹
                val folder = FolderImpl(suffix)
                folder
            }
            item.init(reader)
            item
        } finally {
            try {
                reader.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        @JvmStatic
        fun getFileDownloadUrl(suffix: String): URL {
            val instance = Lanzou()
            val item = instance.parseSuffix(suffix)
            require(item !is FolderImpl) { "This is not a file" }
            return (item as FileItem).downloadableUrl
        }
    }
}