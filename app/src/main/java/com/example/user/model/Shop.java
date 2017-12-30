package com.example.user.model;

/**
 * Created by user on 2017/12/13.
 */
public class Shop {

    private static final long serialVersionUID = 1L;
    private int sid;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    private int uid;
    private String name;
    private String img;
    private String phone;
    private String location;
    private String remark;
    public int getSid() {
        return sid;
    }
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img =  img;
    }
    public void setSid(int sid) {
        this.sid = sid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
