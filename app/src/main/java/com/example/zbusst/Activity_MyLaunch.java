package com.example.zbusst;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zbusst.Adapter.MyListViewAdapterForGoods;
import com.example.zbusst.Adapter.MyListViewAdapterForMyLaunchs;
import com.example.zbusst.Bean.GoodInTransaction;
import com.example.zbusst.Bean.MyLaunchOrder;
import com.example.zbusst.Bean.SingleGoods;
import com.example.zbusst.Bean.User;
import com.example.zbusst.Dao.Impl.ImlGoodTransaction;
import com.example.zbusst.Dao.Impl.ImlUser;
import com.example.zbusst.Dao.Impl.ImplGoodList;
import com.example.zbusst.Dao.Impl.ImplMessageList;
import com.example.zbusst.MyViews.MyListViewForScrollView;
import com.example.zbusst.Util.ChangeTabColor;

import java.util.ArrayList;
import java.util.List;

public class Activity_MyLaunch extends AppCompatActivity implements View.OnClickListener{

    private static final int DB_COMPLATE = 1;
    private static final int CONFIRM_DELETE = 100;
    public static final int UP_SKIP = 1;
    public static final int DOWN_SKIP = 2;

    public String TAG = "hua";
    private LinearLayout ll_wholesearch,ll_shaixuan,ll_launch_main;
    private ImageView iv_search;
    private Button btn_search,btn_delete,btn_urge,btn_compelte;
    private EditText et_text;
    private Toolbar toolbar;
    private TextView tv_unreceive,tv_transcation,tv_whole;
    private Thread getlistthread;
    private Context context;

