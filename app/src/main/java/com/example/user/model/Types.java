package com.example.user.model;

/**
 * Created by user on 2017/12/13.
 */
public class Types {

    private static final long serialVersionUID = 1L;

    private int sid;
    private int tid;
    private String name;
    public int getSid() {
        return sid;
    }
    public void setSid(int sid) {
        this.sid = sid;
    }
    public int getTid() {
        return tid;
    }
    public void setTid(int tid) {
        this.tid = tid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
