package me.lovesasuna.lanzou.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.lovesasuna.lanzou.bean.Triple;
import me.lovesasuna.lanzou.bean.User;
import me.lovesasuna.lanzou.network.Uploadable;
import me.lovesasuna.lanzou.util.NetWorkUtil;

import java.io.*;
import java.util.Objects;

/**
 * @author LovesAsuna
 * @date 2020/8/22 15:42
 **/
public class FileAdapter implements Uploadable {
    private User user;
    private File file;

    public FileAdapter(User user, File file) {
        this.user = user;
        this.file = file;
    }

    public boolean upload() {
        return uploadFile(user, file, 0);
    }

    @Override
    public boolean upload(User user, File file, int loc) {
        return uploadFile(user, file, loc);
    }

    private static boolean uploadFile(User user, File file, int loc) {
        Objects.requireNonNull(user, "user can not be null");
        Objects.requireNonNull(file, "file can not be null");
        StringBuilder builder = new StringBuilder();
        String random = "test";
        String contentType = "multipart/form-data; boundary=----WebKitFormBoundary" + random;
        String prefix = "------WebKitFormBoundary" + random;
        try {
            InputStream inputStream = new FileInputStream(file);
            long size = file.length();
            writeNode(builder, prefix, "task", "1");
            writeNode(builder, prefix, "ve", "2");
            writeNode(builder, prefix, "id", "WU_FILE_" + loc);
            writeNode(builder, prefix, "name", file.getName());
            writeNode(builder, prefix, "type", "application/x-zip-compressed");
            writeNode(builder, prefix, "lastModifiedDate", "Thu Jan 1 2020 00:00:00 GMT+0800 (中国标准时间)");
            writeNode(builder, prefix, "size", String.valueOf(size));
            writeNode(builder, prefix, "folder_id_bb_n", "-1");
            builder.append(prefix)
                    .append("\n")
                    .append(disposition("upload_file\"; filename=\"" + file.getName()))
                    .append("\n")
                    .append("Content-Type: application/x-zip-compressed")
                    .append("\n")
                    .append("\n");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(builder.toString().getBytes());
            baos.write(NetWorkUtil.inputStreamClone(inputStream).toByteArray());
            baos.write(("\n" + prefix + "--").getBytes());
            Triple<Integer, InputStream, Long> result = NetWorkUtil.post("https://pc.woozooo.com/fileup.php",
                    baos.toByteArray(),
                    new String[]{"content-type", contentType},
                    new String[]{"cookie", user.getCookie()},
                    new String[]{"origin", "https://pc.woozooo.com"},
                    new String[]{"referer", "https://pc.woozooo.com/mydisk.php?item=files&action=index&u=" + user.getYlogin()}
            );
            Objects.requireNonNull(result);
            BufferedReader reader = new BufferedReader(new InputStreamReader(result.second));
            String line = reader.readLine();
            ObjectMapper mapper = new ObjectMapper();
            System.out.println(mapper.readTree(line).toString());
            reader.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void writeNode(StringBuilder builder, String prefix, String name, String content) {
        builder.append(prefix)
                .append("\n")
                .append(disposition(name))
                .append("\n")
                .append("\n")
                .append(content)
                .append("\n");
    }

    private static String disposition(String name) {
        return "Content-Disposition: form-data; name=\"" + name + "\"";
    }
}
