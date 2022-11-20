package com.example.zbusst.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.zbusst.Activity_Individual_MyWallet;
import com.example.zbusst.Activity_MoneyTransaction;
import com.example.zbusst.Activity_MyAccept;
import com.example.zbusst.Activity_MyLaunch;
import com.example.zbusst.Activity_NewOrder;
import com.example.zbusst.BaiduGPS;
import com.example.zbusst.Bean.User;
import com.example.zbusst.Dao.Impl.ImlUser;
import com.example.zbusst.R;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class BlankFragment_ThiPage extends Fragment implements View.OnClickListener{
    public static final String APPID = "102029896";
    public static final String AUTHORITIES = "com.tencent.login.fileprovider_zbusst";
    public static final int INDEX_ACTIVITY_NEWORDER = 2;
    public static final int INDEX_ACTIVITY_MYLAUNCH = 3;
    public static final int INDEX_ACTIVITY_MYWALLET = 4;
    public static final int INDEX_ACTIVITY_MYACCEPT = 5;
    public static final int INDEX_ACTIVITY_MONEYTRANS = 6;
    public static final int INDEX_ACTIVITY_LOCATION = 7;
    public static final int GETLOCATION = 300;
    public static final int LOCATIONSUCCESS = 302;
    public static final int LOCATIONFAIL = 304;
    private static final String TAG = "qq";


    private Context context;
    View root;
    private ImageButton iv_login_pic;
    private TextView tv_nickname,tv_getlocation;
    private Tencent mTencent;
    private LinearLayout ll_neworder,ll_mylaunch,ll_mywallet,ll_myaccept,ll_moneytrans;
    private BaseUiListener mloginlistener;
    private SharedPreferences sharedPreferences;


    public BlankFragment_ThiPage(Context context) {
        this.context = context;
        // Required empty public constructor
    }

    public BlankFragment_ThiPage(){
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTencent = Tencent.createInstance(APPID,context, AUTHORITIES);
        Tencent.setIsPermissionGranted(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(null == root){
            root = inflater.inflate(R.layout.fragment_blank__thi_page, container, false);
        }
        intitab();

        return root;
    }

    @SuppressLint("ResourceAsColor")
    private void intitab() {
        TextView tabtitle = root.findViewById(R.id.tab_title);
        tabtitle.setText(R.string.tabtitle_personal);
        tabtitle.setBackgroundResource(R.color.personalcolor);
        tabtitle.setTextColor(Color.WHITE);
        ll_neworder = root.findViewById(R.id.individual_neworder);
        ll_mylaunch = root.findViewById(R.id.individual_mylaunch);
        ll_myaccept = root.findViewById(R.id.individual_myaccept);
        ll_mywallet = root.findViewById(R.id.individual_mywallet);
        ll_moneytrans = root.findViewById(R.id.individual_moneytransaction);


        tv_getlocation = root.findViewById(R.id.tv_getlocation);
        tv_nickname = root.findViewById(R.id.tv_nickname);
        iv_login_pic = root.findViewById(R.id.iv_login);

        iv_login_pic.setOnClickListener(this);

        ll_neworder.setOnClickListener(this);
        ll_mylaunch.setOnClickListener(this);
        ll_mywallet.setOnClickListener(this);
        ll_myaccept.setOnClickListener(this);
        ll_moneytrans.setOnClickListener(this);

        tv_getlocation.setOnClickListener(this);

        sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        if(sharedPreferences != null){
            String touxiang = sharedPreferences.getString("figureurl_qq_2","");
            if(touxiang.equals(""))
                iv_login_pic.setImageResource(R.drawable.qqlogo);
            else
                Glide.with(context).load(touxiang)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(iv_login_pic);
            tv_nickname.setText(sharedPreferences.getString("nickname","qq登录"));
        }
        else {
            tv_nickname.setText("点击登录");
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_login:
                login();
                break;
            case R.id.individual_neworder:
                jumpToActivity(INDEX_ACTIVITY_NEWORDER);
                break;
            case R.id.individual_mylaunch:
                jumpToActivity(INDEX_ACTIVITY_MYLAUNCH);
                break;
            case R.id.individual_mywallet:
                jumpToActivity(INDEX_ACTIVITY_MYWALLET);
                break;
            case R.id.individual_myaccept:
                jumpToActivity(INDEX_ACTIVITY_MYACCEPT);
                break;
            case R.id.individual_moneytransaction:
                jumpToActivity(INDEX_ACTIVITY_MONEYTRANS);
                break;
            case R.id.tv_getlocation:
                jumpToActivity(INDEX_ACTIVITY_LOCATION);
                break;

        }
    }


    private void login(){
        mTencent = Tencent.createInstance(APPID,context, AUTHORITIES);
        mloginlistener = new BaseUiListener();
        mTencent.login(this.getActivity(), "all", mloginlistener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTencent.onActivityResultData(requestCode,resultCode,data,mloginlistener);
        if(Constants.REQUEST_API == requestCode){
            if(Constants.REQUEST_LOGIN ==  requestCode){
                Tencent.handleResultData(data,mloginlistener);
            }
        }
        if(requestCode == GETLOCATION){
            if(resultCode == LOCATIONSUCCESS){ //地址返回成功
                String location = data.getStringExtra("location");
                tv_getlocation.setText(location);
            }else{
                tv_getlocation.setText("请检测是否开通权限");
            }
        }
    }

    private void jumpToActivity(int index){
        switch (index){
            case INDEX_ACTIVITY_NEWORDER:
                Intent intent = new Intent(context, Activity_NewOrder.class);
                context.startActivity(intent);
                break;
            case INDEX_ACTIVITY_MYLAUNCH:
                Intent intent2 = new Intent(context, Activity_MyLaunch.class);
                context.startActivity(intent2);
                break;
            case INDEX_ACTIVITY_MYWALLET:
                Intent intent3 = new Intent(context, Activity_Individual_MyWallet.class);
                context.startActivity(intent3);
                break;
            case INDEX_ACTIVITY_MYACCEPT:
                Intent intent4 = new Intent(context, Activity_MyAccept.class);
                context.startActivity(intent4);
                break;
            case INDEX_ACTIVITY_MONEYTRANS:
                Intent intent5 = new Intent(context, Activity_MoneyTransaction.class);
                context.startActivity(intent5);
                break;
            case INDEX_ACTIVITY_LOCATION:
                Intent intent6 = new Intent(context, BaiduGPS.class);
                startActivityForResult(intent6,GETLOCATION);
//                context.startActivity(intent6);
                break;
        }

    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            JSONObject jsonObject = (JSONObject) o;
            try {
                String openid = jsonObject.getString("openid");
                Log.e(TAG, "onComplete:  openid  "+openid);
                String access_token = jsonObject.getString("access_token");
                String expires_in = jsonObject.getString("expires_in");

                mTencent.setOpenId(openid);
                mTencent.setAccessToken(access_token,expires_in);

                QQToken qqToken = mTencent.getQQToken();
                UserInfo userInfo = new UserInfo(context,qqToken);
                userInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject2 = (JSONObject) o;
                        String nickname = jsonObject2.optString("nickname");
                        String figureurl_qq_2 =  jsonObject2.optString("figureurl_qq_2");
                        Log.e(TAG, "onComplete:  nickname  "+nickname);
                        Log.e(TAG, "onComplete:  figurenurl_qq_2  "+figureurl_qq_2);
                        //存入缓存
                        sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
                        sharedPreferences.edit()
                                .putString("openid",openid)
                                .putString("nickname",nickname)
                                .putString("figureurl_qq_2",figureurl_qq_2)
                                .apply();

                        //存入数据库
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ImlUser imlUser = new ImlUser();
                                User user = new User(openid,nickname,figureurl_qq_2);
                                user.setMoney("100");
                                imlUser.add(user);
                            }
                        }).start();



                        tv_nickname.setText(nickname);
                        Glide.with(context).load(figureurl_qq_2).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(iv_login_pic);
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG, "onError: 内错误   "+uiError );
                    }
                    @Override
                    public void onCancel() {
                    }
                    @Override
                    public void onWarning(int i) {
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onError(UiError uiError) {
            Log.e(TAG, "onError: 外错误   "+uiError );
        }
        @Override
        public void onCancel() {
        }

        @Override
        public void onWarning(int i) {
        }
    };
}