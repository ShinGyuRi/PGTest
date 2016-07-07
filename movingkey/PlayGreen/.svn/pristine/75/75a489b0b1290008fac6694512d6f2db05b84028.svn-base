package com.moyusoft.util;

import android.content.Intent;
import android.net.Uri;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.ParentAct;
import com.moyusoft.util.ToforClient.ToforClientListener;


public class ToforChromeClient extends WebChromeClient {

	private ToforClientListener listener;
	private ParentAct parentAct;

	public ToforChromeClient(ToforClientListener listener, ParentAct parentAct) {
		this.listener = listener;
		this.parentAct = parentAct;
	}


	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		super.onProgressChanged(view, newProgress);
	}

	@Override
	public boolean onJsAlert(WebView view, String url, String message,
							 JsResult result) {
		return super.onJsAlert(view, url, message, result);
	}

	@Override
	public boolean onJsBeforeUnload(WebView view, String url,
									String message, JsResult result) {
		Debug.showDebug("onJsBeforeUnload" + url + "=========" + message);
		return super.onJsBeforeUnload(view, url, message, result);
	}

	@Override
	public boolean onJsConfirm(WebView view, String url, String message,
							   JsResult result) {
		Debug.showDebug("onJsConfirm" + url + "=========" + message);
		return super.onJsConfirm(view, url, message, result);
	}

	private ValueCallback<Uri> mUploadMessage;
	public final static int FILECHOOSER_RESULTCODE = 1;

	@SuppressWarnings("unused")
	// Android < 3.0
	public void openFileChooser(ValueCallback<Uri> uploadMsg) {
		openFileChooser(uploadMsg, "");
	}

	// Android 3.0+
	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
		mUploadMessage = uploadMsg;
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.addCategory(Intent.CATEGORY_OPENABLE);
		i.setType("image/*");
		parentAct.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
	}

	// Android 4.1+
	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
		openFileChooser(uploadMsg, "");
	}

}
