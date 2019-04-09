package com.example.hasee.uqdate.forstart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.example.hasee.uqdate.MainActivity;
import com.example.hasee.uqdate.R;
import com.example.hasee.uqdate.activitises.BaseActivity;
import com.example.hasee.uqdate.activitises.LoginActivity;
import com.example.hasee.uqdate.adapter.GuidePagerAdapter;
import com.example.hasee.uqdate.helper.SharePrefrenceHelper;

import java.util.ArrayList;
import java.util.List;
/**
* @Description:    这个类负责处理引导页的处理逻辑和小点与引导页的联动
* @Author:         Wnliam
* @CreateDate:     2019/1/22 9:36
* @UpdateUser:     Wnliam
* @UpdateDate:     2019/1/22 9:36
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class GuideActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private final static String TAG = "GuideActivity";
    ViewPager viewPager;
    private ImageView[] dotViews;//小圆点
    private LinearLayout llPoint;
    private  View point;
    private int mPointWidth;
    private int mHeight;
    //引导页视图的图片
    private int[] images= new int[]{R.drawable.loan_intro_1_loan,R.drawable.loan_intro_2_loan,R.drawable.loan_intro_3_loan};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        setBarSetting(this);
//        findViewById(R.id.btn_start_guide_a).setOnClickListener(this);
        llPoint = findViewById(R.id.ll_point_guide);
        point = findViewById(R.id.iv_point_blue_guide);
        viewPager = findViewById(R.id.viewp_guide);
        viewPager.setOnPageChangeListener(this);

        initData();
//        initDots();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData() {
        List<View> views = new ArrayList<>();
        LayoutInflater lif = getLayoutInflater();
        View mView = null;
        ImageView imageView = null;
        final int count = images.length;
        for(int i=0;i<count;i++){
            mView = lif.inflate(R.layout.pager_guide, null);
            imageView = mView.findViewById(R.id.iv_guide);
            imageView.setBackgroundResource(images[i]);
//            views.add(mView);
            mView.findViewById(R.id.btn_start_guide_a).setOnClickListener(this);
            if(i == count-1){
                Button barStart = mView.findViewById(R.id.btn_start_guide);
                barStart.setVisibility(View.VISIBLE);
                barStart.setOnClickListener(this);
            }

            //添加页面
            views.add(mView);

            //添加白色圆点
            View view = lif.inflate(R.layout.iv_point_guide, null);
            llPoint.addView(view);
        }
        //这里是引导页页面的pager
        viewPager.setAdapter(new GuidePagerAdapter(views));

        // 获取视图树观察者, 对layout结束事件进行监听
        llPoint.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    // 当layout执行结束后回调此方法
                    @Override
                    public void onGlobalLayout() {
                        llPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        //算出点与点之间的间距（从中心算起）
                        mPointWidth = llPoint.getChildAt(1).getLeft() - llPoint.getChildAt(0).getLeft();
                        mHeight = llPoint.getHeight();
                    }
                });
//        point.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                point.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//            }
//        });

    }



    private void gotoMainActivity(){
        Intent intent = new Intent(GuideActivity.this,MainActivity.class);
        startActivity(intent);

        SharePrefrenceHelper sph = new SharePrefrenceHelper(getApplicationContext());
        sph.open("first_run");
        sph.putBoolean("is_first_run", true);

        finish();
    }
    private void gotoLoginActivity(){
        Intent intent = new Intent(GuideActivity.this,LoginActivity.class);
        startActivity(intent);

        SharePrefrenceHelper sph = new SharePrefrenceHelper(getApplicationContext());
        sph.open("first_run");
        sph.putBoolean("is_first_run", true);

        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start_guide:
                //gotoMainActivity();
                //2019/4/9
                gotoLoginActivity();
                break;
            case R.id.btn_start_guide_a:
                //gotoMainActivity();
                //2019/4/9
                gotoLoginActivity();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.i(TAG, "---------onPageScrolled-------------positionOffsetPixels = " + positionOffsetPixels);

        // System.out.println("当前位置:" + position + ";百分比:" + positionOffset
        // + ";移动距离:" + positionOffsetPixels);
        final double PI = 3.141592;
        double way = 1- (0.5*Math.sin(positionOffset*PI));
        int len = (int) (mPointWidth * positionOffset) + position* mPointWidth;
        int changeHight =(int) (mHeight*way);
//        System.out.println(Math.abs(positionOffset-1/2)+1/2);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) point.getLayoutParams();// 获取当前红点的布局参数
        params.leftMargin = len;// 设置左边距
        if(changeHight>0)
            params.height =changeHight;//设置hight
        if(changeHight>0 || changeHight< 1/2*mHeight)
            params.topMargin =(int) (mHeight*(1-way)/2);
        point.setLayoutParams(params);// 重新给小点设置布局参数
    }

    @Override
    public void onPageSelected(int position) {


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
