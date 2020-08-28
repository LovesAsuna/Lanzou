package me.lovesasuna.lanzou.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.lovesasuna.lanzou.bean.Triple;
import me.lovesasuna.lanzou.bean.User;
import me.lovesasuna.lanzou.network.Uploadable;
import me.lovesasuna.lanzou.util.Multipart;
import me.lovesasuna.lanzou.util.NetWorkUtil;

import java.io.*;
import java.util.Objects;

/**
 * @author LovesAsuna
 * @date 2020/8/22 15:42
 **/
public class FileAdapter extends Multipart implements Uploadable {
    private User user;
    private File file;

    public FileAdapter(User user, File file) {
        super("test");
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

    private boolean uploadFile(User user, File file, int loc) {
        Objects.requireNonNull(user, "user can not be null");
        Objects.requireNonNull(file, "file can not be null");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            long size = file.length();
            writeContentNode(baos, "task", "1");
            writeContentNode(baos, "ve", "2");
            writeContentNode(baos, "id", "WU_FILE_" + loc);
            writeContentNode(baos, "name", file.getName());
            writeContentNode(baos, "type", "application/x-zip-compressed");
            writeContentNode(baos, "lastModifiedDate", "Thu Jan 1 2020 00:00:00 GMT+0800 (中国标准时间)");
            writeContentNode(baos, "size", String.valueOf(size));
            writeContentNode(baos, "folder_id_bb_n", "-1");
            writeFileNode(baos, "upload_file", file, "application/x-zip-compressed");
            finishNode(baos);
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

}
