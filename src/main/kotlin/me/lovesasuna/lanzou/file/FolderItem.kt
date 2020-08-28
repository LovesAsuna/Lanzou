package me.lovesasuna.lanzou.file;

import java.util.Set;

/**
 * @author LovesAsuna
 * @date 2020/8/22 10:11
 **/
public abstract class FolderItem extends Item {
    protected Set<FileItem> fileItemSet;

    public Set<FileItem> getFileItemSet() {
        return fileItemSet;
    }
}
