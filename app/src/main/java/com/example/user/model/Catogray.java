package com.example.user.model;

import java.util.List;

/**
 * Created by user on 2017/12/13.
 */
public class Catogray {
    private String kind;
    private List<Dish> list;
    private int count;
    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public List<Dish> getList() {
        return list;
    }

    public void setList(List<Dish> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "CatograyBean{" +
                "kind='" + kind + '\'' +
                ", list=" + list +
                ", count=" + count +
                '}';
    }
}