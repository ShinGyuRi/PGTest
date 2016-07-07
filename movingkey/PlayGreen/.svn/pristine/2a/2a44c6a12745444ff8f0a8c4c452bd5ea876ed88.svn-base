package kr.innisfree.playgreen.activity.pg21;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ParentAct;
import com.android.volley.toolbox.StringRequest;
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.ACTIVITY_REQUEST_CODE;
import com.moyusoft.util.Util;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.NetworkConstantUtil.URLS;
import com.volley.network.NetworkController;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.adapter.PG21MissionAdapter;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.data.PlayGreenEvent;
import kr.innisfree.playgreen.data.PlayGreenEvent.EVENT_TYPE;
import kr.innisfree.playgreen.fragment.PG21MissionMakeFrag;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-03-11.
 */
public class PG21TodayMissionAct extends ParentAct implements View.OnClickListener, AdapterItemClickListener {

	private ArrayList<NetworkData> missions;
	private RecyclerView recyclerView;
	private RecyclerViewHeader recyclerViewHeader;
	private PG21MissionAdapter missionAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_pg21_today_mission);
		setLoading(this);
		initToolbar();

		if (missions == null) missions = new ArrayList<NetworkData>();
		if (missionAdapter == null) {
			missionAdapter = new PG21MissionAdapter(this);
		}
		missionAdapter.setAdapterItemClickListener(this);

		recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
		recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
		recyclerView.setAdapter(missionAdapter);
		recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		recyclerViewHeader = (RecyclerViewHeader) findViewById(R.id.header);
		recyclerViewHeader.attachTo(recyclerView, true);

		requestMissonList();
	}

	private void initToolbar() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		findViewById(R.id.layout_back).setOnClickListener(this);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.layout_back:
				onBackPressed();
				break;
		}
	}

	@Override
	public void onAdapterItemClick(View view, int position) {
		if (view.getId() == R.id.layout_mission) {
			if (position == 0) {
				Fragment fragment = PG21MissionMakeFrag.newInstance();
				switchContent(fragment, R.id.container, true, false);
			} else {
				NetworkData item = missionAdapter.getItem(position);
				gotoImageUploadAct(item.PG21_MS_ID, item.PG21_MS_TITLE);
			}
		}
	}

	public void gotoImageUploadAct(String id, String title) {
		Intent intent = new Intent(this, PG21MissionImageAct.class);
		intent.putExtra(Definitions.INTENT_KEY.MISSION_ID_STR, id);
		intent.putExtra(Definitions.INTENT_KEY.MISSION_TITLE_STR, title);
		Util.moveActivity(this, intent, 0, 0, false, false, ACTIVITY_REQUEST_CODE.PG21_TODAY_MISSION_UPLOAD_COMPLETE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTIVITY_REQUEST_CODE.PG21_TODAY_MISSION_UPLOAD_COMPLETE) {
			if (resultCode == RESULT_OK) {
				EventBus.getDefault().post(new PlayGreenEvent(EVENT_TYPE.PG21_REFRESH));
				finish();
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void requestMissonList() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken());
		StringRequest myReq = netUtil.requestPost(APIKEY.PG21_MISSION_LIST, URLS.PG21_MISSION_LIST, params);
		NetworkController.addToRequestQueue(myReq);
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
			case APIKEY.PG21_MISSION_LIST:
				missions = json.LIST;
				missions.add(0, new NetworkData());
				missionAdapter.setItemArray(json.LIST);
				break;
		}
	}


	//	public void makeMisison() {
//		missions = new ArrayList<>();
//		String packageName = getPackageName();
//		int index = 0;
//		for (String str : getResources().getStringArray(R.array.array_mission)) {
//			PlayGreenMission mission = new PlayGreenMission();
//			mission.missionSubject = str;
//			if (index == 0) {
//				mission.missionImageRes = R.drawable.btn_add_misson;
//			} else {
//				mission.missionImageRes = getResources().getIdentifier(
//						index < 10 ? ("btn_misson0" + index) : ("btn_misson" + index), "drawable", packageName);
//			}
//			missions.add(mission);
//			index++;
//		}
//	}

}
