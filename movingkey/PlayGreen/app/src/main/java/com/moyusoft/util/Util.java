package com.moyusoft.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;

public class Util {
	/**
	 * @param preAct
	 * @param intent
	 * @param isFinish
	 */
	public static void moveActivity(Activity preAct, Intent intent, int enterAniRes, int exitAniRes, boolean isFinish,boolean isAnimation) {
		preAct.startActivity(intent);
		if (isAnimation)
			preAct.overridePendingTransition(enterAniRes, exitAniRes);
		if (isFinish)
			preAct.finish();
	}
	
	public static void moveActivity(Activity preAct, Intent intent, int enterAniRes, int exitAniRes, boolean isFinish, boolean isAnimation, int requestCode) {
		preAct.startActivityForResult(intent,requestCode);
		if (isAnimation)
			preAct.overridePendingTransition(enterAniRes, exitAniRes);
		if (isFinish)
			preAct.finish();
	}

	/**
	 * 구글마켓
	 * 
	 * @param packageName
	 * @param activity
	 */
	public static void setupApp(String packageName, Activity activity) {
		Uri marketUri = Uri.parse("market://details?id=" + packageName);
		Intent marketIntent = new Intent(Intent.ACTION_VIEW).setData(marketUri);
		activity.startActivity(marketIntent);
	}

	/**
	 * GCM
	 */
	// public static void checkGcm(Activity activity,
	// RegistrationCompletedHandler handler) {
	// GCMRegistrar.checkDevice(BaseApplication.getContext());
	// GCMRegistrar.checkManifest(BaseApplication.getContext());
	// String resId =
	// GCMRegistrar.getRegistrationId(BaseApplication.getContext());
	// String resPrefId =
	// PrefUtil.getInstance().getStringPreference(Definitions.PREFKEY.GCM_REGISTER_ID_STR);
	// if (TextUtils.isEmpty(resId) || TextUtils.isEmpty(resPrefId)) {
	// GCMRegistrar.register(BaseApplication.getContext(),
	// GCMIntentService.SEND_ID);
	// }
	// JYLog.D("resID:" + resId, new Throwable());
	// //7.5버전
	// GCMClientManager gcmClientManager = new GCMClientManager(activity,
	// BaseApplication.getContext().getString(R.string.gcm_defaultSenderId));
	// gcmClientManager.registerIfNeeded(handler);
	// }

	public static void webSettings(WebView webView, ToforChromeClient chrome,
			ToforClient client) {

		webView.getSettings().setJavaScriptEnabled(true);
		// webView.setScrollBarStyle(0);
		// webView.getSettings().setBuiltInZoomControls(true);
		// webView.getSettings().setSupportZoom(true);
		webView.getSettings().setUseWideViewPort(false);
		// webView.getSettings().setSavePassword(false);
		webView.setInitialScale(1);
		webView.getSettings().setPluginState(PluginState.ON);
		webView.setHorizontalScrollBarEnabled(false);
		webView.setVerticalScrollBarEnabled(false);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setSupportMultipleWindows(true);
		// webView.setWebChromeClient(chrome);
		webView.setWebViewClient(client);

	}

}
