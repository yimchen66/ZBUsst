package com.example.zbusst.Bean;

public class User {
    private String openid;
    private String nickname;
    private String figureurl_qq_2;
    private String money;

    public User(String openid, String nickname, String figureurl_qq_2) {
        this.openid = openid;
        this.nickname = nickname;
        this.figureurl_qq_2 = figureurl_qq_2;
    }

    public User() {
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFigureurl_qq_2() {
        return figureurl_qq_2;
    }

    public void setFigureurl_qq_2(String figureurl_qq_2) {
        this.figureurl_qq_2 = figureurl_qq_2;
    }
}