    private SharedPreferences sharedPreferences;
    private String useropenid,nickname;
    private List<MyLaunchOrder> list = new ArrayList<>();
    private MyListViewForScrollView lv_mylaunch;
    private boolean unreceive = false,whole = true,transacion = false;
    private  Handler handler;
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_launch);

        sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        if(sharedPreferences == null || sharedPreferences.getString("openid","0001").equals("0001")){
//            showFailDialog("请先登录");
        }else{
            useropenid = sharedPreferences.getString("openid","0001");
            nickname = sharedPreferences.getString("nickname","没登录");
        }
        context = this;
        inittab();
        initdb();


        lv_mylaunch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "点击了  : "+position );
                MyLaunchOrder currentgood =(MyLaunchOrder) lv_mylaunch.getItemAtPosition(position);
                if(currentgood.getState() != 0){
                    Intent intent = new Intent(context,Activity_DetailGood_InTransaction.class);
                    intent.putExtra("goodid",currentgood.getGoodid());
                    startActivityForResult(intent,111);
                }
            }
        });


    }

    private void inittab(){
        toolbar = findViewById(R.id.tb_mylaunch);
        toolbar.setTitle("我发起的");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ll_wholesearch = findViewById(R.id.whole_search);
        ll_shaixuan = findViewById(R.id.ll_launch_shaixuan);
        ll_launch_main = findViewById(R.id.ll_launch_main);

        iv_search = findViewById(R.id.search_searchimg);
        btn_search = findViewById(R.id.search_btn);
        et_text = findViewById(R.id.search_text);
        lv_mylaunch = findViewById(R.id.lv_mylaunch);
        tv_transcation = findViewById(R.id.launch_tv_transcation);
        tv_unreceive = findViewById(R.id.launch_tv_unreceive);
        tv_whole = findViewById(R.id.launch_tv_whole);

        ll_wholesearch.setBackgroundResource(R.drawable.shape_searchcolor_mylaunch);
        iv_search.setImageResource(R.drawable.searchfangda_blue);
        btn_search.setBackgroundResource(R.drawable.shape_searchbutton_mylaunch);


        ChangeTabColor.setStatusBarColor(this,Color.parseColor("#758eeb"),true);

        btn_search.setOnClickListener(this);
        tv_transcation.setOnClickListener(this);
        tv_unreceive.setOnClickListener(this);
        tv_whole.setOnClickListener(this);

    }

    private void initdb(){
        Context context = this;
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case DB_COMPLATE:
                        MyListViewAdapterForMyLaunchs madapter = new MyListViewAdapterForMyLaunchs(list,context);
                        lv_mylaunch.setAdapter(madapter);
                        orderDelete( madapter,list);
                        break;
                    default:
                        break;
                }
            }
        };
        getlistthread = new Thread(new Runnable() {
            @Override
            public void run() {
                list = new ArrayList<>();
                User user = new User();
                SingleGoods singleGoods = new SingleGoods();
                GoodInTransaction goodInTransaction = new GoodInTransaction();

                ImplGoodList implGoodList = new ImplGoodList();
                List<SingleGoods> sellerList_whole = implGoodList.getSellerList(useropenid);
                for(SingleGoods good : sellerList_whole){
                    int state = good.getState();

                    MyLaunchOrder launchOrder = new MyLaunchOrder();
                    launchOrder.setSelleropenid(useropenid);
                    launchOrder.setGoodid(good.getGoodid());
                    launchOrder.setGoodintro(good.getGoodintroduct());
                    launchOrder.setGoodprice(good.getGoodprice());
                    launchOrder.setGoodpic(good.getGoodpicture());
                    launchOrder.setUploadtime(good.getUploadtime());
                    launchOrder.setState(state);

                    if(state == 1){  //接单
                        ImlGoodTransaction imlGoodTransaction = new ImlGoodTransaction();
                        String goodid = good.getGoodid();
                        GoodInTransaction receivedgood = imlGoodTransaction.getSingle(goodid);
                        launchOrder.setState_buyer(receivedgood.getState_buyer());
                        launchOrder.setState_seller(receivedgood.getState_seller());
                        launchOrder.setBuyeropenid(receivedgood.getBuyeropenid());

                        ImlUser imlUser = new ImlUser();
                        User buyeruser = imlUser.getSingle(receivedgood.getBuyeropenid());
                        launchOrder.setBuyernickname(buyeruser.getNickname());
                        launchOrder.setBuyertouxiang(buyeruser.getFigureurl_qq_2());
                    }else{ //未接单
                        launchOrder.setState_buyer(0);
                        launchOrder.setState_seller(0);
                        launchOrder.setBuyeropenid("");
                        launchOrder.setBuyernickname("");
                        launchOrder.setBuyertouxiang("");
                    }

                    list.add(launchOrder);
                }
                Message msg = new Message();
                msg.what = DB_COMPLATE;
                handler.sendMessage(msg);
            }
        });
        getlistthread.start();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.launch_tv_unreceive:
                unreceive = true;
                transacion = false;
                whole = false;
                fliterOrders();
                break;
            case R.id.launch_tv_whole:
                unreceive = false;
                transacion = false;
                whole = true;
                fliterOrders();
                break;
            case R.id.launch_tv_transcation:
                unreceive = false;
                transacion = true;
                whole = false;
                fliterOrders();
                break;
            case R.id.search_btn:
                fliterOrders();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 111 && resultCode == 0)
            initdb();
    }

    //子控件点击事件
    private void orderDelete(MyListViewAdapterForMyLaunchs adapter,List<MyLaunchOrder> mlist) {
            adapter.setOnItemDeleteClickListener(new MyListViewAdapterForMyLaunchs.onItemDeleteListener() {
                @Override
                public void onDeleteClick(int i) {
                    showDialog("确认删除吗？");
                    MyLaunchOrder deleteorder =(MyLaunchOrder) adapter.getItem(i);
                    handler = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what){
                                case CONFIRM_DELETE:
                                    mlist.remove(i);
                                    adapter.notifyDataSetChanged();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String id = deleteorder.getGoodid();
                                            ImplGoodList implGoodList = new ImplGoodList();
                                            int del = implGoodList.del(id);

                                            ImplMessageList implMessageList = new ImplMessageList();
                                            implMessageList.deleteMessage(id);

                                            ImlGoodTransaction imlGoodTransaction = new ImlGoodTransaction();
                                            int del1 = imlGoodTransaction.del(id);
                                            Log.e(TAG, "hua删除交易表: "+del1 );

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                list.removeIf(myLaunchOrder -> (myLaunchOrder.getGoodid().equals(id)));
                                            }
                                        }
                                    }).start();
                                    break;
                                default:
                                    break;
                            }
                        }
                    };

                }
            });
    }



    private void fliterOrders(){
        List<MyLaunchOrder> newlist = new ArrayList<>();
        String searchtext = et_text.getText().toString();
        if(et_text.getText() == null)
            searchtext = "";

        if(whole){
            for(MyLaunchOrder order : list){
                if(order.getGoodintro().contains(searchtext) || order.getBuyernickname().contains(searchtext))
                    newlist.add(order);
            }
        }else if(unreceive){
            for(MyLaunchOrder order : list)
                if((order.getGoodintro().contains(searchtext) || order.getBuyernickname().contains(searchtext))
                        && order.getState() == 0 ){
                    newlist.add(order);
                }

        }else{
            for(MyLaunchOrder order : list)
                if((order.getGoodintro().contains(searchtext) || order.getBuyernickname().contains(searchtext))
                        && order.getState() == 1){
                    newlist.add(order);
                }
        }
        MyListViewAdapterForMyLaunchs adapter = new MyListViewAdapterForMyLaunchs(newlist,this);
        lv_mylaunch.setAdapter(adapter);
        orderDelete(adapter,newlist);
    }


    public String showDialog(String msg){
        final String[] result = new String[1];
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        // 设置布局
        View view = View.inflate(this, R.layout.dialog_tips_layout, null);
        dialog.setContentView(view);
        TextView titletext = view.findViewById(R.id.dialog_tips_text);
        Button canceltext = view.findViewById(R.id.dailog_tips_cancel);
        Button confirmtext = view.findViewById(R.id.dailog_tips_confirm);
        titletext.setText(msg);
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
                result[0] = "取消";
                dialog.cancel();
            }
        });
        confirmtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = CONFIRM_DELETE;
                handler.sendMessage(msg);
                result[0] = "确定";
                dialog.cancel();

            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                result[0] = "取消";
            }
        });

        return result[0];
    }

}