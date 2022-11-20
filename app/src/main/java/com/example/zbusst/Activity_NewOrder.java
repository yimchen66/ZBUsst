package com.example.zbusst;

import static com.example.zbusst.Activity_DetailGood.PAY;
import static com.example.zbusst.Activity_DetailGood.SUCCESS_PAYOK;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zbusst.Adapter.MyListViewAdapterForGoods;
import com.example.zbusst.Bean.SingleGoods;
import com.example.zbusst.Dao.Impl.ImplGoodList;
import com.example.zbusst.Util.ChangeTabColor;
import com.example.zbusst.Util.DialogManager;
import com.example.zbusst.Util.UpLoadToUpYun;
import com.yalantis.ucrop.UCrop;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Activity_NewOrder extends AppCompatActivity implements View.OnClickListener{
    public static final int REQUEST_CODE_CAMERA = 103; //相机
    public static final int REQUEST_CODE_ALBUM = 102; //相册
    public static final int RESULT_CODE_UPLOAD = 200;  //上传成功返回码
    public static final int RESULT_CODE_SUCCESS = 1;
    public static final int RESULT_CODE_FAIL = 0;
    public static final int RESULT_CODE_EXCEPTION = -1;
    public static final int PAY_NOTARGET = 106;

    public static final String AUTHORITIES = "com.xiaozeng.cameraapplication.fileprovider_zbusst";
    public static final String PATH_YUN_PIC = "http://zbusst-image.test.upcdn.net/test/";
    private static final String TAG = "add";


    private String[] starArray = {"代拿快递", "学业帮助", "出二手货", "其它"};
    private String goodtype = "";
    private EditText et_goodintro,et_goodprice,et_goodbeizhu;
    private Button btn_submit;
    private ImageView iv_addimage,detail_loading;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;


    private Uri photoUri;//记录图片地址标识
    private File cropFile ;//记录图片地址
    private String fileName;//记录图片地址用于命名
    private String openid,nickname,figureurl_qq_2;
    private int result;//上传结果
    private String goodintro,goodbeizhu,goodprice;
    private boolean hasPic = false;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        ChangeTabColor.setStatusBarColor(this, Color.parseColor("#758eeb"),true);
        initSpinner();

        context = this;

        sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        if(sharedPreferences == null || sharedPreferences.getString("openid","0001").equals("0001")){
            showFailDialog("请先登录");
        }else{
            openid = sharedPreferences.getString("openid","0001");
            nickname = sharedPreferences.getString("nickname","没登录");
            figureurl_qq_2 = sharedPreferences.getString("figureurl_qq_2",null);
        }

        et_goodbeizhu = findViewById(R.id.et_order_beizhu);
        et_goodprice = findViewById(R.id.et_order_price);
        et_goodintro = findViewById(R.id.et_order_intro);
        btn_submit = findViewById(R.id.btn_sumbit);
        iv_addimage = findViewById(R.id.iv_addimage);
        toolbar = findViewById(R.id.neworder_tb);
        detail_loading = findViewById(R.id.detail_loading);
        detail_loading.setVisibility(View.GONE);

        iv_addimage.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_NewOrder.this.finish();
            }
        });
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_sumbit:
                goodbeizhu = et_goodbeizhu.getText().toString();
                goodintro = et_goodintro.getText().toString();
                goodprice = et_goodprice.getText().toString();
                if((goodintro == null || goodintro.equals("")) ||
                    (goodprice == null || goodprice.equals("")) || (goodtype==null || goodtype.equals(""))
                    || !(cropFile!=null && fileName!= null && !fileName.equals(""))
                ){
                    showFailDialog("请将信息填写完整");
                    return;
                }

                orderSumbit();

                break;
            case R.id.iv_addimage:
                showBottomDialog();
                break;

        }
    }

