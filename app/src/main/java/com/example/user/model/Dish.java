package com.example.user.model;

/**
 * Created by user on 2017/12/14.
 */
public class Dish {
    private static final long serialVersionUID = 1L;

    private int did;
    private int sid;
    private String name;
    private double price;
    private String remark;
    private String img;
    private int sellcount;
    private int tid;
    public int getDid() {
        return did;
    }
    public void setDid(int did) {
        this.did = did;
    }
    public int getSid() {
        return sid;
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
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remarkString) {
        this.remark = remarkString;
    }
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public int getSellcount() {
        return sellcount;
    }
    public void setSellcount(int sellcount) {
        this.sellcount = sellcount;
    }
    public void setTid(int tid){
        this.tid=tid;
    }
    public int getTid(){
        return tid;
    }
    @Override
    public int hashCode() {

        return this.getDid()+this.getName().hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Dish other = (Dish) obj;
        if (did != other.did)
            return false;
        return true;
    }


}
