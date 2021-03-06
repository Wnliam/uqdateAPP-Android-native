package com.example.hasee.uqdate;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.hasee.uqdate.activitises.BaseActivity;
import com.example.hasee.uqdate.activitises.LoginActivity;
import com.example.hasee.uqdate.adapter.MainPagerAdapter;
import com.example.hasee.uqdate.helper.SharePrefrenceHelper;
import com.example.hasee.uqdate.pager.BasePager;
import com.example.hasee.uqdate.pager.FilePager;
import com.example.hasee.uqdate.pager.SettingPager;
import com.example.hasee.uqdate.pager.UploadPager;
import com.tencent.tauth.Tencent;

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
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {
    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;
    private RadioGroup radioGroup;
    private ViewPager vp;
    private RadioButton rbtn_uplod,rbtn_file,rbtn_setting;
    private RadioButton [] radioButtons;
    private List<BasePager> pagers;

    private static final String APP_ID = "1108216764";
    private Tencent mTencent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBarSetting(this);
        radioGroup = findViewById(R.id.radioGroup_main);
        rbtn_uplod = findViewById(R.id.radio_upload);
        rbtn_file = findViewById(R.id.radio_files);
        rbtn_setting = findViewById(R.id.radio_setting);
        vp = findViewById(R.id.vp_main);
        vp.setOnPageChangeListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        initPager(vp);
        rbtn_uplod.setChecked(true);
        //2019/4/10
        if (null == mTencent)
            mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
        //
        String qname = "";
        SharePrefrenceHelper sph = new SharePrefrenceHelper(getApplicationContext());
        sph.open("login_info");
        qname = sph.getString("qname");
        if ("".equals(qname))
            gotoLoginActivity();
        else if (!mTencent.isSessionValid())
            gotoLoginActivity();

    }

    private void gotoLoginActivity() {
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
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

//2019/1/24优化携带参数方式，不使用重写onRestart方法加载参数，而是在每次跳转时销毁并在下次进入时重新加载

    /**
    * 重写返回键实现按两次推出程序
    * @author      Wnliam
    * @return      boolean
    * @exception
    * @date        2019/1/24 14:44
    */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        pagers.get(0).initData(getIntent());
    }
}
