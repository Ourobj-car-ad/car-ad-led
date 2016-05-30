package com.example.zhengsuren.padapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhengsuren on 16/5/30.
 */
public class Activity_main extends AppCompatActivity{

    private TextView AdsInfo;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdsInfo = (TextView) findViewById(R.id.textView);
        context = this;

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 0:
                    {
                        String adsInfo = (String) msg.obj;
                        if (adsInfo != null)
                        {
                            AdsInfo.setText(adsInfo);
                        }

                        break;
                    }

                    default:
                        break;
                }
            }
        };

        //添加计时器任务
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                new AdsThread("http://139.129.132.60/api/getbyuserid","11",handler,context).start();
            }
        };

        //设定计时器，使得计时器任务延迟0ms开始，每10000ms执行一次
        Timer timer = new Timer(true);
        timer.schedule(task,0,10000);
    }
}
