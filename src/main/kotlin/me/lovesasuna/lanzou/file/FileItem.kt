package me.lovesasuna.lanzou.file

import java.net.URL

/**
 * @author LovesAsuna
 * @date 2020/8/22 10:11
 */
abstract class FileItem : Item() {
    protected val MAGIC = "_c_c"
    lateinit var downloadableUrl: URL
        protected set
    lateinit var icon: String
    lateinit var name: String
    lateinit var size: String
    lateinit var time: String
}