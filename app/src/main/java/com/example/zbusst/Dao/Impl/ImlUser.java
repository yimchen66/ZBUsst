package com.example.zbusst.Dao.Impl;

import com.example.zbusst.Bean.SingleGoods;
import com.example.zbusst.Bean.User;
import com.example.zbusst.Dao.BaseDao.BaseDaoGoodList;
import com.example.zbusst.Dao.BaseDao.BaseDaoGoodTransaction;
import com.example.zbusst.Dao.Interfaces.DaoUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImlUser extends BaseDaoGoodTransaction implements DaoUser {
    @Override
    public int add(User user){
        String sql = "insert into user values(?,?,?,?)";
        Object[] objects = {user.getOpenid(),user.getNickname(),user.getFigureurl_qq_2(),user.getMoney()};
        return excuteUpdate(sql,objects);
    }

    @Override
    public int del(String openid){
        String sql = "delete from user where openid=?";
        Object[] objects = {openid};
        return excuteUpdate(sql,objects);
    }

    @Override
    public int updateState(User user) {
        String sql = "update user set money=? where openid=?";
        Object[] objects = {user.getMoney(),user.getOpenid()};
        return excuteUpdate(sql,objects);
    }

    @Override
    public List<User> getUserList() {
        List<User> list = new ArrayList<>();
        String sql = "select * from user";
        Connection con = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()){
                User user = new User();
                try {
                    user.setOpenid(rs.getString("openid"));
                    user.setNickname(rs.getString("nickname"));
                    user.setFigureurl_qq_2(rs.getString("figureurl"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                list.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return list;
    }

    @Override
    public User getSingle(String openid) {
        User user = new User();
        String sql = "select * from user where openid=?";
        Connection con = getConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1,String.valueOf(openid));
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                user.setOpenid(rs.getString("openid"));
                user.setNickname(rs.getString("nickname"));
                user.setFigureurl_qq_2(rs.getString("figureurl"));
                user.setMoney(rs.getString("money"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return user;
    }
}
