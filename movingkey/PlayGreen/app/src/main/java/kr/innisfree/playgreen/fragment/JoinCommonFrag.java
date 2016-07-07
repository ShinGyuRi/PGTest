package kr.innisfree.playgreen.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ParentAct;
import com.ParentFrag;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.moyusoft.util.TextUtil;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.NetworkConstantUtil.NETWORK_RESULT_CODE;
import com.volley.network.NetworkConstantUtil.URLS;
import com.volley.network.NetworkController;
import com.volley.network.dto.NetworkJson;

import java.util.HashMap;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.user.EmailLoginAct;

/**
 * Created by jooyoung on 2016-02-18.
 */
public class JoinCommonFrag extends ParentFrag implements View.OnClickListener, TextWatcher {

	public static final int FIND_PASSWORD = 1;
	public static final int EMAIL_AUTH = 2;
	public static final int EMAIL_AUTH_CHECK = 3;

	private int type;
	private String emailAddr;

	private TextView txtDescription, txtConfirm, txtResend;
	private EditText editInput;
	private String userInput;

	public JoinCommonFrag() {
	}

	@SuppressLint("ValidFragment")
	public JoinCommonFrag(int type, String emailAddr) {
		this.type = type;
		this.emailAddr = emailAddr;
	}

	public static JoinCommonFrag newInstance(int type, String emailAddr) {
		JoinCommonFrag frag = new JoinCommonFrag(type, emailAddr);
		return frag;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_join_common, null);
		setCutOffBackgroundTouch(view);

		txtDescription = (TextView) view.findViewById(R.id.txt_description);
		txtConfirm = (TextView) view.findViewById(R.id.txt_confirm);
		txtConfirm.setEnabled(false);
		txtConfirm.setOnClickListener(this);
		editInput = (EditText) view.findViewById(R.id.edit_input);
		editInput.addTextChangedListener(this);
		txtResend = (TextView) view.findViewById(R.id.txt_resend);
		txtResend.setOnClickListener(this);
		txtResend.setText(Html.fromHtml("<u>" + getString(R.string.str_resend_code) + "</u>"));
		txtResend.setVisibility(View.GONE);

