package com.example.zbusst;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.zbusst.Adapter.MyViewPagerAdapter;
import com.example.zbusst.Util.ChangeTabColor;
import com.example.zbusst.fragment.BlankFragment_FisPage;
import com.example.zbusst.fragment.BlankFragment_SecPage;
import com.example.zbusst.fragment.BlankFragment_ThiPage;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ViewPager2 viewpager;
    private String[] llString = {"tab_main","tab_message","tab_personal"};
    private String[] ivString = {"tab_main_pic","tab_message_pic","tab_personal_pic"};
    private List<LinearLayout> list_linearlayout = new ArrayList<>();
    private LinearLayout llmain,llmess,llperson;
    private ImageView ivmain,ivmess,ivperson,ivcurrent;
    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPaper();
        initTavLayout();
    }

    private void initTavLayout() {
        llmain = findViewById(R.id.tab_main);
        llmess = findViewById(R.id.tab_message);
        llperson = findViewById(R.id.tab_personal);

        llmain.setOnClickListener(this);
        llmess.setOnClickListener(this);
        llperson.setOnClickListener(this);

        ivmain = findViewById(R.id.tab_main_pic);
        ivmess = findViewById(R.id.tab_message_pic);
        ivperson = findViewById(R.id.tab_personal_pic);

        //默认选择订单
        ivmain.setSelected(true);
        ivcurrent = ivmain;
    }

    private void initPaper() {
        viewpager = findViewById(R.id.id_viewpaper);
        List<Fragment> list = new ArrayList<>();
        list.add(new BlankFragment_FisPage(this));
        list.add(new BlankFragment_SecPage(this));
        list.add(new BlankFragment_ThiPage(this));
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager(),getLifecycle(),list);
        viewpager.setAdapter(adapter);
        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            //滑动过度动画
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            //页面选中 添加滑动效果
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                changePager(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    //设置页面左右滑动效果
    private void changePager(int position) {
        ivcurrent.setSelected(false);
        switch (position){
            case 0:
                ivmain.setSelected(true);
                ivcurrent = ivmain;
                ChangeTabColor.setStatusBarColor(this,Color.parseColor("#32CD32"),true);
                break;
            case 1:
                ivmess.setSelected(true);
                ivcurrent = ivmess;
                ChangeTabColor.setStatusBarColor(this,Color.parseColor("#f0f0f0"),false);
                break;
            case 2:
                ivperson.setSelected(true);
                ivcurrent = ivperson;
                ChangeTabColor.setStatusBarColor(this,Color.parseColor("#758eeb"),true);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tab_main:
                viewpager.setCurrentItem(0);
                break;
            case R.id.tab_message:
                viewpager.setCurrentItem(1);
                break;
            case R.id.tab_personal:
                viewpager.setCurrentItem(2);
                break;
        }
    }

    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }
    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(10);

    //fragment触摸事件分发，将触摸事件分发给每个能够响应的fragment
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            if(listener != null) {
                listener.onTouch(ev);
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }
    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener) ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment mFragment : fragments) {
                mFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    //返回键，不退出应用
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}