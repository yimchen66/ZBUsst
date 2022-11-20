package com.example.zbusst.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zbusst.Activity_DetailGood_InTransaction;
import com.example.zbusst.Adapter.MyListViewAdapterForGoods;
import com.example.zbusst.Adapter.MyListViewAdapterForMessage;
import com.example.zbusst.Bean.GoodInTransaction;
import com.example.zbusst.Bean.MyLaunchOrder;
import com.example.zbusst.Bean.SingleGoods;
import com.example.zbusst.Bean.SingleMessage;
import com.example.zbusst.Bean.User;
import com.example.zbusst.Dao.Impl.ImlGoodTransaction;
import com.example.zbusst.Dao.Impl.ImlUser;
import com.example.zbusst.Dao.Impl.ImplGoodList;
import com.example.zbusst.Dao.Impl.ImplMessageList;
import com.example.zbusst.MainActivity;
import com.example.zbusst.MyViews.MyListViewForScrollView;
import com.example.zbusst.R;
import com.example.zbusst.Util.ChangeTabColor;

import java.util.List;

public class BlankFragment_SecPage extends Fragment {
    public static final int DB_COMPLATE = 1;
    public String TAG = "msg";

    private MyListViewForScrollView lv_message;
    private Fragment fragment = this;

    View root;
    Context context;
    private SharedPreferences sharedPreferences;
    private String useropenid,nickname;
    private List<SingleMessage> messageList;
    private boolean isGetData = false;


    public BlankFragment_SecPage() {
    }

    public BlankFragment_SecPage(Context context) {
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "调用pause   ");
    }

    @Override
    public void onResume() {
        Log.e(TAG, "调用resume: ");
        initdb();
        super.onResume();
    }

    @Override
    public void onStart() {
        Log.e(TAG, "调用start: ");
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(null == root){
            root = inflater.inflate(R.layout.fragment_blank__sec_page, container, false);
        }
        sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        if(sharedPreferences == null || sharedPreferences.getString("openid","0001").equals("0001"))
            Toast.makeText(context, "未登录", Toast.LENGTH_SHORT).show();
        else{
            useropenid = sharedPreferences.getString("openid","0001");
            nickname = sharedPreferences.getString("nickname","没登录");
        }

        intitab();
        initdb();
        return root;
    }

    @SuppressLint("ResourceAsColor")
    private void intitab() {
        TextView tabtitle = root.findViewById(R.id.tab_title);
        tabtitle.setText(R.string.tabtitle_message);
        tabtitle.setBackgroundResource(R.color.messagecolor);
        tabtitle.setTextColor(Color.BLACK);

        lv_message = root.findViewById(R.id.message_list);

        lv_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SingleMessage currentgood =(SingleMessage) lv_message.getItemAtPosition(position);
                msgRead(currentgood.getGoodid(),currentgood.getMessagetime());

                Intent intent = new Intent(context, Activity_DetailGood_InTransaction.class);
                intent.putExtra("goodid",currentgood.getGoodid());
                startActivityForResult(intent,111);
            }
        });
    }

    private void initdb(){
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case DB_COMPLATE:
                        Log.e(TAG, "渲染:" );
                        lv_message.setAdapter(new MyListViewAdapterForMessage(messageList,context,fragment));
                        break;
                    default:
                        break;
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
                if(sharedPreferences == null || sharedPreferences.getString("openid","0001").equals("0001")){
//                    Toast.makeText(context, "未登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    useropenid = sharedPreferences.getString("openid","0001");
                    nickname = sharedPreferences.getString("nickname","没登录");
                }
                ImplMessageList implMessageList = new ImplMessageList();
                messageList = implMessageList.getMessageList(useropenid);
                Log.e(TAG, "消息个数: "+messageList.size() );
                Log.e(TAG, "useropenid: "+useropenid);
                for(SingleMessage message : messageList){
                    //查找头像、昵称
                    String sendopenid = message.getSendopenid();
                    ImlUser imlUser = new ImlUser();
                    User senduser = imlUser.getSingle(sendopenid);
                    message.setTargetnickname(senduser.getNickname());
                    message.setTargettouxiang(senduser.getFigureurl_qq_2());

                    //查找订单信息
                    ImplGoodList implGoodList = new ImplGoodList();
                    SingleGoods single = implGoodList.getSingle(message.getGoodid());
                    message.setGoodintro(single.getGoodintroduct());
                }
                Message msg = new Message();
                msg.what = DB_COMPLATE;
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 111 && resultCode == 0)
            initdb();
    }

    private void msgRead(String goodid,String time){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ImplMessageList implMessageList = new ImplMessageList();
                implMessageList.updateRead(goodid,time);
            }
        }).start();
    }

}