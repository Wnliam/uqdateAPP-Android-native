package com.example.hasee.uqdate.util;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import okhttp3.*;

/**
* @Description:    上传文件的工具类（同步实现）
* @Author:         Wnliam
* @CreateDate:     2019/1/28 14:08
* @UpdateUser:     Wnliam
* @UpdateDate:     2019/1/28 14:08
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class ClientUploadUtils {
    public static ResponseBody upload(String url, String filePath, String fileName) throws Exception {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName,
                        RequestBody.create(MediaType.parse("multipart/form-data"), new File(filePath)))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + UUID.randomUUID())
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body();
    }
    /**
    * 异步实现上传
    * @author      Wnliam
    * @return
    * @exception
    * @date        2019/2/14 10:25
    */
    public static void upload(String url, String filePath, String fileName,Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName,
                        RequestBody.create(MediaType.parse("multipart/form-data"), new File(filePath)))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + UUID.randomUUID())
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
        return;
    }
//2019/5/7新增openid
    public static void upload(String url, String filePath, String fileName,Callback callback , String openID){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName,
                        RequestBody.create(MediaType.parse("multipart/form-data"), new File(filePath)))
                .addFormDataPart("openid", openID)
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + UUID.randomUUID())
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
        return;
    }
}
