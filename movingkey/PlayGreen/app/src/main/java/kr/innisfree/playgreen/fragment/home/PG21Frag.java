package kr.innisfree.playgreen.fragment.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ParentAct;
import com.ParentFrag;
import com.android.volley.toolbox.StringRequest;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.INTENT_KEY;
import com.moyusoft.util.Definitions.YN;
import com.moyusoft.util.PrefUtil;
import com.moyusoft.util.TextUtil;
import com.moyusoft.util.Util;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.NetworkConstantUtil.URLS;
import com.volley.network.NetworkController;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.ExpandImageAct;
import kr.innisfree.playgreen.activity.pg21.PG21TodayMissionAct;
import kr.innisfree.playgreen.activity.pg21.SubscriptionAct;
import kr.innisfree.playgreen.activity.search.SearchAct;
import kr.innisfree.playgreen.adapter.PG21Adapter;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.data.PlayGreenEvent;
import kr.innisfree.playgreen.data.PlayGreenEvent.EVENT_TYPE;
import kr.innisfree.playgreen.dialog.MissionPeriodEndDialog;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-03-11.
 */
public class PG21Frag extends ParentFrag implements View.OnClickListener, AdapterItemClickListener {

	private View view;
	private TextView txtTitle;
	private RecyclerView recyclerView;
	private PG21Adapter pg21Adapter;

	private NetworkData headerData;
	private ArrayList<NetworkData> netResutDataArray;
	private String membId;
	private boolean isMyPg21;

	public PG21Frag() {
	}

	@SuppressLint("ValidFragment")
	public PG21Frag(String membId) {
		this.membId = membId;
	}

	public static PG21Frag newInstance(String membId) {
		PG21Frag frag = new PG21Frag(membId);
		return frag;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_pg21, null);
		setCutOffBackgroundTouch(view);

		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		//if (pg21Adapter == null)
		pg21Adapter = new PG21Adapter(getActivity());
		pg21Adapter.setAdapterItemClickListener(this);

		if (EventBus.getDefault().isRegistered(this) == false) EventBus.getDefault().register(this);

		recyclerView.setAdapter(pg21Adapter);

		if (netResutDataArray == null)
			netResutDataArray = new ArrayList<NetworkData>();

		if (membId == null || membId.equals(PlaygreenManager.getUserInfo().MEMB_ID)) {
			isMyPg21 = true;
		} else {
			isMyPg21 = false;
		}

		initToolbar();
		requestPG21(membId);

