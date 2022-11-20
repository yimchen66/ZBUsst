package com.example.zbusst;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.zbusst.Bean.User;
import com.example.zbusst.Dao.Impl.ImlUser;
import com.example.zbusst.Util.ChangeTabColor;

public class Activity_Pay extends AppCompatActivity {
    public static final int ERROR_NOMONEY = 404;
    public static final int SUCCESS_PAYOK = 200;


    private TextView tv_money,tv_name,tv_mywalley;
    private Button btn_pay;
    private ImageView iv_touxiang;
    private boolean haveTarget = false;
    private String useropenid,nickname,money,mywallet,targetname,targettouxiang;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Intent intent = getIntent();
        haveTarget = intent.getIntExtra("havetarget", 106) == 105;
        money = intent.getStringExtra("money");
        ChangeTabColor.setStatusBarColor(this, Color.parseColor("#FFFFFFFF"),false);

        sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        if(sharedPreferences == null || sharedPreferences.getString("openid","0001").equals("0001")){
            showFailDialog("请先登录");
        }else{
            useropenid = sharedPreferences.getString("openid","0001");
            nickname = sharedPreferences.getString("nickname","没登录");
        }

        btn_pay = findViewById(R.id.pay_btn_submit);
        tv_money = findViewById(R.id.pay_money);
        tv_mywalley = findViewById(R.id.pay_mywallet);
        iv_touxiang = findViewById(R.id.pay_targettouxiang);
        tv_name = findViewById(R.id.pay_targetname);

        if(!haveTarget){  //没有支付目标
            iv_touxiang.setVisibility(View.GONE);
            tv_name.setText("请先支付佣金");
        }else{ //有支付对象
            targetname = intent.getStringExtra("name");
            targettouxiang = intent.getStringExtra("touxiang");


            Glide.with(this).load(targettouxiang).apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(iv_touxiang);
            tv_name.setText(targetname);
        }

        tv_money.setText(money);

        initdb();

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Float.parseFloat(money)> Float.parseFloat(mywallet)){ //余额不足
                    showFailDialog("余额不足");
                }else
                    actionPay();

            }
        });
    }


    private void initdb(){
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1)
                    tv_mywalley.setText(mywallet);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                ImlUser imlUser = new ImlUser();
                User user = imlUser.getSingle(useropenid);
                mywallet = user.getMoney();

                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();

    }

    private void showFailDialog(String err){
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        // 设置布局
        View view = View.inflate(this, R.layout.dailog_fail, null);
        dialog.setContentView(view);

        TextView text = view.findViewById(R.id.dialog_fail_text);
        text.setText(err);

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

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent intent = new Intent();
                setResult(ERROR_NOMONEY,intent);
                Activity_Pay.this.finish();
            }
        });

        dialog.show();

    }

    private void actionPay(){
        Handler handler2 = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == SUCCESS_PAYOK){ //支付成功
                    showSuccessDialog();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                ImlUser imlUser = new ImlUser();
                User user = imlUser.getSingle(useropenid);
                user.setMoney(String.valueOf(Float.parseFloat(mywallet) - Float.parseFloat(money)));
                imlUser.updateState(user);

                Message msg = new Message();
                msg.what = SUCCESS_PAYOK;
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
                Intent intent = new Intent();
                setResult(SUCCESS_PAYOK,intent);
                Activity_Pay.this.finish();
            }
        });

    }
}