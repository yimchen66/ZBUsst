package com.example.zbusst.Bean;

public class MyLaunchOrder {
    private String buyertouxiang = "";
    private String buyernickname = "";
    private String buyeropenid = "";
    private String selleropenid;
    private String goodid;
    private String goodintro;
    private String uploadtime;
    private String goodpic;
    private float goodprice;
    private int state_seller = 0;
    private int state_buyer = 0;
    private int state;


    public MyLaunchOrder(String buyertouxiang, String buyernickname, String buyeropenid,
                         String selleropenid, String goodid, String goodintro,String goodpic,int state,
                         String uploadtime, float goodprice, int state_seller, int state_buyer) {
        this.buyertouxiang = buyertouxiang;
        this.buyernickname = buyernickname;
        this.buyeropenid = buyeropenid;
        this.selleropenid = selleropenid;
        this.goodid = goodid;
        this.goodintro = goodintro;
        this.uploadtime = uploadtime;
        this.goodprice = goodprice;
        this.state_seller = state_seller;
        this.state_buyer = state_buyer;
        this.goodpic = goodpic;
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public MyLaunchOrder() {
    }

    public String getGoodpic() {
        return goodpic;
    }

    public void setGoodpic(String goodpic) {
        this.goodpic = goodpic;
    }

    public String getBuyertouxiang() {
        return buyertouxiang;
    }

    public void setBuyertouxiang(String buyertouxiang) {
        this.buyertouxiang = buyertouxiang;
    }

    public String getBuyernickname() {
        return buyernickname;
    }

    public void setBuyernickname(String buyernickname) {
        this.buyernickname = buyernickname;
    }

    public String getBuyeropenid() {
        return buyeropenid;
    }

    public void setBuyeropenid(String buyeropenid) {
        this.buyeropenid = buyeropenid;
    }

    public String getSelleropenid() {
        return selleropenid;
    }

    public void setSelleropenid(String selleropenid) {
        this.selleropenid = selleropenid;
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
