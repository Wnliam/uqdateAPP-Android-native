/**
* @Description:    申请动态授权的UI
* @Author:         Wnliam
* @CreateDate:     2019/2/14 17:21
* @UpdateUser:     Wnliam
* @UpdateDate:     2019/2/14 17:21
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
package com.example.hasee.uqdate.activitises;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.example.hasee.uqdate.R;
import com.example.hasee.uqdate.forstart.SplashActivity;
import com.example.hasee.uqdate.util.PermissionUtil;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class EasyPermissionsActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks  {
    //2019/2/15：增加用户组授权，对手动授权后打开闪退进行了修复
    private String[] mPermissions = {Manifest.permission_group.STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET};
    public static final int CODE = 0x001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //检查权限，防止重复获取
        mPermissions = PermissionUtil.getDeniedPermissions(this, mPermissions);
        if (mPermissions.length > 0) {
            /**
             * 1.上下文
             * 2.权限失败后弹出对话框的内容
             * 3.requestCode
             * 4.要申请的权限
             */
            EasyPermissions.requestPermissions(this, PermissionUtil.permissionText(mPermissions), CODE, mPermissions);
            gotoSplashActivity();
        }else
            gotoSplashActivity();
    }


    private void gotoSplashActivity(){
        Intent intent = new Intent(EasyPermissionsActivity.this,SplashActivity.class);
        startActivity(intent);
        finish();
    }
    //所有的权限申请成功的回调
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //do something
    }

    //权限获取失败的回调
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //存在被永久拒绝(拒绝&不再询问)的权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            mPermissions = PermissionUtil.getDeniedPermissions(this, mPermissions);
            PermissionUtil.PermissionDialog(this, PermissionUtil.permissionText(mPermissions) + "请在应用权限管理进行设置！");
        } else {
            EasyPermissions.requestPermissions(this, PermissionUtil.permissionText(mPermissions), CODE, mPermissions);
        }
    }

    //权限被拒绝后的显示提示对话框，点击确认的回调
    @Override
    public void onRationaleAccepted(int requestCode) {
        //会自动再次获取没有申请成功的权限
        //do something
    }

    //权限被拒绝后的显示提示对话框，点击取消的回调
    @Override
    public void onRationaleDenied(int requestCode) {
        //什么都不会做
        //do something
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //将结果传入EasyPermissions中
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
