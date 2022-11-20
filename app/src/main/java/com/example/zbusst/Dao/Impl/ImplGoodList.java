package com.example.zbusst.Dao.Impl;

import com.example.zbusst.Bean.SingleGoods;
import com.example.zbusst.Dao.BaseDao.BaseDaoGoodList;
import com.example.zbusst.Dao.Interfaces.DaoGoodList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//dao实现类
public class ImplGoodList extends BaseDaoGoodList implements DaoGoodList {
    //增加订单
    @Override
    public int add(SingleGoods goods){
        String sql = "insert into wholeorder values(?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] objects = {goods.getGoodid(),goods.getOpenid(),goods.getHostname(),goods.getHostopenid(),
                    goods.getUploadtime(),goods.getHostuserinfo(),goods.getGoodbeizhu(),goods.getGoodpicture(),
                    goods.getGoodintroduct(),goods.getGoodprice(),
                    goods.getState(),goods.getGoodtype()
                };
        return excuteUpdate(sql,objects);
    }

    @Override
    public int del(String goodid) {
        String sql = "delete from wholeorder where _id=?";
        Object[] objects = {goodid};
        return excuteUpdate(sql,objects);
    }

    @Override
    public int updateState(SingleGoods goods){
        String sql = "update wholeorder set orderstate=? where _id=?";
        Object[] objects = {goods.getState(),goods.getGoodid()};
        return excuteUpdate(sql,objects);

    }

    @Override
    public List<SingleGoods> getGoodList() {
        List<SingleGoods> list = new ArrayList<>();
        String sql = "select * from wholeorder where orderstate = ? ";
        Connection con = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1,0);
            rs = ps.executeQuery();

            while (rs.next()){
                SingleGoods singleGoods = new SingleGoods();
                try {
                    singleGoods.setGoodid(rs.getString("_id"));
                    singleGoods.setGoodtype(rs.getString("ordertype"));
                    singleGoods.setGoodprice(rs.getFloat("ordermoney"));
                    singleGoods.setGoodintroduct(rs.getString("orderjianjie"));
                    singleGoods.setGoodbeizhu(rs.getString("orderbeizhu"));
                    singleGoods.setGoodpicture(rs.getString("orderimage"));
                    singleGoods.setState(rs.getInt("orderstate"));
                    singleGoods.setUploadtime(rs.getString("hosttime"));
                    singleGoods.setHostopenid(rs.getString("hostopenid"));
                    singleGoods.setOpenid(rs.getString("_openid"));
                    singleGoods.setHostname(rs.getString("hostname"));
                    singleGoods.setHostuserinfo(rs.getString("hostuserinfo"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                list.add(singleGoods);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return list;
    }

    @Override
    public List<SingleGoods> getGoodList(String str_edittext, String str_type,
                                         String asc_price, String asc_time,String current_order){
        /**
         * @Description: 精确查询
         * @Params: str_edittext 搜索框内容
         * @Params: str_type 类型筛选器
         * @Params: asc_price 价钱顺序
         * @Params: asc_time 时间顺序
         * @Params: current_order 当前选择的主排序属性
         * @Return 查询结果
         */
        List<SingleGoods> list = new ArrayList<>();
        String sql = "select * from wholeorder where ordertype like ?" +
                "and (orderjianjie like ? or ordertype like ? or hostname like ?)" +
                "and orderstate = ? "+
                "order by "+current_order;
        if(current_order.equals("hosttime"))
            sql = sql + " " + asc_time;
        else sql = sql + " " + asc_price;
        Connection con = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1,"%"+str_type+"%");
            ps.setString(2,"%"+str_edittext+"%");
            ps.setString(3,"%"+str_edittext+"%");
            ps.setString(4,"%"+str_edittext+"%");
            ps.setInt(5,0);

            rs = ps.executeQuery();

            while (rs.next()){
                SingleGoods singleGoods = new SingleGoods();
                try {
                    singleGoods.setGoodid(rs.getString("_id"));
                    singleGoods.setGoodtype(rs.getString("ordertype"));
                    singleGoods.setGoodprice(rs.getFloat("ordermoney"));
                    singleGoods.setGoodintroduct(rs.getString("orderjianjie"));
                    singleGoods.setGoodbeizhu(rs.getString("orderbeizhu"));
                    singleGoods.setGoodpicture(rs.getString("orderimage"));
                    singleGoods.setState(rs.getInt("orderstate"));
                    singleGoods.setUploadtime(rs.getString("hosttime"));
                    singleGoods.setHostopenid(rs.getString("hostopenid"));
                    singleGoods.setOpenid(rs.getString("_openid"));
                    singleGoods.setHostname(rs.getString("hostname"));
                    singleGoods.setHostuserinfo(rs.getString("hostuserinfo"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                list.add(singleGoods);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return list;
    }

    @Override
    public SingleGoods getSingle(String goodid) {
        SingleGoods singleGoods = new SingleGoods();
        String sql = "select * from wholeorder where _id=?";
        Connection con = getConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1,String.valueOf(goodid));
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                singleGoods.setGoodid(rs.getString("_id"));
                singleGoods.setGoodtype(rs.getString("ordertype"));
                singleGoods.setGoodprice(rs.getFloat("ordermoney"));
                singleGoods.setGoodintroduct(rs.getString("orderjianjie"));
                singleGoods.setGoodbeizhu(rs.getString("orderbeizhu"));
                singleGoods.setGoodpicture(rs.getString("orderimage"));
                singleGoods.setState(rs.getInt("orderstate"));
                singleGoods.setUploadtime(rs.getString("hosttime"));
                singleGoods.setHostopenid(rs.getString("hostopenid"));
                singleGoods.setOpenid(rs.getString("_openid"));
                singleGoods.setHostname(rs.getString("hostname"));
                singleGoods.setHostuserinfo(rs.getString("hostuserinfo"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return singleGoods;
    }

    @Override
    public List<SingleGoods> getSellerList(String selleropenid) {
        List<SingleGoods> list = new ArrayList<>();
        String sql = "select * from wholeorder where hostopenid = ? ";
        Connection con = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1,selleropenid);
            rs = ps.executeQuery();

            while (rs.next()){
                SingleGoods singleGoods = new SingleGoods();
                try {
                    singleGoods.setGoodid(rs.getString("_id"));
                    singleGoods.setGoodtype(rs.getString("ordertype"));
                    singleGoods.setGoodprice(rs.getFloat("ordermoney"));
                    singleGoods.setGoodintroduct(rs.getString("orderjianjie"));
                    singleGoods.setGoodbeizhu(rs.getString("orderbeizhu"));
                    singleGoods.setGoodpicture(rs.getString("orderimage"));
                    singleGoods.setState(rs.getInt("orderstate"));
                    singleGoods.setUploadtime(rs.getString("hosttime"));
                    singleGoods.setHostopenid(rs.getString("hostopenid"));
                    singleGoods.setOpenid(rs.getString("_openid"));
                    singleGoods.setHostname(rs.getString("hostname"));
                    singleGoods.setHostuserinfo(rs.getString("hostuserinfo"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                list.add(singleGoods);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return list;

    }
}
