package me.lovesasuna.lanzou.util;

import me.lovesasuna.lanzou.bean.Triple;
import okhttp3.*;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * @author LovesAsuna
 * @date 2020/8/22 9:30
 **/
public class NetWorkUtil {
    private static final OkHttpClient client = new OkHttpClient();

    public static Triple<Integer, InputStream, Long> get(String urlString, String[]... headers) {
        Request.Builder builder = new Request.Builder().url(urlString).get();
        return getTriple(builder, headers);
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

    public static Triple<Integer, InputStream, Long> post(String urlString, byte[] body, String[]... headers) {
        RequestBody requestBody = RequestBody.create(body, null);
        Request.Builder builder = new Request.Builder().url(urlString).post(requestBody);
        return getTriple(builder, headers);
    }

    private static Triple<Integer, InputStream, Long> getTriple(Request.Builder builder, String[]... headers) {
        builder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36");
        for (String[] head : headers) {
            builder.addHeader(head[0], head[1]);
        }
        Call call = client.newCall(builder.build());
        try {
            Response response = call.execute();
            return new Triple<>(response.code(), response.body().byteStream(), response.body().contentLength());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
