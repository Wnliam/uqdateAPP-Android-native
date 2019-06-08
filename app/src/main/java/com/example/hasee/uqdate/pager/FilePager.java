package com.example.hasee.uqdate.pager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.hasee.uqdate.R;
import com.example.hasee.uqdate.activitises.FileBowserActivity;
import com.example.hasee.uqdate.activitises.FileServerActivity;
import com.example.hasee.uqdate.activitises.LoginActivity;
import com.example.hasee.uqdate.adapter.FileCategoryAdapter;
import com.example.hasee.uqdate.bean.FileCategoryBean;
import com.example.hasee.uqdate.helper.SharePrefrenceHelper;
import com.example.hasee.uqdate.util.ClientFileUtils;
import com.example.hasee.uqdate.util.URLConfigUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
* @Description:    用于实现云端文件浏览的功能
* @Author:         Wnliam
* @CreateDate:     2019/1/22 10:39
* @UpdateUser:     Wnliam
* @UpdateDate:     2019/1/22 10:39
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class FilePager extends BasePager{
    private static final String [] categorys = new String[] {"视频","文档","图片","音乐","安装包","压缩包","所有文件"};
    private static final String [] falgs = new String[] {"video","text","image","music","apk","z_r","all_file"};
    private static final int [] imgs = new int[] {R.drawable.ic_shipin,R.drawable.ic_wendang,R.drawable.ic_picture,
            R.drawable.ic_music,R.drawable.ic_apk,R.drawable.ic_file_rarzip,R.drawable.ic_wenjianjia};
    private GridView gv_file;
    private Button btn_file_search;
    private FileCategoryAdapter mAdapter;
    private EditText ed_file_search;
    private String openID = "";
    public static final String Tag = "FilePager";
    public FilePager(Context context) {
        super(context);
        SharePrefrenceHelper sph = new SharePrefrenceHelper(mContext.getApplicationContext());
        sph.open("login_info");
        openID = sph.getString("openid");

    }

    @Override
    public void initLayout() {
        loadLayoutById(R.layout.pager_files);
    }

    @Override
    public void initViews() {

        gv_file = mRootView.findViewById(R.id.grid_file_category);
        btn_file_search = mRootView.findViewById(R.id.btn_file_search);
        ed_file_search = mRootView.findViewById(R.id.et_file_search);
        ArrayList<FileCategoryBean> fileCategoryBeans = new ArrayList<>();
        Log.e("aaab",fileCategoryBeans.toString()+ "a"+falgs);
        fileCategoryBeans.addAll(getFileCategorys(categorys, falgs, imgs));
        mAdapter = new FileCategoryAdapter(fileCategoryBeans);
        gv_file.setAdapter(mAdapter);

        gv_file.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String flag = falgs[position];
                Log.e("bbb",flag);
                String url = "";
                try {
                    url = URLConfigUtil.getServerURL(mContext);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (flag.equals("all_file"))
                    url = url+"/category/all";
                else
                    url = url+"/category";
                ClientFileUtils.okPost(url, openID, flag, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(Tag, e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Intent intent = new Intent(mContext,FileServerActivity.class);
                        String fileArray = response.body().string();
                        Log.e("bbb",fileArray);
                        SharePrefrenceHelper sph = new SharePrefrenceHelper(mContext.getApplicationContext());
                        sph.open("file_pager");

                        sph.putString("file_array", fileArray);
                        mContext.startActivity(intent);
                    }
                });

            }
        });

        btn_file_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = ed_file_search.getText().toString();
                Log.e("searchfile", fileName);
                if (!fileName.equals("")){
                    String url = "";
                    try {
                        url = URLConfigUtil.getServerURL(mContext);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    url = url+"/searchfile";
                    ClientFileUtils.okPostSearch(url, openID, fileName, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e(Tag, e.toString());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Intent intent = new Intent(mContext,FileServerActivity.class);
                            String fileArray = response.body().string();
                            Log.e("bbb",fileArray);
                            SharePrefrenceHelper sph = new SharePrefrenceHelper(mContext.getApplicationContext());
                            sph.open("file_pager");

                            sph.putString("file_array", fileArray);
                            mContext.startActivity(intent);
                        }
                    });
                }
            }
        });

    }

    @Override
    public void initData(Object data) {
        SharePrefrenceHelper sph = new SharePrefrenceHelper(mContext.getApplicationContext());
        sph.open("login_info");
        openID = sph.getString("openid");
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

//    public void bulidInfomation(){
//        String [] categorys = new String[] {"视频","文档","图片","音乐","安装包","压缩包","所有文件"};
//        String [] falgs = new String[] {"video","text","image","music","apk","z_r","all_file"};
//        int [] imgs = new int[] {R.drawable.ic_shipin,R.drawable.ic_wendang,R.drawable.ic_picture,
//                R.drawable.ic_music,R.drawable.ic_apk,R.drawable.ic_file_rarzip,R.drawable.ic_wenjianjia};
//    }

}
