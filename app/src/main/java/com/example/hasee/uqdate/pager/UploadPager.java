package com.example.hasee.uqdate.pager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hasee.uqdate.MainActivity;
import com.example.hasee.uqdate.R;
import com.example.hasee.uqdate.activitises.FileBowserActivity;
import com.example.hasee.uqdate.helper.SharePrefrenceHelper;
import com.example.hasee.uqdate.util.ClientUploadUtils;
import com.example.hasee.uqdate.util.FileTypeUtil;
import com.example.hasee.uqdate.util.URLConfigUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
* @Description:    用于实现文件上传的功能
* @Author:         Wnliam
* @CreateDate:     2019/1/22 10:40
* @UpdateUser:     Wnliam
* @UpdateDate:     2019/1/22 10:40
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class UploadPager extends BasePager {
    Button btn_upload,button_tobowser;
    TextView tv_1,tv_2;
    Intent globalIntent = null;
    public UploadPager(Context context) {
        super(context);
    }

    @Override
    public void initLayout() {
        loadLayoutById(R.layout.pager_upload);
    }

    @Override
    public void initViews() {
        initData(null);
        btn_upload = mRootView.findViewById(R.id.btn_upload);
        button_tobowser = mRootView.findViewById(R.id.btn_tobowser);
        tv_1 = mRootView.findViewById(R.id.textView2);
        tv_2 = mRootView.findViewById(R.id.textView3);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_1.setText(getIntentFileURL());
                //对非文件或空对象进行异常捕获
                try {
                    tv_2.setText(FileTypeUtil.getFileType(getIntentFileURL()));
                } catch (Exception e) {
                    tv_2.setText("传入的文件名不正确或为空");
                    return;
                }
                //2019/1/28 同步调用出现主线程占用问题
//                try {
//                    ClientUploadUtils.upload(URLConfigUtil.getServerURL(mContext)+"/file",
//                            getIntentFileURL(), new File(getIntentFileURL()).getName());
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//                }
                //2019/1/28
                //2019/1/29尝试异步调用
                String url = "";
                try {
                    url = URLConfigUtil.getServerURL(mContext);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //2019/5/7
                SharePrefrenceHelper sph = new SharePrefrenceHelper(mContext.getApplicationContext());
                sph.open("login_info");
                String openID = sph.getString("openid");//
                ClientUploadUtils.upload(url + "/file", getIntentFileURL(),
                        new File(getIntentFileURL()).getName(), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                tv_2.setText("上传失败");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                tv_2.setText("上传成功");
                                response.body().close();
                            }
                        },openID);
                //2019/1/29
            }
        });

        button_tobowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,FileBowserActivity.class);
                mContext.startActivity(intent);
                //增加跳转后结束本次UI的指令
                // 2019/4/10更新：Main为栈内复用，所以不再注销主ui。
                Activity m = (Activity)mContext;
                m.finish();
            }
        });
    }

    /**
    * 在这里我们对每次载入传进来的数据进行处理
    * @author      Wnliam
    * @return
    * @exception
    * @date        2019/1/23 14:42
    */
    @Override
    public void initData(Object data) {
        if (null != data)            //进行为空判断，避免空指针
        globalIntent = (Intent)data;
    }


    /**
    * 通过从MainActivity中传来的intent对象获取所上传的文件的路径。
    * @author      Wnliam
    * @return      String
    * @exception
    * @date        2019/1/23 9:56
    */
    private String getIntentFileURL(){
        String str = "";
        String action = globalIntent.getAction();
        String type = globalIntent.getType();
        if (globalIntent.ACTION_VIEW.equals(action)) {
            Uri uri = globalIntent.getData();
            str = Uri.decode(uri.getEncodedPath());
            System.out.println(str);
            System.out.println(type);
        }else {//2019/1/24：这里处理从本地文件选择中选择的文件
            if(null != globalIntent.getStringExtra("localFileURL"));
            str =  globalIntent.getStringExtra("localFileURL");
        }
        return str;
    }
}

