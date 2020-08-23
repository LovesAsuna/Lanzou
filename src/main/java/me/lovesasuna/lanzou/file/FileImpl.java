package me.lovesasuna.lanzou.file;

import me.lovesasuna.lanzou.bean.Debuger;
import me.lovesasuna.lanzou.bean.Triple;
import me.lovesasuna.lanzou.network.Downloadable;
import me.lovesasuna.lanzou.util.DownloadUtil;
import me.lovesasuna.lanzou.util.NetWorkUtil;
import me.lovesasuna.lanzou.util.ReaderUtil;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.regex.Pattern.compile;

/**
 * @author LovesAsuna
 * @date 2020/8/22 10:05
 **/
public class FileImpl extends FileItem implements Downloadable {

    public FileImpl(String id) {
        this.id = id;
    }

    public FileImpl setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public FileImpl setName(String name) {
        this.name = name;
        return this;
    }

    public FileImpl setTime(String time) {
        this.time = time;
        return this;
    }

    public FileImpl setSize(String size) {
        this.size = size;
        return this;
    }

    public void setUrl(String url) throws IOException {
        downloadableUrl = new URL(url);
    }

    @Override
    public boolean download(Path path) {
        return DownloadUtil.download(downloadableUrl.toString(), name, path.normalize().toString(), new String[]{"Accept-Language", "zh-CN,zh;q=0.9"});
    }

    @Override
    public void init(BufferedReader reader, boolean debug) {
        try {
            // 文件
            Debuger debuger = new Debuger();
            debuger.setItem(this);
            Triple<Integer, InputStream, Integer> result;
            ReaderUtil.readAnyTime(3, reader);
            String line = reader.readLine();
            Matcher matcher;
            // 获取文件名
            name = line.split(">")[1].replace("</div", "");
            ReaderUtil.readAnyTime(5, reader);
            // 获取文件大小
            line = reader.readLine();
            matcher = compile("\\w+\\.\\d \\w").matcher(line);
            if (matcher.find()) {
                size = matcher.group();
            }

            // 获取文件类型
            matcher = compile("\\w+$").matcher(name);
            if (matcher.find()) {
                icon = matcher.group();
            }

            // 获取上传时间
            line = reader.readLine();
            matcher = compile("(\\w+ [\\u4e00-\\u9fa5]{2})|(\\d{4}-\\d{2}-\\d{2})").matcher(line);
            if (matcher.find()) {
                time = matcher.group();
            }
            ReaderUtil.readAnyTime(15, reader);
            String src = reader.readLine();
            debuger.setSrc(src);
            reader.close();
            String fn = src.split("\"")[5];
            String fnurl = "https://wwa.lanzous.com" + fn;
            debuger.setFnurl(fnurl);
            result = NetWorkUtil.get(fnurl);
            Objects.requireNonNull(result);
            reader = new BufferedReader(new InputStreamReader(result.second));
            Pattern pattern = compile("'\\w*_c_c'");

            String list = reader.lines().filter(s -> s.contains(MAGIC)).collect(Collectors.toList()).get(0);
            reader.close();
            matcher = pattern.matcher(list);
            String sign = null;
            if (matcher.find()) {
                sign = matcher.group();
                Objects.requireNonNull(sign);
            }
            debuger.setSign(sign);
            String data = "action=downprocess&sign=" + sign + "&ves=1";
            debuger.setData(data);
            result = NetWorkUtil.post("https://wwa.lanzous.com/ajaxm.php", data.getBytes(),
                    new String[]{"Referer", fnurl},
                    new String[]{"Cookie", "noads=1; pc_ad1=1"},
                    new String[]{"Host", "wwa.lanzous.com"});
            Objects.requireNonNull(result);
            reader = new BufferedReader(new InputStreamReader(result.second));
            setUrl("https://vip.d0.baidupan.com/file/" + reader.readLine().split("\"")[9]);
            reader.close();
            if (debug) {
                System.out.println(debuger);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "FileImpl{" +
                "downloadableUrl=" + downloadableUrl +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", time='" + time + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
