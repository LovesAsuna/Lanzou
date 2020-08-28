package me.lovesasuna.lanzou.file

import me.lovesasuna.lanzou.network.Downloadable
import me.lovesasuna.lanzou.util.DownloadUtil
import me.lovesasuna.lanzou.util.NetWorkUtil
import me.lovesasuna.lanzou.util.ReaderUtil
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.nio.file.Path
import java.util.*
import java.util.stream.Collectors

/**
 * @author LovesAsuna
 * @date 2020/8/22 10:05
 */
class FileImpl(id: String) : FileItem(), Downloadable {
    override fun download(path: Path): Boolean {
        return DownloadUtil.download(
            downloadableUrl.toString(),
            name,
            path.normalize().toString(),
            arrayOf("Accept-Language", "zh-CN,zh;q=0.9")
        )
    }

    override fun init(reader: BufferedReader) {
        var reader = reader
        try {
            // 文件
            var result: Triple<Int, InputStream, Long>?
            ReaderUtil.readAnyTime(3, reader)
            var line = reader.readLine()
            var matcher: MatchResult?
            // 获取文件名
            name = line.split(">").toTypedArray()[1].replace("</div", "")
            ReaderUtil.readAnyTime(5, reader)
            // 获取文件大小
            line = reader.readLine()
            matcher = Regex("\\w+\\.\\d \\w").find(line)
            if (matcher != null) {
                size = matcher.value
            }

            // 获取文件类型
            matcher = Regex("\\w+$").find(name)
            if (matcher != null) {
                icon = matcher.value
            }

            // 获取上传时间
            line = reader.readLine()
            matcher = Regex("(\\w+ [\\u4e00-\\u9fa5]{2})|(\\d{4}-\\d{2}-\\d{2})").find(line)
            if (matcher != null) {
                time = matcher.value
            }
            ReaderUtil.readAnyTime(15, reader)
            val src = reader.readLine()
            reader.close()
            val fn = src.split("\"").toTypedArray()[5]
            val fnurl = "https://wwa.lanzous.com$fn"
            result = NetWorkUtil.get(fnurl)
            Objects.requireNonNull(result)
            reader = BufferedReader(InputStreamReader(result!!.second))
            val regex = Regex("'\\w*$MAGIC'")
            val list = reader.lines().filter { s: String -> s.contains(MAGIC) }
                .collect(Collectors.toList())[0]
            reader.close()
            matcher = regex.find(list)
            var sign: String? = null
            if (matcher != null) {
                sign = matcher.value
                Objects.requireNonNull(sign)
            }
            val data = "action=downprocess&sign=$sign&ves=1"
            result = NetWorkUtil.post(
                "https://wwa.lanzous.com/ajaxm.php",
                data.toByteArray(),
                arrayOf("Referer", fnurl),
                arrayOf("Cookie", "pc_ad1=1"),
                arrayOf("Host", "wwa.lanzous.com"),
                arrayOf("Content-Type", "application/x-www-form-urlencoded")
            )
            Objects.requireNonNull(result)
            reader = BufferedReader(InputStreamReader(result!!.second))
            downloadableUrl = URL("https://vip.d0.baidupan.com/file/" + reader.readLine().split("\"").toTypedArray()[9])
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }



    init {
        this.id = id
    }
}