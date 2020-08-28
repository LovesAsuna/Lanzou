package me.lovesasuna.lanzou.util

import java.io.*

/**
 * @author LovesAsuna
 * @date 2020/8/28 15:12
 */
open class Multipart(randomString: String) {
    protected val baos: ByteArrayOutputStream = ByteArrayOutputStream()
    protected val randomString: String = randomString
    protected val contentType: String = "multipart/form-data; boundary=----WebKitFormBoundary$randomString"
    private val prefix: String = "------WebKitFormBoundary$randomString"
    private val suffix: String = "$prefix--"
    private fun disposition(name: String): String {
        return "Content-Disposition: form-data; name=\"$name\""
    }

    protected fun writeFileNode(baos: ByteArrayOutputStream, name: String, file: File, contentType: String?): Boolean {
        val builder = StringBuilder()
        builder.append(prefix)
            .append("\n")
            .append(disposition(name + "\"; filename=\"" + file.name))
            .append("\n")
            .append("Content-Type: ")
            .append(contentType)
            .append("\n\n")
        return try {
            val inputStream: InputStream = FileInputStream(file)
            baos.write(builder.toString().toByteArray())
            baos.write(NetWorkUtil.inputStreamClone(inputStream)!!.toByteArray())
            baos.write("\n".toByteArray())
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    protected fun writeContentNode(baos: ByteArrayOutputStream, name: String, content: String?): Boolean {
        val builder = StringBuilder()
        builder.append(prefix)
            .append("\n")
            .append(disposition(name))
            .append("\n\n")
            .append(content)
            .append("\n")
        return try {
            baos.write(builder.toString().toByteArray())
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    protected fun finishNode(baos: ByteArrayOutputStream) {
        try {
            baos.write(suffix.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}