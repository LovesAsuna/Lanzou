package me.lovesasuna.lanzou.bean;

import me.lovesasuna.lanzou.file.Item;

/**
 * @author LovesAsuna
 * @date 2020/8/22 21:09
 **/
public class Debuger {
    private Item item;
    private String src;
    private String fnurl;
    private String sign;
    private String data;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getFnurl() {
        return fnurl;
    }

    public void setFnurl(String fnurl) {
        this.fnurl = fnurl;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    @Override
    public String toString() {
        return "Debuger{" +
                "item=" + item +
                ", src='" + src + '\'' +
                ", fnurl='" + fnurl + '\'' +
                ", sign='" + sign + '\'' +
                ", data='" + data + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
