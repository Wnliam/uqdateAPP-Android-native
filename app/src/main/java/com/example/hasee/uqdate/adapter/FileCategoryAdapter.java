package com.example.hasee.uqdate.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hasee.uqdate.R;
import com.example.hasee.uqdate.bean.FileCategoryBean;

import java.util.ArrayList;

public class FileCategoryAdapter extends BaseAdapter {
    private ArrayList<FileCategoryBean> fileCategoryBeans = null;

    public FileCategoryAdapter(ArrayList<FileCategoryBean> fileCategoryBeans){

        this.fileCategoryBeans = fileCategoryBeans;
    }
    @Override
    public int getCount() {
        if (fileCategoryBeans==null){
            return 0;
        }
        return fileCategoryBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return fileCategoryBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            holder = new ViewHolder();
            LayoutInflater lf = LayoutInflater.from(parent.getContext());

            convertView = lf.inflate(R.layout.item_file_gridview,null);
            holder.gvImg = convertView.findViewById(R.id.iv_file_gvimg);
            holder.gvText = convertView.findViewById(R.id.tv_file_gvtext);


            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }



        holder.gvText.setText(fileCategoryBeans.get(position).getGvText());
        holder.gvImg.setImageResource(fileCategoryBeans.get(position).getGvImg());


        return convertView;
    }


    private class ViewHolder {
        ImageView gvImg;
        TextView gvText;
    }
}

