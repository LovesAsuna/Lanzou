package me.lovesasuna.lanzou.util;

import java.io.*;

/**
 * @author LovesAsuna
 * @date 2020/8/28 15:12
 **/
public class Multipart {
    protected final ByteArrayOutputStream baos;
    protected final String randomString;
    protected final String contentType;
    protected final String prefix;
    protected final String suffix;

    public Multipart(String randomString) {
        this.baos = new ByteArrayOutputStream();
        this.randomString = randomString;
        this.contentType = "multipart/form-data; boundary=----WebKitFormBoundary" + randomString;
        this.prefix = "------WebKitFormBoundary" + randomString;
        this.suffix = prefix + "--";
    }

    private String disposition(String name) {
        return "Content-Disposition: form-data; name=\"" + name + "\"";
    }

    protected boolean writeFileNode(ByteArrayOutputStream baos, String name, File file, String contentType) {
        StringBuilder builder = new StringBuilder();
        builder.append(prefix)
                .append("\n")
                .append(disposition(name + "\"; filename=\"" + file.getName()))
                .append("\n")
                .append("Content-Type: ")
                .append(contentType)
                .append("\n\n");
        try {
            InputStream inputStream = new FileInputStream(file);
            baos.write(builder.toString().getBytes());
            baos.write(NetWorkUtil.inputStreamClone(inputStream).toByteArray());
            baos.write("\n".getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean writeContentNode(ByteArrayOutputStream baos, String name, String content) {
        StringBuilder builder = new StringBuilder();
        builder.append(prefix)
                .append("\n")
                .append(disposition(name))
                .append("\n\n")
                .append(content)
                .append("\n");
        try {
            baos.write(builder.toString().getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void finishNode(ByteArrayOutputStream baos) {
        try {
            baos.write(suffix.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
