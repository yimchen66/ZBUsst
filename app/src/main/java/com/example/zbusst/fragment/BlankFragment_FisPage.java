package com.example.zbusst.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zbusst.Activity_DetailGood;
import com.example.zbusst.Adapter.MyListViewAdapterForGoods;
import com.example.zbusst.Bean.SingleGoods;
import com.example.zbusst.MainActivity;
import com.example.zbusst.MyViews.MyListViewForScrollView;
import com.example.zbusst.R;
import com.example.zbusst.Util.ChangeTabColor;
import com.example.zbusst.Util.CreatDataUtil;
import com.example.zbusst.Util.SetDialogUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BlankFragment_FisPage extends Fragment implements View.OnClickListener {
    public static final String GOOD_TYPE_KUAIDI = "代拿快递";
    public static final String GOOD_TYPE_XUEYE = "学业帮助";
    public static final String GOOD_TYPE_ERSHOU = "出二手货";
    public static final String GOOD_TYPE_QITA = "其他";
    public static final String GOOD_TYPE_QUANBU = "全部类型";

    public static final int DB_COMPLATE = 1;
    public static final int UP_SKIP = 1;
    public static final int DOWN_SKIP = 2;
    private static final String TAG = "chen";

    private List<SingleGoods> goodList = new ArrayList<>();

    private Context context;
    private View root;
    private String type = GOOD_TYPE_QUANBU;
    private String searchstr = "";
    private String shaixuan_price = "desc";
    private String shaixuan_time = "desc";
    private TextView tv_type;
    private LinearLayout ll_type,ll_wholesearch,ll_wholeshaixuan,ll_time,ll_price;
    private MyListViewForScrollView good_list;
    private Fragment fragment = this;
    private EditText et_search;
    private Button btn_search;
    private String current_order = "hosttime";
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;


    public BlankFragment_FisPage(Context context) {
        this.context = context;
        // Required empty public constructor
    }

    public BlankFragment_FisPage(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(null == root){
            root = inflater.inflate(R.layout.fragment_blank__fis_page, container, false);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//设置软键盘弹出遮盖其他布局
        //找组件、改变导航栏、状态栏
        MyonTouch();
        intitab();

        initdb();









        return root;
    }


    //获取组件、改变导航栏、状态栏、监听器
    @SuppressLint("ResourceAsColor")
    private void intitab() {
        //设置标题、颜色
        TextView tabtitle = root.findViewById(R.id.tab_title);
        tabtitle.setText(R.string.tabtitle_mian);
        tabtitle.setBackgroundResource(R.color.mainlistcolor);
        tabtitle.setTextColor(Color.WHITE);
        //获取组件
        ll_type = root.findViewById(R.id.ll_choosetype);//选择类型
        ll_price = root.findViewById(R.id.ll_chooseprice);//选择类型
        ll_time = root.findViewById(R.id.ll_choosetime);//选择类型

        tv_type = root.findViewById(R.id.tv_type);
        good_list = root.findViewById(R.id.good_list);
        ll_wholesearch = root.findViewById(R.id.whole_search);
        ll_wholeshaixuan = root.findViewById(R.id.whole_shaixuan);
        et_search = root.findViewById(R.id.search_text);
        btn_search = root.findViewById(R.id.search_btn);

        ll_type.setOnClickListener(this);
        ll_price.setOnClickListener(this);
        ll_time.setOnClickListener(this);
        btn_search.setOnClickListener(this);

        good_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SingleGoods currentgood =(SingleGoods) good_list.getItemAtPosition(i);
                Bundle bundle = new Bundle();
                bundle.putSerializable("currentgood",currentgood);
                Intent intent = new Intent(context, Activity_DetailGood.class);
                intent.putExtras(bundle);
//                context.startActivity(intent);
                startActivityForResult(intent,111);
            }
        });


    }

    //数据库操作、设置list适配器
    public void initdb(){
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case DB_COMPLATE:
                        good_list.setAdapter(new MyListViewAdapterForGoods(goodList,context,fragment));
                        break;
                    default:
                        break;
                }
            }
        };

        Thread thread_db = new Thread(new Runnable() {
            @Override
            public void run() {
                goodList = CreatDataUtil.getGoodList(searchstr,type,shaixuan_price,shaixuan_time,current_order);
                Message msg = new Message();
                msg.what = DB_COMPLATE;
                handler.sendMessage(msg);
            }
        });
        thread_db.start();
    }

    //重写监听器
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_choosetype:
                setTypeDialog();
                break;
            case R.id.search_btn:
                et_search.clearFocus();
                searchstr = et_search.getText().toString();
                if(searchstr.equals(""))
                    return;
                initdb();
                break;
            case R.id.ll_chooseprice:
                if(shaixuan_price.equals("desc"))
                    shaixuan_price = "asc";
                else shaixuan_price = "desc";
                current_order = "ordermoney";
                initdb();
                break;
            case R.id.ll_choosetime:
                if(shaixuan_time.equals("desc"))
                    shaixuan_time = "asc";
                else shaixuan_time = "desc";
                current_order = "hosttime";
                initdb();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 111 && resultCode == 0)
            initdb();
    }

    /**
     * @Description: 监听上、下滑事件
     */
    public void MyonTouch(){
        MainActivity.MyOnTouchListener mTouchListener = new MainActivity.MyOnTouchListener(){
            @Override
            public boolean onTouch(MotionEvent event) {
                //隐藏软键盘
                InputMethodManager manager = ((InputMethodManager) context.
                        getSystemService(Context.INPUT_METHOD_SERVICE));
                manager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                //继承了Activity的onTouchEvent方法，直接监听点击事件
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    //当手指按下的时候
                    x1 = event.getX();
                    y1 = event.getY();
                }
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    //当手指离开的时候
                    x2 = event.getX();
                    y2 = event.getY();
                    if(y1 - y2 > 100) {
                        hideSearch(UP_SKIP);
                    } else if(y2 - y1 > 50) {
                        hideSearch(DOWN_SKIP);
                    }
                }

                return false;
            }

        };

        ((MainActivity) getActivity()).registerMyOnTouchListener(mTouchListener);

    }

    public void hideSearch(int action){
        switch (action){
            case UP_SKIP:
                setSearchHidden();
                break;
            case DOWN_SKIP:
                setSearchShow();
                break;
        }
    }

    public void setSearchShow(){
        if(ll_wholeshaixuan.getVisibility() == View.VISIBLE)
            return;
        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(300);
        ll_wholesearch.startAnimation(mShowAction);
        ll_wholeshaixuan.startAnimation(mShowAction);
        ll_wholesearch.setVisibility(View.VISIBLE);
        ll_wholeshaixuan.setVisibility(View.VISIBLE);
    }
    public void setSearchHidden(){
        if(ll_wholeshaixuan.getVisibility() == View.GONE)
            return;
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        mHiddenAction.setDuration(300);
        ll_wholesearch.startAnimation(mHiddenAction);
        ll_wholeshaixuan.startAnimation(mHiddenAction);
        ll_wholesearch.setVisibility(View.GONE);
        ll_wholeshaixuan.setVisibility(View.GONE);
    }




    /**
     * @Description: 添加自定义dialog
     */
    public void setTypeDialog() {
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
                tv_type.setText(":"+GOOD_TYPE_KUAIDI);
                type = GOOD_TYPE_KUAIDI;
                dialog.dismiss();
                initdb();
            }
        });
        dialog.findViewById(R.id.tv_xueye).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_type.setText(":"+GOOD_TYPE_XUEYE);
                type = GOOD_TYPE_XUEYE;
                dialog.dismiss();
                initdb();
            }
        });
        dialog.findViewById(R.id.tv_ershou).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_type.setText(":"+GOOD_TYPE_ERSHOU);
                type = GOOD_TYPE_ERSHOU;
                dialog.dismiss();
                initdb();
            }
        });
        dialog.findViewById(R.id.tv_qita).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_type.setText(":"+GOOD_TYPE_QITA);
                type = GOOD_TYPE_QITA;
                dialog.dismiss();
                initdb();
            }
        });
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_type.setText(":"+GOOD_TYPE_QUANBU);
                type = GOOD_TYPE_QUANBU;
                dialog.dismiss();
                initdb();
            }
        });
    }



}