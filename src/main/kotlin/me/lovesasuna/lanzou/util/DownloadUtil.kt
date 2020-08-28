package me.lovesasuna.lanzou.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author LovesAsuna
 * @date 2020/8/22 13:44
 **/
public class DownloadUtil {
    public static boolean download(String urlString, String fileName, String savePath, String[]... heads) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            for (String[] head : heads) {
                conn.setRequestProperty(head[0], head[1]);
            }
            conn.connect();
            download(conn, fileName, savePath);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * @param conn     已经设置好属性并已经连接的HttpURLConnection
     * @param fileName 不包含路径的文件名
     * @param savePath 不包含文件名的路径
     */
    public static void download(HttpURLConnection conn, String fileName, String savePath) throws IOException{
        download(conn, new File(savePath + File.separator + fileName));
    }

    /**
     * @param conn     已经设置好属性并已经连接的HttpURLConnection
     * @param file     文件绝对路径
     */

    public static void download(HttpURLConnection conn, File file) throws IOException{
        InputStream inputStream = conn.getInputStream();
        int length = 0;
        byte[] bytes = new byte[2048];
        FileOutputStream fout = new FileOutputStream(file);
        while ((length = inputStream.read(bytes)) != -1) {
            fout.write(bytes, 0, length);
        }
        fout.close();
        conn.disconnect();
    }
}
