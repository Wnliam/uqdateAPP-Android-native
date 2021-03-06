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
public class FileBowserActivity extends BaseActivity {
    //用来计算返回键间隔时间
    private long exitTime = 0;
    TextView textView;
    ListView listView;
    Button button;
    //父文件夹路径
    File crrentParent = null;
    File[] crrentParentFiles = null;
    String rooturl = Environment.getExternalStorageDirectory().getPath();
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
        File root = new File(rooturl);
        System.out.println("rootURL:"+rooturl);
        if(root.exists()){
            crrentParent = root;
            System.out.println("root:"+root.getAbsoluteFile());
            crrentParentFiles = root.listFiles();
            System.out.println("crrentParentFiles:"+crrentParentFiles);
            infiltListView(crrentParentFiles);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (crrentParentFiles[i].isFile()) {//当点击的是文件时
                        Intent intent = new Intent(FileBowserActivity.this, MainActivity.class);
                        intent.putExtra("localFileURL", crrentParentFiles[i].getAbsolutePath());
                        startActivity(intent);
                        finish();
//                        return;
                    } else {
                        File[] tmp = crrentParentFiles[i].listFiles();
                        if (tmp == null || tmp.length == 0) {
                            Toast.makeText(FileBowserActivity.this, "当前目录不可访问或该路径下没有文件", Toast.LENGTH_SHORT).show();
                        } else {
                            //获取单击列表项对应的文件夹，设为当前的付文件夹
                            crrentParent = crrentParentFiles[i];
                            //保存当前付文件夹内的全部文件和文件夹
                            crrentParentFiles = tmp;
                            //再次更新listview
                            infiltListView(crrentParentFiles);

                        }
                    }
                }
            });
            //获取上一级目录的按钮
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String crrentParentCanonicalPath ="";
                    try {
                        crrentParentCanonicalPath= crrentParent.getCanonicalPath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(!crrentParentCanonicalPath.equals(rooturl)){
                            //获取上一级目录
                            crrentParent = crrentParent.getParentFile();
                            //列出当前目录所有文件
                            crrentParentFiles = crrentParent.listFiles();
                            //更新listview
                            infiltListView(crrentParentFiles);
                        }
                        else{

//                            finish();
                        if ((System.currentTimeMillis() - exitTime) > 2000) {
                            //弹出提示，可以有多种方式
                            Toast.makeText(FileBowserActivity.this, "已到达sd卡根目录，" +
                                    "再按一次退出", Toast.LENGTH_SHORT).show();
                            exitTime = System.currentTimeMillis();
                        } else {
                            gotoMainActivity();
                        }

                        }

                }
            });
        }else {
            textView.setText("没有找到sd卡");
        }
    }



    /**
    * 加载本地文件视图的方法
    * @author      Wnliam
    * @return
    * @exception
    * @date        2019/1/28 15:12
    */
    private void infiltListView(File[] files){
        List<Map<String,Object>> listItems= new ArrayList<Map<String,Object>>();
        //Android7.0后除了安装时授权，还需要手动授权或动态获取权限
        if(null == files) {
            //手动授权的提示
            Toast.makeText(FileBowserActivity.this, "您没有打开文件的权限，" +
                    "请到设置/应用设置/权限中打开文件读写权限", Toast.LENGTH_SHORT).show();
            return;
        }
        for(int i=0;i<files.length;i++){
            Map<String,Object> listItem = new HashMap<String, Object>();
            if(files[i].isDirectory()){
                listItem.put("icon", R.drawable.dir);
            }else{
                listItem.put("icon", R.drawable.file);
            }
            listItem.put("fileName", files[i].getName());
//            System.out.print(files[i].getPath());
            listItems.add(listItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,listItems,R.layout.bowser_item,new String[]{"icon","fileName"}, new int[]{R.id.imageView4,R.id.textView14});
        listView.setAdapter(simpleAdapter);
        try {
            textView.setText("当前路径为："+crrentParent.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            String crrentParentCanonicalPath ="";
            try {
                crrentParentCanonicalPath= crrentParent.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(!crrentParentCanonicalPath.equals(rooturl)){
                    //获取上一级目录
                    crrentParent = crrentParent.getParentFile();
                    //列出当前目录所有文件
                    crrentParentFiles = crrentParent.listFiles();
                    //更新listview
                    infiltListView(crrentParentFiles);
                return true;
            }else
                gotoMainActivity();

        }
        return super.onKeyDown(keyCode, event);
    }
//2019/1/24增加返回主UI的方法
    private void gotoMainActivity(){
        Intent intent = new Intent(FileBowserActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
