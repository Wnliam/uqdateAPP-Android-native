package com.example.hasee.uqdate.pager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.hasee.uqdate.R;

public abstract class BasePager {
    public static final String TAG = "BasePager";

    public Context mContext;
    public View mRootView;// 布局对象
    protected TextView tvCenter;


    protected LayoutInflater lf;

    protected int LayouID = R.layout.pager_main;

    public BasePager(Context context) {
        mContext = context;
        initLayout();
        lf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = lf.inflate(LayouID, null);
//        ButterKnife.bind(mRootView);

        //加载子类时进行操作
        initView();
    }
    //加载一页layout
    protected void loadLayoutById(int LayoutID) {
        this.LayouID = LayoutID;
    }

    private void initView() {
        tvCenter = mRootView.findViewById(R.id.tv_center_main);
        //加载子类view
        initViews();
    }

    public abstract void initLayout();
    /**
     * 初始化布局
     */
    public abstract void initViews();

    public abstract void initData(Object data);


    /***
     * 无数据默认加载
     */
    public void loadViews() {

    }


    public String getResString(int resId) {
        return mContext.getResources().getString(resId);
    }


}
