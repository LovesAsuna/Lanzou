package me.lovesasuna.lanzou.network

import me.lovesasuna.lanzou.bean.User
import java.io.File

/**
 * @author LovesAsuna
 * @date 2020/8/22 14:07
 */
interface Uploadable {
    fun upload(user: User, loc: Int, vararg file: File): Boolean
}