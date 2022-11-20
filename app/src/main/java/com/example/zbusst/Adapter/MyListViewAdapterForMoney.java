package com.example.zbusst.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.zbusst.Bean.MoneyTransaction;
import com.example.zbusst.Bean.SingleGoods;
import com.example.zbusst.Bean.User;
import com.example.zbusst.Dao.Impl.ImlGoodTransaction;
import com.example.zbusst.Dao.Impl.ImlUser;
import com.example.zbusst.Dao.Impl.ImplGoodList;
import com.example.zbusst.R;

import java.util.List;

public class MyListViewAdapterForMoney extends BaseAdapter {
    private Context context;
    List<MoneyTransaction> list;
    private String useropenid;


    public MyListViewAdapterForMoney(Context context, List<MoneyTransaction> list,String useropenid) {
        this.context = context;
        this.list = list;
        this.useropenid = useropenid;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null ) {
            convertView = LayoutInflater.from(context).inflate(R.layout.moneytrans_layout, parent, false);
        }

        MoneyTransaction moneyTransaction = list.get(position);
        TextView tv_money = convertView.findViewById(R.id.money_money);
        TextView tv_time = convertView.findViewById(R.id.money_time);
        ImageView iv_touxiang = convertView.findViewById(R.id.money_touxiang);
        TextView tv_name = convertView.findViewById(R.id.money_tv_nickname);
        TextView tv_money_source = convertView.findViewById(R.id.money_source);

        Glide.with(context).load(moneyTransaction.getTouxiang())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(iv_touxiang);
        tv_money.setText(moneyTransaction.getMoney());
        tv_name.setText(moneyTransaction.getName());
        tv_time.setText(moneyTransaction.getTime());

        if(moneyTransaction.getMoney().contains("+")){
            tv_money.setTextColor(Color.parseColor("#FAC542"));
            tv_money_source.setText("来自");
        }

        else{
            tv_money.setTextColor(Color.parseColor("#000000"));
            tv_money_source.setText("转账给");
        }

        return convertView;
    }
}
