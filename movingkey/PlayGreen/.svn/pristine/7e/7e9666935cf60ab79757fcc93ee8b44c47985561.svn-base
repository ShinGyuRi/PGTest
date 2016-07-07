package kr.innisfree.playgreen.fragment.setting;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.ParentFrag;
import com.moyusoft.util.Definitions;
import com.volley.network.dto.NetworkData;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.setting.HelpAct;

/**
 * Created by jooyoung on 2016-04-18.
 */
public class PolicyFrag extends ParentFrag  {

	private TextView txtTitle;
	private int policyType;
	private WebView webView;

	public static PolicyFrag newInstance(int type) {
		return new PolicyFrag(type);
	}

	public PolicyFrag() {
	}

	@SuppressLint("ValidFragment")
	public PolicyFrag(int type) {
		this.policyType = type;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_policy, null);
		setCutOffBackgroundTouch(view);

		/** Init Toolbar*/
		Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
		view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});
		((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);


		txtTitle = (TextView) view.findViewById(R.id.txt_title);
		txtTitle.setText(policyType);

		webView = (WebView) view.findViewById(R.id.wv);

		switch (policyType){
			case Definitions.POLICY_TYPE.USE_POLICY:
				webView.loadUrl("file:///android_asset/pi_use_policy.html");
				break;
			case Definitions.POLICY_TYPE.PRIVACY_POLICY:
				webView.loadUrl("file:///android_asset/pi_privacy_policy.html");
				break;
			case Definitions.POLICY_TYPE.MANAGE_POLICY:
				webView.loadUrl("file:///android_asset/pi_manage_policy.html");
				break;
			case Definitions.POLICY_TYPE.LOCATION_POLICY:
				webView.loadUrl("file:///android_asset/pi_location_policy.html");
				break;
		}



		return view;
	}



	public interface OnCountrySelectListener {
		void OnCountrySelect(NetworkData data);
	}

}
