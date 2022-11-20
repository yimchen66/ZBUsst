package com.example.zbusst;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zbusst.Bean.GoodInTransaction;
import com.example.zbusst.Bean.SingleGoods;
import com.example.zbusst.Bean.SingleMessage;
import com.example.zbusst.Bean.User;
import com.example.zbusst.Dao.Impl.ImlGoodTransaction;
import com.example.zbusst.Dao.Impl.ImlUser;
import com.example.zbusst.Dao.Impl.ImplGoodList;
import com.example.zbusst.Dao.Impl.ImplMessageList;
import com.example.zbusst.Util.ChangeTabColor;
import com.example.zbusst.Util.PayUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Activity_DetailGood_InTransaction extends AppCompatActivity implements View.OnClickListener{
    public static final int CONFIRM_COMPLETE = 101;
    public static final int CONFIRM_URGE= 102;
    public static final int CONFIRM_DELETE= 100;

    private TextView tv_goodprice,tv_goodintro,tv_goodbeizhu,tv_hostname,tv_uploadtime,tv_goodtype,tv_goodstate,tv_goodbuyer,tv_goodaccepttime;
    private ImageView iv_goodpic,detail_loading;
    private Button btn_delete,btn_compelete,btn_urge;
    private String goodid,goodintro,goodprice,goodpic,goodsellername,goodbuyername,goodtime,goodaccepttime,goodtype,goodbeizhu;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private Context context;
    private String useropenid,nickname;
    private boolean isBenren;
    private int state_buyer,state_seller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_good_intransaction);
        sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        if(sharedPreferences == null || sharedPreferences.getString("openid","0001").equals("0001")){
//            showFailDialog("请先登录");
        }else{
            useropenid = sharedPreferences.getString("openid","0001");
            nickname = sharedPreferences.getString("nickname","没登录");
        }

        Intent intent = getIntent();
        goodid = intent.getStringExtra("goodid");
        context = this;

        toolbar = findViewById(R.id.tb_return_intra);
        iv_goodpic = findViewById(R.id.detail_good_pic_intra);
        tv_goodprice = findViewById(R.id.detail_good_price_intra);
        tv_goodintro = findViewById(R.id.detail_good_intro_intra);
        tv_goodbeizhu = findViewById(R.id.detail_good_beizhu_intra);
        tv_hostname = findViewById(R.id.detail_good_hostname_intra);
        tv_uploadtime = findViewById(R.id.detail_good_uploadtime_intra);
        tv_goodtype = findViewById(R.id.detail_good_type_intra);

        tv_goodbuyer = findViewById(R.id.detail_good_buyername);
        tv_goodaccepttime = findViewById(R.id.detail_good_buyertime);
        tv_goodstate = findViewById(R.id.detail_good_state);

        btn_delete = findViewById(R.id.detail_btn_delete);
        btn_compelete = findViewById(R.id.detail_btn_compelte);
        btn_urge = findViewById(R.id.detail_btn_urge);

        detail_loading = findViewById(R.id.detail_loading_intra);
        detail_loading.setVisibility(View.GONE);

        toolbar.setTitle("详细情况");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ChangeTabColor.setStatusBarColor(this,Color.parseColor("#758eeb"),true);

        initdb();

        btn_delete.setOnClickListener(this);
        btn_urge.setOnClickListener(this);
        btn_compelete.setOnClickListener(this);
    }

    private void initdb(){
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){
                    Glide.with(context).load(goodpic).into(iv_goodpic);
                    tv_goodprice.setText(goodprice);

                    tv_goodtype.setText(goodtype);
                    tv_hostname.setText(goodsellername);
                    tv_goodbeizhu.setText(goodbeizhu);
                    tv_uploadtime.setText(goodtime);

                    tv_goodbuyer.setText(goodbuyername);
                    tv_goodaccepttime.setText(goodaccepttime);

                    String introduce = "啊啊啊啊啊"+ goodintro;
                    SpannableStringBuilder span = new SpannableStringBuilder(introduce);
                    span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 5,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                    tv_goodintro.setText(span);

                    if(state_buyer == 1 && state_seller == 1){ //订单已经完成
                        btnDeleteState(false);
                        btnCompelteState(false);
                        btnUrgeState(false);
                        tv_goodstate.setText("订单已经完成");
                    }else if(state_buyer == 0 && state_seller == 0){ //订单均未完成
                        btnDeleteState(false);
                        btnCompelteState(true);
                        btnUrgeState(true);
                        tv_goodstate.setText("订单未完成");
                    }else if(state_buyer == 1 && state_seller == 0){//买家完成，卖家未
                        btnDeleteState(false);
                        if(isBenren){ //卖家是本人
                            btnCompelteState(true);
                            btnUrgeState(false);
                            tv_goodstate.setText("己方未确认完成");
                        }else{ //买家是本人
                            btnCompelteState(false);
                            btnUrgeState(true);
                            tv_goodstate.setText("对方未确认完成");
                        }
                    }else{
                        btnDeleteState(false);
                        if(isBenren){ //卖家是本人
                            btnCompelteState(false);
                            btnUrgeState(true);
                            tv_goodstate.setText("对方未确认完成");
                        }else{ //买家是本人
                            btnCompelteState(true);
                            btnUrgeState(false);
                            tv_goodstate.setText("己方未确认完成");
                        }
                    }
                }

            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                ImlGoodTransaction imlGoodTransaction = new ImlGoodTransaction();
                GoodInTransaction goodintransaction = imlGoodTransaction.getSingle(goodid);
                goodaccepttime = goodintransaction.getAccepttime(); //接单时间
                state_buyer = goodintransaction.getState_buyer();
                state_seller = goodintransaction.getState_seller();

                String selletopenid = goodintransaction.getSelleropenid();
                String buyeropenid = goodintransaction.getBuyeropenid();

                //是否是本人发布的订单子
                isBenren = selletopenid.equals(useropenid);
                //查抄买家、卖家名字
                ImlUser imlUser = new ImlUser();
                User selleruser = imlUser.getSingle(selletopenid);
                User buyeruser = imlUser.getSingle(buyeropenid);
                goodsellername = selleruser.getNickname();
                goodbuyername = buyeruser.getNickname();

                ImplGoodList implGoodList = new ImplGoodList();
                SingleGoods origood = implGoodList.getSingle(goodid);
                goodpic = origood.getGoodpicture();
                goodtype = origood.getGoodtype();
                goodprice = String.valueOf(origood.getGoodprice());
                goodintro = origood.getGoodintroduct();
                goodbeizhu = origood.getGoodbeizhu();
                goodtime = origood.getUploadtime();

                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detail_btn_delete:
                showDialog("删除后不可恢复，确认删除吗");
                break;
            case R.id.detail_btn_urge:
                showDialog("催促对方？");
                break;
            case R.id.detail_btn_compelte:
                showDialog("确认完成订单？双方均确认后将完成交易。");
                break;
        }
    }

    private void btnDeleteState(boolean isVisible){
        if(isVisible){ //设置可以点击
            btn_delete.setTextColor(Color.parseColor("#758eeb"));
            btn_delete.setBackgroundResource(R.drawable.shape_mylaunch_btn_click);
            btn_delete.setEnabled(true);
        }else{
            btn_delete.setTextColor(Color.parseColor("#E1E1E1"));
            btn_delete.setBackgroundResource(R.drawable.shape_mylaunch_btn_unclick);
            btn_delete.setEnabled(false);
        }
    }
    private void btnUrgeState(boolean isVisible){
        if(isVisible){ //设置可以点击
            btn_urge.setTextColor(Color.parseColor("#758eeb"));
            btn_urge.setBackgroundResource(R.drawable.shape_mylaunch_btn_click);
            btn_urge.setEnabled(true);
        }else{
            btn_urge.setTextColor(Color.parseColor("#E1E1E1"));
            btn_urge.setBackgroundResource(R.drawable.shape_mylaunch_btn_unclick);
            btn_urge.setEnabled(false);
        }
    }
    private void btnCompelteState(boolean isVisible){
        if(isVisible){ //设置可以点击
            btn_compelete.setTextColor(Color.parseColor("#758eeb"));
            btn_compelete.setBackgroundResource(R.drawable.shape_mylaunch_btn_click);
            btn_compelete.setEnabled(true);
        }else{
            btn_compelete.setTextColor(Color.parseColor("#E1E1E1"));
            btn_compelete.setBackgroundResource(R.drawable.shape_mylaunch_btn_unclick);
            btn_compelete.setEnabled(false);
        }
    }

    public void showDialog(String msgs){
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        // 设置布局
        View view = View.inflate(this, R.layout.dialog_tips_layout, null);
        dialog.setContentView(view);
        TextView titletext = view.findViewById(R.id.dialog_tips_text);
        Button canceltext = view.findViewById(R.id.dailog_tips_cancel);
        Button confirmtext = view.findViewById(R.id.dailog_tips_confirm);
        titletext.setText(msgs);
        canceltext.setText("取消");
        confirmtext.setText("确定");
        //设置大小
        Window window = dialog.getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = 50;
        params.width = (int) ( display.getWidth() * (0.9));
        window.setAttributes(params);
        // 设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        // 设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        dialog.show();
        canceltext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        confirmtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                if(msgs.contains("完成"))
                    msg.what = CONFIRM_COMPLETE;
                else if(msgs.contains("催"))
                    msg.what = CONFIRM_URGE;
                else
                    msg.what = CONFIRM_DELETE;
                handler.sendMessage(msg);
                dialog.cancel();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
    }
    private Handler handler =  new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CONFIRM_DELETE:
                    actionDelete();
                    break;
                case CONFIRM_COMPLETE:
                    actionCompelte();
                    break;
                case CONFIRM_URGE:
                    actionUrge();
                    break;
            }
        }
    };

    private void actionDelete(){
        Handler handler2 = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 4){
                    showSuccessDialog();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                ImlGoodTransaction imlGoodTransaction = new ImlGoodTransaction();
                imlGoodTransaction.del(goodid);
                ImplGoodList implGoodList = new ImplGoodList();
                implGoodList.del(goodid);
                ImplMessageList implMessageList = new ImplMessageList();
                implMessageList.deleteMessage(goodid);

                Message msg = new Message();
                msg.what = 4;
                handler2.sendMessage(msg);

            }
        }).start();
    }
    private void actionCompelte(){
        Handler handler2 = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 4){
                    showSuccessDialog();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                //更新订单数据库和消息数据库
                ImlGoodTransaction imlGoodTransaction = new ImlGoodTransaction();
                GoodInTransaction inTransaction = imlGoodTransaction.getSingle(goodid);
                SingleMessage singleMessage = new SingleMessage();
                singleMessage.setMsg("我已经确认完成。");
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String currenttime = sdf.format(date);
                singleMessage.setMessagetime(currenttime);
                singleMessage.setGoodid(goodid);

                if(state_seller == 0 && state_buyer == 0){
                    if(isBenren){
                        inTransaction.setState_seller(1);
                        imlGoodTransaction.updateStateSeller(inTransaction);
                        singleMessage.setSendopenid(inTransaction.getSelleropenid());
                        singleMessage.setTargetopenid(inTransaction.getBuyeropenid());
                    }else{
                        inTransaction.setState_buyer(1);
                        imlGoodTransaction.updateStateBuyer(inTransaction);
                        singleMessage.setSendopenid(inTransaction.getBuyeropenid());
                        singleMessage.setTargetopenid(inTransaction.getSelleropenid());
                    }
                }else if(state_seller == 0 && state_buyer == 1){
                    inTransaction.setState_seller(1);
                    imlGoodTransaction.updateStateSeller(inTransaction);
                    singleMessage.setSendopenid(inTransaction.getSelleropenid());
                    singleMessage.setTargetopenid(inTransaction.getBuyeropenid());
                }
                else{
                    inTransaction.setState_buyer(1);
                    imlGoodTransaction.updateStateBuyer(inTransaction);
                    singleMessage.setSendopenid(inTransaction.getBuyeropenid());
                    singleMessage.setTargetopenid(inTransaction.getSelleropenid());
                }

                //更新消息数据库
                ImplMessageList implMessageList = new ImplMessageList();
                implMessageList.add(singleMessage);

                //检测订单是否完成
                PayUtil.payCompelte(goodid);

                Message msg = new Message();
                msg.what = 4;
                handler2.sendMessage(msg);
            }
        }).start();
    }

    private void actionUrge(){
        Handler handler2 = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 4){
                    showSuccessDialog();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                ImlGoodTransaction imlGoodTransaction = new ImlGoodTransaction();
                GoodInTransaction inTransaction = imlGoodTransaction.getSingle(goodid);
                SingleMessage singleMessage = new SingleMessage();
                singleMessage.setMsg("催你一下，尽快完成。");
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String currenttime = sdf.format(date);
                singleMessage.setMessagetime(currenttime);
                singleMessage.setGoodid(goodid);

                if(isBenren){
                    singleMessage.setSendopenid(inTransaction.getSelleropenid());
                    singleMessage.setTargetopenid(inTransaction.getBuyeropenid());
                }else{
                    singleMessage.setSendopenid(inTransaction.getBuyeropenid());
                    singleMessage.setTargetopenid(inTransaction.getSelleropenid());
                }

                ImplMessageList implMessageList = new ImplMessageList();
                implMessageList.add(singleMessage);

                Message msg = new Message();
                msg.what = 4;
                handler2.sendMessage(msg);
            }
        }).start();


    }

    private void showSuccessDialog(){
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        // 设置布局
        View view = View.inflate(this, R.layout.dialog_success, null);
        dialog.setContentView(view);

        //设置大小
        Window window = dialog.getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) ( display.getWidth() * (0.6));
        window.setAttributes(params);

        // 设置弹出位置
        window.setGravity(Gravity.CENTER);
        // 设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);

        dialog.show();

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent intent=new Intent();
                setResult(0,intent);
                Activity_DetailGood_InTransaction.this.finish();
            }
        });

    }
}