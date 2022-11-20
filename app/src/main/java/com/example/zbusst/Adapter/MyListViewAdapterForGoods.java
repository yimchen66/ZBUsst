package com.example.zbusst.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.zbusst.Bean.SingleGoods;
import com.example.zbusst.MainActivity;
import com.example.zbusst.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyListViewAdapterForGoods extends BaseAdapter {
    List<SingleGoods> list = new ArrayList<>();
    Context context;
    Fragment fragment;

    public MyListViewAdapterForGoods(List<SingleGoods> list, Context context , Fragment fragment) {
        this.list = list;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null ) {
            view = LayoutInflater.from(context).inflate(R.layout.goodslist_layout, viewGroup, false);
        }
        ImageView good_img = view.findViewById(R.id.good_img);
        TextView good_introduct = view.findViewById(R.id.good_introduct);
        TextView good_price = view.findViewById(R.id.good_price);
        TextView good_type = view.findViewById(R.id.good_type);
        TextView uploadtime = view.findViewById(R.id.uploadtime);

        Glide.with(fragment).load(list.get(i).getGoodpicture()).into(good_img);
//        good_introduct.setText(list.get(i).getGoodintroduct());
        good_price.setText("¥ "+String.valueOf(list.get(i).getGoodprice()));
        good_type.setText(list.get(i).getGoodtype());
        uploadtime.setText(list.get(i).getUploadtime());

        String introduce = "啊啊啊啊啊"+ list.get(i).getGoodintroduct();
        SpannableStringBuilder span = new SpannableStringBuilder(introduce);

        span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 5,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        good_introduct.setText(span);


        return view;


    }


}
