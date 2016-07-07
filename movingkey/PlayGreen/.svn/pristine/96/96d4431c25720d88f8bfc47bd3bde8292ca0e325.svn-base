package kr.innisfree.playgreen.activity.user;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ParentAct;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
import com.google.gson.Gson;
import com.jy.sns.instagram.InstagramApp;
import com.jy.sns.instagram.InstagramData;
import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.TalkProtocol;
import com.moyusoft.util.Debug;
import com.moyusoft.util.Definitions.ACTIVITY_REQUEST_CODE;
import com.moyusoft.util.Definitions.AUTH_CHANNEL;
import com.moyusoft.util.Definitions.INTENT_KEY;
import com.moyusoft.util.Definitions.YN;
import com.moyusoft.util.DeviceUtil;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;
import com.moyusoft.util.Util;
import com.navercorp.volleyextensions.volleyer.Volleyer;
import com.navercorp.volleyextensions.volleyer.builder.PostBuilder;
import com.navercorp.volleyextensions.volleyer.factory.DefaultRequestQueueFactory;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.NetworkErrorUtill;
import com.volley.network.dto.NetworkJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.sdk.KaKaoSessionCallbackListener;
import kr.innisfree.playgreen.sdk.SessionCallback;

/**
 * Created by jooyoung on 2016-02-16.
 */
public class LoginAct extends ParentAct implements View.OnClickListener, KaKaoSessionCallbackListener {

	private final static String TAG = "LoginAct::";

