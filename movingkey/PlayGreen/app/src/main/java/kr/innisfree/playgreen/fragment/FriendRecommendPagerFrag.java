package kr.innisfree.playgreen.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ParentFrag;
import com.android.volley.toolbox.StringRequest;
import com.moyusoft.util.JYLog;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.NetworkController;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import java.util.ArrayList;
import java.util.HashMap;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.common.view.viewpagerindicator.CirclePageIndicator;
import kr.innisfree.playgreen.common.view.viewpagerindicator.PageIndicator;

/**
 * Created by jooyoung on 2016-02-19.
 */
public class FriendRecommendPagerFrag extends ParentFrag implements View.OnClickListener {

	private ArrayList<NetworkData> recommendFriendArray;

	private ViewPager pager;
	private MyPagerAdapter adapterPager;
	private PageIndicator indicator;
	private FrameLayout layoutIndicator;

	public FriendRecommendPagerFrag() {
	}


	public static FriendRecommendPagerFrag newInstance() {
		FriendRecommendPagerFrag frag = new FriendRecommendPagerFrag();
		return frag;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_friend_recommend_pager, null);
		setCutOffBackgroundTouch(view);

		recommendFriendArray = new ArrayList<NetworkData>();

		layoutIndicator = (FrameLayout)view.findViewById(R.id.layout_indicator);
		pager = (ViewPager) view.findViewById(R.id.viewPager);
		adapterPager = new MyPagerAdapter(getChildFragmentManager());
		indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
		pager.setAdapter(adapterPager);
		indicator.setViewPager(pager);

		pager.setClipToPadding(false);
		pager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.dp_8));
		pager.setPadding(getResources().getDimensionPixelOffset(R.dimen.dp_30), 0, getResources().getDimensionPixelOffset(R.dimen.dp_30), 0);

		requestRecommendFriend();

		boolean hasMenuKey = ViewConfiguration.get(getContext()).hasPermanentMenuKey();
		boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
		if(!hasMenuKey && !hasBackKey) {
			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)layoutIndicator.getLayoutParams();
			params.bottomMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.dp_20);
			layoutIndicator.setLayoutParams(params);
			JYLog.D("111", new Throwable());
		}else{
			JYLog.D("222",new Throwable());
		}

		return view;
	}


	@Override
	public void onClick(View v) {

	}

	public void requestRecommendFriend() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
		params.put("FRIEND_CATEGORY",  "R");
		StringRequest myReq = netUtil.requestPost(NetworkConstantUtil.APIKEY.FRIEND_RECOMMEND, NetworkConstantUtil.URLS.FRIEND_RECOMMEND, params);
		NetworkController.addToRequestQueue(myReq);
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
			case NetworkConstantUtil.APIKEY.FRIEND_RECOMMEND:
				if(recommendFriendArray!=null)	recommendFriendArray.clear();
				recommendFriendArray = json.LIST;
				adapterPager.notifyDataSetChanged();
				break;
		}
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {
		public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			if(recommendFriendArray!=null && recommendFriendArray.size()>0)
				return recommendFriendArray.size();
			else
				return  0;
		}

		@Override
		public Fragment getItem(int position) {
			//JYLog.D(recommendFriendArray.get(position).FRIEND_YN, new Throwable());
			return FriendRecommendFrag.newInstance(recommendFriendArray.get(position));
		}

	}
}
