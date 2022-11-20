package com.example.zbusst.Bean;

import java.io.Serializable;

public class SingleGoods  implements Serializable {
    private String goodpicture;
    private String goodtype;
    private String goodintroduct;
    private String goodbeizhu;
    private String uploadtime;
    private float goodprice;
    private String goodid;
    private String openid;
    private String hostname;
    private String hostopenid;
    private String hostuserinfo;
    private int state;



    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getHostopenid() {
        return hostopenid;
    }

    public void setHostopenid(String hostopenid) {
        this.hostopenid = hostopenid;
    }

    public String getHostuserinfo() {
        return hostuserinfo;
    }

    public void setHostuserinfo(String hostuserinfo) {
        this.hostuserinfo = hostuserinfo;
    }

    public String getGoodid() {
        return goodid;
    }

    public void setGoodid(String goodid) {
        this.goodid = goodid;
    }

    public String getGoodpicture() {
        return goodpicture;
    }

    public void setGoodpicture(String goodpicture) {
        this.goodpicture = goodpicture;
    }

    public String getGoodtype() {
        return goodtype;
    }

    public void setGoodtype(String goodtype) {
        this.goodtype = goodtype;
    }

    public String getGoodintroduct() {
        return goodintroduct;
    }

    public void setGoodintroduct(String goodintroduct) {
        this.goodintroduct = goodintroduct;
    }

    public String getGoodbeizhu() {
        return goodbeizhu;
    }

    public void setGoodbeizhu(String goodbeizhu) {
        this.goodbeizhu = goodbeizhu;
    }

    public String getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }

    public float getGoodprice() {
        return goodprice;
    }

    public void setGoodprice(float goodprice) {
        this.goodprice = goodprice;
    }

//    @Override
//    public String toString() {
//        return "SingleGoods{" +
//                "goodtype='" + goodtype + '\'' +
//                ", goodintroduct='" + goodintroduct + '\'' +
//                ", uploadtime='" + uploadtime + '\'' +
//                ", goodprice=" + goodprice +
//                '}';
//    }
}
