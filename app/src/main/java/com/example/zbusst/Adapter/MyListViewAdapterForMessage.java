package com.example.zbusst.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.zbusst.Bean.MyLaunchOrder;
import com.example.zbusst.Bean.SingleMessage;
import com.example.zbusst.R;
import com.jauker.widget.BadgeView;

import java.util.ArrayList;
import java.util.List;

public class MyListViewAdapterForMessage extends BaseAdapter {

    private Context context;
    private List<SingleMessage> list = new ArrayList<>();
    private Fragment fragment;

    public MyListViewAdapterForMessage(List<SingleMessage> list, Context context,Fragment fragment) {
        this.context = context;
        this.list = list;
        this.fragment = fragment;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null ) {
            view = LayoutInflater.from(context).inflate(R.layout.messagelist_layout2, viewGroup, false);
        }
        SingleMessage message = list.get(i);

        ImageView iv_touxiang = view.findViewById(R.id.logo1);
        TextView tv_nickname = view.findViewById(R.id.message_tv_nickname);
        TextView tv_goodintro = view.findViewById(R.id.message_tv_goodintro);
        TextView tv_time = view.findViewById(R.id.message_tv_time);
        int state = message.getRead_state();

        Glide.with(fragment).load(message.getTargettouxiang())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                .into(iv_touxiang);
        tv_nickname.setText(message.getTargetnickname());
        tv_goodintro.setText(message.getMsg()+message.getGoodintro());
        String time = String.valueOf(message.getMessagetime().subSequence(10,19));
        tv_time.setText(time);

        if(state == 0){
            BadgeView badgeView = new BadgeView(context);
            badgeView.setBadgeCount(1);
            badgeView.setTargetView(iv_touxiang);
            badgeView.setBadgeMargin(0, 0, 0, 0);
        }


        return view;
    }
}
