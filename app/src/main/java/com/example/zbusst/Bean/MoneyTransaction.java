package com.example.zbusst.Bean;

public class MoneyTransaction {
    private String goodid;
    private String selleropenid;
    private String buyeropenid;
    private String time;
    private String money;
    private String touxiang;
    private String name;
    private String goodtype;



    public MoneyTransaction() {
    }

    public String getTouxiang() {
        return touxiang;
    }

    public void setTouxiang(String touxiang) {
        this.touxiang = touxiang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoodtype() {
        return goodtype;
    }

    public void setGoodtype(String goodtype) {
        this.goodtype = goodtype;
    }

    public String getGoodid() {
        return goodid;
    }

    public void setGoodid(String goodid) {
        this.goodid = goodid;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