		return view;
	}

	private void initToolbar() {
		Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
		view.findViewById(R.id.layout_back).setOnClickListener(this);
		view.findViewById(R.id.btn_option).setOnClickListener(this);
		txtTitle = (TextView)view.findViewById(R.id.txt_title);
		txtTitle.setText(R.string.str_title_playgreen_21);
		if(Definitions.Gotham != null)	txtTitle.setTypeface(Definitions.Gotham);
		((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.layout_back:
				getActivity().onBackPressed();
				break;
			case R.id.btn_option:
				Intent intent = new Intent(getActivity(), SearchAct.class);
				Util.moveActivity(getActivity(), intent, 0, 0, false, false);
				break;
		}
	}

	@Override
	public void onAdapterItemClick(View view, int position) {
		switch (view.getId()) {
			case R.id.img_pg21_item_bg:
				NetworkData item = pg21Adapter.getItem(position);
				if (item.isTodayMission) {
					if (TextUtil.isNull(headerData.TODAY_YN) || headerData.TODAY_YN.equals(YN.NO)) {
						gotoTodayMission();
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
						builder.setMessage(R.string.str_pg21_today_mission_warning_message);
						builder.setPositiveButton(R.string.str_confirm, null);
						builder.show();
					}
				} else {
					Intent intent = new Intent(getActivity(), ExpandImageAct.class);
					intent.putExtra(INTENT_KEY.DATA, item.PG21_MS_ENTER_IMG);
					Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				}
				break;
			case R.id.txt_earthbox_request:    //earth box 응모하기
				gotoRewardAct(SubscriptionAct.TYPE_EARTH_BOX, -1);
				break;
			case R.id.btn_test:
				showMissionEndDialog();
				break;
		}
	}

	public void showMissionEndDialog() {
		if ("E".equals(headerData.POPUP_DEPT)) {
			MissionPeriodEndDialog dialog = new MissionPeriodEndDialog();
			((ParentAct) getActivity()).dialogShow(dialog, "dialog");
		} else if ("S".equals(headerData.POINT_DEPT)) {
			gotoRewardAct(SubscriptionAct.TYPE_SUPER_GREENER, headerData.SUPERGREENER_RANK);
		}
//		if (headerData != null && headerData.SUPERGREENER_RANK > 0 && headerData.SUPERGREENER_RANK < 6) {
//			//랭킹 1~5위까지 리워드 화면 이동
//			gotoRewardAct(SubscriptionAct.TYPE_SUPER_GREENER, headerData.SUPERGREENER_RANK);
//		}else {
//			MissionPeriodEndDialog dialog = new MissionPeriodEndDialog();
//			((ParentAct) getActivity()).dialogShow(dialog, "dialog");
//		}
	}

	public void gotoRewardAct(int type, int grade) {
		Intent intent = new Intent(getActivity(), SubscriptionAct.class);
		intent.putExtra(INTENT_KEY.DATA, type);
		intent.putExtra(INTENT_KEY.GRADE_INT, grade);
		intent.putExtra(INTENT_KEY.SUPERGREENER_ID, headerData.SUPERGREENER_ENTER_ID);
		Util.moveActivity(getActivity(), intent, -1, -1, false, false);
	}

	public void gotoTodayMission() {
		Intent intent = new Intent(getActivity(), PG21TodayMissionAct.class);
		Util.moveActivity(getActivity(), intent, -1, -1, false, false);
	}

	@Subscribe
	public void onEvent(PlayGreenEvent event) {
		if (event.getEvent() == EVENT_TYPE.PG21_REFRESH) {
			requestPG21(membId);
		}
	}

	@Override
	public void onUIRefresh() {
		super.onUIRefresh();

		if (isMyPg21) {
			if (TextUtils.isEmpty(headerData.POPUP_DEPT) == false) {
				if (YN.NO.equals(headerData.POPUP_DEPT) == false)
					showMissionEndDialog();
			}
			//END_DT 비교하여 기간 지났으면 참여기간종료 팝업
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//			sdf.setTimeZone(Calendar.getInstance().getTimeZone());
//			try {
//				Date endDate = sdf.parse(sdf.format(new Date(headerData.END_DT * 1000)));
//				Date today = sdf.parse(sdf.format(new Date()));
//				boolean bool = today.after(endDate);
//				if (bool) showMissionEndDialog();
//				//JYLog.D(bool + ", " + endDate.toString() + ", " + today.toString(), new Throwable());
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
			NetworkData emptyData = new NetworkData();
			emptyData.isTodayMission = true;
			netResutDataArray.add(0, emptyData);
		}
		pg21Adapter.setHeaderData(headerData);
		pg21Adapter.setItemArray(netResutDataArray);
	}

	public void requestPG21(String membId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken());
		if (!TextUtil.isNull(membId))
			params.put("MEMB_ID", membId);
		StringRequest myReq = netUtil.requestPost(APIKEY.PG21_ENTER_LIST, URLS.PG21_ENTER_LIST, params);
		NetworkController.addToRequestQueue(myReq);
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
			case APIKEY.PG21_ENTER_LIST:
				headerData = new NetworkData();
				headerData.isMine = isMyPg21;
				headerData.POPUP_DEPT = json.POPUP_DEPT;
				headerData.SUPERGREENER_ENTER_ID = json.SUPERGREENER_ENTER_ID;
				headerData.ENTER_YN = json.ENTER_YN;
				headerData.MEMB_NAME = json.MEMB_NAME;
				if(TextUtil.isNull(headerData.MEMB_NAME) && isMyPg21){
					headerData.MEMB_NAME = PrefUtil.getInstance().getStringPreference(Definitions.PREFKEY.MEMB_NAME_STR);
				}
				headerData.TODAY_YN = json.TODAY_YN;
				headerData.START_DT = json.START_DT;
				headerData.END_DT = json.END_DT;
				headerData.SUPERGREENER_RANK = json.SUPERGREENER_RANK;
				headerData.PG21_TITLE = json.PG21_TITLE;
				headerData.ENTER_COUNT = json.ENTER_COUNT;
				headerData.EARTHBOX_IMG = json.EARTHBOX_IMG;
				netResutDataArray = json.LIST;
				onUIRefresh();
				break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
