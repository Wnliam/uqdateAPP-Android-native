package com.example.hasee.uqdate.pager;

import android.content.Context;

import com.example.hasee.uqdate.R;
/**
* @Description:    用于实现云端文件浏览的功能
* @Author:         Wnliam
* @CreateDate:     2019/1/22 10:39
* @UpdateUser:     Wnliam
* @UpdateDate:     2019/1/22 10:39
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class FilePager extends BasePager {
    public FilePager(Context context) {
        super(context);
    }

    @Override
    public void initLayout() {
        loadLayoutById(R.layout.pager_files);
    }

    @Override
    public void initViews() {
    }

    @Override
    public void initData(Object data) {

    }
}