	private LinearLayout layoutBg;
	private LinearLayout layoutFacebook, layoutInsta, layoutKakao, layoutEmail, layoutWechat, layoutLine;
	private TextView txtLoginText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_login);
		setLoading(this);

		layoutBg = (LinearLayout) findViewById(R.id.layout_login);
		if (Build.VERSION.SDK_INT >= 21) {
			layoutBg.setPadding(0, DeviceUtil.getStatusBarHeight(this), 0, 0);
		}
		txtLoginText = (TextView) findViewById(R.id.txt_login_text);
		layoutFacebook = (LinearLayout) findViewById(R.id.layout_facebook);
		layoutInsta = (LinearLayout) findViewById(R.id.layout_instagram);
		layoutKakao = (LinearLayout) findViewById(R.id.layout_kakaotalk);
		layoutEmail = (LinearLayout) findViewById(R.id.layout_email);
		layoutWechat = (LinearLayout)findViewById(R.id.layout_wechat);
		layoutLine = (LinearLayout)findViewById(R.id.layout_line);

		layoutFacebook.setOnClickListener(this);
		layoutInsta.setOnClickListener(this);
		layoutKakao.setOnClickListener(this);
		layoutEmail.setOnClickListener(this);
		layoutWechat.setOnClickListener(this);
		layoutLine.setOnClickListener(this);

		languageCheck();
		setSpan();
	}

	public void languageCheck(){
		String currentLanguage = Locale.getDefault().getLanguage();
		JYLog.D(currentLanguage, new Throwable());
		if(TextUtils.isEmpty(currentLanguage))	return;
		if(currentLanguage.equals(Locale.CHINA.getLanguage())
				|| currentLanguage.equals(Locale.CHINESE.getLanguage())
				|| currentLanguage.equals(Locale.SIMPLIFIED_CHINESE.getLanguage())
				|| currentLanguage.equals(Locale.TRADITIONAL_CHINESE.getLanguage())
				|| currentLanguage.equals(Locale.ENGLISH.getLanguage())){
			layoutWechat.setVisibility(View.VISIBLE);
			layoutLine.setVisibility(View.VISIBLE);
			layoutKakao.setVisibility(View.GONE);
		}else{
			layoutWechat.setVisibility(View.GONE);
			layoutLine.setVisibility(View.GONE);
			layoutKakao.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.layout_facebook:
				facebookLogin();
				break;
			case R.id.layout_instagram:
				instagramLogin();
				break;
			case R.id.layout_kakaotalk:
				kakaoLogin();
				break;
			case R.id.layout_email:
				Intent intent = new Intent(this, EmailLoginAct.class);
				Util.moveActivity(this, intent, -1, -1, false, true, ACTIVITY_REQUEST_CODE.EMAIL_LOGIN);
				break;
			case R.id.layout_wechat:
				//TODO 위챗연결
				break;
			case R.id.layout_line:
				//TODO 라인연결
				break;
		}
	}

	public void gotoFriendRecommendAct() {
		Intent i = new Intent(this, FriendAddAct.class);
		i.putExtra(INTENT_KEY.FROM_LOGIN_BOOL, true);
		Util.moveActivity(this, i, -1, -1, true, false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
			return;
		} else {
			switch (requestCode) {
				case ACTIVITY_REQUEST_CODE.EMAIL_LOGIN:
					if (resultCode == RESULT_OK) {
						gotoFriendRecommendAct();
					}
					break;
				case ACTIVITY_REQUEST_CODE.FACEBOOK_CONNECT:
					if (resultCode == RESULT_OK) {
						//if(facebookConnect!=null && facebookConnect.getCallbackManager()!=null){
						//	facebookConnect.getCallbackManager().onActivityResult(requestCode, resultCode, data);
						//}
						callbackManager.onActivityResult(requestCode, resultCode, data);
					}
					break;
				case ACTIVITY_REQUEST_CODE.SNS_ACCOUNT_INPUT:
					if (resultCode == RESULT_OK) {
						gotoFriendRecommendAct();
					}
					break;
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void setSpan() {
		SpannableString span = new SpannableString(txtLoginText.getText().toString());
		span.setSpan(new StyleSpan(Typeface.BOLD), 5, 16, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		txtLoginText.setText(span);
	}


	public void requestSNSLogin(String snsId, final String nickname, final String profilePath, final String email, final String authChannel) {
		showLoading();
		final RequestQueue q = DefaultRequestQueueFactory.create(this);
		q.start();
		PostBuilder pb = Volleyer.volleyer(q).post(NetworkConstantUtil.URLS.REGIST_MEMBER)
				.addStringPart("LOCATION", PlaygreenManager.getLocation())
				.addStringPart("LANGUAGE", Locale.getDefault().getLanguage())
				.addStringPart("OS", "Android OS " + Build.VERSION.SDK_INT)
						//.addStringPart("PUSH_KEY", "UNKNOWN")
				.addStringPart("MOBILE", Build.MODEL)
				.addStringPart("AUTH_CHANNEL", authChannel);

		if (!TextUtil.isNull(profilePath))
			pb.addStringPart("MEMB_IMG_LINK", profilePath);
		if (!TextUtil.isNull(email))
			pb.addStringPart("EMAIL", email);
		if (!TextUtil.isNull(nickname))
			pb.addStringPart("MEMB_NAME", nickname);
		if (!TextUtil.isNull(snsId))
			pb.addStringPart("SNS_ID", snsId);

		pb.withListener(new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JYLog.D("NETWORK", response.trim(), new Throwable());
				hideLoading();
				Gson gson = new Gson();
				NetworkJson networkJson = gson.fromJson(response, NetworkJson.class);

				if (NetworkErrorUtill.isJsonResultCheck(networkJson)) {
					PlaygreenManager.saveUserInfo(networkJson.DATA);

					/** SNS 로그인 후 계정정보 입력 화면으로 분기*/
					if (!TextUtil.isNull(networkJson.DATA.MEMB_INFO_REGIST_YN) && networkJson.DATA.MEMB_INFO_REGIST_YN.equals(YN.YES)) {
						Intent intent = new Intent(LoginAct.this, SNSAccountInputAct.class);
						intent.putExtra(INTENT_KEY.IMAGE_URL, profilePath);
						intent.putExtra(INTENT_KEY.NICKNAME, nickname);
						intent.putExtra(INTENT_KEY.EMAIL, email);
						Util.moveActivity(LoginAct.this, intent, -1, -1, false, false, ACTIVITY_REQUEST_CODE.SNS_ACCOUNT_INPUT);
					} else {
						gotoFriendRecommendAct();
					}
				} else {
				}
				q.stop();
			}
		}).withErrorListener(new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				JYLog.D("NETWORK", arg0.getMessage(), new Throwable());
				hideLoading();
				q.stop();
			}
		}).execute();
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
	}

	@Override
	protected void onDestroy() {
		if (callBack != null)
			Session.getCurrentSession().removeCallback(callBack);
		super.onDestroy();
	}

	/**********************************************
	 * 카카오톡 로그인
	 **********************************************/
	private SessionCallback callBack;
	private UserProfile kakaoUserProfile;

	@Override
	public void onSessionResult(KakaoException exception) {
		if (exception == null) {
			requestMe();
		} else {
		}
	}

	public void kakaoLogin() {
		if (kakaoUserProfile != null) {
			kakaoLoginCheck(kakaoUserProfile);
			return;
		}

		callBack = new SessionCallback(this);
		Session.getCurrentSession().addCallback(callBack);
		Session.getCurrentSession().checkAndImplicitOpen();

		if (Session.getCurrentSession().isOpened()) {
			requestMe();
		} else {
			if (TalkProtocol.existCapriLoginActivityInTalk(this, Session.getCurrentSession().isProjectLogin())) {
				Session.getCurrentSession().open(AuthType.KAKAO_TALK, this);
			} else {
				Session.getCurrentSession().open(AuthType.KAKAO_ACCOUNT, this);
			}
		}
	}


	public void kakaoLoginCheck(UserProfile userProfile) {
		if (userProfile == null) {
			return;
		}
		requestSNSLogin(userProfile.getId() + "", userProfile.getNickname(), userProfile.getProfileImagePath(), null, AUTH_CHANNEL.KAKAOTALK);
	}

	/**
	 * 카카오톡 프로필 조회
	 */
	protected void requestMe() {
		UserManagement.requestMe(new MeResponseCallback() {
			@Override
			public void onFailure(ErrorResult errorResult) {
				JYLog.D(new Throwable());
			}

			@Override
			public void onSessionClosed(ErrorResult errorResult) {
				JYLog.D(new Throwable());
			}

			@Override
			public void onSuccess(UserProfile userProfile) {
				JYLog.D(new Throwable());
				//null체크 중요!!!!!! -> null체크 안하면 onSuccess 호출이 여러번 들어오기때문에 회원가입화면이 복수개로 뜬다
				if (kakaoUserProfile == null) {
					kakaoUserProfile = userProfile;
					JYLog.D(TAG + "ID:" + userProfile.getId(), null);
					JYLog.D(TAG + "NICKNAME:" + userProfile.getNickname(), null);
					JYLog.D(TAG + "PROFILE IMAGE PATH:" + userProfile.getProfileImagePath(), null);
					kakaoLoginCheck(userProfile);
				}
			}

			@Override
			public void onNotSignedUp() {
			}
		});
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
				JYLog.D(userInfoHashmap.get(InstagramApp.TAG_ID), new Throwable());
				JYLog.D(userInfoHashmap.get(InstagramApp.TAG_USERNAME), new Throwable());
				JYLog.D(userInfoHashmap.get(InstagramApp.TAG_PROFILE_PICTURE), new Throwable());

				requestSNSLogin(userInfoHashmap.get(InstagramApp.TAG_ID), userInfoHashmap.get(InstagramApp.TAG_USERNAME),
						userInfoHashmap.get(InstagramApp.TAG_PROFILE_PICTURE), null, AUTH_CHANNEL.INSTAGRAM);

			} else if (msg.what == InstagramApp.WHAT_ERROR) {
				Toast.makeText(LoginAct.this, "Check your network.", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(LoginAct.this, error, Toast.LENGTH_SHORT).show();
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

	private AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
		@Override
		protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
			JYLog.D(new Throwable());
			Profile.fetchProfileForCurrentAccessToken();
		}
	};

	public void facebookLogin() {
		loginManager = LoginManager.getInstance();
		loginManager.logOut();
		callbackManager = CallbackManager.Factory.create();
		loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
//				for(Object obj : loginResult.getRecentlyGrantedPermissions().toArray()){
//					JYLog.D(obj.toString(),new Throwable());
//				}
				JYLog.D("token : \n" + loginResult.getAccessToken().getToken(), new Throwable());
				JYLog.D("profileTracker : " + profileTracker.isTracking(), new Throwable());
				if (profileTracker.isTracking() == false) profileTracker.startTracking();
				accessToken = loginResult.getAccessToken();
				Profile.fetchProfileForCurrentAccessToken();
			}

			@Override
			public void onCancel() {
				profileTracker.stopTracking();
				accessTokenTracker.stopTracking();
			}

			@Override
			public void onError(FacebookException exception) {
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
		//permissions.add("publish_actions");
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
										facebookLoginCheck();
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							});
					// fields : id, name, gender, picture(profile's image url) in public_profile
					// fields : email in email
					Bundle parameters = new Bundle();
					parameters.putString("fields", "email");
					request.setParameters(parameters);
					request.executeAsync();
				}
				profileTracker.stopTracking();
			}
		};
	}

	public void facebookLoginCheck() {
		JYLog.D(facebookEmail + facebookReferenceId + facebookName + facebookImagePath, new Throwable());
		requestSNSLogin(facebookReferenceId, facebookName, facebookImagePath, facebookEmail, AUTH_CHANNEL.FACEBOOK);
	}
}
