package com.example.zbusst.Dao.Interfaces;

import com.example.zbusst.Bean.SingleGoods;
import com.example.zbusst.Bean.User;

import java.sql.SQLException;
import java.util.List;

public interface DaoUser {
    int add(User user) throws SQLException, ClassNotFoundException;
    int del(String goodid) throws SQLException, ClassNotFoundException;
    int updateState(User user) throws SQLException, ClassNotFoundException;
    List<User> getUserList() throws SQLException, ClassNotFoundException;
    User getSingle(String openid) throws SQLException, ClassNotFoundException;
}
