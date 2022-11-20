package com.example.zbusst.Bean;

public class SingleMessage {
    private String targetnickname;
    private String targetopenid;
    private String sendopenid;
    private String msg;
    private String goodid;
    private String targettouxiang;
    private String goodintro;
    private String messagetime;
    private int read_state;

    public SingleMessage(String targetnickname, String targetopenid,
                         String goodid, String targettouxiang,
                         String sendopenid,String msg,
                         String goodintro, String messagetime,int read_state) {
        this.targetnickname = targetnickname;
        this.targetopenid = targetopenid;
        this.goodid = goodid;
        this.targettouxiang = targettouxiang;
        this.goodintro = goodintro;
        this.messagetime = messagetime;
        this.read_state = read_state;
        this.msg = msg;
        this.sendopenid = sendopenid;
    }

    public String getSendopenid() {
        return sendopenid;
    }

    public void setSendopenid(String sendopenid) {
        this.sendopenid = sendopenid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getRead_state() {
        return read_state;
    }

    public void setRead_state(int read_state) {
        this.read_state = read_state;
    }

    public SingleMessage() {
    }

    public String getTargetnickname() {
        return targetnickname;
    }

    public void setTargetnickname(String targetnickname) {
        this.targetnickname = targetnickname;
    }

    public String getTargetopenid() {
        return targetopenid;
    }

    public void setTargetopenid(String targetopenid) {
        this.targetopenid = targetopenid;
    }

    public String getGoodid() {
        return goodid;
    }

    public void setGoodid(String goodid) {
        this.goodid = goodid;
    }

    public String getTargettouxiang() {
        return targettouxiang;
    }

    public void setTargettouxiang(String targettouxiang) {
        this.targettouxiang = targettouxiang;
    }

    public String getGoodintro() {
        return goodintro;
    }

    public void setGoodintro(String goodintro) {
        this.goodintro = goodintro;
    }

    public String getMessagetime() {
        return messagetime;
    }

    public void setMessagetime(String messagetime) {
        this.messagetime = messagetime;
    }
}
