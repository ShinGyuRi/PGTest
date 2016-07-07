package kr.innisfree.playgreen.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ParentAct;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.jy.sns.instagram.InstagramApp;
import com.jy.sns.instagram.InstagramData;
import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.TalkProtocol;
import com.moyusoft.util.Debug;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.AUTH_CHANNEL;
import com.moyusoft.util.Definitions.PREFKEY;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.PrefUtil;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.data.PlayGreenEvent;
import kr.innisfree.playgreen.fragment.setting.LeaveMemberFrag;
import kr.innisfree.playgreen.sdk.KaKaoSessionCallbackListener;
import kr.innisfree.playgreen.sdk.SessionCallback;

/**
 * Created by jooyoung on 2016-04-14.
 */
public class ManageAccountAct extends ParentAct implements View.OnClickListener {

	private NetworkData myInfo;
	private PrefUtil prefUtil;

	private TextView btnFacebookAccount, btnInstagranAccount, btnKakakoAccount;
	private TextView txtFacebookState, txtInstagramState, txtKakaoState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_manage_account);
		setLoading(this);
		initToolbar();

		btnFacebookAccount = (TextView) findViewById(R.id.btn_facebook_account);
		btnInstagranAccount = (TextView) findViewById(R.id.btn_instagram_account);
		btnKakakoAccount = (TextView) findViewById(R.id.btn_kakao_account);
		txtFacebookState = (TextView) findViewById(R.id.txt_facebook_state);
		txtInstagramState = (TextView) findViewById(R.id.txt_instagram_state);
		txtKakaoState = (TextView) findViewById(R.id.txt_kakao_state);

		btnFacebookAccount.setOnClickListener(this);
		btnInstagranAccount.setOnClickListener(this);
		btnKakakoAccount.setOnClickListener(this);
		findViewById(R.id.layout_leave_member).setOnClickListener(this);

		prefUtil = PrefUtil.getInstance();
		myInfo = PlaygreenManager.getUserInfo();
		if (myInfo.isFacebookConnected) {
			btnFacebookAccount.setSelected(true);
			txtFacebookState.setText(R.string.str_connect);
		}
		if (myInfo.isInstagramConnected) {
			btnInstagranAccount.setSelected(true);
			txtInstagramState.setText(R.string.str_connect);
		}
		if (myInfo.isKakaoConnected) {
			btnKakakoAccount.setSelected(true);
			txtKakaoState.setText(R.string.str_connect);
		}

	}

	private void initToolbar() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		findViewById(R.id.layout_back).setOnClickListener(this);
		((TextView) findViewById(R.id.txt_title)).setText(R.string.str_setting_manage_account);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.layout_back:
				onBackPressed();
				break;
			case R.id.btn_facebook_account:
				facebookLogin();
				break;
			case R.id.btn_instagram_account:
				instagramLogin();
				break;
			case R.id.btn_kakao_account:
				kakaoLogin();
				break;
			case R.id.layout_leave_member:
				LeaveMemberFrag frag = LeaveMemberFrag.newInstance();
				frag.setLeaveMemberListener(new LeaveMemberFrag.OnLeaveMemberListener() {
					@Override
					public void OnLeaveMember() {
						EventBus.getDefault().post(new PlayGreenEvent(PlayGreenEvent.EVENT_TYPE.LEAVE_MEMBER));
						finish();
					}
				});
				switchContent(frag, R.id.container, true, false);
				break;
		}
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
		if (json == null || json.RESULT_CODE == null) return;
		switch (json.RESULT_CODE) {
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		hiddenKeyboard();
	}

	public void snsAccountStateChange(String whatSns, boolean isConnected) {
		int stateRes = isConnected ? R.string.str_connect : R.string.str_not_connect;
		switch (whatSns) {
			case AUTH_CHANNEL.FACEBOOK:
				btnFacebookAccount.setSelected(isConnected);
				txtFacebookState.setText(stateRes);
				prefUtil.putPreference(PREFKEY.SNS_ACCOUNT_FACEBOOK_BOOL, isConnected);
				break;
			case AUTH_CHANNEL.INSTAGRAM:
				btnInstagranAccount.setSelected(isConnected);
				txtInstagramState.setText(stateRes);
				prefUtil.putPreference(PREFKEY.SNS_ACCOUNT_INSTAGRAM_BOOL, isConnected);
				break;
			case AUTH_CHANNEL.KAKAOTALK:
				btnKakakoAccount.setSelected(isConnected);
				txtKakaoState.setText(stateRes);
				prefUtil.putPreference(PREFKEY.SNS_ACCOUNT_KAKAO_BOOL, isConnected);
				break;
		}
	}

	/**********************************************
	 * 카카오톡 로그인
	 **********************************************/
	private SessionCallback callBack;
	private KaKaoSessionCallbackListener kaKaoSessionCallbackListener = new KaKaoSessionCallbackListener() {
		@Override
		public void onSessionResult(KakaoException exception) {
			if (exception == null) {
				snsAccountStateChange(AUTH_CHANNEL.KAKAOTALK, true);
			} else {
				snsAccountStateChange(AUTH_CHANNEL.KAKAOTALK, false);
			}
		}
	};

	public void kakaoLogin() {
		if (callBack == null) {
			callBack = new SessionCallback(kaKaoSessionCallbackListener);
			Session.getCurrentSession().addCallback(callBack);
		}

		if (btnKakakoAccount.isSelected() == true) {
			Session.getCurrentSession().close();
			return;
		}

		Session.getCurrentSession().checkAndImplicitOpen();
		if (Session.getCurrentSession().isOpened()) {
		} else {
			if (TalkProtocol.existCapriLoginActivityInTalk(this, Session.getCurrentSession().isProjectLogin())) {
				Session.getCurrentSession().open(AuthType.KAKAO_TALK, this);
			} else {
				Session.getCurrentSession().open(AuthType.KAKAO_ACCOUNT, this);
			}
		}
	}

	@Override
	protected void onDestroy() {
		if (callBack != null)
			Session.getCurrentSession().removeCallback(callBack);
		super.onDestroy();
	}

	/***************************
	 * 인스타  로그인
	 ***************************************/
	private InstagramApp instagramApp;

	public void instagramLogin() {
		if (instagramApp == null) {
			instagramApp = new InstagramApp(this, InstagramData.CLIENT_ID, InstagramData.CLIENT_SECRET, InstagramData.CALLBACK_URL);
			instagramApp.setListener(instaAuthListener);
		}

		if (btnInstagranAccount.isSelected() == true) {
			instagramApp.resetAccessToken();
			snsAccountStateChange(AUTH_CHANNEL.INSTAGRAM, false);
			return;
		}
		instagramApp.authorize();
	}

	private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();

	/**
	 * 인스타 로그인 성공 시 여기로 콜백 들어온다
	 */
	private Handler instaLoginHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == InstagramApp.WHAT_FINALIZE) {
				userInfoHashmap = instagramApp.getUserInfo();
				snsAccountStateChange(AUTH_CHANNEL.INSTAGRAM, true);
			} else if (msg.what == InstagramApp.WHAT_ERROR) {
			}
			return false;
		}
	});

	private InstagramApp.OAuthAuthenticationListener instaAuthListener = new InstagramApp.OAuthAuthenticationListener() {
		@Override
		public void onSuccess() {
			instagramApp.fetchUserName(instaLoginHandler);
		}

		@Override
		public void onFail(String error) {
			Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
		}
	};

	/***************************
	 * Facebook 로그인
	 ***************************************/
	private LoginManager loginManager;
	private CallbackManager callbackManager;
	private AccessToken accessToken;
	private String facebookReferenceId = null;
	private String facebookImagePath = null;
	private String facebookName = null;
	private String facebookEmail = null;
	private ProfileTracker profileTracker;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
			return;
		}
		if (requestCode == Definitions.ACTIVITY_REQUEST_CODE.FACEBOOK_CONNECT) {
			callbackManager.onActivityResult(requestCode, resultCode, data);
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
		@Override
		protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
			Profile.fetchProfileForCurrentAccessToken();
		}
	};

	public void facebookLogin() {
		loginManager = LoginManager.getInstance();
		loginManager.logOut();
		if (btnFacebookAccount.isSelected() == true) {
			snsAccountStateChange(AUTH_CHANNEL.FACEBOOK, false);
			return;
		}
		callbackManager = CallbackManager.Factory.create();
		loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				JYLog.D("token : \n" + loginResult.getAccessToken().getToken(), new Throwable());
				JYLog.D("profileTracker : " + profileTracker.isTracking(), new Throwable());
				if (profileTracker.isTracking() == false) profileTracker.startTracking();
				accessToken = loginResult.getAccessToken();
				Profile.fetchProfileForCurrentAccessToken();
			}

			@Override
			public void onCancel() {
				JYLog.D(new Throwable());
				profileTracker.stopTracking();
				accessTokenTracker.stopTracking();
			}

			@Override
			public void onError(FacebookException exception) {
				JYLog.D(new Throwable());
				if (exception != null)
					Debug.showDebug(exception.toString());
				profileTracker.stopTracking();
				accessTokenTracker.stopTracking();
			}
		});

		ArrayList<String> permissions = new ArrayList<>();
		permissions.add("user_friends");
		permissions.add("public_profile");
		permissions.add("email");
		loginManager.logInWithReadPermissions(this, permissions);

		profileTracker = new ProfileTracker() {
			@Override
			protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
				if (currentProfile != null) {
					facebookReferenceId = currentProfile.getId();
					facebookImagePath = currentProfile.getProfilePictureUri(500, 500).toString();
					facebookName = currentProfile.getName();
					GraphRequest request = GraphRequest.newMeRequest(accessToken,
							new GraphRequest.GraphJSONObjectCallback() {
								@Override
								public void onCompleted(JSONObject object, GraphResponse response) {
									if (response != null)
										JYLog.D(response.toString(), new Throwable());
									try {
										facebookEmail = object.getString("email");
										snsAccountStateChange(AUTH_CHANNEL.FACEBOOK, true);
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							});
					Bundle parameters = new Bundle();
					parameters.putString("fields", "email");
					request.setParameters(parameters);
					request.executeAsync();
				}
				profileTracker.stopTracking();
			}
		};
	}

}
