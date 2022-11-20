package com.example.zbusst;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

import com.example.zbusst.Bean.User;
import com.example.zbusst.Dao.Impl.ImlUser;
import com.example.zbusst.Util.ChangeTabColor;

public class Activity_Individual_MyWallet extends AppCompatActivity {

    private TextView tv_mywallet;
    private String useropenid,nickname,mywallet;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_my_wallet);

        ChangeTabColor.setStatusBarColor(this, Color.parseColor("#758eeb"),true);
        tv_mywallet = findViewById(R.id.mywallet);

        sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        if(sharedPreferences == null || sharedPreferences.getString("openid","0001").equals("0001")){
            showFailDialog("请先登录");
        }else{
            useropenid = sharedPreferences.getString("openid","0001");
            nickname = sharedPreferences.getString("nickname","没登录");
        }

        initdb();
    }

    private void initdb(){
        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == 1){
                    tv_mywallet.setText(mywallet);
                }
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

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Activity_Individual_MyWallet.this.finish();
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

    }
}