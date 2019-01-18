package com.example.hasee.uqdate.forstart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.hasee.uqdate.MainActivity;
import com.example.hasee.uqdate.R;
import com.example.hasee.uqdate.helper.SharePrefrenceHelper;


public class SplashActivity extends Activity {
    //延时三秒后跳转到引导页
    private static final int Delayed = 3000;
    private static final int TO_GUIDE = 1010;
//    private static final String URL = "http://static.jstv.com/img/2018/7/3/2018731530571088588_14053.jpg";
    ImageView imageView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,

                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_splash);
        imageView = findViewById(R.id.iv_splash);

//        Picasso.with(getApplicationContext()).load(URL).into(imageView);
//        Glide.with(getApplicationContext()).load(URL).into(imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(TO_GUIDE, Delayed);
    }

    private void gotoGuideActivity(){
        Intent intent = new Intent(SplashActivity.this,GuideActivity.class);
        startActivity(intent);
        finish();
    }
    private void gotoMainActivity(){
        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    //延时启动引导页
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case TO_GUIDE:
                    //判断是否第一次启动
                    SharePrefrenceHelper sph = new SharePrefrenceHelper(getApplicationContext());
                    sph.open("first_run");
                    boolean isFirstRan = sph.getBoolean("is_first_run");
                    if (!isFirstRan){
                        //跳转到引导页
                        gotoGuideActivity();
                    }else{
                        //跳转到主页
                        gotoMainActivity();
                    }
                    break;
            }
        }
    };
}
