package com;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.VolleyError;
import com.volley.network.NetworkListener;
import com.volley.network.NetworkRequestFunction;
import com.volley.network.NetworkRequestUtil;
import com.volley.network.dto.NetworkJson;

public abstract class ParentFrag extends Fragment implements NetworkListener {
	public NetworkRequestUtil netUtil;
	public NetworkRequestFunction netFunc;
	private InputMethodManager inputManager;

	public void onUIRefresh(){}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		netUtil = new NetworkRequestUtil(this);
		netFunc = new NetworkRequestFunction(netUtil);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (netUtil == null) {
			netUtil = new NetworkRequestUtil(this);
		}
		if(netFunc ==null)		netFunc = new NetworkRequestFunction(netUtil);
	}

	public void showKeyboard(final View view) {
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}

	public void hiddenKeyboard() {
		if (inputManager == null)
			inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputManager.isActive()) {
			inputManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
		}
	}

	public void setCutOffBackgroundTouch(View view) {
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
		});
	}

	@Override
	public void onNetworkStart(int idx) {
		showLoading();
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		hideLoading();
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		hideLoading();
		if (json != null && json.RESULT_CODE != null) {
			switch (json.RESULT_CODE) {
//				case NETWORK_RESULT_CODE.EXPIRE_ACCESS_TOKEN:
//					Toast.makeText(getActivity(), "만료된 토큰입니다. 다시 로그인 해주세요", Toast.LENGTH_LONG).show();
//					PlaygreenManager.removeUserInfo();
////					if (getActivity() != null && getActivity() instanceof ParentAct)
////						((ParentAct) getActivity()).gotoLoginAct(true);
//					break;
			}
		}
	}

	public void showLoading() {
		if (getActivity() != null && getActivity() instanceof ParentAct)
			((ParentAct) getActivity()).showLoading();
	}

	public void hideLoading() {
		if (getActivity() != null && getActivity() instanceof ParentAct)
			((ParentAct) getActivity()).hideLoading();
	}



	public boolean permissionCheck(String[] reqPermission, int reqCode) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			boolean isDenied = false;
			for (String req : reqPermission) {
				if (ActivityCompat.checkSelfPermission(getContext(), req) == PackageManager.PERMISSION_DENIED) {
					isDenied = true;
					break;
				}
			}
			if (isDenied) {
				requestPermissions(reqPermission, reqCode);
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//		for(String permission: permissions)
//			JYLog.D(requestCode +" permission: "+ permission, new Throwable());
//		for(int result : grantResults)
//			JYLog.D(requestCode +" result: "+ result, new Throwable());
//		switch (requestCode){
//			case Definitions.ACTIVITY_REQUEST_CODE.PERMISSION_ABOUT_CAMERA:
//				for(int result : grantResults){
//					if(result != PackageManager.PERMISSION_GRANTED){
//						showPermissionDialog(R.string.str_permission_message_camera);
//						return;
//					}
//				}
//				goCamera(0);
//				break;
//			case Definitions.ACTIVITY_REQUEST_CODE.PERMISSION_ABOUT_GALLERY:
//				goGallery(0);
//				break;
//		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		netFunc = null;
		netUtil = null;
	}
}
