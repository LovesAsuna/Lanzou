package me.lovesasuna.lanzou.file;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.lovesasuna.lanzou.bean.Triple;
import me.lovesasuna.lanzou.util.BasicUtil;
import me.lovesasuna.lanzou.util.NetWorkUtil;
import me.lovesasuna.lanzou.util.ReaderUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author LovesAsuna
 * @date 2020/8/22 10:31
 **/
public class FolderImpl extends FolderItem {

    public FolderImpl(String id) {
        this.id = id;
    }

    @Override
    public void init(BufferedReader reader) {
        try {
            // 文件夹
            Triple<Integer, InputStream, Long> result;
            ReaderUtil.readAnyTime(68, reader);
            String t = reader.readLine().split("'")[1];
            String k = reader.readLine().split("'")[1];
            ReaderUtil.readAnyTime(9, reader);
            String lx = "2";
            String fid = "" + BasicUtil.extractInt(reader.readLine(), 0);
            String uid = "" + BasicUtil.extractInt(reader.readLine(), 0);
            String pg = "1";
            String rep = "0";
            String up = "1";
            reader.close();
            StringBuilder builder = new StringBuilder();
            builder.append("lx=").append(lx).append("&")
                    .append("fid=").append(fid).append("&")
                    .append("uid=").append(uid).append("&")
                    .append("pg=").append(pg).append("&")
                    .append("rep=").append(rep).append("&")
                    .append("t=").append(t).append("&")
                    .append("k=").append(k).append("&")
                    .append("up=").append(up);
            result = NetWorkUtil.post("https://wwa.lanzous.com/filemoreajax.php", builder.toString().getBytes());
            Objects.requireNonNull(result);
            reader = new BufferedReader(new InputStreamReader(result.second));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(reader.readLine());
            reader.close();
            JsonNode text = root.get("text");
            int length = text.size();
            fileItemSet = new HashSet<>();
            for (int i = 0; i < length; i++) {
                JsonNode fileNode = text.get(i);
                String id = fileNode.get("id").asText();
                FileItem file = new FileImpl(id);
                file.icon = fileNode.get("icon").asText();
                file.name = fileNode.get("name_all").asText();
                file.size = fileNode.get("size").asText();
                file.time = fileNode.get("time").asText();

                String lanzousUrl = "https://wwa.lanzous.com/" + id;
                result = NetWorkUtil.get(lanzousUrl);
                Objects.requireNonNull(result);
                reader = new BufferedReader(new InputStreamReader(result.second));
                if (Item.isFile(reader)) {
                    file.init(reader);
                }
                fileItemSet.add(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean download(Path path) {
        AtomicBoolean success = new AtomicBoolean(true);
        fileItemSet.forEach(fileItem -> {
            success.set(success.get() && fileItem.download(path));
        });
        return success.get();
    }
}
