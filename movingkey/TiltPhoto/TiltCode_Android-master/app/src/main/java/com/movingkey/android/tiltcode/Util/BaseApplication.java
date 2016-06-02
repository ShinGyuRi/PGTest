package Util;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Created by Gyul on 2016-05-24.
 */
public class BaseApplication extends Application{
    public static Util.BaseApplication mContext;


    public BaseApplication() {
        super();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }

    public static int getDebugMode() {
        return mContext.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE;
    }
}
