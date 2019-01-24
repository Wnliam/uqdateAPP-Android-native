package com.example.hasee.uqdate;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.hasee.uqdate.adapter.MainPagerAdapter;
import com.example.hasee.uqdate.pager.BasePager;
import com.example.hasee.uqdate.pager.FilePager;
import com.example.hasee.uqdate.pager.SettingPager;
import com.example.hasee.uqdate.pager.UploadPager;

import java.util.ArrayList;
import java.util.List;
/**
* @Description:    用户主UI
* @Author:         Wnliam
* @CreateDate:     2019/1/22 10:04
* @UpdateUser:     Wnliam
* @UpdateDate:     2019/1/22 10:04
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {
    RadioGroup radioGroup;
    ViewPager vp;
    RadioButton rbtn_uplod,rbtn_file,rbtn_setting;
    RadioButton [] radioButtons;
    private List<BasePager> pagers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = findViewById(R.id.radioGroup_main);
        rbtn_uplod = findViewById(R.id.radio_upload);
        rbtn_file = findViewById(R.id.radio_files);
        rbtn_setting = findViewById(R.id.radio_setting);
        vp = findViewById(R.id.vp_main);
        vp.setOnPageChangeListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        initPager(vp);
        rbtn_uplod.setChecked(true);
    }

    //向ViewPager中添加数据
    private void initPager(ViewPager v){
        //数据源
        pagers = new ArrayList<>();
        pagers.add(new UploadPager(this));
        pagers.add(new FilePager(this));
        pagers.add(new SettingPager(this));
//适配器
        MainPagerAdapter mAdapter = new MainPagerAdapter(pagers);
        //配置适配器
        v.setAdapter(mAdapter);
        //默认加载第一页
        pagers.get(0).initData(getIntent());
    }
    //radioGroup监听
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int index = -1;
        switch (checkedId){
            case R.id.radio_upload:
                index = 0;
                break;
            case R.id.radio_files:
                index = 1;
                break;
            case R.id.radio_setting:
                index = 2;
                break;


        }
        if(index >= 0){
            vp.setCurrentItem(index);
        }
    }
    //ViewPager监听
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.i("", "----onPageSelected----position = " + position);
        int resld = radioGroup.getChildAt(position).getId();
        radioGroup.check(resld);
        //对接收Intent进行限定
//        if(position == 0)
//            pagers.get(position).initData(getIntent());
//        else
            pagers.get(position).initData(null);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //2019/1/24新增：当MainActivity重新调起时，将Pager定位到UploadPager并重传入Intent
    @Override
    protected void onRestart() {
        super.onRestart();
        pagers.get(0).initData(getIntent());
    }
}
