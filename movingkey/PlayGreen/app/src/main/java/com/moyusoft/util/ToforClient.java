package com.moyusoft.util;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ToforClient extends WebViewClient {

	public interface ToforClientListener {
		public boolean shouldOverrideUrlLoading(WebView view, String url);

		public void onPageFinished(WebView view, String url);

		public void onPageStarted(WebView view, String url);

		public void onReceivedError(WebView view, int errorCode,
									String description, String failingUrl);
	}

	private ToforClientListener listener;

	public ToforClient(ToforClientListener listener) {
		this.listener = listener;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		boolean isUrlCheck =listener.shouldOverrideUrlLoading(view, url);
		return isUrlCheck;
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		super.onReceivedError(view, errorCode, description, failingUrl);
	}

}
