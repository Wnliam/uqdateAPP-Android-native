package com.example.hasee.uqdate.util;

import android.content.Intent;

import java.io.File;

/**
* @Description:    这个类对文件类型进行判断
* @Author:         Wnliam
* @CreateDate:     2019/1/23 10:45
* @UpdateUser:     Wnliam
* @UpdateDate:     2019/1/23 10:45
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class FileTypeUtil {

    /**
    * 该方法判断通过Intent获取的文件类型
    * @author      Wnliam
    * @return
    * @exception
    * @date        2019/1/23 10:46
    */
    public static String getTypeFactory(Intent intent){
        String type = intent.getType();
        if (type.equals("text/plain")) return "txt";
        if (type.equals("text/html")) return "html";
        if (type.equals("application/xhtml+xml")) return "xhtml";
        if (type.equals("image/jpeg")) return "jpg";
        if (type.equals("image/png")) return "png";
        if (type.equals("image/webp")) return "webp";
        if (type.equals("application/vnd.ms-excel")) return "xls";
        if (type.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) return "xlsx";
        if (type.equals("application/msword")) return "doc";
        if (type.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) return "docx";
        if (type.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")) return "pptx";
        if (type.equals("application/vnd.ms-powerpoint")) return "ppt";
        if (type.equals("application/pdf")) return "pdf";
        if (type.equals("application/vnd.android.package-archive")) return "apk";
        if (type.equals("video/x-msvideo")) return "avi";
        if (type.equals("application/javascript")) return "js";
        if (type.equals("application/json")) return "json";
        if (type.equals("video/mp4")) return "mp4";
        if (type.equals("audio/mpeg")) return "mp3";
        if (type.equals("application/zip")) return "zip";
        if (type.equals("application/x-rar-compressed")) return "rar";
        else return "unknow";
    }

    /**
    * 以下两个方法通过文件信息获取文件类型
    * @author      Wnliam
    * @return
    * @exception
    * @date        2019/1/23 11:11
    */
    public static String getFileType(String fileName) throws Exception {
        if ("".equals(fileName)) throw new Exception("传入的文件名为空");
        String fileType=fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        return fileType;
    }
    public static String getFileType(File file){
        String fileName = file.getName();
        String fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        return fileType;
    }
}
