package me.lovesasuna.lanzou.bean;


import javafx.scene.paint.Material;

import java.util.regex.Matcher;

import static java.util.regex.Pattern.compile;

/**
 * @author LovesAsuna
 * @date 2020/8/22 9:14
 **/
public class User {
    private String ylogin;
    private String cookie;

    public User() {
    }

    public User(String cookie) {
        this.cookie = cookie;
        Matcher matcher = compile("ylogin=\\d+").matcher(cookie);
        if (!matcher.find()) {
            throw new IllegalArgumentException("cookie is invalid");
        }
        this.ylogin = matcher.group().replace("ylogin=", "");
    }

    public String getYlogin() {
        return ylogin;
    }

    public void setYlogin(String ylogin) {
        this.ylogin = ylogin;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
