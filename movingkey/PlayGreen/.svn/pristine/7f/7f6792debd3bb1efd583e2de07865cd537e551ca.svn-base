package kr.innisfree.playgreen.fragment.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ParentFrag;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.Util;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.NetworkConstantUtil.URLS;
import com.volley.network.NetworkController;
import com.volley.network.dto.NetworkJson;

import java.util.HashMap;
import java.util.Map;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.BridgeAct;
import kr.innisfree.playgreen.activity.setting.PGPointAct;
import kr.innisfree.playgreen.common.PlaygreenManager;

/**
 * Created by jooyoung on 2016-03-24.
 */
public class PGNowWebviewFrag extends ParentFrag {

	private String category;
	private WebView webView;

	@SuppressLint("ValidFragment")
	public PGNowWebviewFrag(String category) {
		this.category = category;
	}

	public PGNowWebviewFrag() {
	}

	public static PGNowWebviewFrag newInstance(String ctgr) {
		return new PGNowWebviewFrag(ctgr);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_pgnow_webview, null);
		webView = (WebView) view.findViewById(R.id.webview);
		webView.setWebViewClient(webViewClient);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDefaultTextEncodingName("UTF-8");
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
			}
		});

		//webView.loadUrl("file:///android_asset/test.html");
		requestPGNow();

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		webView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		webView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		webView.destroy();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if(isVisibleToUser == false) {
			if(webView !=null) webView.onPause();
		}else{
			if(webView!=null)webView.onResume();
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	public WebViewClient webViewClient = new WebViewClient() {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			JYLog.D(url, new Throwable());
			Intent intent = null;
			if (url.contains("showMyPGPoint")) {
				intent = new Intent(getActivity(), PGPointAct.class);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				return true;
			}else if(url.contains("showMyPG21")){
				intent = new Intent(getActivity(), BridgeAct.class);
				intent.putExtra(Definitions.INTENT_KEY.DATA, Definitions.GOTO.PG21);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				return true;
			}else if(url.contains("goWeb")){
				String[] result = url.split("=");
				if(result != null && result.length>0){
					String webUrl = result[result.length-1];
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
					startActivity(intent);
				}
				return true;
			}
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			showLoading();
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			hideLoading();
			super.onPageFinished(view, url);
		}

	};

	public void requestPGNow() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
		params.put("CATEGORY", category);
		params.put("LANG", PlaygreenManager.getLanguagePG());
		StringRequest myReq = requestPost111(APIKEY.PGNOW, URLS.PGNOW, params);
		NetworkController.addToRequestQueue(myReq);
	}

	public StringRequest requestPost111(final int api_idx, String url, final HashMap<String, String> params) {
		StringRequest stringRequest = null;
		onNetworkStart(api_idx);
		JYLog.D("NETWORK", "URL=====> " + url, null);
		netUtil.createParams(params);
		stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JYLog.D("NETWORK", "Response=====> " + response, new Throwable());
				NetworkJson json = new NetworkJson();
				json.msg = response;
				onNetworkResult(api_idx, json);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				JYLog.D("NETWORK", new Throwable());
				if (error instanceof TimeoutError || error instanceof NoConnectionError) {
					//     Toast.makeText(BaseApplication.getContext(), R.string.str_error_network_timeout, Toast.LENGTH_LONG).show();
				} else if (error instanceof AuthFailureError) {
					//TODO
				} else if (error instanceof ServerError) {
					//TODO
				} else if (error instanceof NetworkError) {
					//TODO
				} else if (error instanceof ParseError) {
					//TODO
				}
				onNetworkError(api_idx, error, null);
			}
		}) {
			@Override
			protected Map<String, String> getParams() {
				return params;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> tempParams = new HashMap<String, String>();
				tempParams.put("Content-Type", "application/x-www-form-urlencoded");
				return tempParams;
			}
		};

		stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		return stringRequest;
	}


	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
			case APIKEY.PGNOW:
				webView.clearCache(true);
				if (Build.VERSION.SDK_INT <= 14) {
					webView.loadData(json.msg, "text/html", "UTF-8");
				} else {
					//webView.loadDataWithBaseURL(null,json.msg,"text/html", "UTF-8", null);
					webView.loadData(json.msg, "text/html; charset=UTF-8", null);
				}
				webView.reload();
				break;
		}
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
		if (json == null) return;
		switch (json.RESULT_CODE) {
		}
	}
}
