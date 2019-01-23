package com.example.hasee.uqdate.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class URLConfigUtil {
    private static String port = null;
    private static String server = null;
    private URLConfigUtil(){
    }
    private static Properties properties = new Properties();

    public static String getServerURL(Context context) throws IOException {
        String serverURL;
        InputStream inputStream = context.getAssets().open("urlconfig.properties");
        properties.load(inputStream);
        server = properties.getProperty("server");
        port = properties.getProperty("port");
        serverURL = server + ":" +port;
        return serverURL;
    }



}
