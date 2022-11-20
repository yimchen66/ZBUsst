package com.example.zbusst.Dao.Impl;

import com.example.zbusst.Bean.GoodInTransaction;
import com.example.zbusst.Dao.BaseDao.BaseDaoGoodTransaction;
import com.example.zbusst.Dao.Interfaces.DaoGoodTransaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImlGoodTransaction extends BaseDaoGoodTransaction implements DaoGoodTransaction {
    @Override
    public int add(GoodInTransaction goodInTransaction){
        String sql = "insert into transactionorder values(?,?,?,?,?,?)";
        Object[] objects = {
                goodInTransaction.getGoodid(),goodInTransaction.getSelleropenid(),
                goodInTransaction.getBuyeropenid(),goodInTransaction.getAccepttime(),
                goodInTransaction.getState_seller(),goodInTransaction.getState_buyer()
        };
        return excuteUpdate(sql,objects);
    }

    @Override
    public int del(String goodid)  {
        String sql = "delete from transactionorder where goodid=?";
        Object[] objects = {goodid};
        return excuteUpdate(sql,objects);
    }

    @Override
    public int updateStateSeller(GoodInTransaction goodInTransaction) {
        String sql = "update transactionorder set state_seller=? where goodid=?";
        Object[] objects = {goodInTransaction.getState_seller(),goodInTransaction.getGoodid()};
        return excuteUpdate(sql,objects);
    }

    @Override
    public int updateStateBuyer(GoodInTransaction goodInTransaction)  {
        String sql = "update transactionorder set state_buyer=?";
        Object[] objects = {goodInTransaction.getState_buyer()};
        return excuteUpdate(sql,objects);
    }

    @Override
    public List<GoodInTransaction> getSellerOrderList(String selleropenid)  {
        List<GoodInTransaction> list = new ArrayList<>();
        String sql = "select * from transactionorder where selleropenid = ? order by accepttime desc";
        Connection con = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1,selleropenid);

            rs = ps.executeQuery();

            while (rs.next()){
                GoodInTransaction goodInTransaction = new GoodInTransaction();
                try {
                    goodInTransaction.setGoodid(rs.getString("goodid"));
                    goodInTransaction.setSelleropenid(rs.getString("selleropenid"));
                    goodInTransaction.setBuyeropenid(rs.getString("buyeropenid"));
                    goodInTransaction.setAccepttime(rs.getString("accepttime"));
                    goodInTransaction.setState_seller(rs.getInt("state_seller"));
                    goodInTransaction.setState_buyer(rs.getInt("state_buyer"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                list.add(goodInTransaction);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return list;
    }

    @Override
    public List<GoodInTransaction> getBuyerOrderList(String buyeropenid)  {
        List<GoodInTransaction> list = new ArrayList<>();
        String sql = "select * from transactionorder where buyeropenid = ? order by accepttime desc";
        Connection con = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1,buyeropenid);

            rs = ps.executeQuery();

            while (rs.next()){
                GoodInTransaction goodInTransaction = new GoodInTransaction();
                try {
                    goodInTransaction.setGoodid(rs.getString("goodid"));
                    goodInTransaction.setSelleropenid(rs.getString("selleropenid"));
                    goodInTransaction.setBuyeropenid(rs.getString("buyeropenid"));
                    goodInTransaction.setAccepttime(rs.getString("accepttime"));
                    goodInTransaction.setState_seller(rs.getInt("state_seller"));
                    goodInTransaction.setState_buyer(rs.getInt("state_buyer"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                list.add(goodInTransaction);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return list;
    }

    @Override
    public GoodInTransaction getSingle(String goodid) {
        String sql = "select * from transactionorder where goodid = ?";
        GoodInTransaction goodInTransaction = new GoodInTransaction();
        Connection con = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1,goodid);

            rs = ps.executeQuery();

            if (rs.next()){

                try {
                    goodInTransaction.setGoodid(rs.getString("goodid"));
                    goodInTransaction.setSelleropenid(rs.getString("selleropenid"));
                    goodInTransaction.setBuyeropenid(rs.getString("buyeropenid"));
                    goodInTransaction.setAccepttime(rs.getString("accepttime"));
                    goodInTransaction.setState_seller(rs.getInt("state_seller"));
                    goodInTransaction.setState_buyer(rs.getInt("state_buyer"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return goodInTransaction;
    }
}