// 提交
    private void orderSumbit(){
        showLoading();
        Thread thread_uoload = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                //如果选择了图片,继续执行
                if(cropFile!=null && fileName!= null && !fileName.equals("")){
                    Log.e(TAG, "有图片: ");
                    result = UpLoadToUpYun.getuploadResult(cropFile, fileName);
                    hasPic =true;
                }
                else{
                    Message msg = new Message();
                    msg.what = RESULT_CODE_FAIL;
                    handler.sendMessage(msg);
                    hasPic = false;
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(goodtype.contains("货")){  //不用支付佣金
                    thread_uoload.start();
                    if(!hasPic)
                        return;
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = sdf.format(date);
                    SingleGoods good = new SingleGoods();
                    //设置订单信息
                    good.setOpenid(openid);
                    good.setHostopenid(openid);
                    good.setHostname(nickname);
                    good.setHostuserinfo(figureurl_qq_2);
                    good.setGoodbeizhu(goodbeizhu);
                    good.setGoodintroduct(goodintro);
                    good.setGoodprice(Float.parseFloat(goodprice));
                    good.setGoodid(openid+System.currentTimeMillis());
                    good.setGoodtype(goodtype);
                    good.setUploadtime(time);
                    good.setState(0);
                    try {
                        thread_uoload.join();
                        if(RESULT_CODE_UPLOAD == result){  //图片上传成功，写入数据库
                            good.setGoodpicture(PATH_YUN_PIC+fileName);
                            ImplGoodList implGoodList = new ImplGoodList();
                            implGoodList.add(good);
                            Log.e(TAG, "发布成功: ");

                            Message msg = new Message();
                            msg.what = RESULT_CODE_SUCCESS;
                            handler.sendMessage(msg);

                        }else{  //上传失败
                            Message msg = new Message();
                            msg.what = RESULT_CODE_EXCEPTION;
                            handler.sendMessage(msg);
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{  //需要提前支付佣金
                    Intent intent = new Intent(context,Activity_Pay.class);
                    intent.putExtra("havetarget",PAY_NOTARGET);
                    intent.putExtra("money",goodprice);

                    startActivityForResult(intent,PAY);
                }


            }
        }).start();

    }


    // 打开相机
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Build.VERSION.SDK_INT：获取当前系统sdk版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
            String fileName = String.format("pic_%s_%s.jpg", System.currentTimeMillis(),openid);
            File cropFile = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
            photoUri = FileProvider.getUriForFile(this, AUTHORITIES, cropFile);//7.0
        } else {
            photoUri = getDestinationUri();
        }
        // android11以后强制分区存储
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    // 打开相册
    private void openAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        startActivityForResult(intent, REQUEST_CODE_ALBUM);
    }


    //回调
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ALBUM:
                    doCrop(data.getData());
                    break;
                case REQUEST_CODE_CAMERA:
                    //截图
                    doCrop(photoUri);
                    break;
                case UCrop.REQUEST_CROP:
                    Glide.with(this).load(UCrop.getOutput(data)).into(iv_addimage);//方形图像
                    break;
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
        if(requestCode == PAY){ //支付界面
            if(resultCode == SUCCESS_PAYOK){ //支付成功，可以成功发布订单
                Log.e(TAG, "支付成功: " );
                showLoading();
                Thread thread_uoload = new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        //如果选择了图片,继续执行
                        if(cropFile!=null && fileName!= null && !fileName.equals("")){
                            Log.e(TAG, "有图片: ");
                            result = UpLoadToUpYun.getuploadResult(cropFile, fileName);
                            hasPic =true;
                        }
                        else{
                            Message msg = new Message();
                            msg.what = RESULT_CODE_FAIL;
                            handler.sendMessage(msg);
                            hasPic = false;
                        }
                    }
                });
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        thread_uoload.start();
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = sdf.format(date);
                        SingleGoods good = new SingleGoods();
                        //设置订单信息
                        good.setOpenid(openid);
                        good.setHostopenid(openid);
                        good.setHostname(nickname);
                        good.setHostuserinfo(figureurl_qq_2);
                        good.setGoodbeizhu(goodbeizhu);
                        good.setGoodintroduct(goodintro);
                        good.setGoodprice(Float.parseFloat(goodprice));
                        good.setGoodid(openid+System.currentTimeMillis());
                        good.setGoodtype(goodtype);
                        good.setUploadtime(time);
                        good.setState(0);
                        try {
                            thread_uoload.join();
                            if(RESULT_CODE_UPLOAD == result){  //图片上传成功，写入数据库
                                good.setGoodpicture(PATH_YUN_PIC+fileName);
                                ImplGoodList implGoodList = new ImplGoodList();
                                implGoodList.add(good);

                                Message msg = new Message();
                                msg.what = RESULT_CODE_SUCCESS;
                                handler.sendMessage(msg);

                            }else{  //上传失败
                                Message msg = new Message();
                                msg.what = RESULT_CODE_EXCEPTION;
                                handler.sendMessage(msg);
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }else{ //支付失败
                showFailDialog("支付失败");
            }

        }
    }


    // 裁剪方法
    private void doCrop(Uri data) {
        UCrop.of(data, getDestinationUri())//当前资源，保存目标位置
                .start(this);
    }

    private void showBottomDialog() {
        // 使用Dialog、设置style
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        // 设置布局
        View view = View.inflate(this, R.layout.dialog_choosepic_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        // 设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        // 设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        // 设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        dialog.findViewById(R.id.tv_take_photo).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                // 判断是否有相机权限
                ifHaveCameraPermission();
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_take_pic).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                // 判断是否有文件存储权限
                ifHaveAlbumPermission(Activity_NewOrder.this);
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }



    // 判断是否有相机权限
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void ifHaveCameraPermission() {
        //是否有摄像权限
        if (!AndPermission.hasPermissions(this, Permission.Group.CAMERA)) {
            // 动态申请权限
            AndPermission.with(this).runtime().permission(Permission.Group.CAMERA)
                    .onGranted(permissions -> {
                        openCamera();
                    })
                    .onDenied(denieds -> {
                        if (denieds != null && denieds.size() > 0) {
                            for (int i = 0; i < denieds.size(); i++) {
                                if (!shouldShowRequestPermissionRationale(denieds.get(i))) {
                                    DialogManager.permissionDialog(this, "没有拍摄和录制权限！");
                                    break;
                                }
                            }
                        }
                    }).start();
        } else {
            // 有权限 打开相机
            openCamera();
        }
    }

    // 判断是否有文件存储权限
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void ifHaveAlbumPermission(Activity activity) {
        //  Permission.Group.STORAGE：文件存储权限
        if (!AndPermission.hasPermissions(activity, Permission.Group.STORAGE)) {
            // Request permission
            AndPermission.with(activity).runtime().permission(Permission.Group.STORAGE).onGranted(permissions -> {
                openAlbum();
            }).onDenied(denieds -> {
                if (denieds != null && denieds.size() > 0) {
                    for (int i = 0; i < denieds.size(); i++) {
                        if (!activity.shouldShowRequestPermissionRationale(denieds.get(i))) {
                            DialogManager.permissionDialog(activity, "没有访问存储权限！");
                            break;
                        }
                    }
                }
            }).start();
        } else {
            openAlbum();
        }
    }

    //获取存储SD卡路径
    private Uri getDestinationUri() {
        fileName = String.format("pic_%s_%s.jpg", System.currentTimeMillis(),openid);
        cropFile = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);

        return Uri.fromFile(cropFile);
    }

    private Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    detail_loading.clearAnimation();
                    detail_loading.setVisibility(View.GONE);
                    switch (msg.what){
                        case RESULT_CODE_FAIL: //失败
                            showFailDialog("请将信息填写完整");
                            break;
                        case RESULT_CODE_SUCCESS://成功
                            showSuccessDialog();
                            break;
                        case RESULT_CODE_EXCEPTION:
                            showFailDialog("发生未知错误，请稍后重试");
                        default:
                            break;
                    }
                }
            };


    private void initSpinner() {
        //声明一个下拉列表的数组适配器
        ArrayAdapter<String> starAdapter = new ArrayAdapter<String>(this, R.layout.item_select, starArray);
        //设置数组适配器的布局样式
        starAdapter.setDropDownViewResource(R.layout.item_drapdown);
        //从布局文件中获取名叫sp_dialog的下拉框
        Spinner sp = findViewById(R.id.spinner);
        //设置下拉框的标题，不设置就没有难看的标题了
        sp.setPrompt("请选择配送方式");
        //设置下拉框的数组适配器
        sp.setAdapter(starAdapter);
        //设置下拉框默认的显示第一项
        sp.setSelection(0);
        //给下拉框设置选择监听器，一旦用户选中某一项，就触发监听器的onItemSelected方法
        sp.setOnItemSelectedListener(new MySelectedListener());
    }


    private class MySelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            goodtype = starArray[i];
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    private void showSuccessDialog(){
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        // 设置布局
        View view = View.inflate(this, R.layout.dialog_success, null);
        dialog.setContentView(view);


        //设置大小
        Window window = dialog.getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) ( display.getWidth() * (0.6));
        window.setAttributes(params);

        // 设置弹出位置
        window.setGravity(Gravity.CENTER);
        // 设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);

        dialog.show();

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Activity_NewOrder.this.finish();
            }
        });

    }

    private void showFailDialog(String err){
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        // 设置布局
        View view = View.inflate(this, R.layout.dailog_fail, null);
        dialog.setContentView(view);

        TextView text = view.findViewById(R.id.dialog_fail_text);
        text.setText(err);

        //设置大小
        Window window = dialog.getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) ( display.getWidth() * (0.6));
        window.setAttributes(params);

        // 设置弹出位置
        window.setGravity(Gravity.CENTER);
        // 设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(err.contains("登录"))
                    Activity_NewOrder.this.finish();
            }
        });

        dialog.show();

    }

    private void showLoading(){
        detail_loading.setVisibility(View.VISIBLE);
        Animation anim =new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setRepeatCount(Animation.INFINITE);//设定无限循环
        anim.setDuration(1000); // 设置动画时间
        anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        detail_loading.startAnimation(anim);
    }

}