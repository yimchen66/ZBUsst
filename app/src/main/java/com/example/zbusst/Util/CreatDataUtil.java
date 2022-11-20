package com.example.zbusst.Util;

import android.util.Log;

import com.example.zbusst.Bean.SingleGoods;
import com.example.zbusst.Dao.Impl.ImplGoodList;

import java.util.ArrayList;
import java.util.List;

public class CreatDataUtil {
    private static final String TAG = "select";

    //    public static List<SingleGoods> getDataList(){
//        List<SingleGoods> list = new ArrayList<>();
//        for(int i=1;i<9;i++){
//            SingleGoods goods = new SingleGoods();
//            goods.setGoodpicture("http://radiotext.test.upcdn.net/book"+i+".jpg");
//            goods.setGoodbeizhu("备注备注备注备注备注备注备注备注备注备注备注备注"+i);
//            goods.setGoodintroduct("订单状态啊"+"介绍介绍介绍计算机技术绍介绍计算机技术计算机计." +
//                    "绍介绍计算机技术机技术介绍"+i);
//            goods.setGoodtype("拿快递"+i);
//            goods.setGoodprice(i);
//            goods.setUploadtime("2022.10.18 22:29:1"+i);
//            list.add(goods);
//        }
//
//        return  list;
//    }
    public static List<SingleGoods> getGoodList(){
        ImplGoodList goodList = new ImplGoodList();
        return goodList.getGoodList();
    }

    public static List<SingleGoods> getGoodList(String str_edittext, String str_type,
                                                String asc_price, String asc_time,String current_order){
        ImplGoodList goodList = new ImplGoodList();
        if(null == str_edittext)
            str_edittext = "";
        if(null == asc_time)
            asc_time = "desc";
        if("全部类型".equals(str_type))
            str_type = "";

        List<SingleGoods> goodList1 = goodList.getGoodList(str_edittext, str_type, asc_price, asc_time,current_order);
        Log.e(TAG, "getGoodList: 个数  "+goodList1.size());
        return goodList1;

    }
}
