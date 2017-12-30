package com.example.user.model;
import java.sql.Timestamp;
/**
 * Created by user on 2017/12/19.
 */
public class Order {
    private int sid;
    private int oid;
    private int uid;
    private String time;
    private String location;
    private double price;
    private int state;
    private double discount;
    private int pay;
    private String remark;
    private String name;
    private String wlocation;
    private String phone;
    private int sex;

    public int getSid() {
        return sid;
    }
    public void setSid(int sid) {
        this.sid = sid;
    }
    public int getOid() {
        return oid;
    }
    public void setOid(int oid) {
        this.oid = oid;
    }
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public double getDiscount() {
        return discount;
    }
    public void setDiscount(double discount) {
        this.discount = discount;
    }
    public int getPay() {
        return pay;
    }
    public void setPay(int pay) {
        this.pay = pay;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getWlocation() {
        return wlocation;
    }
    public void setWlocation(String wlocation) {
        this.wlocation = wlocation;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public int getSex() {
        return sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
}
