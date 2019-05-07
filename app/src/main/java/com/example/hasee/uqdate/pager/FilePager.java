package com.example.hasee.uqdate.pager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.hasee.uqdate.R;
import com.example.hasee.uqdate.adapter.FileCategoryAdapter;
import com.example.hasee.uqdate.bean.FileCategoryBean;

import java.util.ArrayList;
import java.util.List;

/**
* @Description:    用于实现云端文件浏览的功能
* @Author:         Wnliam
* @CreateDate:     2019/1/22 10:39
* @UpdateUser:     Wnliam
* @UpdateDate:     2019/1/22 10:39
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class FilePager extends BasePager implements AdapterView.OnItemSelectedListener {
    private static final String [] categorys = new String[] {"视频","文档","图片","音乐","安装包","压缩包","所有文件"};
    private static final String [] falgs = new String[] {"video","text","image","music","apk","z_r","all_file"};
    private static final int [] imgs = new int[] {R.drawable.ic_shipin,R.drawable.ic_wendang,R.drawable.ic_picture,
            R.drawable.ic_music,R.drawable.ic_apk,R.drawable.ic_file_rarzip,R.drawable.ic_wenjianjia};
    private GridView gv_file;
    private Button btn_file_search;
    private FileCategoryAdapter mAdapter;

    public FilePager(Context context) {
        super(context);
    }

    @Override
    public void initLayout() {
        loadLayoutById(R.layout.pager_files);
    }

    @Override
    public void initViews() {
        gv_file = mRootView.findViewById(R.id.grid_file_category);
        btn_file_search = mRootView.findViewById(R.id.btn_file_search);
        gv_file.setOnItemSelectedListener(this);

        ArrayList<FileCategoryBean> fileCategoryBeans = new ArrayList<>();
        Log.e("aaab",fileCategoryBeans.toString()+ "a"+falgs);
        fileCategoryBeans.addAll(getFileCategorys(categorys, falgs, imgs));
        mAdapter = new FileCategoryAdapter(fileCategoryBeans);
        gv_file.setAdapter(mAdapter);

    }

    @Override
    public void initData(Object data) {
    }

    /**
    * 注入对象
    * @author      Wnliam
    * @return
    * @exception
    * @date        2019/5/7 12:20
    */
    private ArrayList<FileCategoryBean> getFileCategorys(String [] categorys,String [] falgs ,int [] imgs ){
        ArrayList<FileCategoryBean> fileCategoryBeans = new ArrayList<>();
        for(int i = 0;i < falgs.length; i++){
            FileCategoryBean fileCategoryBean = new FileCategoryBean();
            fileCategoryBean.setFlag(falgs[i]);
            fileCategoryBean.setGvImg(imgs[i]);
            fileCategoryBean.setGvText(categorys[i]);
            fileCategoryBeans.add(fileCategoryBean);
        }
        return fileCategoryBeans;
    }

    public void bulidInfomation(){
        String [] categorys = new String[] {"视频","文档","图片","音乐","安装包","压缩包","所有文件"};
        String [] falgs = new String[] {"video","text","image","music","apk","z_r","all_file"};
        int [] imgs = new int[] {R.drawable.ic_shipin,R.drawable.ic_wendang,R.drawable.ic_picture,
                R.drawable.ic_music,R.drawable.ic_apk,R.drawable.ic_file_rarzip,R.drawable.ic_wenjianjia};
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
