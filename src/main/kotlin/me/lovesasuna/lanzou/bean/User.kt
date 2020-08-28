package me.lovesasuna.lanzou.bean

import java.util.regex.Pattern

/**
 * @author LovesAsuna
 * @date 2020/8/22 9:14
 */
class User {
    lateinit var ylogin: String
    lateinit var cookie: String

    constructor() {}
    constructor(cookie: String) {
        this.cookie = cookie
        val matcher = Pattern.compile("ylogin=\\d+").matcher(cookie)
        require(matcher.find()) { "cookie is invalid" }
        ylogin = matcher.group().replace("ylogin=", "")
    }
}