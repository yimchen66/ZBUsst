package com.example.zbusst;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.zbusst.Adapter.MyListViewAdapterForMoney;
import com.example.zbusst.Bean.MoneyTransaction;
import com.example.zbusst.Bean.SingleGoods;
import com.example.zbusst.Bean.User;
import com.example.zbusst.Dao.Impl.ImlUser;
import com.example.zbusst.Dao.Impl.ImplGoodList;
import com.example.zbusst.Dao.Impl.ImplMoneyTransaction;
import com.example.zbusst.Util.ChangeTabColor;

import java.util.List;

public class Activity_MoneyTransaction extends AppCompatActivity {
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private String useropenid,nickname;
    private Context context;
    private List<MoneyTransaction> list;
    private ListView lv_money;

    private String TAG = "money";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transaction);
        sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        if(sharedPreferences == null || sharedPreferences.getString("openid","0001").equals("0001")){
//            showFailDialog("请先登录");
        }else{
            useropenid = sharedPreferences.getString("openid","0001");
            nickname = sharedPreferences.getString("nickname","没登录");
        }
        context = this;
        toolbar = findViewById(R.id.tb_moneytrans);
        lv_money = findViewById(R.id.moneytrans_list);


        toolbar.setTitle("交易明细");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ChangeTabColor.setStatusBarColor(this,Color.parseColor("#758eeb"),true);

        initdb();
    }

    private void initdb(){
        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == 1){
                    MyListViewAdapterForMoney adapter = new MyListViewAdapterForMoney(context,list,useropenid);
                    lv_money.setAdapter(adapter);
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                ImplMoneyTransaction implMoneyTransaction = new ImplMoneyTransaction();
                list = implMoneyTransaction.getMoneyTrans(useropenid);
                for(MoneyTransaction moneyTransaction : list){
                    String name,time,touxiang,money;
                    String goodid = moneyTransaction.getGoodid();
                    String selleropenid = moneyTransaction.getSelleropenid();
                    String buyeropenid = moneyTransaction.getBuyeropenid();
                    time = moneyTransaction.getTime();
                    money = String.valueOf(moneyTransaction.getMoney());

                    ImplGoodList implGoodList = new ImplGoodList();
                    SingleGoods singlegood = implGoodList.getSingle(goodid);
                    String goodtype = singlegood.getGoodtype();
                    ImlUser imlUser = new ImlUser();
                    if(useropenid.equals(selleropenid)){ //是本人的订单
                        if(goodtype.contains("货")){ //本人卖二手货成功
                            money = "+"+money;
                        }else
                            money = "-"+money;
                        User user = imlUser.getSingle(buyeropenid);
                        touxiang = user.getFigureurl_qq_2();
                        name = user.getNickname();
                    }else{  //非本人订单
                        if(goodtype.contains("货")){ //买二手货成功
                            money = "-"+money;
                        }else
                            money = "+"+money;

                        User user = imlUser.getSingle(selleropenid);
                        touxiang = user.getFigureurl_qq_2();
                        name = user.getNickname();
                    }
                    moneyTransaction.setName(name);
                    moneyTransaction.setTouxiang(touxiang);
                    moneyTransaction.setGoodtype(goodtype);
                    moneyTransaction.setMoney(money);
                }

                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();
    }
}