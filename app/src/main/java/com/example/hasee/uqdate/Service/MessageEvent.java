package com.example.hasee.uqdate.Service;

public class MessageEvent {
    private int tag;   //标志位，方便EventBus辨识
    private String url;//下载地址，我也利用这个传入一些操作信息，所以名字不标准
    private int progress;//反馈过程的进度

    public MessageEvent(int tag,String url,int progress) {
        super();
        this.tag = tag;
        this.url = url;
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public int getTag() {
        return tag;
    }

    public String getUrl() {
        return url;
    }
}

