package com.example.hasee.uqdate.pager;

import android.content.Context;

import com.example.hasee.uqdate.R;
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
    public UploadPager(Context context) {
        super(context);
    }

    @Override
    public void initLayout() {
        loadLayoutById(R.layout.pager_upload);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initData(Object data) {

    }
}
