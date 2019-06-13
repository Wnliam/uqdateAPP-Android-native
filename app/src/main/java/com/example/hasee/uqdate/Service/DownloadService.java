package com.example.hasee.uqdate.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.hasee.uqdate.MainActivity;
import com.example.hasee.uqdate.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadService extends Service {static final int START_DOWNLOAD = 1;            //标志位：开始下载
    static final int FAILED__DOWNLOAD = 2;          //标志位：下载失败
    static final int SUCCESS_DOWNLOAD = 3;          //标志位：下载完成
    static final int CANCEL_DOWNLOAD = 4;           //标志位：取消下载
    static final int PAUSE_DOWNLOAD = 5;            //标志位：暂停下载
    static final int UPDATE_PROGRESS = 6;           //标志位：更新进度

    private boolean isStarted = false;            //操作子线程的标志状态：是否运行
    private boolean isCanceled = false;           //操作子线程的标志状态：是否取消
    private boolean isPaused = false;             //操作子线程的标志状态：是否暂停

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSubThread(MessageEvent event) {
        switch (event.getTag()) {
            case START_DOWNLOAD:
                //最开始的启动前台服务消息
                startForeground(1,getNotification("正在获取资源...",-1));

                InputStream is = null;
                RandomAccessFile saveFile = null;
                File file = null;
                try {
                    //已经下载的长度
                    long downloadLength = 0;

                    //获得传入的下载地址
                    String downloadUrl = event.getUrl();

                    //根据地址截取文件名
                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));

                    //文件名中不能包含特殊字符
                    fileName = fileName.replace("?", "");
                    fileName = fileName.replace("\\", "");
                    fileName = fileName.replace(":", "");
                    fileName = fileName.replace("*", "");

                    //获取SD卡下载目录的路径
                    String directory = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS).getPath();

                    //根据文件名和下载目录生成文件
                    file = new File(directory + fileName);

                    //如果文件存在，获取已下载的长度，等等实现断点下载
                    if (file.exists()) {
                        downloadLength = file.length();
                    }

                    //获取文件总长度
                    long contentLength = getContentLength(downloadUrl);

                    if (contentLength == 0) {
                        //获取不了将要下载的文件的总长度
                        //发出信息，下载失败
                        EventBus.getDefault().post(new MessageEvent(FAILED__DOWNLOAD,
                                "下载地址有误!!", -1));
                        isStarted = false;
                        return;
                    } else if (contentLength == downloadLength) {
                        //总长度等于已下载的长度
                        //说明已经完整下载好了
                        //发出信息，无需下载，已经下载好了
                        EventBus.getDefault().post(new MessageEvent(SUCCESS_DOWNLOAD,
                                "已经下载完成!!", -1));
                        return;
                    }

                    //开始断点下载
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .readTimeout(15, TimeUnit.SECONDS)
                            .writeTimeout(15, TimeUnit.SECONDS)
//                            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())//配置
//                            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())//配置  !!看文章底部!!
                            .build();

                    Request request = new Request.Builder()
                            //这个就是跳到断点的地方
                            .addHeader("RANGE", "bytes=" + downloadLength + "-")
                            .url(downloadUrl)
                            .build();

                    Response response = client.newCall(request).execute();
                    if (response != null) {
                        is = response.body().byteStream();
                        saveFile = new RandomAccessFile(file, "rw");
                        //跳到断点的地方
                        saveFile.seek(downloadLength);
                        byte[] b = new byte[1024];
                        int total = 0;
                        int len;
                        while ((len = is.read(b)) != -1) {

                            total += len;
                            saveFile.write(b, 0, len);

                            //计算已下载的百分比
                            int progress = (int) ((total + downloadLength) * 100 / contentLength);

                            //发出消息，更新UI界面进度
                            EventBus.getDefault().post(new MessageEvent(UPDATE_PROGRESS, "", progress));
                            getManager().notify(1, getNotification("正在下载...", progress));

                            if (isCanceled) {
                                //发出消息，取消下载
                                EventBus.getDefault().post(new MessageEvent(CANCEL_DOWNLOAD,
                                        "取消下载!!", -1));
                                return;
                            }
                            if (isPaused) {
                                //发出消息，暂停下载
                                EventBus.getDefault().post(new MessageEvent(PAUSE_DOWNLOAD,
                                        "暂停下载!!", progress));
                                return;
                            }
                        }
                        response.body().close();
                        //发出消息，下载成功
                        EventBus.getDefault().post(new MessageEvent(SUCCESS_DOWNLOAD, "", -1));
                    }
                } catch (Exception e) {
                    EventBus.getDefault().post(new MessageEvent(FAILED__DOWNLOAD,
                            e.getMessage(), -1));
                    e.printStackTrace();
                }finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (saveFile != null) {
                            saveFile.close();
                        }
                        if (isCanceled && file != null) {
                            isCanceled = false;
                            file.delete();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
        }
    }
    //获取下载文件总长度大小
    private long getContentLength(String downloadUrl) throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
//                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())//配置
//                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())//配置   !!看文章底部!!
                .build();

        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();

        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            response.body().close();
            return contentLength;
        }
        return 0;
    }
    //获取NotificationManager
    private NotificationManager getManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    //发送前台服务的方法
    private Notification getNotification(String content, int progress){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentTitle("Day3_22");//这里我改成自己的app名字
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentText(content);
        if (progress >= 0) {
            builder.setContentText(progress + " %");
            builder.setProgress(100, progress, false);
        }

        return builder.build();
    }
//主线程回显操作
@Subscribe(threadMode = ThreadMode.MAIN)
public void onMainThread(MessageEvent event) {
    switch (event.getTag()) {
        case FAILED__DOWNLOAD:
            isStarted = false;
            Toast.makeText(this,
                    event.getUrl(),Toast.LENGTH_SHORT).show();
            stopForeground(true);
            getManager().notify(1, getNotification("下载失败," + event.getUrl(), -1));
            break;

        case SUCCESS_DOWNLOAD:
            isStarted = false;
            Toast.makeText(this,
                    "下载完成!!",Toast.LENGTH_SHORT).show();
            stopForeground(true);
            getManager().notify(1, getNotification("下载完成!!", -1));
            break;

        case CANCEL_DOWNLOAD:
            isStarted = false;
            Toast.makeText(this,
                    "取消下载并删除残留文件!!",Toast.LENGTH_SHORT).show();
            stopForeground(true);
            getManager().notify(1, getNotification("取消下载并删除残留文件!!", -1));
            break;

        case PAUSE_DOWNLOAD:
            isStarted = false;
            isPaused = false;
            Toast.makeText(this,
                    "暂停下载!!",Toast.LENGTH_SHORT).show();
            getManager().notify(1, getNotification("暂停下载!!", -1));
            break;
    }
}


    //
    private DownloadBinder mBinder = new DownloadBinder();
    class DownloadBinder extends Binder {
        void startDownload(String downloadUrl){
            isStarted = true;
            EventBus.getDefault().post(new MessageEvent(START_DOWNLOAD, downloadUrl, 0));
        }

        void pauseDownload(){
            isStarted = false;
            isPaused = true;
        }

        void cancelDownload(){
            isStarted = false;
            isCanceled = true;
        }

        boolean isStarted(){
            return isStarted;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
