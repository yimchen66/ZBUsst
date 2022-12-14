package com.example.zbusst.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.zbusst.Bean.GoodInTransaction;
import com.example.zbusst.Bean.MyLaunchOrder;
import com.example.zbusst.Bean.SingleGoods;
import com.example.zbusst.Bean.SingleMessage;
import com.example.zbusst.Dao.Impl.ImlGoodTransaction;
import com.example.zbusst.Dao.Impl.ImplMessageList;
import com.example.zbusst.R;
import com.example.zbusst.Util.PayUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyListViewAdapterForMyLaunchs extends BaseAdapter {
    public static final int CONFIRM_COMPLETE = 101;
    public static final int CONFIRM_URGE= 102;
    private static final String TAG = "hua";
    private Context context;
    private List<MyLaunchOrder> list = new ArrayList<>();
    private Handler handler;


    public MyListViewAdapterForMyLaunchs(List<MyLaunchOrder> list, Context context) {
        this.context = context;
        this.list = list;
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
            view = LayoutInflater.from(context).inflate(R.layout.mylaunch_layout, viewGroup, false);
        }
        ImageView iv_touxiang = view.findViewById(R.id.mylaunch_iv_touxiang);
        ImageView iv_goodimg = view.findViewById(R.id.mylaunch_iv_goodimg);
        TextView tv_goodintro = view.findViewById(R.id.mylaunch_tv_goodintro);
        TextView tv_nickname = view.findViewById(R.id.mylaunch_tv_nickname);
        TextView tv_goodprice = view.findViewById(R.id.mylaunch_tv_goodprice);
        TextView tv_state = view.findViewById(R.id.mylaunch_tv_state);
        TextView tv_time = view.findViewById(R.id.mylaunch_tv_time);
        Button btn_compelte = view.findViewById(R.id.mylaunch_btn_compelte);
        Button btn_delete = view.findViewById(R.id.mylaunch_btn_delete);
        Button btn_urge = view.findViewById(R.id.mylaunch_btn_urge);

        MyLaunchOrder order = list.get(i);
        String touxiang = order.getBuyertouxiang();
        if(touxiang!= null && !touxiang.equals(""))
            Glide.with(context).load(touxiang).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(iv_touxiang);
        String nickname = order.getBuyernickname();
        Log.e(TAG, "nickname???: "+nickname );
        tv_nickname.setText(nickname.equals("")?"":nickname);
        Glide.with(context).load(order.getGoodpic()).into(iv_goodimg);

        tv_goodintro.setText(order.getGoodintro());
        tv_goodprice.setText(String.valueOf(order.getGoodprice()));
        tv_time.setText(order.getUploadtime());

        if(order.getState() == 0){ //????????????????????????
            btn_delete.setTextColor(Color.parseColor("#758eeb"));
            btn_compelte.setTextColor(Color.parseColor("#E1E1E1"));
            btn_urge.setTextColor(Color.parseColor("#E1E1E1"));

            btn_compelte.setBackgroundResource(R.drawable.shape_mylaunch_btn_unclick);
            btn_urge.setBackgroundResource(R.drawable.shape_mylaunch_btn_unclick);
            btn_delete.setBackgroundResource(R.drawable.shape_mylaunch_btn_click);

            btn_compelte.setEnabled(false);
            btn_urge.setEnabled(false);
            btn_delete.setEnabled(true);


            tv_state.setText("????????????");

        }else{ //???????????????
            //??????????????????
            if(order.getState_seller() == 0 && order.getState_buyer() == 0){
                btn_delete.setBackgroundResource(R.drawable.shape_mylaunch_btn_unclick);
                btn_compelte.setBackgroundResource(R.drawable.shape_mylaunch_btn_click);
                btn_urge.setBackgroundResource(R.drawable.shape_mylaunch_btn_click);

                btn_delete.setTextColor(Color.parseColor("#E1E1E1"));
                btn_compelte.setTextColor(Color.parseColor("#758eeb"));
                btn_urge.setTextColor(Color.parseColor("#758eeb"));

                btn_compelte.setEnabled(true);
                btn_delete.setEnabled(false);
                btn_urge.setEnabled(true);

                tv_state.setText("?????????");
            }else if(order.getState_seller() == 1 && order.getState_buyer() == 0){ //??????????????????????????????
                btn_delete.setBackgroundResource(R.drawable.shape_mylaunch_btn_unclick);
                btn_compelte.setBackgroundResource(R.drawable.shape_mylaunch_btn_unclick);
                btn_urge.setBackgroundResource(R.drawable.shape_mylaunch_btn_click);

                btn_delete.setTextColor(Color.parseColor("#E1E1E1"));
                btn_compelte.setTextColor(Color.parseColor("#E1E1E1"));
                btn_urge.setTextColor(Color.parseColor("#758eeb"));

                btn_compelte.setEnabled(false);
                btn_delete.setEnabled(false);
                btn_urge.setEnabled(true);

                tv_state.setText("???????????????");
            }else if(order.getState_seller() == 0 && order.getState_buyer() == 1){ //??????????????????????????????
                btn_delete.setBackgroundResource(R.drawable.shape_mylaunch_btn_unclick);
                btn_compelte.setBackgroundResource(R.drawable.shape_mylaunch_btn_click);
                btn_urge.setBackgroundResource(R.drawable.shape_mylaunch_btn_unclick);

                btn_delete.setTextColor(Color.parseColor("#E1E1E1"));
                btn_compelte.setTextColor(Color.parseColor("#758eeb"));
                btn_urge.setTextColor(Color.parseColor("#E1E1E1"));

                btn_compelte.setEnabled(true);
                btn_delete.setEnabled(false);
                btn_urge.setEnabled(false);
                tv_state.setText("???????????????");
            }else{  //????????????
                btn_delete.setBackgroundResource(R.drawable.shape_mylaunch_btn_unclick);
                btn_compelte.setBackgroundResource(R.drawable.shape_mylaunch_btn_unclick);
                btn_urge.setBackgroundResource(R.drawable.shape_mylaunch_btn_unclick);

                btn_delete.setTextColor(Color.parseColor("#E1E1E1"));
                btn_compelte.setTextColor(Color.parseColor("#E1E1E1"));
                btn_urge.setTextColor(Color.parseColor("#E1E1E1"));

                btn_compelte.setEnabled(false);
                btn_delete.setEnabled(false);
                btn_urge.setEnabled(false);
                tv_state.setText("?????????");
            }

        }

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_delete.isClickable()){
                    mOnItemDeleteListener.onDeleteClick(i);
                }
            }
        });

        btn_compelte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("???????????????");
                handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        if(msg.what == CONFIRM_COMPLETE){
                            Handler handler2 = new Handler(){
                                @Override
                                public void handleMessage(Message msg2) {
                                    if(msg2.what == 0){
                                        btn_compelte.setEnabled(false);
                                        btn_compelte.setBackgroundResource(R.drawable.shape_mylaunch_btn_unclick);
                                        btn_compelte.setTextColor(Color.parseColor("#E1E1E1"));
                                        showSuccessDialog();
                                    }
                                }
                            };
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //???????????????
                                    MyLaunchOrder myLaunchOrder =(MyLaunchOrder) getItem(i);
                                    GoodInTransaction goodInTransaction = new GoodInTransaction();
                                    goodInTransaction.setGoodid(myLaunchOrder.getGoodid());
                                    goodInTransaction.setState_buyer(myLaunchOrder.getState_buyer());
                                    goodInTransaction.setState_seller(1);
                                    goodInTransaction.setBuyeropenid(myLaunchOrder.getBuyeropenid());
                                    goodInTransaction.setSelleropenid(myLaunchOrder.getSelleropenid());
                                    goodInTransaction.setAccepttime(myLaunchOrder.getUploadtime());
                                    ImlGoodTransaction imlGoodTransaction = new ImlGoodTransaction();
                                    imlGoodTransaction.updateStateSeller(goodInTransaction);
                                    list.get(i).setState_seller(1);
                                    //???????????????
                                    SingleMessage singleMessage = new SingleMessage();
                                    singleMessage.setGoodid(myLaunchOrder.getGoodid());
                                    singleMessage.setTargetopenid(myLaunchOrder.getBuyeropenid());
                                    singleMessage.setSendopenid(myLaunchOrder.getSelleropenid());
                                    singleMessage.setMsg("????????????????????????");
                                    Date date = new Date();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                                    String currenttime = sdf.format(date);
                                    singleMessage.setMessagetime(currenttime);

                                    ImplMessageList implMessageList = new ImplMessageList();
                                    implMessageList.add(singleMessage);

                                    //????????????????????????
                                    PayUtil.payCompelte(myLaunchOrder.getGoodid());

                                    Message msg2 = new Message();
                                    msg2.what = 0;
                                    handler2.sendMessage(msg2);

                                }
                            }).start();
                        }

                    }
                };
            }
        });

        btn_urge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("???????????????");
                handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        if(msg.what == CONFIRM_URGE){
                            Handler handler2 = new Handler(){
                                @Override
                                public void handleMessage(Message msg2) {
                                    if(msg2.what == 0){
                                        btn_urge.setEnabled(false);
                                        btn_urge.setBackgroundResource(R.drawable.shape_mylaunch_btn_unclick);
                                        btn_urge.setTextColor(Color.parseColor("#E1E1E1"));
                                        showSuccessDialog();
                                    }
                                }
                            };
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    MyLaunchOrder myLaunchOrder =(MyLaunchOrder) getItem(i);
                                    SingleMessage singleMessage = new SingleMessage();
                                    singleMessage.setGoodid(myLaunchOrder.getGoodid());
                                    singleMessage.setTargetopenid(myLaunchOrder.getBuyeropenid());
                                    singleMessage.setSendopenid(myLaunchOrder.getSelleropenid());
                                    singleMessage.setMsg("????????????????????????????????????");
                                    Date date = new Date();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                                    String currenttime = sdf.format(date);
                                    singleMessage.setMessagetime(currenttime);

                                    ImplMessageList implMessageList = new ImplMessageList();
                                    implMessageList.add(singleMessage);

                                    Message msg2 = new Message();
                                    msg2.what = 0;
                                    handler2.sendMessage(msg2);

                                }
                            }).start();
                        }

                    }
                };
            }
        });

        return view;
    }

        /**
         * ???????????????????????????
         */
        public interface onItemDeleteListener {
            void onDeleteClick(int i);
        }

        private onItemDeleteListener mOnItemDeleteListener;

        public void setOnItemDeleteClickListener(onItemDeleteListener mOnItemDeleteListener) {
            this.mOnItemDeleteListener = mOnItemDeleteListener;
        }


    public void showDialog(String msgs){
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        // ????????????
        View view = View.inflate(context, R.layout.dialog_tips_layout, null);
        dialog.setContentView(view);
        TextView titletext = view.findViewById(R.id.dialog_tips_text);
        Button canceltext = view.findViewById(R.id.dailog_tips_cancel);
        Button confirmtext = view.findViewById(R.id.dailog_tips_confirm);
        titletext.setText(msgs);
        canceltext.setText("??????");
        confirmtext.setText("??????");
        //????????????
        Window window = dialog.getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = 50;
        params.width = (int) ( display.getWidth() * (0.9));
        window.setAttributes(params);
        // ??????????????????
        window.setGravity(Gravity.BOTTOM);
        // ??????????????????
        window.setWindowAnimations(R.style.main_menu_animStyle);
        dialog.show();
        canceltext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        confirmtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                if(msgs.contains("??????"))
                    msg.what = CONFIRM_COMPLETE;
                else
                    msg.what = CONFIRM_URGE;
                handler.sendMessage(msg);
                dialog.cancel();

            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });

    }

    private void showSuccessDialog(){
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        // ????????????
        View view = View.inflate(context, R.layout.dialog_success, null);
        dialog.setContentView(view);

        //????????????
        Window window = dialog.getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) ( display.getWidth() * (0.6));
        window.setAttributes(params);

        // ??????????????????
        window.setGravity(Gravity.CENTER);
        // ??????????????????
        window.setWindowAnimations(R.style.main_menu_animStyle);

        dialog.show();

    }
}
