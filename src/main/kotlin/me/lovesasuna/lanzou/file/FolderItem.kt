package me.lovesasuna.lanzou.file

/**
 * @author LovesAsuna
 * @date 2020/8/22 10:11
 */
abstract class FolderItem : Item() {
    lateinit var fileItemSet: MutableSet<FileItem>
        protected set
}