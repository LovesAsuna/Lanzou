package me.lovesasuna.lanzou.bean;

/**
 * @author LovesAsuna
 * @date 2020/8/22 9:31
 **/
public class Triple<A, B, C> {
    public A first;
    public B second;
    public C third;

    public Triple(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
}
