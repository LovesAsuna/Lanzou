package me.lovesasuna.lanzou.network;

import me.lovesasuna.lanzou.bean.User;

import java.io.File;

/**
 * @author LovesAsuna
 * @date 2020/8/22 14:07
 **/
public interface Uploadable {
    boolean upload(User user, File file, int loc);
}
