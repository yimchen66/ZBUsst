package com.example.zbusst.Dao.Interfaces;

import com.example.zbusst.Bean.MoneyTransaction;

import java.util.List;

public interface DaoMoneyTransaction {
    int add(MoneyTransaction moneyTransaction);
    int del(String goodid);
    List<MoneyTransaction> getMoneyTrans(String opneid);
}
