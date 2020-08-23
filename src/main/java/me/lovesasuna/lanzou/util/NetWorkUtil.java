package me.lovesasuna.lanzou.util;

import me.lovesasuna.lanzou.bean.Triple;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author LovesAsuna
 * @date 2020/8/22 9:30
 **/
public class NetWorkUtil {
    public static Triple<Integer, InputStream, Integer> get(String urlString, String[]... headers) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            connect(conn, headers);
            int responseCore = conn.getResponseCode();
            InputStream inputStream = null;
            if (responseCore == 200) {
                inputStream = conn.getInputStream();
            } else {
                inputStream = conn.getErrorStream();
            }
            int length = conn.getContentLength();
            return new Triple<>(responseCore, inputStream, length);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void connect(HttpURLConnection conn, String[]... headers) {
        try {
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36");
            for (String[] header : headers) {
                conn.setRequestProperty(header[0], header[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ByteArrayOutputStream inputStreamClone(InputStream inputStream) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Triple<Integer, InputStream, Integer> post(String urlString, byte[] body, String[]... headers) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            connect(conn, headers);
            OutputStream outputStream = conn.getOutputStream();
            DataOutputStream writer = new DataOutputStream(outputStream);
            writer.write(body);
            writer.flush();
            int responseCore = conn.getResponseCode();
            InputStream inputStream;
            if (responseCore == 200) {
                inputStream = conn.getInputStream();
            } else {
                inputStream = conn.getErrorStream();
            }
            int length = conn.getContentLength();
            return new Triple<>(responseCore, inputStream, length);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
