package com.android.bentabasura.benta_basura.Utils;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by reymond on 18/10/2017.
 */

public class ConnectionDetector {
    static Context context;
    public ConnectionDetector(Context context){
        this.context = context;
    }
    public static boolean isConnected()
    {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if(connectivity != null)
            {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                 if(info != null)
                 {
                     if (info.getState() == NetworkInfo.State.CONNECTED)
                     {
                         return true;
                     }
                 }
            }
        return false;
    }
}
