package com.example.hasee.uqdate.pager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hasee.uqdate.R;
import com.example.hasee.uqdate.util.FileTypeUtil;

import java.io.File;

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
    Button btn_upload;
    TextView tv_1,tv_2;
    Intent intent = null;
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
                }
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
        //进行为空判断，避免空指针
        if (null != data)
        intent = (Intent)data;
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
        String action = intent.getAction();
        String type = intent.getType();
        if (intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();
            str = Uri.decode(uri.getEncodedPath());
            System.out.println(str);
            System.out.println(type);
        }
        return str;
    }
}

