package com.example.hasee.uqdate.pager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.hasee.uqdate.MainActivity;
import com.example.hasee.uqdate.R;
import com.example.hasee.uqdate.activitises.BaseActivity;
import com.example.hasee.uqdate.activitises.LoginActivity;
import com.example.hasee.uqdate.forstart.SplashActivity;
import com.tencent.tauth.Tencent;

import static android.provider.UserDictionary.Words.APP_ID;

/**
* @Description:    用于实现设置的功能
* @Author:         Wnliam
* @CreateDate:     2019/1/22 10:39
* @UpdateUser:     Wnliam
* @UpdateDate:     2019/1/22 10:39
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class SettingPager extends BasePager {
    private static final String APP_ID = "1108216764";//官方获取的APPID
    private Tencent mTencent;
    private Button btn_logout;
    public SettingPager(Context context) {
        super(context);
    }

    @Override
    public void initLayout() {
        loadLayoutById(R.layout.pager_setting);
    }


    @Override
    public void initViews() {
        //以下代码实现注销登陆
        btn_logout = mRootView.findViewById(R.id.btn_setting_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mTencent = Tencent.createInstance(APP_ID, mContext.getApplicationContext());
              mTencent.logout(mContext);
              gotoLoginActivity();
            }
        });
    }

    /**
     * 实现跳转
     * @author      Wnliam
     * @return
     * @exception
     * @date        2019/4/9 17:21
     */
    private void gotoLoginActivity(){
        Intent intent = new Intent(mContext,LoginActivity.class);
        mContext.startActivity(intent);
        Activity m = (Activity)mContext;
        m.finish();
    }


    @Override
    public void initData(Object data) {

    }
}
