package com.example.wangpeijian.miniweather.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * Created by wangpeijian on 2016/9/27.
 */
public class NetUtil {
    public static final int NETWORK_NONE = 0;
    public static final int NETWORK_WIFI = 1;
    public static final int NETWORK_MOBILE = 1;

    public static int getNetWorkState(Context context){
        ConnectivityManager connManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if(state == NetworkInfo.State.CONNECTED||state == NetworkInfo.State.DISCONNECTING){
            return NETWORK_WIFI;
        }
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if(state == NetworkInfo.State.CONNECTED||state == NetworkInfo.State.DISCONNECTING){
            return NETWORK_MOBILE;
        }
        return NETWORK_NONE;
    }
}

