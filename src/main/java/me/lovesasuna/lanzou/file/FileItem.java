package me.lovesasuna.lanzou.file;

import java.io.BufferedReader;
import java.net.URL;

/**
 * @author LovesAsuna
 * @date 2020/8/22 10:11
 **/
public abstract class FileItem extends Item {
    protected final String MAGIC = "_c_c";
    URL downloadableUrl;
    protected String icon;
    protected String name;
    protected String size;
    protected String time;
}
