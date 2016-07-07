package kr.innisfree.playgreen.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ParentAct;
import com.ParentFrag;
import com.android.volley.toolbox.StringRequest;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.ACTIVITY_REQUEST_CODE;
import com.moyusoft.util.TextUtil;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.NetworkConstantUtil.URLS;
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
public class FriendContactFrag extends ParentFrag implements View.OnClickListener {

	private LinearLayout layoutFriendList;
	private LinearLayout layoutConnect;

	private RecyclerView recyclerView;
	private FriendAdapter friendAdapter;
	private TextView txtConnectMessage, txtConnect;
	private TextView txtFriendCount, txtFollowAll;

	private ArrayList<NetworkData> contactFriendArray;
	private ArrayList<NetworkData> netResultDataArray;
	private ProgressDialog progressDialog;
	private Handler updateBarHandler;
	private Cursor cursor;
	private int counter;
	private Thread thread;

	private NetworkData selectFriend;

	public FriendContactFrag() {
	}


	public static FriendContactFrag newInstance() {
		FriendContactFrag frag = new FriendContactFrag();
		return frag;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_friend_mine, null);
		setCutOffBackgroundTouch(view);

		layoutFriendList = (LinearLayout) view.findViewById(R.id.layout_friend_list);
		layoutConnect = (LinearLayout) view.findViewById(R.id.layout_connect);

