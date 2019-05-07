package com.example.hasee.uqdate.activitises;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hasee.uqdate.MainActivity;
import com.example.hasee.uqdate.R;
import com.example.hasee.uqdate.forstart.SplashActivity;
import com.example.hasee.uqdate.helper.SharePrefrenceHelper;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private static final String APP_ID = "1108216764";//官方获取的APPID
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (null == mTencent)
            mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());

        //根据本地内容进行判断2019/5/6
        String qname = "";
        SharePrefrenceHelper sph = new SharePrefrenceHelper(getApplicationContext());
        sph.open("login_info");
        qname = sph.getString("qname");
        if (!"".equals(qname))
            gotoMainActivity();
        //2019/5/6
        //根据qqsession进行是否登陆的判断2019/4
        else if (mTencent.isSessionValid())
            gotoMainActivity();
        //2019/4
    }

    public void buttonLogin(View v){
        /**通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，第二个参数SCOPO 是一个String类型的字符串，表示一些权限
         官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。例如：SCOPE = “get_user_info,add_t”；所有权限用“all”
         第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类 */
        mIUiListener = new BaseUiListener();
        //all表示获取所有权限
        if (!mTencent.isSessionValid())
            mTencent.login(LoginActivity.this,"all", mIUiListener);
    }

    /**
     * 自定义监听器实现IUiListener接口后，需要实现的3个方法
     * onComplete完成 onError错误 onCancel取消
     */
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Toast.makeText(LoginActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                //2019/5/6
                SharePrefrenceHelper sph = new SharePrefrenceHelper(getApplicationContext());
                sph.open("login_info");
                sph.putString("openid", openID);
                //2019/5/6
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken,expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(),qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        Log.e("zzz","登录成功"+response.toString());

                        //2019/5/6增加本地信息的存储
                        JSONObject reinfo = (JSONObject)response;
                        String qname = "";
                        String qimg = "";
                        try {
                            qname = reinfo.getString("nickname");
                            qimg = reinfo.getString("figureurl_2");
                        } catch (JSONException e) {
                            Log.e(TAG,"json解析出错");
                            e.printStackTrace();
                        }
                        SharePrefrenceHelper sph = new SharePrefrenceHelper(getApplicationContext());
                        sph.open("login_info");
                        sph.putString("qname", qname);
                        sph.putString("qimg", qimg);

                        //2019/5/6

                        gotoMainActivity();
                    }
                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG,"登录失败"+uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG,"登录取消");

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();

        }

    }

    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_LOGIN){
            Tencent.onActivityResultData(requestCode,resultCode,data,mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void gotoMainActivity(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

}
