package com.example.zbusst.Util;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.view.ViewCompat;

public class ChangeTabColor {

    public static void setStatusBarColor(Activity activity, int statusColor,boolean textcolor) {
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        int option = View.SYSTEM_UI_FLAG_VISIBLE;
        //状态栏字体颜色为黑色
        if(!textcolor){
            option = option | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }

        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        //设置系统状态栏处于可见状态
//        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE | option);
        decorView.setSystemUiVisibility(option);

        //设置状态栏颜色
        window.setStatusBarColor(statusColor);

        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }


}
