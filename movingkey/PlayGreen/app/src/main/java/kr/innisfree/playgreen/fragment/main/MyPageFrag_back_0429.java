package kr.innisfree.playgreen.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ParentFrag;
import com.android.volley.VolleyError;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.ACTIVITY_REQUEST_CODE;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.Util;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import net.yazeed44.imagepicker.ui.SpacesItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.BridgeAct;
import kr.innisfree.playgreen.activity.TimelineDetailAct;
import kr.innisfree.playgreen.activity.search.ProfileDetailAct;
import kr.innisfree.playgreen.activity.setting.AlarmAct;
import kr.innisfree.playgreen.activity.setting.PGPointAct;
import kr.innisfree.playgreen.activity.setting.ProfileEditAct;
import kr.innisfree.playgreen.activity.setting.SettingAct;
import kr.innisfree.playgreen.activity.setting.UsersLikeDetailAct;
import kr.innisfree.playgreen.activity.user.UserListAct;
import kr.innisfree.playgreen.adapter.Timeline2TypeAdapter;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.data.PlayGreenEvent;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-02-22.
 */
public class MyPageFrag_back_0429 extends ParentFrag implements View.OnClickListener, AdapterItemClickListener {

	private View view;
	private Toolbar mToolbar;

	private RecyclerView recyclerView;
	private Timeline2TypeAdapter timelineAdapter;
	private LinearLayoutManager linearLayoutManager;
	private GridLayoutManager gridLayoutManager;
	private TextView txtBadge;

	private NetworkData selectItem;
	private int page;
	private NetworkData paging;

	public MyPageFrag_back_0429() {
	}

	public static MyPageFrag_back_0429 newInstance() {
		MyPageFrag_back_0429 frag = new MyPageFrag_back_0429();
		return frag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_mypage, null);
		setCutOffBackgroundTouch(view);

		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
		recyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_1)));
		linearLayoutManager = new LinearLayoutManager(getContext());
		gridLayoutManager = new GridLayoutManager(getContext(), 3);
		gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
			@Override
			public int getSpanSize(int position) {
				if (timelineAdapter.getItemViewType(position) == 90) return 3;
				else return 1;
			}
		});
		timelineAdapter = new Timeline2TypeAdapter(getContext(), this, recyclerView);
		timelineAdapter.setHeaderExist(true);
		recyclerView.setLayoutManager(gridLayoutManager);
		recyclerView.setAdapter(timelineAdapter);
		recyclerView.setNestedScrollingEnabled(false);
		recyclerView.setHasFixedSize(false);

		
		if(getActivity() instanceof ProfileDetailAct){

		}else{
			initToolbar();
			updateBadgeCount();
		}
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		page = 1;
		netFunc.requestUserInfo(PlaygreenManager.getUserInfo().MEMB_ID);
		netFunc.requestTimeline("M", PlaygreenManager.getUserInfo().MEMB_ID, page + "", null);
	}

	private void initToolbar() {
		mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
		view.findViewById(R.id.btn_option).setOnClickListener(this);
		view.findViewById(R.id.layout_alarm).setOnClickListener(this);
		txtBadge = (TextView) view.findViewById(R.id.txt_mypage_badge);
		((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	public void updateBadgeCount(){
		if(Definitions.ALARM_COUNT>0){
			txtBadge.setVisibility(View.VISIBLE);
			txtBadge.setText(""+Definitions.ALARM_COUNT);
		}else{
			txtBadge.setVisibility(View.GONE);
		}
		PlaygreenManager.updateIconBadge(getContext());
	}


	@Override
	public void onAdapterItemClick(View v, int position) {
		Intent intent = null;
		selectItem = timelineAdapter.getItem(position);
		switch (v.getId()) {
			/** 헤더뷰 클릭(position = 0) */
			case R.id.btn_show_grid:
				recyclerView.setLayoutManager(gridLayoutManager);
				break;
			case R.id.btn_show_list:
				recyclerView.setLayoutManager(linearLayoutManager);
				break;
			case R.id.btn_profile_img_edit:
				intent = new Intent(getActivity(), ProfileEditAct.class);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			case R.id.layout_follower:
				intent = new Intent(getActivity(), UserListAct.class);
				intent.putExtra(Definitions.INTENT_KEY.USER_TYPE, Definitions.USER_LIST_TYPE.FOLLOWER);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			case R.id.layout_following:
				intent = new Intent(getActivity(), UserListAct.class);
				intent.putExtra(Definitions.INTENT_KEY.USER_TYPE, Definitions.USER_LIST_TYPE.FOLLOWING);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			case R.id.layout_like:
				intent = new Intent(getActivity(), UsersLikeDetailAct.class);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			case R.id.layout_pg21:
				intent = new Intent(getActivity(), BridgeAct.class);
				intent.putExtra(Definitions.INTENT_KEY.DATA, Definitions.GOTO.PG21);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			case R.id.layout_pg_point:
				intent = new Intent(getActivity(), PGPointAct.class);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			/** 아이템 클릭 */
			case R.id.img_item_content:
				if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
					intent = new Intent(getActivity(), TimelineDetailAct.class);
					intent.putExtra(Definitions.INTENT_KEY.TIMELINE_ID, selectItem.TIMELINE_ID);
					Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				}
				break;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
			case R.id.btn_option:
				intent = new Intent(getActivity(), SettingAct.class);
				Util.moveActivity(getActivity(), intent, 0, 0, false, false, ACTIVITY_REQUEST_CODE.IS_LOGOUT);
				break;
			case R.id.layout_alarm:
				intent = new Intent(getActivity(), AlarmAct.class);
				Util.moveActivity(getActivity(), intent, 0, 0, false, false);
				break;
		}
	}

	@Subscribe
	public void onEvent(PlayGreenEvent event) {
//		if (event.getEvent() == EVENT_TYPE.MYPAGE_REFRESH) {
//			netFunc.requestUserInfo(PlaygreenManager.getUserInfo().MEMB_ID);
//			netFunc.requestTimeline("M", PlaygreenManager.getUserInfo().MEMB_ID, page + "", null);
//		}else if(event.getEvent() == EVENT_TYPE.ALARM_COUNT_UPDATE){
//			updateBadgeCount();
//		}
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
			case APIKEY.MEMB_INFO:
				NetworkData headerData = json.DATA;
				headerData.isMyProfile = true;
				timelineAdapter.setHeaderData(headerData);
				break;
			case APIKEY.TIMELINE_LIST:
				paging = json.PAGING;
				if(paging!=null && paging.CURRENT_PAGE ==1){
					timelineAdapter.clearItemArray();
				}
				JYLog.D(new Throwable());
				timelineAdapter.setTotalCount(json.TIMELINE_COUNT);
				timelineAdapter.setItemArray(json.LIST);
				break;
		}
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
}
