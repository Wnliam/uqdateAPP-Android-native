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

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @Description:    这个类是获取本地文件的UI
* @Author:         Wnliam
* @CreateDate:     2019/1/28 11:13
* @UpdateUser:     Wnliam
* @UpdateDate:     2019/1/28 11:13
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
        //获取sd卡目录
//        File root = new File("storage/emulated/0");
        SharePrefrenceHelper sph = new SharePrefrenceHelper(this.getApplicationContext());
        sph.open("file_pager");
        files = sph.getString("file_array");

        try {
            jsonArray = new JSONArray(files);
            infiltListView(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }



            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {




                }
            });
            //获取上一级目录的按钮
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        if ((System.currentTimeMillis() - exitTime) > 2000) {
                            //弹出提示，可以有多种方式
                            Toast.makeText(FileServerActivity.this, "已到达sd卡根目录，" +
                                    "再按一次退出", Toast.LENGTH_SHORT).show();
                            exitTime = System.currentTimeMillis();
                        } else {
                            gotoMainActivity();
                        }



                }
            });

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
        //Android7.0后除了安装时授权，还需要手动授权或动态获取权限
        if(null == files) {
            //手动授权的提示
            Toast.makeText(FileServerActivity.this, "您没有打开文件的权限，" +
                    "请到设置/应用设置/权限中打开文件读写权限", Toast.LENGTH_SHORT).show();
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


//2019/1/24增加返回主UI的方法
    private void gotoMainActivity(){
        Intent intent = new Intent(FileServerActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
