package com.example.zbusst;

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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zbusst.Adapter.MyListViewAdapterForMyAccepts;
import com.example.zbusst.Adapter.MyListViewAdapterForMyAccepts;
import com.example.zbusst.Bean.GoodInTransaction;
import com.example.zbusst.Bean.MyAcceptOrder;
import com.example.zbusst.Bean.MyAcceptOrder;
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

public class Activity_MyAccept extends AppCompatActivity implements View.OnClickListener{

    private static final int DB_COMPLATE = 1;
    private static final int CONFIRM_DELETE = 100;
    public static final int UP_SKIP = 1;
    public static final int DOWN_SKIP = 2;

    public String TAG = "hua";
    private LinearLayout ll_wholesearch,ll_shaixuan,ll_accept_main;
    private ImageView iv_search;
    private Button btn_search,btn_delete,btn_urge,btn_compelte;
    private EditText et_text;
    private Toolbar toolbar;
    private TextView tv_unreceive,tv_transcation,tv_whole;
    private Thread getlistthread;
    private Context context;


    private SharedPreferences sharedPreferences;
    private String useropenid,nickname;
    private List<MyAcceptOrder> list = new ArrayList<>();
    private MyListViewForScrollView lv_myaccept;
    private boolean unreceive = false,whole = true,transacion = false;
    private Handler handler;
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_accept);

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
    }

    private void inittab(){
        toolbar = findViewById(R.id.tb_myaccept);
        toolbar.setTitle("我接受的");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ll_wholesearch = findViewById(R.id.whole_search);
        ll_shaixuan = findViewById(R.id.ll_accept_shaixuan);
        ll_accept_main = findViewById(R.id.ll_accept_main);

        iv_search = findViewById(R.id.search_searchimg);
        btn_search = findViewById(R.id.search_btn);
        et_text = findViewById(R.id.search_text);
        lv_myaccept = findViewById(R.id.lv_myaccept);
        tv_transcation = findViewById(R.id.accept_tv_transcation);
        tv_unreceive = findViewById(R.id.accept_tv_unreceive);
        tv_whole = findViewById(R.id.accept_tv_whole);

        ll_wholesearch.setBackgroundResource(R.drawable.shape_searchcolor_mylaunch);
        iv_search.setImageResource(R.drawable.searchfangda_blue);
        btn_search.setBackgroundResource(R.drawable.shape_searchbutton_mylaunch);


        ChangeTabColor.setStatusBarColor(this,Color.parseColor("#758eeb"),true);

        btn_search.setOnClickListener(this);
        tv_transcation.setOnClickListener(this);
        tv_unreceive.setOnClickListener(this);
        tv_whole.setOnClickListener(this);

        lv_myaccept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "点击了  : "+position );
                MyAcceptOrder currentgood =(MyAcceptOrder) lv_myaccept.getItemAtPosition(position);
                Intent intent = new Intent(context,Activity_DetailGood_InTransaction.class);
                intent.putExtra("goodid",currentgood.getGoodid());
                startActivityForResult(intent,111);
//                context.startActivity(intent);
            }
        });

    }

    private void initdb(){
        Context context = this;
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case DB_COMPLATE:
                        MyListViewAdapterForMyAccepts madapter = new MyListViewAdapterForMyAccepts(list,context);
                        lv_myaccept.setAdapter(madapter);
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
                ImlGoodTransaction imlGoodTransaction = new ImlGoodTransaction();
                List<GoodInTransaction> buyerOrderList = imlGoodTransaction.getBuyerOrderList(useropenid);
                for(GoodInTransaction singlegood : buyerOrderList){
                    String sellerorder = singlegood.getSelleropenid();
                    String goodid = singlegood.getGoodid();
                    ImlUser user = new ImlUser();
                    User selleruser = user.getSingle(sellerorder);
                    ImplGoodList goodList = new ImplGoodList();
                    SingleGoods singlegoodinfo = goodList.getSingle(goodid);


                    MyAcceptOrder myAcceptOrder = new MyAcceptOrder();
                    myAcceptOrder.setSelleropenid(selleruser.getOpenid());
                    myAcceptOrder.setSellernickname(selleruser.getNickname());
                    myAcceptOrder.setSellertouxiang(selleruser.getFigureurl_qq_2());
                    myAcceptOrder.setBuyeropenid(useropenid);
                    myAcceptOrder.setGoodid(goodid);
                    myAcceptOrder.setGoodintro(singlegoodinfo.getGoodintroduct());
                    myAcceptOrder.setGoodpic(singlegoodinfo.getGoodpicture());
                    myAcceptOrder.setGoodprice(singlegoodinfo.getGoodprice());
                    myAcceptOrder.setState_buyer(singlegood.getState_buyer());
                    myAcceptOrder.setState_seller(singlegood.getState_seller());
                    myAcceptOrder.setUploadtime(singlegoodinfo.getUploadtime());

                    list.add(myAcceptOrder);
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
            case R.id.accept_tv_unreceive:
                unreceive = true;
                transacion = false;
                whole = false;
                fliterOrders();
                break;
            case R.id.accept_tv_whole:
                unreceive = false;
                transacion = false;
                whole = true;
                fliterOrders();
                break;
            case R.id.accept_tv_transcation:
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
    private void orderDelete(MyListViewAdapterForMyAccepts adapter,List<MyAcceptOrder> mlist) {
        adapter.setOnItemDeleteClickListener(new MyListViewAdapterForMyAccepts.onItemDeleteListener() {
            @Override
            public void onDeleteClick(int i) {
                showDialog("确认删除吗？");
                MyAcceptOrder deleteorder =(MyAcceptOrder) adapter.getItem(i);
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

                                        ImlGoodTransaction imlGoodTransaction = new ImlGoodTransaction();
                                        int del1 = imlGoodTransaction.del(id);
                                        ImplMessageList implMessageList = new ImplMessageList();
                                        implMessageList.deleteMessage(id);

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            list.removeIf(myAcceptOrder -> (myAcceptOrder.getGoodid().equals(id)));
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
        List<MyAcceptOrder> newlist = new ArrayList<>();
        String searchtext = et_text.getText().toString();
        if(et_text.getText() == null)
            searchtext = "";

        if(whole){
            for(MyAcceptOrder order : list){
                if(order.getGoodintro().contains(searchtext) || order.getSellernickname().contains(searchtext))
                    newlist.add(order);
            }
        }else if(unreceive){ //交易中
            for(MyAcceptOrder order : list)
                if((order.getGoodintro().contains(searchtext) || order.getSellernickname().contains(searchtext))
                        && !(order.getState_buyer() == 1 && order.getState_seller() == 1) ){
                    newlist.add(order);
                }

        }else{
            for(MyAcceptOrder order : list)
                if((order.getGoodintro().contains(searchtext) || order.getSellernickname().contains(searchtext))
                        && (order.getState_buyer() == 1 && order.getState_seller() == 1)){
                    newlist.add(order);
                }
        }
        MyListViewAdapterForMyAccepts adapter = new MyListViewAdapterForMyAccepts(newlist,this);
        lv_myaccept.setAdapter(adapter);
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