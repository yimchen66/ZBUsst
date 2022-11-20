package com.example.zbusst.Dao.Impl;

import com.example.zbusst.Bean.MoneyTransaction;
import com.example.zbusst.Bean.User;
import com.example.zbusst.Dao.BaseDao.BaseDaoGoodList;
import com.example.zbusst.Dao.Interfaces.DaoMoneyTransaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImplMoneyTransaction extends BaseDaoGoodList implements DaoMoneyTransaction {
    @Override
    public int add(MoneyTransaction moneyTransaction) {
        String sql = "insert into compelteorder values(?,?,?,?,?)";
        Object[] objects = {moneyTransaction.getGoodid(),moneyTransaction.getSelleropenid(),
            moneyTransaction.getBuyeropenid(),moneyTransaction.getMoney(),moneyTransaction.getTime()};
        return excuteUpdate(sql,objects);
    }

    @Override
    public int del(String goodid) {
        return 0;
    }

    @Override
    public List<MoneyTransaction> getMoneyTrans(String opneid) {
        List<MoneyTransaction> list = new ArrayList<>();
        String sql = "select * from compelteorder where selleropenid=? or buyeropenid=? order by time desc";
        Connection con = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1,opneid);
            ps.setString(2,opneid);

            rs = ps.executeQuery();
            while (rs.next()){
                MoneyTransaction moneyTransaction = new MoneyTransaction();
                try {
                    moneyTransaction.setGoodid(rs.getString("goodid"));
                    moneyTransaction.setSelleropenid(rs.getString("selleropenid"));
                    moneyTransaction.setBuyeropenid(rs.getString("buyeropenid"));
                    moneyTransaction.setMoney((rs.getString("money")));
                    moneyTransaction.setTime(rs.getString("time"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                list.add(moneyTransaction);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }
}
