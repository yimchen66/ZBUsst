package com.example.zbusst.Dao.Interfaces;

import com.example.zbusst.Bean.SingleGoods;

import java.sql.SQLException;
import java.util.List;

public interface DaoGoodList {

    int add(SingleGoods goods) throws SQLException, ClassNotFoundException;
    int del(String goodid) throws SQLException, ClassNotFoundException;
    int updateState(SingleGoods goods) throws SQLException, ClassNotFoundException;
    List<SingleGoods> getGoodList() throws SQLException, ClassNotFoundException;
    List<SingleGoods> getGoodList(String str_edittext,String str_type,String asc_price,String asc_time,String current_order) throws SQLException, ClassNotFoundException;
    SingleGoods getSingle(String goodid) throws SQLException, ClassNotFoundException;
    List<SingleGoods> getSellerList(String selleropenid) throws SQLException, ClassNotFoundException;

}
