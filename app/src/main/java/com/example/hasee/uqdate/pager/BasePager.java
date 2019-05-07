package com.example.hasee.uqdate.pager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.hasee.uqdate.R;

/**
* @Description:    用于定义主框架Pager的抽象类，所有的Pager继承这个类并在展示的Activity中声明，
 * BasePager提供每个Pager的生命周期和必要的方法
* @Author:         Wnliam
* @CreateDate:     2019/1/22 9:40
* @UpdateUser:     Wnliam
* @UpdateDate:     2019/1/22 9:40
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public abstract class BasePager {
    public static final String TAG = "BasePager";

    public Context mContext;
    public View mRootView;// 布局对象
    protected TextView tvCenter;


    protected LayoutInflater lf;

    protected int LayouID = R.layout.pager_main;

    /**
    * 构造方法，BasePager的每个子类对象必须传入使用其的Activity的上下文（Context）
    * @author      Wnliam
    * @return
    * @exception
    * @date        2019/1/22 9:47
    */
    public BasePager(Context context) {
        mContext = context;
        initLayout();
        lf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = lf.inflate(LayouID, null);
//        ButterKnife.bind(mRootView);

        //加载子类时进行操作
        initView();
    }
    /**
    * 用于配置Pager对应的图形Layout，一般在子类中调用
    * @author      Wnliam
    * @return
    * @exception
    * @date        2019/1/22 9:43
    */
    protected void loadLayoutById(int LayoutID) {
        this.LayouID = LayoutID;
    }

    /**
    * 实现View的初始化，实例每个Pager对象时会执行该方法
    * @author      Wnliam
    * @return
    * @exception
    * @date        2019/1/22 9:49
    */
    private void initView() {
        tvCenter = mRootView.findViewById(R.id.tv_center_main);
        //加载子类view
        initViews();
    }

    /**
    * 对配置图形Layout绑定进行强制约束，一般在该方法中执行LoadLayoutById()
    * @author      Wnliam
    * @return
    * @exception
    * @date        2019/1/22 9:51
    */
    public abstract void initLayout();

    /**
    * 为子类提供的初始化方法，Pager的主要业务和逻辑在该方法中实现
    * @author      Wnliam
    * @return
    * @exception
    * @date        2019/1/22 9:54
    */
    public abstract void initViews();


    /**
    * 进行数据的初始化
    * @author      Wnliam
    * @return
    * @exception
    * @date        2019/1/22 9:57
    */
    public abstract void initData(Object data);


    /***
     * 无数据默认加载
     */
    public void loadViews() {

    }


    public String getResString(int resId) {
        return mContext.getResources().getString(resId);
    }

    /**
    * 若传入对象为Activity可调用此方法获得Activity对象
    * @author      Wnliam
    * @return      Activity
    * @exception
    * @date        2019/5/4 16:38
    */
    public Activity getThisActivity(){
//        if (null != mContext)
        return (Activity) mContext;
    }
}
