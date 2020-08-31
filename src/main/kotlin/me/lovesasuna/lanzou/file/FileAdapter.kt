package me.lovesasuna.lanzou.file

import com.fasterxml.jackson.databind.ObjectMapper
import me.lovesasuna.lanzou.bean.User
import me.lovesasuna.lanzou.network.Uploadable
import me.lovesasuna.lanzou.util.Multipart
import me.lovesasuna.lanzou.util.NetWorkUtil
import java.io.*
import java.util.*
import kotlin.jvm.Throws

/**
 * @author LovesAsuna
 * @date 2020/8/22 15:42
 */
class FileAdapter(private val user: User, private vararg val file: File) : Multipart("test"), Uploadable {
    fun upload(): Boolean {
        return uploadFile(user, 0, *file)
    }

    override fun upload(user: User, loc: Int, vararg file: File): Boolean {
        return uploadFile(user, loc, *file)
    }

    private fun uploadFile(user: User, loc: Int, vararg file: File): Boolean {
        Objects.requireNonNull(user, "user can not be null")
        Objects.requireNonNull(file, "file can not be null")
        try {
            for (singleFile in file) {
                listFile(singleFile) {
                    uploadSingleFile(it, loc)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
        return true
    }

    @Throws(IOException::class)
    private fun uploadSingleFile(file: File, loc: Int) {
        val baos = ByteArrayOutputStream()
        val size = file.length()
        writeContentNode(baos, "task", "1")
        writeContentNode(baos, "ve", "2")
        writeContentNode(baos, "id", "WU_FILE_$loc")
        writeContentNode(baos, "name", file.name)
        writeContentNode(baos, "type", "application/x-zip-compressed")
        writeContentNode(baos, "lastModifiedDate", "Thu Jan 1 2020 00:00:00 GMT+0800 (中国标准时间)")
        writeContentNode(baos, "size", size.toString())
        writeContentNode(baos, "folder_id_bb_n", "-1")
        writeFileNode(baos, "upload_file", file, "application/x-zip-compressed")
        finishNode(baos)
        val result = NetWorkUtil.post(
            "https://pc.woozooo.com/fileup.php",
            baos.toByteArray(),
            arrayOf("content-type", contentType),
            arrayOf("cookie", user.cookie),
            arrayOf("origin", "https://pc.woozooo.com"),
            arrayOf("referer", "https://pc.woozooo.com/mydisk.php?item=files&action=index&u=" + user.ylogin)
        )
        Objects.requireNonNull(result)
        val reader = BufferedReader(InputStreamReader(result!!.second))
        val line = reader.readLine()
        val mapper = ObjectMapper()
        println(mapper.readTree(line).toString())
        reader.close()
    }

    private fun listFile(file: File, action: (f: File) -> Unit) {
        for (f in file.listFiles()) {
            if (f.isDirectory) {
                listFile(f, action)
            } else {
                action.invoke(f)
            }
        }
    }
}