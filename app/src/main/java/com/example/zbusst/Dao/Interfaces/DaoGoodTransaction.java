package com.example.zbusst.Dao.Interfaces;

import com.example.zbusst.Bean.GoodInTransaction;
import com.example.zbusst.Bean.SingleGoods;

import java.sql.SQLException;
import java.util.List;

public interface DaoGoodTransaction {
    int add(GoodInTransaction goodInTransaction) throws SQLException, ClassNotFoundException;
    int del(String goodid) throws SQLException, ClassNotFoundException;
    int updateStateSeller(GoodInTransaction goodInTransaction) throws SQLException, ClassNotFoundException;
    int updateStateBuyer(GoodInTransaction goodInTransaction) throws SQLException, ClassNotFoundException;
    List<GoodInTransaction> getSellerOrderList(String selleropenid) throws SQLException, ClassNotFoundException;
    List<GoodInTransaction> getBuyerOrderList(String buyeropenid) throws SQLException, ClassNotFoundException;
    GoodInTransaction getSingle(String goodid) throws SQLException, ClassNotFoundException;


}
