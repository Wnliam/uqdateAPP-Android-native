package com.example.hasee.uqdate.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class GuidePagerAdapter extends PagerAdapter {
    private static String TAG = "GuidePagerAdapter";
    private List<View> views;

    public GuidePagerAdapter(List<View> pagers) {
        this.views = pagers;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        Log.i(TAG, "-----getCount-----");
        if(views==null){
            return 0;
        }
        return views.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        container.removeView(views.get(position));

    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position);
        container.addView(view);
        return view;
    }
}
