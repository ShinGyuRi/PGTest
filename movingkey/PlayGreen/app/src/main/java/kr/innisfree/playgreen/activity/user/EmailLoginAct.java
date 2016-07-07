package kr.innisfree.playgreen.activity.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ParentAct;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.moyusoft.util.Definitions.YN;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.NetworkConstantUtil.NETWORK_RESULT_CODE;
import com.volley.network.NetworkController;
import com.volley.network.dto.NetworkJson;

import java.util.HashMap;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.fragment.JoinCommonFrag;

/**
 * Created by jooyoung on 2016-02-17.
 */
public class EmailLoginAct extends ParentAct implements View.OnClickListener, TextWatcher {

	private final static String TAG = "EmailLoginAct::";

	private Toolbar mToolbar;
	private EditText editEmail, editPW;
	private TextView txtLogin, txtFindPW, txtJoin;

	private String inputEmail, inputPW;

	public EmailLoginAct() {
	}

	public static EmailLoginAct newInstance() {
		EmailLoginAct frag = new EmailLoginAct();
		return frag;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_email_login);
		setLoading(this);
		initToolbar();

		editEmail = (EditText) findViewById(R.id.edit_email);
		editPW = (EditText) findViewById(R.id.edit_password);
		txtLogin = (TextView) findViewById(R.id.txt_login);
		txtFindPW = (TextView) findViewById(R.id.txt_find_password);
		txtJoin = (TextView) findViewById(R.id.txt_join);

		txtFindPW.setText(Html.fromHtml("<u>" + getString(R.string.str_find_password) + "</u>"));

		txtLogin.setOnClickListener(this);
		txtFindPW.setOnClickListener(this);
		txtJoin.setOnClickListener(this);
		txtLogin.setEnabled(false);
		editEmail.addTextChangedListener(this);
		editPW.addTextChangedListener(this);

	}

	private void initToolbar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		((TextView) findViewById(R.id.txt_title)).setText(R.string.str_title_email_login);
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	public void setTitle(int titleRes) {
		((TextView) findViewById(R.id.txt_title)).setText(titleRes);
	}


	@Override
	public void onClick(View v) {
		Fragment fragment = null;
		switch (v.getId()) {
			case R.id.txt_login:
				if (TextUtil.isEmailCheck(inputEmail))
					requestLogin();
				else
					Toast.makeText(this, R.string.str_toast_invalid_email_form, Toast.LENGTH_LONG).show();
				break;
			case R.id.txt_find_password:
				fragment = JoinCommonFrag.newInstance(JoinCommonFrag.FIND_PASSWORD, null);
				switchContent(fragment, R.id.container, true, false);
				break;
			case R.id.txt_join:
				fragment = JoinCommonFrag.newInstance(JoinCommonFrag.EMAIL_AUTH, null);
				switchContent(fragment, R.id.container, true, false);
				break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
			initToolbar();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		txtLogin.setEnabled(checkValidation());
	}

	public boolean checkValidation() {
		inputEmail = editEmail.getText().toString();
		inputPW = editPW.getText().toString();
		if (!TextUtil.isNull(inputEmail) && !TextUtil.isNull(inputPW) && inputPW.length() > 3) {
			return true;
		}
		return false;
	}

	public void requestLogin() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("EMAIL", editEmail.getText().toString());
		params.put("MEMB_PWD", editPW.getText().toString());
		StringRequest myReq = netUtil.requestPost(NetworkConstantUtil.APIKEY.LOGIN_EMAIL, NetworkConstantUtil.URLS.LOGIN_EMAIL, params);
		NetworkController.addToRequestQueue(myReq);

	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
			case NetworkConstantUtil.APIKEY.LOGIN_EMAIL:
				if (json.DATA != null) {
					if(!TextUtil.isNull(json.DATA.PWD_UPDATE_YN) && json.DATA.PWD_UPDATE_YN.equals(YN.YES)){
						Toast.makeText(getApplicationContext(), R.string.str_toast_change_password, Toast.LENGTH_LONG).show();
					}
					if(!TextUtil.isNull(json.DATA.TEMP_PWD_YN) && json.DATA.TEMP_PWD_YN.equals(YN.YES)){
						Toast.makeText(getApplicationContext(), R.string.str_toast_change_password_at_mypage, Toast.LENGTH_LONG).show();
					}
					PlaygreenManager.saveUserInfo(json.DATA);
					setResult(RESULT_OK);
					finish();
				}
				break;
		}
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
		switch (idx) {
			case NetworkConstantUtil.APIKEY.LOGIN_EMAIL:
				JYLog.D(TAG + "login is fail.", null);
				if(json ==null || json.RESULT_CODE == null)	return;
				int resultCode = json.RESULT_CODE;
				if (resultCode == NETWORK_RESULT_CODE.NOT_EXIST_EMAIL) {
					// 이메일 정보 없음
					Toast.makeText(this, R.string.str_toast_login_email_error, Toast.LENGTH_SHORT).show();
				} else if (resultCode == NETWORK_RESULT_CODE.NOT_MATCHED_PASSWORD) {
					// 암호 틀림
					Toast.makeText(this, R.string.str_toast_login_pw_error, Toast.LENGTH_SHORT).show();
				} else if (resultCode == NETWORK_RESULT_CODE.LEAVE_USER) {
					// 탈퇴한 회원
					Toast.makeText(this, R.string.str_toast_login_already_drop_out_error, Toast.LENGTH_SHORT).show();
				} else if (resultCode == NETWORK_RESULT_CODE.NOT_AUTHORIZED) {
					// 조회 권한 없음
					Toast.makeText(this, R.string.str_toast_login_not_have_right_error, Toast.LENGTH_SHORT).show();
				} else if (resultCode == NETWORK_RESULT_CODE.EXPIRE_ACCESS_TOKEN) {
					// 305, 인증 기간 만료
					Toast.makeText(this, R.string.str_toast_login_expire_token_error, Toast.LENGTH_SHORT).show();
				} else if (resultCode == NETWORK_RESULT_CODE.NOT_EXIST_MEMBER) {
					// 존재하지 않는 회원
					Toast.makeText(this, R.string.str_toast_login_not_exist_member, Toast.LENGTH_SHORT).show();
				} else if(resultCode ==NETWORK_RESULT_CODE.NOT_MATCHED_PASSWORD_WAIT_30MIN){
					// 비번 5회 오류 후 30분경과되지않음
					Toast.makeText(this, R.string.str_toast_login_not_matched_password_wait_30min, Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}
}
