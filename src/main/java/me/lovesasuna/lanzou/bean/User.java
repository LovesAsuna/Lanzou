package me.lovesasuna.lanzou.bean;

/**
 * @author LovesAsuna
 * @date 2020/8/22 9:14
 **/
public class User {
    private String ylogin;
    private String cookie;

    public User() {}

    public User(String cookie) {
        this.cookie = cookie;
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
