package com.example.hasee.uqdate.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


import com.example.hasee.uqdate.pager.BasePager;

import java.util.List;

/**
 * 主页页面切换的适配器
 * Created by panshq on 2017/3/3.
 */
public class MainPagerAdapter extends PagerAdapter {

    private List<BasePager> pagers;

    public MainPagerAdapter(List<BasePager> pagers) {
        this.pagers = pagers;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        if(pagers==null){
            return 0;
        }
        return pagers.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        container.removeView(pagers.get(position).mRootView);

    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = pagers.get(position).mRootView;
        container.addView(view);
        return view;
    }
}
