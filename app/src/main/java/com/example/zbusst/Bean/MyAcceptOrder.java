package com.example.zbusst.Bean;

public class MyAcceptOrder {
    private String sellertouxiang = "";
    private String sellernickname = "";
    private String selleropenid = "";
    private String buyeropenid;
    private String goodid;
    private String goodintro;
    private String uploadtime;
    private String goodpic;
    private float goodprice;
    private int state_seller = 0;
    private int state_buyer = 0;


    public MyAcceptOrder() {
    }

    public String getSellertouxiang() {
        return sellertouxiang;
    }

    public void setSellertouxiang(String sellertouxiang) {
        this.sellertouxiang = sellertouxiang;
    }

    public String getSellernickname() {
        return sellernickname;
    }

    public void setSellernickname(String sellernickname) {
        this.sellernickname = sellernickname;
    }

    public String getSelleropenid() {
        return selleropenid;
    }

    public void setSelleropenid(String selleropenid) {
        this.selleropenid = selleropenid;
    }

    public String getBuyeropenid() {
        return buyeropenid;
    }

    public void setBuyeropenid(String buyeropenid) {
        this.buyeropenid = buyeropenid;
    }

    public String getGoodid() {
        return goodid;
    }

    public void setGoodid(String goodid) {
        this.goodid = goodid;
    }

    public String getGoodintro() {
        return goodintro;
    }

    public void setGoodintro(String goodintro) {
        this.goodintro = goodintro;
    }

    public String getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }

    public String getGoodpic() {
        return goodpic;
    }

    public void setGoodpic(String goodpic) {
        this.goodpic = goodpic;
    }

    public float getGoodprice() {
        return goodprice;
    }

    public void setGoodprice(float goodprice) {
        this.goodprice = goodprice;
    }

    public int getState_seller() {
        return state_seller;
    }

    public void setState_seller(int state_seller) {
        this.state_seller = state_seller;
    }

    public int getState_buyer() {
        return state_buyer;
    }

    public void setState_buyer(int state_buyer) {
        this.state_buyer = state_buyer;
    }
}
