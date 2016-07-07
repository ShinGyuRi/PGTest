package kr.innisfree.playgreen.activity.setting;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ParentAct;
import com.android.volley.VolleyError;
import com.moyusoft.util.Definitions.POLICY_TYPE;
import com.moyusoft.util.DeviceUtil;
import com.volley.network.dto.NetworkJson;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.fragment.setting.PolicyFrag;

/**
 * Created by jooyoung on 2016-04-18.
 */
public class ProgramInfoAct extends ParentAct implements View.OnClickListener {

	private TextView txtVersionInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_program_info);
		setLoading(this);
		initToolbar();

		txtVersionInfo = (TextView)findViewById(R.id.txt_version);
		txtVersionInfo.setText(getString(R.string.str_current_version_info, DeviceUtil.getVersionName(this)));

		findViewById(R.id.layout_use_policy).setOnClickListener(this);
		findViewById(R.id.layout_privacy_policy).setOnClickListener(this);
		findViewById(R.id.layout_location_policy).setOnClickListener(this);
		findViewById(R.id.layout_manage_policy).setOnClickListener(this);
		findViewById(R.id.layout_legal_notice).setOnClickListener(this);

	}

	private void initToolbar() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		findViewById(R.id.layout_back).setOnClickListener(this);
		((TextView) findViewById(R.id.txt_title)).setText(R.string.str_setting_program_info);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.layout_back:
				onBackPressed();
				break;
			case R.id.layout_use_policy:
				moveToDetail(POLICY_TYPE.USE_POLICY);
				break;
			case R.id.layout_privacy_policy:
				moveToDetail(POLICY_TYPE.PRIVACY_POLICY);
				break;
			case R.id.layout_location_policy:
				moveToDetail(POLICY_TYPE.LOCATION_POLICY);
				break;
			case R.id.layout_manage_policy:
				moveToDetail(POLICY_TYPE.MANAGE_POLICY);
				break;
			case R.id.layout_legal_notice:
				moveToDetail(POLICY_TYPE.LEGAL_NOTICE);
				break;
		}
	}

	public void moveToDetail(int type){
		PolicyFrag frag = PolicyFrag.newInstance(type);
		switchContent(frag, R.id.container, true, false);
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
	}
}
