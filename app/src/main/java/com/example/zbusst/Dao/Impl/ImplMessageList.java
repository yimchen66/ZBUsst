package com.example.zbusst.Dao.Impl;

import com.example.zbusst.Bean.SingleGoods;
import com.example.zbusst.Bean.SingleMessage;
import com.example.zbusst.Dao.BaseDao.BaseDaoGoodList;
import com.example.zbusst.Dao.Interfaces.DaoMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImplMessageList extends BaseDaoGoodList implements DaoMessage {
    @Override
    public int add(SingleMessage singleMessage) {
        String sql = "insert into message values(?,?,?,?,?,?)";
        Object[] objects = { singleMessage.getGoodid(),singleMessage.getTargetopenid(),
                singleMessage.getSendopenid(),singleMessage.getMsg(),
                singleMessage.getRead_state(),singleMessage.getMessagetime()
        };
        return excuteUpdate(sql,objects);
    }

    @Override
    public int updateState(SingleMessage singleMessage) {
        String sql = "update message set readstate=? where goodid=? and time=?";
        Object[] objects = {singleMessage.getRead_state(),singleMessage.getGoodid(),singleMessage.getMessagetime()};
        return excuteUpdate(sql,objects);
    }

    @Override
    public int updateRead(String goodid, String time) {
        String sql = "update message set readstate=1 where goodid=? and time=?";
        Object[] objects = {goodid,time};
        return excuteUpdate(sql,objects);
    }

    @Override
    public List<SingleMessage> getMessageList(String targetopenid) {
        List<SingleMessage> list = new ArrayList<>();
        String sql = "select * from message where targetopenid=? order by time desc";
        Connection con = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1,targetopenid);
            rs = ps.executeQuery();

            while (rs.next()){
                SingleMessage singleMessage = new SingleMessage();
                try {
                    singleMessage.setGoodid(rs.getString("goodid"));
                    singleMessage.setTargetopenid(rs.getString("targetopenid"));
                    singleMessage.setSendopenid(rs.getString("sendopenid"));
                    singleMessage.setMsg(rs.getString("msg"));
                    singleMessage.setRead_state(rs.getInt("readstate"));
                    singleMessage.setMessagetime(rs.getString("time"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                list.add(singleMessage);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return list;
    }

    @Override
    public int deleteMessage(String goodid){
        String sql = "delete from message where goodid=?";
        Object[] objects = {goodid};
        return excuteUpdate(sql,objects);

    }

    @Override
    public SingleMessage getSingleMessage(String goodid, String time){
        SingleMessage singleMessage = new SingleMessage();
        String sql = "select * from message where goodid=? and time=?";
        Connection con = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1,goodid);
            ps.setString(2,time);
            rs = ps.executeQuery();

            if (rs.next()){
                try {
                    singleMessage.setGoodid(rs.getString("goodid"));
                    singleMessage.setTargetopenid(rs.getString("targetopenid"));
                    singleMessage.setSendopenid(rs.getString("sendopenid"));
                    singleMessage.setMsg(rs.getString("msg"));
                    singleMessage.setRead_state(rs.getInt("readstate"));
                    singleMessage.setMessagetime(rs.getString("time"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return singleMessage;
    }


}
