package me.lovesasuna.lanzou.util

import java.io.File
import java.nio.file.Paths
import java.util.function.Consumer
import java.util.regex.Pattern

/**
 * @author LovesAsuna
 * @date 2020/8/22 9:21
 */
object BasicUtil {
    private val pattern = Pattern.compile("\\d+")
    fun extractInt(string: String?, defaultValue: Int): Int {
        val builder = StringBuilder()
        val matcher = pattern.matcher(string)
        while (matcher.find()) {
            builder.append(matcher.group())
        }
        return if (builder.toString().isEmpty()) {
            defaultValue
        } else {
            builder.toString().toInt()
        }
    }

    fun getLocation(c: Class<*>): File {
        return if (System.getProperty("os.name").contains("Windows")) {
            Paths.get(c.protectionDomain.codeSource.location.path.replaceFirst("/".toRegex(), "")).parent.toFile()
        } else {
            Paths.get(c.protectionDomain.codeSource.location.path).parent.toFile()
        }
    }

    fun getLocation(fileName: String?): File {
        val builder = StringBuilder()
        builder.append(getLocation(BasicUtil::class.java).path).append(File.separator).append(fileName)
        return File(builder.toString())
    }

    fun <T> runAnyTimes(times: Int, t: T, consumer: Consumer<T>) {
        for (i in 0 until times) {
            consumer.accept(t)
        }
    }
}