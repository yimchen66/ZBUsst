package com.example.zbusst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.zbusst.Util.ChangeTabColor;

import java.util.Timer;
import java.util.TimerTask;

public class Activity_Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ChangeTabColor.setStatusBarColor(this, Color.parseColor("#FFFFFFFF"),false);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //start new activity
                Intent intent = new Intent();
                intent.setClass(Activity_Welcome.this,MainActivity.class);
                startActivity(intent);
                Activity_Welcome.this.finish();
            }
        }, 1000);


    }
}