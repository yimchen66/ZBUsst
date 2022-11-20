package com.example.zbusst;

import androidx.annotation.Nullable;
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
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zbusst.Adapter.MyListViewAdapterForGoods;
import com.example.zbusst.Bean.GoodInTransaction;
import com.example.zbusst.Bean.SingleGoods;
import com.example.zbusst.Bean.SingleMessage;
import com.example.zbusst.Bean.User;
import com.example.zbusst.Dao.Impl.ImlGoodTransaction;
import com.example.zbusst.Dao.Impl.ImlUser;
import com.example.zbusst.Dao.Impl.ImplGoodList;
import com.example.zbusst.Dao.Impl.ImplMessageList;
import com.example.zbusst.Util.ChangeTabColor;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Activity_DetailGood extends AppCompatActivity implements View.OnClickListener{
    public static final int STATE_NORECEIVE = 0;
    public static final int STATE_RECEIVED = 1;
    public static final int SHOWLOADING = 2;
    public static final int HIDELOADING = 3;
    public static final int SHOWSUCCESS = 4;
    public static final int SHOWFAIL = 5;
    public static final int PAY = 100;
    public static final int PAY_HAVETARGET = 105;
    public static final int SUCCESS_PAYOK = 200;
    public static final int ERROR_NOMONEY = 404;

    private TextView tv_goodprice,tv_goodintro,tv_goodbeizhu,tv_hostname,tv_uploadtime,tv_goodtype;
    private ImageView iv_goodpic,detail_loading;
    private Button btn_receive;
    private SingleGoods singlegood;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private Context context;
    private String targettouxiang,targetname,targetmoney;

    private String useropenid,nickname;
    private boolean isBenren;
    private boolean isAccepted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_good);
        sharedPreferences = getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        if(sharedPreferences == null || sharedPreferences.getString("openid","0001").equals("0001")){
            showFailDialog("请先登录");
        }else{
            useropenid = sharedPreferences.getString("openid","0001");
            nickname = sharedPreferences.getString("nickname","没登录");
        }

        Intent intent = getIntent();
        singlegood =(SingleGoods) intent.getSerializableExtra("currentgood");
        if(singlegood.getHostopenid().equals(useropenid))
            isBenren = true;
        else isBenren = false;

        context = this;
        toolbar = findViewById(R.id.tb_return);
        iv_goodpic = findViewById(R.id.detail_good_pic);
        tv_goodprice = findViewById(R.id.detail_good_price);
        tv_goodintro = findViewById(R.id.detail_good_intro);
        tv_goodbeizhu = findViewById(R.id.detail_good_beizhu);
        tv_hostname = findViewById(R.id.detail_good_hostname);
        tv_uploadtime = findViewById(R.id.detail_good_uploadtime);
        tv_goodtype = findViewById(R.id.detail_good_type);
        btn_receive = findViewById(R.id.detail_receive);
        detail_loading = findViewById(R.id.detail_loading);
        detail_loading.setVisibility(View.GONE);

        showDetail();
    }

    public void showDetail(){
        String introduce = "啊啊啊啊啊"+ singlegood.getGoodintroduct();
        SpannableStringBuilder span = new SpannableStringBuilder(introduce);
        span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 5,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        Glide.with(this).load(singlegood.getGoodpicture()).into(iv_goodpic);
        tv_goodprice.setText("¥ "+String.valueOf(singlegood.getGoodprice()));
        tv_goodbeizhu.setText(singlegood.getGoodbeizhu()==""?"该订单没有备注":singlegood.getGoodbeizhu());
        tv_hostname.setText(singlegood.getHostname());
        tv_uploadtime.setText(singlegood.getUploadtime());
        tv_goodintro.setText(span);
        tv_goodtype.setText(singlegood.getGoodtype());

        toolbar.setTitle("详细情况");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitActivity();
            }
        });
        ChangeTabColor.setStatusBarColor(this,Color.parseColor("#32CD32"),true);
        btn_receive.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detail_receive:
                if(isBenren){
                    showFailDialog("这是您自己的订单");
                    return;
                }
                showDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        detail_loading.clearAnimation();
        detail_loading.setVisibility(View.GONE);
        switch (requestCode){
            case PAY:
                if(resultCode == SUCCESS_PAYOK){ //支付成功，返回吗200
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Date date = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                            String currenttime = sdf.format(date);
                            ImlGoodTransaction imlGoodTransaction = new ImlGoodTransaction();
                            GoodInTransaction goodInTransaction = new GoodInTransaction();
                            goodInTransaction.setGoodid(singlegood.getGoodid());
                            goodInTransaction.setSelleropenid(singlegood.getOpenid());
                            goodInTransaction.setBuyeropenid(useropenid);
                            goodInTransaction.setAccepttime(currenttime);
                            goodInTransaction.setState_seller(0);
                            goodInTransaction.setState_buyer(0);
                            //写入订单交易表
                            imlGoodTransaction.add(goodInTransaction);
                            //写入发送信息表
                            ImplMessageList implMessageList = new ImplMessageList();
                            SingleMessage message = new SingleMessage();
                            message.setGoodid(singlegood.getGoodid());
                            message.setTargetopenid(singlegood.getOpenid());
                            message.setSendopenid(useropenid);
                            message.setMsg("你好，我接了你一个订单。");
                            message.setMessagetime(currenttime);
                            implMessageList.add(message);
                        }
                    }).start();
                    showSuccessDialog();
                }else{  //返回码404，没钱
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ImplGoodList implGoodList = new ImplGoodList();
                            SingleGoods currentgood = implGoodList.getSingle(singlegood.getGoodid());
                            currentgood.setState(0);
                            implGoodList.updateState(currentgood);
                            isAccepted = false;
                        }
                    }).start();
                }
                break;
        }
    }

    private void checkAndReceive(){
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case SHOWLOADING:
                        showLoading();
                        break;
                    case HIDELOADING:
                        break;
                    case SHOWSUCCESS: //确认接单，判断是否是卖货
                        if(singlegood.getGoodtype().contains("货")){ //卖货订单，接单需要先付钱
                            detail_loading.setVisibility(View.GONE);
                            Intent intent = new Intent(context,Activity_Pay.class);
                            intent.putExtra("havetarget",PAY_HAVETARGET);
                            intent.putExtra("money",String.valueOf(singlegood.getGoodprice()));
                            intent.putExtra("touxiang",targettouxiang);
                            intent.putExtra("name",targetname);

                            //支付
                            startActivityForResult(intent,PAY);
                        }else{ //非二手货直接接单
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Date date = new Date();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                                    String currenttime = sdf.format(date);
                                    ImlGoodTransaction imlGoodTransaction = new ImlGoodTransaction();
                                    GoodInTransaction goodInTransaction = new GoodInTransaction();
                                    goodInTransaction.setGoodid(singlegood.getGoodid());
                                    goodInTransaction.setSelleropenid(singlegood.getOpenid());
                                    goodInTransaction.setBuyeropenid(useropenid);
                                    goodInTransaction.setAccepttime(currenttime);
                                    goodInTransaction.setState_seller(0);
                                    goodInTransaction.setState_buyer(0);
                                    //写入订单交易表
                                    imlGoodTransaction.add(goodInTransaction);

                                    //写入发送信息表
                                    ImplMessageList implMessageList = new ImplMessageList();
                                    SingleMessage message = new SingleMessage();
                                    message.setGoodid(singlegood.getGoodid());
                                    message.setTargetopenid(singlegood.getOpenid());
                                    message.setSendopenid(useropenid);
                                    message.setMsg("你好，我接了你一个订单。");
                                    message.setMessagetime(currenttime);
                                    implMessageList.add(message);
                                }
                            }).start();
                            showSuccessDialog();
                        }
                        break;
                    case SHOWFAIL:
                        detail_loading.clearAnimation();
                        detail_loading.setVisibility(View.GONE);
                        showFailDialog("手慢啦，已被别人接单");
                        break;
                    default:
                        break;
                }
            }
        };

        Thread thread_check = new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = SHOWLOADING;
                handler.sendMessage(msg);

                ImplGoodList implGoodList = new ImplGoodList();
                SingleGoods currentgood = implGoodList.getSingle(singlegood.getGoodid());
                if(currentgood.getState() == STATE_RECEIVED){ //为一表示已经接单
                    Message msg2 = new Message();
                    msg2.what = SHOWFAIL;
                    handler.sendMessage(msg2);
                    isAccepted = true;

                    return;
                }else{ //未被接单，可以接单,将state改为被接单状态
                    currentgood.setState(1);
                    implGoodList.updateState(currentgood);
                    isAccepted = false;
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                thread_check.start();
                try {
                    thread_check.join();
                    if(isAccepted)
                        return;

                    //查找头像
                    ImlUser imlUser = new ImlUser();
                    User user = imlUser.getSingle(singlegood.getOpenid());
                    targettouxiang = user.getFigureurl_qq_2();
                    targetname = user.getNickname();
                    targetmoney = String.valueOf(singlegood.getGoodprice());

                    Message msg = new Message();
                    msg.what = SHOWSUCCESS;
                    handler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void exitActivity(){
        Intent intent=new Intent();
        setResult(0,intent);
        Activity_DetailGood.this.finish();
    }

    private void showLoading(){
        detail_loading.setVisibility(View.VISIBLE);
        Animation anim =new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setRepeatCount(Animation.INFINITE);//设定无限循环
        anim.setDuration(1000); // 设置动画时间
        anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        detail_loading.startAnimation(anim);
    }

    public void showDialog(){

        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        // 设置布局
        View view = View.inflate(this, R.layout.dialog_tips_layout, null);
        dialog.setContentView(view);

        TextView titletext = view.findViewById(R.id.dialog_tips_text);
        Button canceltext = view.findViewById(R.id.dailog_tips_cancel);
        Button confirmtext = view.findViewById(R.id.dailog_tips_confirm);
        titletext.setText("确定接单吗");
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
                checkAndReceive();
                dialog.cancel();
            }
        });

    }

    private void showFailDialog(String err){
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        // 设置布局
        View view = View.inflate(this, R.layout.dailog_fail, null);
        dialog.setContentView(view);

        TextView text = view.findViewById(R.id.dialog_fail_text);
        text.setText(err);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                exitActivity();
            }
        });

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
        dialog.show();

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
                exitActivity();
            }
        });
    }

}