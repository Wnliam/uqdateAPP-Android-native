package com.example.hasee.uqdate.util;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ClientFileUtils {
    public  static  void okPost(String url, String openID, String flag, Callback callback){
        Log.e("bbb",url+"\n"+openID+"\n"+flag);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("openid", openID)
                .add("flag", flag)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body).build();
        client.newCall(request).enqueue(callback);
        return;
    }
    public  static  void okPostSearch(String url, String openID, String filename, Callback callback){
        Log.e("bbb",url+"\n"+openID+"\n"+filename);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("openid", openID)
                .add("filename", filename)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body).build();
        client.newCall(request).enqueue(callback);
        return;
    }
    public  static  void okHttpPost(String url, String openID, String filename, Callback callback){
        Log.e("bbb",url+"\n"+openID+"\n"+filename);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("openid", openID)
                .add("filename", filename)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body).build();
        client.newCall(request).enqueue(callback);
        return;
    }
}
