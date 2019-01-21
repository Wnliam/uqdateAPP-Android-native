package com.example.hasee.uqdate.pager;

import android.content.Context;

import com.example.hasee.uqdate.R;

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
