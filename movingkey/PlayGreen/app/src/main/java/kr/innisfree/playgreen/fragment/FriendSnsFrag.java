package kr.innisfree.playgreen.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ParentFrag;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.NetworkController;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.adapter.FriendAdapter;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-02-19.
 * SNS친구, 주소록 친구
 */
public class FriendSnsFrag extends ParentFrag implements View.OnClickListener {

	private int recommendType;

	private LinearLayout layoutFriendList;
	private LinearLayout layoutConnect;

	private RecyclerView recyclerView;
	private FriendAdapter friendAdapter;
	private TextView txtConnectMessage, txtConnect;
	private TextView txtFriendCount, txtFollowAll;

	private ArrayList<NetworkData> facebookFriendArray;
	private ArrayList<NetworkData> netResultDataArray;
	private NetworkData selectFriend;

	public FriendSnsFrag() {
	}


	@SuppressLint("ValidFragment")
	public FriendSnsFrag(int position) {
		this.recommendType = position;
	}

	public static FriendSnsFrag newInstance() {
		FriendSnsFrag frag = new FriendSnsFrag();
		return frag;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_friend_mine, null);
		setCutOffBackgroundTouch(view);

		layoutFriendList = (LinearLayout) view.findViewById(R.id.layout_friend_list);
		layoutConnect = (LinearLayout) view.findViewById(R.id.layout_connect);

		txtConnectMessage = (TextView) view.findViewById(R.id.txt_find_friend_message);
		txtConnect = (TextView) view.findViewById(R.id.txt_connect);
		txtFriendCount = (TextView) view.findViewById(R.id.txt_friend_count);
		txtFollowAll = (TextView) view.findViewById(R.id.txt_follow_all);

		txtConnectMessage.setText(R.string.str_find_facebook_friend);
		txtConnect.setText(R.string.str_connect_facebook);

		txtConnect.setOnClickListener(this);
		txtFollowAll.setOnClickListener(this);

		if (netResultDataArray == null || netResultDataArray.size() == 0) {
			netResultDataArray = new ArrayList<NetworkData>();
			layoutConnect.setVisibility(View.VISIBLE);
		} else {
			layoutConnect.setVisibility(View.GONE);
			txtFriendCount.setText(getString(R.string.str_friend_count, netResultDataArray.size()));
		}

		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		if (friendAdapter == null) {
			friendAdapter = new FriendAdapter(getActivity());
			recyclerView.setAdapter(friendAdapter);
		} else {
			recyclerView.swapAdapter(friendAdapter, false);
		}

