package me.lovesasuna.lanzou.util

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * @author LovesAsuna
 * @date 2020/8/22 9:30
 */
object NetWorkUtil {
    private val client = OkHttpClient()
    operator fun get(urlString: String, vararg headers: Array<String>): Triple<Int, InputStream, Long>? {
        val builder = Request.Builder().url(urlString).get()
        return getTriple(builder, *headers)
    }

    fun inputStreamClone(inputStream: InputStream): ByteArrayOutputStream? {
        return try {
            val baos = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len: Int
            while (inputStream.read(buffer).also { len = it } != -1) {
                baos.write(buffer, 0, len)
            }
            baos.flush()
            baos
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun post(urlString: String, body: ByteArray, vararg headers: Array<String>): Triple<Int, InputStream, Long>? {
        val requestBody: RequestBody = body.toRequestBody()
        val builder = Request.Builder().url(urlString).post(requestBody)
        return getTriple(builder, *headers)
    }

    private fun getTriple(builder: Request.Builder, vararg headers: Array<String>): Triple<Int, InputStream, Long>? {
        builder.addHeader(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36"
        )
        for (head in headers) {
            builder.addHeader(head[0], head[1])
        }
        val call = client.newCall(builder.build())
        return try {
            val response = call.execute()
            Triple(response.code, response.body!!.byteStream(), response.body!!.contentLength())
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}