		if (contactFriendArray == null || contactFriendArray.size() == 0) {
			contactFriendArray = new ArrayList<NetworkData>();
			layoutConnect.setVisibility(View.VISIBLE);
		} else {
			layoutConnect.setVisibility(View.GONE);
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
				if(selectFriend == null)	return;
				switch (view.getId()) {
					case R.id.txt_follow_item:
						requestFollow(selectFriend.MEMB_ID, selectFriend.FRIEND_YN);
						break;
				}
			}
		});

		txtConnectMessage = (TextView) view.findViewById(R.id.txt_find_friend_message);
		txtConnect = (TextView) view.findViewById(R.id.txt_connect);
		txtFriendCount = (TextView) view.findViewById(R.id.txt_friend_count);
		txtFollowAll = (TextView) view.findViewById(R.id.txt_follow_all);

		txtConnectMessage.setText(R.string.str_find_contact_friend);
		txtConnect.setText(R.string.str_connect_contact);

		txtConnect.setOnClickListener(this);
		txtFollowAll.setOnClickListener(this);

		progressDialog = new ProgressDialog(getContext());
		progressDialog.setCancelable(false);
		progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				thread.interrupt();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if (thread.isInterrupted()) contactFriendArray.clear();
					}
				}, 100);
			}
		});
		updateBarHandler = new Handler();

		txtFriendCount.setText(getString(R.string.str_friend_count, friendAdapter.getItemCount()));

		return view;
	}

	@Override
	public void onUIRefresh() {
		super.onUIRefresh();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.txt_connect:
				boolean isValid = permissionCheck(new String[]{Manifest.permission.READ_CONTACTS}, ACTIVITY_REQUEST_CODE.PERMISSION_CONTACT);
				if (isValid) readContact();
				break;
			case R.id.txt_follow_all:
				requestFollowAll();
				break;
		}
	}

	public void readContact() {
		progressDialog.show();
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				getContacts();
			}
		});
		thread.start();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case ACTIVITY_REQUEST_CODE.PERMISSION_CONTACT:
				for (int result : grantResults) {
					if (result != PackageManager.PERMISSION_GRANTED) {
						((ParentAct) getActivity()).showPermissionDialog(R.string.str_permission_message_contact);
						return;
					}
				}
				readContact();
				break;
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
		StringRequest myReq = netUtil.requestPost(APIKEY.FRIEND_FOLLOW_ALL, URLS.FRIEND_FOLLOW_ALL, params);
		NetworkController.addToRequestQueue(myReq);
	}

	public void requestFriend() {
		JSONArray jsonArray = new JSONArray();
		try {
			for (NetworkData data : contactFriendArray) {
				JSONObject object = new JSONObject();
				//object.put("DEPT", "AD");
				if (!TextUtil.isNull(data.MEMB_NAME))
					object.put("MEMB_NAME", data.MEMB_NAME);
				if (!TextUtil.isNull(data.PHONE_NUMBER))
					object.put("PHONE", data.PHONE_NUMBER);
				if (!TextUtil.isNull(data.EMAIL))
					object.put("EMAIL", data.EMAIL);
				jsonArray.put(object);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		//JYLog.longLog(jsonArray.toString());
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
		params.put("FRIEND_CATEGORY",  "A");
		params.put("LIST", jsonArray.toString());
		StringRequest myReq = netUtil.requestPost(APIKEY.FRIEND_RECOMMEND, URLS.FRIEND_RECOMMEND, params);
		NetworkController.addToRequestQueue(myReq);
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
			case APIKEY.FRIEND_RECOMMEND:
				int count = 0;
				if (json.LIST != null && json.LIST.size() > 0) {
					netResultDataArray = json.LIST;
					count = netResultDataArray.size();
					layoutConnect.setVisibility(View.GONE);
					friendAdapter.setItemArray(json.LIST);
				}else{
					layoutConnect.setVisibility(View.VISIBLE);
					txtConnectMessage.setText(R.string.str_not_exist_contact_friend);
					txtConnect.setVisibility(View.INVISIBLE);
				}
				txtFriendCount.setText(getString(R.string.str_friend_count, count));
				break;
			case APIKEY.FRIEND_FOLLOW_ALL:
				for(NetworkData data : netResultDataArray){
					data.FRIEND_YN = Definitions.YN.YES;
				}
				friendAdapter.notifyDataSetChanged();
				break;
			case NetworkConstantUtil.APIKEY.FRIEND_FOLLOW:
				if(selectFriend !=null){
					selectFriend.FRIEND_YN = Definitions.YN.YES;
					friendAdapter.notifyDataSetChanged();
				}
				break;
			case NetworkConstantUtil.APIKEY.FRIEND_UNFOLLOW:
				if(selectFriend !=null){
					selectFriend.FRIEND_YN = Definitions.YN.NO;
					friendAdapter.notifyDataSetChanged();
				}
				break;
		}
	}

	public void getContacts() {
		String phoneNumber = null;
		String email = null;
		Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
		String _ID = ContactsContract.Contacts._ID;
		String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
		String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
		Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
		String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
		Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
		String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
		String DATA = ContactsContract.CommonDataKinds.Email.DATA;
		ContentResolver contentResolver = getActivity().getContentResolver();
		cursor = contentResolver.query(CONTENT_URI, null, null, null, null);
		// Iterate every contact in the phone
		if (cursor.getCount() > 0) {
			counter = 0;
			while (cursor.moveToNext()) {
				if (thread.isInterrupted()) {
					return;
				}
				// Update the progress message
				updateBarHandler.post(new Runnable() {
					public void run() {
						progressDialog.setMessage("Reading contacts : " + counter++ + "/" + cursor.getCount());
					}
				});
				NetworkData data = new NetworkData();
				String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
				String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
				int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
				if (hasPhoneNumber > 0) {
					data.MEMB_NAME = name;
					//This is to read multiple phone numbers associated with the same contact
					Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
					while (phoneCursor.moveToNext()) {
						phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
						data.PHONE_NUMBER = phoneNumber;
					}
					phoneCursor.close();

					//Read every email id associated with the contact
					Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);
					while (emailCursor.moveToNext()) {
						email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
						data.EMAIL = email;
					}
					emailCursor.close();
				}

				if (!TextUtil.isNull(data.MEMB_NAME) && (!TextUtil.isNull(data.PHONE_NUMBER) || !TextUtil.isNull(data.EMAIL)))
					contactFriendArray.add(data);
			}

			if (thread.isInterrupted()) return;

			// ListView has to be updated using a ui thread
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
//                    layoutConnect.setVisibility(View.GONE);
//                    friendAdapter.setItemArray(contactFriendArray);
					requestFriend();
				}
			});
			// Dismiss the progressbar after 500 millisecondds
			updateBarHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					progressDialog.cancel();
				}
			}, 500);
		}else{
			thread.interrupt();
			updateBarHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					progressDialog.cancel();
					layoutConnect.setVisibility(View.VISIBLE);
					txtConnectMessage.setText(R.string.str_not_exist_contact_friend);
					txtConnect.setVisibility(View.INVISIBLE);
				}
			}, 100);

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		progressDialog.dismiss();
	}
}
