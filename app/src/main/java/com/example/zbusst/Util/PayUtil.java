package com.example.zbusst.Util;

import android.os.Message;

import com.example.zbusst.Bean.GoodInTransaction;
import com.example.zbusst.Bean.MoneyTransaction;
import com.example.zbusst.Bean.SingleGoods;
import com.example.zbusst.Bean.User;
import com.example.zbusst.Dao.Impl.ImlGoodTransaction;
import com.example.zbusst.Dao.Impl.ImlUser;
import com.example.zbusst.Dao.Impl.ImplGoodList;
import com.example.zbusst.Dao.Impl.ImplMoneyTransaction;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PayUtil {

    public static void payCompelte(String goodid){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ImlGoodTransaction imlGoodTransaction = new ImlGoodTransaction();
                GoodInTransaction goodtrans = imlGoodTransaction.getSingle(goodid);
                if( ! (goodtrans.getState_seller() == 1 && goodtrans.getState_buyer() == 1)) //订单未完成
                    return;
                ImplGoodList implGoodList = new ImplGoodList();
                SingleGoods singlegood = implGoodList.getSingle(goodid);

                String selleropenid = goodtrans.getSelleropenid();
                String buyeropenid = goodtrans.getBuyeropenid();
                Float price = singlegood.getGoodprice();
                String goodtype = singlegood.getGoodtype();

                ImlUser imlUser = new ImlUser();
                if(goodtype.contains("货")){  //二手货交易
                    User user = imlUser.getSingle(selleropenid);
                    String userMoney = user.getMoney();
                    user.setMoney(String.valueOf(Float.parseFloat(userMoney) + price));

                    imlUser.updateState(user);
                }else{  //非二手货交易
                    User user = imlUser.getSingle(buyeropenid);
                    String userMoney = user.getMoney();
                    user.setMoney(String.valueOf(Float.parseFloat(userMoney) + price));

                    imlUser.updateState(user);
                }


                //写入交易明细数据库
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

                ImplMoneyTransaction implMoneyTransaction = new ImplMoneyTransaction();
                MoneyTransaction singlemoney = new MoneyTransaction();
                singlemoney.setTime(sdf.format(date));
                singlemoney.setGoodid(goodid);
                singlemoney.setSelleropenid(selleropenid);
                singlemoney.setBuyeropenid(buyeropenid);
                singlemoney.setMoney(String.valueOf(price));
                implMoneyTransaction.add(singlemoney);
            }
        }).start();
    }

}
