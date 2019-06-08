package com.example.hasee.uqdate.activitises;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hasee.uqdate.MainActivity;
import com.example.hasee.uqdate.R;
import com.example.hasee.uqdate.helper.SharePrefrenceHelper;
import com.example.hasee.uqdate.views.ActionSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @Description:    这个类是获取远程文件信息的UI
* @Author:         Wnliam
* @CreateDate:     2019/4/28 11:13
* @UpdateUser:     Wnliam
* @UpdateDate:     2019/5/8
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class FileServerActivity extends BaseActivity {
    //用来计算返回键间隔时间
    private long exitTime = 0;
    TextView textView;
    ListView listView;
    Button button;
    String files = "";
    JSONArray jsonArray = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser_layout);
        setBarSetting(this);
        textView = findViewById(R.id.text_path);
        listView = findViewById(R.id.list_bowser);
        button = findViewById(R.id.btn_bowser);

        textView.setText("云端文件：");
        SharePrefrenceHelper sph = new SharePrefrenceHelper(this.getApplicationContext());
        sph.open("file_pager");
        files = sph.getString("file_array");

        try {
            jsonArray = new JSONArray(files);
//            infiltListView(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }



            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    //这里是弹出菜单的业务逻辑
                    String jfile = null;
                    try {
                        jfile = jsonArray.getJSONObject(i).getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new ActionSheetDialog(FileServerActivity.this)
                            .builder()
                            .setTitle("请选择你对 "+ jfile +" 的操作：")
                            .setCancelable(true)
                            .setCanceledOnTouchOutside(true)
                            .addSheetItem("下载", ActionSheetDialog.SheetItemColor.Blue,
                                    new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Toast.makeText(FileServerActivity.this,
                                            "下载中", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addSheetItem("删除", ActionSheetDialog.SheetItemColor.Red,
                                    new ActionSheetDialog.OnSheetItemClickListener() {
                                        @Override
                                        public void onClick(int which) {
                                            removeFile(i);
                                            Toast.makeText(FileServerActivity.this,
                                                    "删除成功", Toast.LENGTH_SHORT).show();
                                        }
                            }).show();


                }
            });
            //获取上一级目录的按钮
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        if ((System.currentTimeMillis() - exitTime) > 2000) {
                            //弹出提示，可以有多种方式
                            Toast.makeText(FileServerActivity.this,
                                    "再按一次退出云端浏览", Toast.LENGTH_SHORT).show();
                            exitTime = System.currentTimeMillis();
                        } else {
                            gotoMainActivity();
                        }



                }
            });

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            infiltListView(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
    * 加载本地文件视图的方法
    * @author      Wnliam
    * @return
    * @exception
    * @date        2019/1/28 15:12
    */
    private void infiltListView(JSONArray files) throws JSONException {
        List<Map<String,Object>> listItems= new ArrayList<Map<String,Object>>();
        if(null == files) {
            Toast.makeText(FileServerActivity.this, "未加载到文件", Toast.LENGTH_SHORT).show();
            return;
        }
        for(int i=0;i<files.length();i++){
            Map<String,Object> listItem = new HashMap<String, Object>();
            listItem.put("icon", R.drawable.file);
            listItem.put("fileName", files.getJSONObject(i).getString("name"));
//            System.out.print(files[i].getPath());
            listItems.add(listItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,listItems,R.layout.bowser_item,new String[]{"icon","fileName"}, new int[]{R.id.imageView4,R.id.textView14});
        listView.setAdapter(simpleAdapter);

    }


//返回主UI的方法
    private void gotoMainActivity(){
        Intent intent = new Intent(FileServerActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    //删除file元素的方法
    private void removeFile(int i){
        jsonArray.remove(i);
        onResume();
    }
}
