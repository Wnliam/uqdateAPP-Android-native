package com.example.hasee.uqdate.pager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hasee.uqdate.MainActivity;
import com.example.hasee.uqdate.R;
import com.example.hasee.uqdate.activitises.BaseActivity;
import com.example.hasee.uqdate.activitises.LoginActivity;
import com.example.hasee.uqdate.forstart.SplashActivity;
import com.example.hasee.uqdate.helper.SharePrefrenceHelper;
import com.squareup.picasso.Picasso;
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
    private TextView tv_qname;
    private ImageView iv_img;
    String qname = "";
    String qimg = "";

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
        //2019/5/6
        tv_qname = mRootView.findViewById(R.id.tv_setting_name);
        iv_img = mRootView.findViewById(R.id.iv_setting_img);
        //首次数据更新
        SharePrefrenceHelper sph = new SharePrefrenceHelper(mContext.getApplicationContext());
        sph.open("login_info");
        qname = sph.getString("qname");
        qimg = sph.getString("qimg");
        if(!"".equals(qimg))
        Picasso.with(mContext).load(qimg).into(iv_img);
        tv_qname.setText(qname);
        //2019/5/6

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("确定退出登陆吗")
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mTencent = Tencent.createInstance(APP_ID, mContext.getApplicationContext());
                                mTencent.logout(mContext);
                                SharePrefrenceHelper sph = new SharePrefrenceHelper(mContext.getApplicationContext());
                                sph.open("login_info");
                                sph.putString("qname", "");
                                sph.putString("qimg", "");
                                sph.putString("openid", "");
                                gotoLoginActivity();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
               builder.show();
//                mTencent = Tencent.createInstance(APP_ID, mContext.getApplicationContext());
//              mTencent.logout(mContext);
//                SharePrefrenceHelper sph = new SharePrefrenceHelper(mContext.getApplicationContext());
//                sph.open("login_info");
//                sph.putString("qname", "");
//                sph.putString("qimg", "");
//                sph.putString("openid", "");
//              gotoLoginActivity();
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
        //每次重新加载时进行数据更新
        SharePrefrenceHelper sph = new SharePrefrenceHelper(mContext.getApplicationContext());
        sph.open("login_info");
        qname = sph.getString("qname");
        qimg = sph.getString("qimg");
        //做非空判断
        if(!"".equals(qimg))
            Picasso.with(mContext).load(qimg).into(iv_img);
        tv_qname.setText(qname);
    }
}
