package com.airfactory.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.moyusoft.util.JYLog;

/**
 * Created by wonderland on 16. 2. 23..
 */
public class GPSReceiver extends BroadcastReceiver {

    private String TAG = "GPSReceiver";

    public static final String ACTION_ERROR_PERMISSION_GPS = "com.airfactory.ERROR_PERMISSION_GPS";
    public static final String ACTION_ERROR_OFF_GPS = "com.airfactory.ERROR_OFF_GPS";
    public static final String ACTION_FIND_LOCATION = "com.airfactory.ACTION_FIND_LOCATION";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equalsIgnoreCase(ACTION_ERROR_PERMISSION_GPS)) {
            JYLog.D(TAG + "::" + ACTION_ERROR_PERMISSION_GPS, new Throwable());
        } else if (intent.getAction().equalsIgnoreCase(ACTION_ERROR_OFF_GPS)) {
            JYLog.D(TAG + "::" + ACTION_ERROR_OFF_GPS, new Throwable());
        } else if (intent.getAction().equalsIgnoreCase(ACTION_FIND_LOCATION)) {

        }

    }

}