		initialize();
		return view;
	}

	public void initialize() {
		switch (type) {
			case EMAIL_AUTH:
				((EmailLoginAct) getActivity()).setTitle(R.string.str_title_email_certification);
				txtDescription.setText(R.string.str_email_certification_description);
				editInput.setHint(R.string.str_email);
				break;
			case EMAIL_AUTH_CHECK:
				((EmailLoginAct) getActivity()).setTitle(R.string.str_title_input_certification);
				txtDescription.setText(getString(R.string.str_email_certification_code_description, emailAddr));
				editInput.setInputType(InputType.TYPE_CLASS_TEXT);
				editInput.setHint(R.string.str_pin_code);
				txtResend.setVisibility(View.VISIBLE);
				break;
			case FIND_PASSWORD:
				((EmailLoginAct) getActivity()).setTitle(R.string.str_title_find_password);
				txtDescription.setText(R.string.str_find_password_description);
				editInput.setHint(R.string.str_email);
				break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.txt_confirm:
				switch (type) {
					case EMAIL_AUTH:
					case FIND_PASSWORD:
						if (TextUtil.isEmailCheck(userInput)) {
							requestEmailSend(userInput);
						} else {
							Toast.makeText(getActivity(), R.string.str_toast_invalid_email_form, Toast.LENGTH_LONG).show();
						}
						break;
					case EMAIL_AUTH_CHECK:
						requestAuthCheck();
						break;
				}
				break;
			case R.id.txt_resend:
				requestEmailSend(emailAddr);
				break;
		}
	}


	public void requestEmailSend(String emailAddr) {
		int apikey;
		String url;
		if (type == FIND_PASSWORD) {
			apikey = APIKEY.FIND_PW;
			url = URLS.FIND_PW;
		} else {
			apikey = APIKEY.EMAIL_AUTH;
			url = URLS.EMAIL_AUTH;
		}
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("EMAIL", emailAddr);
		StringRequest myReq = netUtil.requestPost(apikey, url, params);
		NetworkController.addToRequestQueue(myReq);
	}

	public void requestAuthCheck() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("EMAIL", emailAddr);
		params.put("AUTH_CODE", userInput);
		StringRequest myReq = netUtil.requestPost(APIKEY.EMAIL_AUTH_CHECK, URLS.EMAIL_AUTH_CHECK, params);
		NetworkController.addToRequestQueue(myReq);
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
			case APIKEY.EMAIL_AUTH:
				if (type == EMAIL_AUTH_CHECK) {
					//코드 재전송
					Toast.makeText(getActivity(), getString(R.string.str_toast_resend_authcode, emailAddr), Toast.LENGTH_SHORT).show();
					return;
				}
				if (json.DATA != null) {
					Fragment fragment = JoinCommonFrag.newInstance(EMAIL_AUTH_CHECK, json.DATA.EMAIL);
					((ParentAct) getActivity()).switchContent(fragment, R.id.container, true, false);
				}
				break;
			case APIKEY.FIND_PW:
				if (json.DATA != null)
					Toast.makeText(getActivity(), getString(R.string.str_toast_send_password_resetting_link), Toast.LENGTH_SHORT).show();
				break;
			case APIKEY.EMAIL_AUTH_CHECK:
				if (json.DATA != null) {
					Fragment fragment = JoinFrag.newInstance(json.DATA.EMAIL);
					((ParentAct) getActivity()).switchContent(fragment, R.id.container, true, false);
				}
				break;
		}
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
		if (json == null) return;
		switch (json.RESULT_CODE) {
			case NETWORK_RESULT_CODE.EMAIL_ADDR_DUPLICATED:
				Toast.makeText(getContext(), R.string.str_toast_email_addr_duplicated, Toast.LENGTH_LONG).show();
				break;
			case NETWORK_RESULT_CODE.NOT_EXIST_EMAIL:
				// 이메일 정보 없음
				Toast.makeText(getContext(), R.string.str_toast_login_email_error, Toast.LENGTH_SHORT).show();
				break;
			case NETWORK_RESULT_CODE.NOT_MATCHED_PASSWORD:
				// 암호 틀림
				Toast.makeText(getContext(), R.string.str_toast_login_pw_error, Toast.LENGTH_SHORT).show();
				break;
			case NETWORK_RESULT_CODE.LEAVE_USER:
				// 탈퇴한 회원
				Toast.makeText(getContext(), R.string.str_toast_login_already_drop_out_error, Toast.LENGTH_SHORT).show();
				break;
			case NETWORK_RESULT_CODE.NOT_AUTHORIZED:
				// 조회 권한 없음
				Toast.makeText(getContext(), R.string.str_toast_login_not_have_right_error, Toast.LENGTH_SHORT).show();
				break;
			case NETWORK_RESULT_CODE.EXPIRE_ACCESS_TOKEN:
				// 305, 인증 기간 만료
				Toast.makeText(getContext(), R.string.str_toast_login_expire_token_error, Toast.LENGTH_SHORT).show();
				break;
			case NETWORK_RESULT_CODE.NOT_EXIST_MEMBER:
				// 존재하지 않는 회원
				Toast.makeText(getContext(), R.string.str_toast_login_not_exist_member, Toast.LENGTH_SHORT).show();
				break;
			case NETWORK_RESULT_CODE.NOT_MATCHED_PASSWORD_WAIT_30MIN:
				// 비번 5회 오류 후 30분경과되지않음
				Toast.makeText(getContext(), R.string.str_toast_login_not_matched_password_wait_30min, Toast.LENGTH_SHORT).show();
				break;
		}
		switch (idx) {
			case APIKEY.EMAIL_AUTH_CHECK:
				//TODO 이메일 인증코드 틀린경우
				if (json.RESULT_CODE == NETWORK_RESULT_CODE.AUTH_CODE_NOT_MATCHED) {
					Toast.makeText(getContext(), R.string.str_toast_auth_code_not_matched, Toast.LENGTH_LONG).show();
					editInput.setText("");
				}
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
		txtConfirm.setEnabled(checkValidation());
	}

	public boolean checkValidation() {
		userInput = editInput.getText().toString();
		if (!TextUtil.isNull(userInput)) {
			return true;
		}
		return false;
	}

	@Override
	public void onPause() {
		super.onPause();
		hiddenKeyboard();
	}
}
