package com.junwen.videoplayer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 作者:卜俊文
 * 邮箱:344176791@qq.com
 * 时间:4/17/16 12:49 AM
 */
public class NetWorkUtils {

    public static final int STATE_ERROE = 0; //无网络
    public static final int STATE_CONNETCT_WIFI = 1; //有网络，wifi
    public static final int STATE_CONNETCT_MOBLIC = 2; //有网络，非wifi

    public static int isNetworkVisiable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.isAvailable()) {
                    if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        return STATE_CONNETCT_WIFI;
                    } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        return STATE_CONNETCT_MOBLIC;
                    }
                }
            }
        }
        return STATE_ERROE;
    }
}
