package me.lovesasuna.lanzou.file

import com.fasterxml.jackson.databind.ObjectMapper
import me.lovesasuna.lanzou.util.BasicUtil
import me.lovesasuna.lanzou.util.NetWorkUtil
import me.lovesasuna.lanzou.util.ReaderUtil
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.Path
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Consumer

/**
 * @author LovesAsuna
 * @date 2020/8/22 10:31
 */
class FolderImpl(id: String) : FolderItem() {
    override fun init(reader: BufferedReader) {
        var reader = reader
        try {
            // 文件夹
            var result: Triple<Int, InputStream, Long>?
            ReaderUtil.readAnyTime(68, reader)
            val t = reader.readLine().split("'").toTypedArray()[1]
            val k = reader.readLine().split("'").toTypedArray()[1]
            ReaderUtil.readAnyTime(9, reader)
            val lx = "2"
            val fid = "" + BasicUtil.extractInt(reader.readLine(), 0)
            val uid = "" + BasicUtil.extractInt(reader.readLine(), 0)
            val pg = "1"
            val rep = "0"
            val up = "1"
            reader.close()
            val builder = StringBuilder()
            builder.append("lx=").append(lx).append("&")
                .append("fid=").append(fid).append("&")
                .append("uid=").append(uid).append("&")
                .append("pg=").append(pg).append("&")
                .append("rep=").append(rep).append("&")
                .append("t=").append(t).append("&")
                .append("k=").append(k).append("&")
                .append("up=").append(up)
            result = NetWorkUtil.post("https://wwa.lanzous.com/filemoreajax.php", builder.toString().toByteArray())
            Objects.requireNonNull(result)
            reader = BufferedReader(InputStreamReader(result!!.second))
            val mapper = ObjectMapper()
            val root = mapper.readTree(reader.readLine())
            reader.close()
            val text = root["text"]
            val length = text.size()
            fileItemSet = HashSet()
            for (i in 0 until length) {
                val fileNode = text[i]
                val id = fileNode["id"].asText()
                val file: FileItem = FileImpl(id)
                file.icon = fileNode["icon"].asText()
                file.name = fileNode["name_all"].asText()
                file.size = fileNode["size"].asText()
                file.time = fileNode["time"].asText()
                val lanzousUrl = "https://wwa.lanzous.com/$id"
                result = NetWorkUtil[lanzousUrl]
                Objects.requireNonNull(result)
                reader = BufferedReader(InputStreamReader(result!!.second))
                if (isFile(reader)) {
                    file.init(reader)
                }
                fileItemSet.add(file)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun download(path: Path): Boolean {
        val success = AtomicBoolean(true)
        fileItemSet.forEach(Consumer { fileItem: FileItem -> success.set(success.get() && fileItem.download(path)) })
        return success.get()
    }

    init {
        this.id = id
    }
}