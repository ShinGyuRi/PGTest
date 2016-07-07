package com.jy.sns.facebook;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.moyusoft.util.Debug;
import com.moyusoft.util.JYLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jooyoung on 2016-04-08.
 */
public class FacebookConnect {

	private FacebookListener listener;
	private LoginManager loginManager;
	private CallbackManager callbackManager;
	private AccessToken accessToken;
//	private ProfileTracker profileTracker;

	private Activity mActivity;
	private Fragment mFragment;

	private String facebookReferenceId = null;
	private String facebookImagePath = null;
	private String facebookName = null;
	private String facebookEmail = null;

	public FacebookConnect(FacebookListener listener, Activity activity, ProfileTracker profileTracker) {
		this.listener = listener;
		this.mActivity = activity;
		//this.profileTracker = profileTracker;
		init();
	}

	private void init() {
		callbackManager = CallbackManager.Factory.create();

//		profileTracker = new ProfileTracker() {
//			@Override
//			protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
//				JYLog.D(new Throwable());
//				if (currentProfile != null) {
//					facebookReferenceId = currentProfile.getId();
//					facebookImagePath = currentProfile.getProfilePictureUri(500, 500).toString();
//					facebookEmail = currentProfile.getName();
//					GraphRequest request = GraphRequest.newMeRequest(accessToken,
//							new GraphRequest.GraphJSONObjectCallback() {
//								@Override
//								public void onCompleted(JSONObject object, GraphResponse response) {
//									if (response != null)
//										JYLog.D(response.toString(), new Throwable());
//									try {
//										facebookEmail = object.getString("email");
//										listener.onComplete();
//									} catch (JSONException e) {
//										e.printStackTrace();
//									}
//								}
//							});
//					// fields : id, name, gender, picture(profile's image url) in public_profile
//					// fields : email in email
//					Bundle parameters = new Bundle();
//					parameters.putString("fields", "email");
//					request.setParameters(parameters);
//					request.executeAsync();
//				}
//				profileTracker.stopTracking();
//			}
//		};

	}

	public void login() {
		loginManager = LoginManager.getInstance();

		loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
//				for(Object obj : loginResult.getRecentlyGrantedPermissions().toArray()){
//					JYLog.D(obj.toString(),new Throwable());
//				}
				JYLog.D("token : \n" + loginResult.getAccessToken().getToken(), new Throwable());
				//JYLog.D("profileTracker : " + profileTracker.isTracking(), new Throwable());
				accessToken = loginResult.getAccessToken();
				Profile.fetchProfileForCurrentAccessToken();

				new ProfileTracker() {
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
												JYLog.D(facebookEmail+ facebookReferenceId+facebookName,new Throwable());
												//facebookLoginCheck();
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
						stopTracking();
						//profileTracker.stopTracking();
					}
				}.startTracking();
			}

			@Override
			public void onCancel() {
				//profileTracker.stopTracking();
				accessTokenTracker.stopTracking();
			}

			@Override
			public void onError(FacebookException exception) {
				if (exception != null)
					Debug.showDebug(exception.toString());
				//profileTracker.stopTracking();
				accessTokenTracker.stopTracking();
			}
		});
		ArrayList<String> permissions = new ArrayList<>();
		permissions.add("user_friends");
		permissions.add("public_profile");
		permissions.add("email");
		//permissions.add("publish_actions");
		loginManager.logInWithReadPermissions(mActivity, permissions);
	}

//	private ProfileTracker profileTracker = new ProfileTracker() {
//		@Override
//		protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
//			JYLog.D(new Throwable());
//			if (currentProfile != null) {
//				facebookReferenceId = currentProfile.getId();
//				facebookImagePath = currentProfile.getProfilePictureUri(500, 500).toString();
//				facebookEmail = currentProfile.getName();
//				GraphRequest request = GraphRequest.newMeRequest(accessToken,
//						new GraphRequest.GraphJSONObjectCallback() {
//							@Override
//							public void onCompleted(JSONObject object, GraphResponse response) {
//								if (response != null)
//									JYLog.D(response.toString(), new Throwable());
//								try {
//									facebookEmail = object.getString("email");
//									listener.onComplete();
//								} catch (JSONException e) {
//									e.printStackTrace();
//								}
//							}
//						});
//				// fields : id, name, gender, picture(profile's image url) in public_profile
//				// fields : email in email
//				Bundle parameters = new Bundle();
//				parameters.putString("fields", "email");
//				request.setParameters(parameters);
//				request.executeAsync();
//			}
//			profileTracker.stopTracking();
//		}
//	};

	public void requestFriends() {
		//TODO 친구리스트 테스트 중
		new GraphRequest(
				AccessToken.getCurrentAccessToken(),
				"/v2.5/me/friends",
				null,
				HttpMethod.GET,
				new GraphRequest.Callback() {
					public void onCompleted(GraphResponse response) {
						JYLog.D(response.toString(), new Throwable());
//						try {
//							JSONArray friendslist = response.getJSONObject().getJSONArray("data");
//							for (int l = 0; l < friendslist.length(); l++) {
//								JYLog.D(friendslist.getJSONObject(l).getString("name"), new Throwable());
//								JYLog.D(friendslist.getJSONObject(l).getString("email"), new Throwable());
//								JYLog.D(friendslist.getJSONObject(l).getString("id"), new Throwable());
//							}
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
					}
				}
		).executeAsync();
	}

	public void logout() {
		loginManager = LoginManager.getInstance();
		loginManager.logOut();
	}

	public CallbackManager getCallbackManager() {
		return callbackManager;
	}

	private AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
		@Override
		protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
			Profile.fetchProfileForCurrentAccessToken();
		}
	};

	public interface FacebookListener {
		void onComplete();
	}
}
