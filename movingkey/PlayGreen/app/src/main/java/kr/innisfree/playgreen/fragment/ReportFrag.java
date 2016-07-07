package kr.innisfree.playgreen.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ParentFrag;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.moyusoft.util.TextUtil;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.NetworkConstantUtil.URLS;
import com.volley.network.NetworkController;
import com.volley.network.dto.NetworkJson;

import java.util.HashMap;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.common.PlaygreenManager;

/**
 * Created by jooyoung on 2016-03-04.
 */
public class ReportFrag extends ParentFrag implements View.OnClickListener {

	private final static String TAG = "ReportFrag::";
	public final static String NOTIFY_CATEGORY_T = "T"; // TimeLine
	public final static String NOTIFY_CATEGORY_P = "P"; // PlayGreen21
	public final static String NOTIFY_CATEGORY_C = "C"; // Comment
	public final static String NOTIFY_DEPTH_N = "N"; // 플레이그린과 상관없는 게시물입니다.
	public final static String NOTIFY_DEPTH_A = "A"; // 음란성/광고성 게시물입니다.
	public final static String NOTIFY_DEPTH_E = "E"; // 기타

	private String category;
	private String targetId;
	private String notifyDepth;

	private RadioButton radioFirst, radioSecond, radioThird;
	private EditText editReport;

	public ReportFrag() {
	}

	@SuppressLint("ValidFragment")
	public ReportFrag(String category, String targetId) {
		this.category = category;
		this.targetId = targetId;
	}

	public static ReportFrag newInstance(String category, String targetId) {
		ReportFrag frag = new ReportFrag(category, targetId);
		return frag;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_report, null);
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

		radioFirst = (RadioButton) view.findViewById(R.id.radio_first);
		radioSecond = (RadioButton) view.findViewById(R.id.radio_second);
		radioThird = (RadioButton) view.findViewById(R.id.radio_third);

		editReport = (EditText) view.findViewById(R.id.edit_report);
		editReport.setEnabled(false);


		radioFirst.setOnClickListener(new OnRadioClickListener());
		radioSecond.setOnClickListener(new OnRadioClickListener());
		radioThird.setOnClickListener(new OnRadioClickListener());
		view.findViewById(R.id.txt_report).setOnClickListener(this);

		radioFirst.callOnClick();

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.txt_report:
				requestReport();
				break;
		}
	}

	private class OnRadioClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			radioFirst.setChecked(false);
			radioSecond.setChecked(false);
			radioThird.setChecked(false);

			RadioButton radioButton = (RadioButton) v;
			radioButton.setChecked(true);

			switch (v.getId()) {
				case R.id.radio_first:
					notifyDepth = NOTIFY_DEPTH_N;
					editReport.setEnabled(false);
					break;
				case R.id.radio_second:
					notifyDepth = NOTIFY_DEPTH_A;
					editReport.setEnabled(false);
					break;
				case R.id.radio_third:
					notifyDepth = NOTIFY_DEPTH_E;
					editReport.setEnabled(true);
					editReport.requestFocus();
					break;
			}
		}
	}


	public void requestReport() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
		params.put("NOTIFY_CATEGORY", category);
		params.put("TARGET_ID", targetId);
		params.put("NOTIFY_DEPT", notifyDepth);
		if (notifyDepth == NOTIFY_DEPTH_E) {
			if (TextUtil.isNull(editReport.getText().toString())) {
				Toast.makeText(getActivity(), getString(R.string.str_toast_input_report), Toast.LENGTH_SHORT).show();
				return;
			}
			params.put("NOTIFY_TEXT", editReport.getText().toString());
		}
		StringRequest myReq = netUtil.requestPost(APIKEY.NOTIFY_REGIST, URLS.NOTIFY_REGIST, params);
		NetworkController.addToRequestQueue(myReq);
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
			case APIKEY.NOTIFY_REGIST:
				Toast.makeText(getContext(), R.string.str_toast_report_complete, Toast.LENGTH_SHORT).show();
				getActivity().onBackPressed();
				break;
		}
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
		if (json == null || json.RESULT_CODE == null) return;
		switch (json.RESULT_CODE) {
			case NetworkConstantUtil.NETWORK_RESULT_CODE.NOT_EXIST_DATA:
				break;
		}
	}
}
