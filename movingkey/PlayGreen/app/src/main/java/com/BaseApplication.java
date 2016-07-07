package com;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import com.facebook.FacebookSdk;
import com.kakao.auth.KakaoSDK;
import com.moyusoft.util.Debug;
import com.volley.network.NetworkController;

import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.security.MessageDigest;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.sdk.KakaoSDKAdapter;

//@ReportsCrashes(
//        formUri = "",
//        resToastText = R.string.crash_toast_text,
//        mode = ReportingInteractionMode.DIALOG,
//        logcatArguments = { "-t", "200", "-v", "long" },
//        resDialogIcon = android.R.drawable.ic_dialog_info,
//        resDialogTitle = R.string.crash_dialog_title,
//        resDialogText = R.string.crash_dialog_text,
//        mailTo = "jylee@airrabbit.com")
@ReportsCrashes(
		formUri = "",
		mailTo = "jylee@airrabbit.com",
		customReportContent = {
				ReportField.APP_VERSION_CODE,
				ReportField.APP_VERSION_NAME,
				ReportField.ANDROID_VERSION,
				ReportField.PHONE_MODEL,
				ReportField.STACK_TRACE,
				ReportField.LOGCAT},
		mode = ReportingInteractionMode.TOAST,
		resToastText = R.string.crash_toast_text
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
//        ACRA.init(this);
		NetworkController.init(this);
		setHashKey();

		// Picasso picasso = new Picasso.Builder(this).memoryCache(new LruCache(MAX_SIZE)).build();

		KakaoSDK.init(new KakaoSDKAdapter());
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
