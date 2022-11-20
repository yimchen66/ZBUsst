package com.example.zbusst.Util;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.zbusst.R;

public class SetDialogUtil {
    private static final String TAG = "chen";
    public static String result = "error";

    public static String getResult(){
        return result;
    }


    public static void setTypeDialog(Context context, TextView textView)  {
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        // 设置布局
        View view = View.inflate(context, R.layout.dialog_choosetype_layout, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        // 设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        // 设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        // 设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        dialog.findViewById(R.id.tv_kuaidi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("代拿快递");
                result = "代拿快递";
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_xueye).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("学业帮助");
                result = "学业帮助";
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_ershou).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("出二手货");
                result = "出二手货";
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_qita).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("其他");
                result = "其他";
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("全部类型");
                result = "全部类型";
                dialog.dismiss();
            }
        });

    }

    public static void setPicDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        // 设置布局
        View view = View.inflate(context, R.layout.dialog_choosepic_layout, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        // 设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        // 设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        // 设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
