package kr.innisfree.playgreen.fragment.setting;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.ParentFrag;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.setting.HelpAct;

/**
 * Created by jooyoung on 2016-04-18.
 */
public class HelpFrag extends ParentFrag  {

	private TextView txtTitle;
	private int type;

	private WebView webView;

	public static HelpFrag newInstance(int type) {
		return new HelpFrag(type);
	}

	public HelpFrag() {
	}

	@SuppressLint("ValidFragment")
	public HelpFrag(int type) {
		this.type = type;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_help, null);
		setCutOffBackgroundTouch(view);

		webView = (WebView) view.findViewById(R.id.wv);

		switch (type){
			case HelpAct.TYPE_HELP_FAQ:
				webView.loadUrl("file:///android_asset/help_faq.html");
				break;
			case HelpAct.TYPE_HELP_INTRODUCE_SERVICE:
				webView.loadUrl("file:///android_asset/help_introduce_service.html");
				break;
			case HelpAct.TYPE_HELP_HOW_TO_USE:
				webView.loadUrl("file:///android_asset/help_how_to_use.html");
				break;
		}


		return view;
	}

}
