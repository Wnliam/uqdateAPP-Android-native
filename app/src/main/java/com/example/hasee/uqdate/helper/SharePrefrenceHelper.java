package com.example.hasee.uqdate.helper;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//import com.belter.btlibrary.util.ULog;

/**
 * 内部存储工具类
 * 使用
 * 1.创建对象，
 * 2.open文件
 * 3.操作put或get方法
 * Created by panshq on 2017/3/10.
 */
public class SharePrefrenceHelper {
    private static final String TAG = SharePrefrenceHelper.class.toString();
    private Context context;
    private SharedPreferences prefrence;

    public SharePrefrenceHelper(Context c) {
        this.context = c.getApplicationContext();
    }

    public void open(String name) {
        this.open(name, 0);
    }

    public void open(String name, int version) {
        String fileName = name + "_" + version;
        this.prefrence = this.context.getSharedPreferences(fileName, 0);
    }

    public void putString(String key, String value) {
        Editor editor = this.prefrence.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return this.prefrence.getString(key, "");
    }

    public void putBoolean(String key, Boolean value) {
        Editor editor = this.prefrence.edit();
        editor.putBoolean(key, value.booleanValue());
        editor.commit();
    }

    public boolean getBoolean(String key) {
        return this.prefrence.getBoolean(key, false);
    }


    public boolean getBoolean(String key, boolean defValue) {
        return this.prefrence.getBoolean(key, defValue);
    }

    public void putLong(String key, Long value) {
        Editor editor = this.prefrence.edit();
        editor.putLong(key, value.longValue());
        editor.commit();
    }

    public long getLong(String key) {
        return this.prefrence.getLong(key, 0L);
    }

    public void putInt(String key, Integer value) {
        Editor editor = this.prefrence.edit();
        editor.putInt(key, value.intValue());
        editor.commit();
    }

    public int getInt(String key) {
        return this.prefrence.getInt(key, -1);
    }

    public void putFloat(String key, Float value) {
        Editor editor = this.prefrence.edit();
        editor.putFloat(key, value.floatValue());
        editor.commit();
    }

    public float getFloat(String key) {
        return this.prefrence.getFloat(key, 0.0F);
    }

    public void put(String key, Object value) {
        if(value != null) {
            try {
                ByteArrayOutputStream t = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(t);
                oos.writeObject(value);
                oos.flush();
                oos.close();
                byte[] data = t.toByteArray();
                String base64 = Base64.encodeToString(data, 2);
                this.putString(key, base64);
            } catch (Throwable var7) {
                Log.w(TAG, var7.toString());
            }

        }
    }

    public Object get(String key) {
        try {
            String t = this.getString(key);
            if(TextUtils.isEmpty(t)) {
                return null;
            } else {
                byte[] data = Base64.decode(t, 2);
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bais);
                Object value = ois.readObject();
                ois.close();
                return value;
            }
        } catch (Throwable var7) {
            Log.w(TAG, var7.toString());
            return null;
        }
    }

    public void remove(String key) {
        Editor editor = this.prefrence.edit();
        editor.remove(key);
        editor.commit();
    }
}
