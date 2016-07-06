package tiltcode.movingkey.com.tiltcode_new.library;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import com.facebook.FacebookSdk;

import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.security.MessageDigest;

import tiltcode.movingkey.com.tiltcode_new.library.util.Debug;

/**
 * Created by Gyul on 2016-06-16.
 */

@ReportsCrashes(
        formUri = "",
        mailTo = "jsin@movingkey.com",
        customReportContent = {
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.PHONE_MODEL,
                ReportField.STACK_TRACE,
                ReportField.LOGCAT},
        mode = ReportingInteractionMode.TOAST
//        resToastText = R.string.crash_toast_text
)
public class BaseApplication extends Application {

    public static BaseApplication mContext;
    private static volatile BaseApplication instance = null;
    private static volatile Activity currentActivity = null;


    public BaseApplication() {
        super();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        BaseApplication.currentActivity = currentActivity;
    }

    public static int getDebugMode() {
        return mContext.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        setHashKey();

        FacebookSdk.sdkInitialize(this.getApplicationContext());

    }

    /**
     * singleton 애플리케이션 객체를 얻는다.
     *
     * @return singleton 애플리케이션 객체
     */
    public static BaseApplication getGlobalApplicationContext() {
        if (instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }


    public void setHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                hashKey = hashKey.trim();
                Debug.showDebug("hash key:::" + hashKey);
            }
        } catch (Exception e) {
        }
    }
}
