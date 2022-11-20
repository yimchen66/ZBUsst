package com.example.zbusst.Dao.Interfaces;

import com.example.zbusst.Bean.SingleGoods;
import com.example.zbusst.Bean.SingleMessage;

import java.sql.SQLException;
import java.util.List;

public interface DaoMessage {
    int add(SingleMessage singleMessage);
    int updateState(SingleMessage singleMessage) throws SQLException, ClassNotFoundException;
    List<SingleMessage> getMessageList(String targetopenid) throws SQLException, ClassNotFoundException;
    int deleteMessage(String goodid) throws SQLException, ClassNotFoundException;
    SingleMessage getSingleMessage(String openid,String time) throws SQLException, ClassNotFoundException;
    int updateRead(String goodid,String time);

}
