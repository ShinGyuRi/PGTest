package kr.innisfree.playgreen.activity.setting;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ParentAct;
import com.android.volley.VolleyError;
import com.moyusoft.util.TextUtil;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.NetworkConstantUtil.NETWORK_RESULT_CODE;
import com.volley.network.dto.NetworkJson;

import kr.innisfree.playgreen.R;

/**
 * Created by jooyoung on 2016-04-14.
 */
public class PasswordChangeAct extends ParentAct implements View.OnClickListener {

	private EditText editCurrentPW, editNewPW, editNewPWConfirm;
	private TextView txtPasswordWarning;

	private String curPW, newPW;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_password_change);
		setLoading(this);
		initToolbar();

		editCurrentPW = (EditText)findViewById(R.id.edit_current_pw);
		editNewPW = (EditText)findViewById(R.id.edit_new_pw);
		editNewPWConfirm = (EditText)findViewById(R.id.edit_new_pw_confirm);
		txtPasswordWarning = (TextView)findViewById(R.id.txt_pw_warning);

	}

	private void initToolbar() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		findViewById(R.id.layout_back).setOnClickListener(this);
		findViewById(R.id.txt_confirm).setOnClickListener(this);
		((TextView) findViewById(R.id.txt_title)).setText(R.string.str_setting_change_pw);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.layout_back:
				onBackPressed();
				break;
			case R.id.txt_confirm:
				if(checkValidation()){
					netFunc.requestChangePW(curPW,newPW);
				}
				break;
		}
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch(idx){
			case APIKEY.CHANGE_PW:
				finish();
				break;
		}
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
		if(json==null || json.RESULT_CODE == null)	return;
		switch (json.RESULT_CODE){
			case NETWORK_RESULT_CODE.NOT_MATCHED_PASSWORD:
				txtPasswordWarning.setText(R.string.str_warning_current_pw_invalid);
				txtPasswordWarning.setVisibility(View.VISIBLE);
				break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		hiddenKeyboard();
	}

	public boolean checkValidation(){
		txtPasswordWarning.setVisibility(View.GONE);
		curPW = editCurrentPW.getText().toString();
		newPW = editNewPW.getText().toString();
		String newPWConfirm = editNewPWConfirm.getText().toString();

		if(TextUtil.isNull(curPW)){
			return false;
		}
		if(TextUtil.isNull(newPW)){
			return false;
		}
		if(TextUtil.isNull(newPWConfirm)){
			return false;
		}
		/** 6자 미만일 때, **/
		if(newPW.length() < 6){
			txtPasswordWarning.setText(R.string.str_warning_new_pw_min_8);
			txtPasswordWarning.setVisibility(View.VISIBLE);
			return false;
		}
		/** 16자 초과일 때, **/
		if(newPW.length() > 16){
			txtPasswordWarning.setText(R.string.str_warning_new_pw_max_16);
			txtPasswordWarning.setVisibility(View.VISIBLE);
			return false;
		}
		/** 비밀번호 확인해서 아닐 때, **/
		if(!newPW.equals(newPWConfirm)){
			txtPasswordWarning.setText(R.string.str_warning_new_pw_not_matched);
			txtPasswordWarning.setVisibility(View.VISIBLE);
			return false;
		}
		/** 공백 포함일 때, **/
		if (newPW.contains(" ")) {
			txtPasswordWarning.setText(R.string.str_warning_pw_rule_01);
			txtPasswordWarning.setVisibility(View.VISIBLE);
			//Toast.makeText(this, R.string.str_warning_pw_rule_01, Toast.LENGTH_SHORT).show();
			return false;
		}
		/** 영어 대문자, 소문자, 숫자 그리고 특수문자 중 2가지의 조합이 아닐 때, **/
		if(TextUtil.isPassworkdCheck(newPW) == false){
			txtPasswordWarning.setText(R.string.str_warning_pw_rule_02);
			txtPasswordWarning.setVisibility(View.VISIBLE);
			//Toast.makeText(this, R.string.str_warning_pw_rule_02, Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}


}
