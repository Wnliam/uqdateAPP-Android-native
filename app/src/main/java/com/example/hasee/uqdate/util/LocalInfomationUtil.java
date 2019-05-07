package com.example.hasee.uqdate.util;

import android.content.Context;

import com.example.hasee.uqdate.helper.SharePrefrenceHelper;
//2019/5/7预计为本地信息存储工具类
public class LocalInfomationUtil {
    public Context context;
    public LocalInfomationUtil(Context context){
        this.context = context;
    }
    SharePrefrenceHelper sph = new SharePrefrenceHelper(context);
}
