package me.lovesasuna.lanzou;

import me.lovesasuna.lanzou.bean.Triple;
import me.lovesasuna.lanzou.file.FileImpl;
import me.lovesasuna.lanzou.file.FolderImpl;
import me.lovesasuna.lanzou.file.Item;
import me.lovesasuna.lanzou.utils.NetWorkUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

/**
 * @author LovesAsuna
 * @date 2020/8/22 9:08
 **/
public class Lanzou {
    private Item item;
    private boolean debug;

    public Lanzou() {
        this(false);
    }

    public Lanzou(boolean debug) {
        this.debug = debug;
    }

    public Item parseSuffix(String suffix) {
        item = getItem(suffix);
        return item;
    }

    public Item parseUrl(URL url) {
        item = getItem(url.getPath().replaceFirst("/", ""));
        return item;
    }

    private Item getItem(String suffix) throws NullPointerException {
        String lanzousUrl = "https://wwa.lanzous.com/" + suffix;
        Triple<Integer, InputStream, Integer> result = NetWorkUtil.get(lanzousUrl);
        Objects.requireNonNull(result);
        BufferedReader reader = new BufferedReader(new InputStreamReader(result.second));
        try {
            Item item;
            if (Item.isFile(reader)) {
                // 文件
                FileImpl file = new FileImpl(suffix);
                item = file;
            } else {
                // 文件夹
                FolderImpl folder = new FolderImpl(suffix);
                item = folder;
            }
            item.init(reader, false);
            return item;
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