		friendAdapter.setAdapterItemClickListener(new AdapterItemClickListener() {
			@Override
			public void onAdapterItemClick(View view, int position) {
				selectFriend = friendAdapter.getItem(position);
				if (selectFriend == null) return;
				switch (view.getId()) {
					case R.id.txt_follow_item:
						requestFollow(selectFriend.MEMB_ID, selectFriend.FRIEND_YN);
						break;
				}
			}
		});

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.txt_connect:
				getFacebookFriend();
				break;
			case R.id.txt_follow_all:
				requestFollowAll();
				break;
		}
	}

	private LoginManager loginManager;
	private CallbackManager callbackManager;

	public void getFacebookFriend() {
		loginManager = LoginManager.getInstance();
		loginManager.logOut();
		callbackManager = CallbackManager.Factory.create();
		loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult login_result) {
				JYLog.D(new Throwable());
				new GraphRequest(
						AccessToken.getCurrentAccessToken(),
						"/me/friends",
						//"/"+referenceId+"/friendlists",
						null,
						HttpMethod.GET,
						new GraphRequest.Callback() {
							public void onCompleted(GraphResponse response) {
								JYLog.D(response.toString(), new Throwable());
								facebookFriendArray = new ArrayList<NetworkData>();
								try {
									JSONArray friendslist = response.getJSONObject().getJSONArray("data");
									JYLog.D(friendslist.length() + "", new Throwable());
									for (int l = 0; l < friendslist.length(); l++) {
										NetworkData fbFriend = new NetworkData();
										fbFriend.MEMB_NAME = friendslist.getJSONObject(l).getString("name");
										fbFriend.SNS_ID = friendslist.getJSONObject(l).getString("id");
										facebookFriendArray.add(fbFriend);
										JYLog.D(fbFriend.MEMB_NAME + "," + fbFriend.SNS_ID, new Throwable());
									}
									requestFriend();
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}
				).executeAsync();
			}

			@Override
			public void onCancel() {
				JYLog.D(new Throwable());
			}

			@Override
			public void onError(FacebookException exception) {
				if (AccessToken.getCurrentAccessToken() != null) {
					LoginManager.getInstance().logOut();
				}
				JYLog.D(exception.toString(), new Throwable());
			}
		});

		ArrayList<String> permissions = new ArrayList<>();
		permissions.add("user_friends");
		permissions.add("public_profile");
		permissions.add("email");
		//permissions.add("publish_actions");
		loginManager.logInWithReadPermissions(this, permissions);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case Definitions.ACTIVITY_REQUEST_CODE.FACEBOOK_CONNECT:
				if (resultCode == -1) {
					JYLog.D(new Throwable());
					//if(facebookConnect!=null && facebookConnect.getCallbackManager()!=null){
					//	facebookConnect.getCallbackManager().onActivityResult(requestCode, resultCode, data);
					//}
					callbackManager.onActivityResult(requestCode, resultCode, data);
				}
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void requestFriend() {
		JSONArray jsonArray = new JSONArray();
		try {
			if (facebookFriendArray != null) {
				for (NetworkData data : facebookFriendArray) {
					JSONObject object = new JSONObject();
					//object.put("DEPT", "FA");
					if (!TextUtil.isNull(data.MEMB_NAME))
						object.put("MEMB_NAME", data.MEMB_NAME);
					if (!TextUtil.isNull(data.SNS_ID))
						object.put("SNS_ID", data.SNS_ID);
					jsonArray.put(object);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		//JYLog.longLog(jsonArray.toString());
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
		params.put("FRIEND_CATEGORY",  "S");
		params.put("LIST", jsonArray.toString());

		StringRequest myReq = netUtil.requestPost(NetworkConstantUtil.APIKEY.FRIEND_RECOMMEND, NetworkConstantUtil.URLS.FRIEND_RECOMMEND, params);
		NetworkController.addToRequestQueue(myReq);
	}

	public void requestFollow(String membId, String followYN) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
		params.put("MEMB_ID", membId);

		StringRequest myReq;
		if (!TextUtil.isNull(followYN) && followYN.equals(Definitions.YN.YES))
			myReq = netUtil.requestPost(NetworkConstantUtil.APIKEY.FRIEND_UNFOLLOW, NetworkConstantUtil.URLS.FRIEND_UNFOLLOW, params);
		else
			myReq = netUtil.requestPost(NetworkConstantUtil.APIKEY.FRIEND_FOLLOW, NetworkConstantUtil.URLS.FRIEND_FOLLOW, params);
		NetworkController.addToRequestQueue(myReq);
	}

	public void requestFollowAll() {
		JSONArray jsonArray = new JSONArray();
		try {
			for (NetworkData data : netResultDataArray) {
				JSONObject object = new JSONObject();
				if (!TextUtil.isNull(data.MEMB_ID))
					object.put("MEMB_ID", data.MEMB_ID);
				jsonArray.put(object);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
		params.put("LIST", jsonArray.toString());
		StringRequest myReq = netUtil.requestPost(NetworkConstantUtil.APIKEY.FRIEND_FOLLOW_ALL, NetworkConstantUtil.URLS.FRIEND_FOLLOW_ALL, params);
		NetworkController.addToRequestQueue(myReq);
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
			case NetworkConstantUtil.APIKEY.FRIEND_RECOMMEND:
				int count = 0;
				if (json.LIST != null && json.LIST.size() > 0) {
					netResultDataArray = json.LIST;
					count = netResultDataArray.size();
					layoutConnect.setVisibility(View.GONE);
					friendAdapter.setItemArray(json.LIST);
				}else{
					layoutConnect.setVisibility(View.VISIBLE);
					txtConnectMessage.setText(R.string.str_not_exist_facebook_friend);
					txtConnect.setVisibility(View.INVISIBLE);
				}
				txtFriendCount.setText(getString(R.string.str_friend_count, count));
				break;
			case NetworkConstantUtil.APIKEY.FRIEND_FOLLOW_ALL:
				for (NetworkData data : netResultDataArray) {
					data.FRIEND_YN = Definitions.YN.YES;
				}
				friendAdapter.notifyDataSetChanged();
				break;
			case NetworkConstantUtil.APIKEY.FRIEND_FOLLOW:
				if (selectFriend != null) {
					selectFriend.FRIEND_YN = Definitions.YN.YES;
					friendAdapter.notifyDataSetChanged();
				}
				break;
			case NetworkConstantUtil.APIKEY.FRIEND_UNFOLLOW:
				if (selectFriend != null) {
					selectFriend.FRIEND_YN = Definitions.YN.NO;
					friendAdapter.notifyDataSetChanged();
				}
				break;
		}
	}
}
