package com.example.zbusst.Bean;

public class GoodInTransaction {
    private String selleropenid;
    private String buyeropenid;
    private String goodid;
    private String accepttime;
    private int state_seller;
    private int state_buyer;

    public GoodInTransaction(String selleropenid, String buyeropenid, String goodid, String accepttime, int state_seller, int state_byer) {
        this.selleropenid = selleropenid;
        this.buyeropenid = buyeropenid;
        this.goodid = goodid;
        this.accepttime = accepttime;
        this.state_seller = state_seller;
        this.state_buyer = state_byer;
    }

    public GoodInTransaction() {
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

    public String getAccepttime() {
        return accepttime;
    }

    public void setAccepttime(String accepttime) {
        this.accepttime = accepttime;
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
