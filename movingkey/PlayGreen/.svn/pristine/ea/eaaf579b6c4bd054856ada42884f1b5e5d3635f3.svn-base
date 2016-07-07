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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ParentFrag;
import com.android.volley.VolleyError;
import com.moyusoft.util.BitmapCircleResize;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.ACTIVITY_REQUEST_CODE;
import com.moyusoft.util.Definitions.INTENT_KEY;
import com.moyusoft.util.Definitions.USER_LIST_TYPE;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;
import com.moyusoft.util.Util;
import com.squareup.picasso.Picasso;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import net.yazeed44.imagepicker.ui.SpacesItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.BridgeAct;
import kr.innisfree.playgreen.activity.setting.AlarmAct;
import kr.innisfree.playgreen.activity.setting.PGPointAct;
import kr.innisfree.playgreen.activity.setting.ProfileEditAct;
import kr.innisfree.playgreen.activity.setting.SettingAct;
import kr.innisfree.playgreen.activity.setting.UsersLikeDetailAct;
import kr.innisfree.playgreen.activity.user.UserListAct;
import kr.innisfree.playgreen.adapter.Timeline2TypeAdapter;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.data.PlayGreenEvent;
import kr.innisfree.playgreen.data.PlayGreenEvent.EVENT_TYPE;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-02-22.
 */
public class MyPageFrag_back extends ParentFrag implements View.OnClickListener, AdapterItemClickListener {

	private View view;
	private Toolbar mToolbar;
	private TextView txtName, txtIntroduce, txtFollowerCount, txtFollowingCount, txtLikeCount, txtPG21Day, txtPGPoint, txtListCount;
	private ImageView imgProfile;
	private Button btnShowGrid, btnShowList;

	private RecyclerView recyclerView;
	private Timeline2TypeAdapter timelineAdapter;
	private LinearLayoutManager linearLayoutManager;
	private GridLayoutManager gridLayoutManager;

	private NetworkData userData;
	private ArrayList<NetworkData> userTimelineArray;

	private int page;
	private NetworkData paging;

	public MyPageFrag_back() {
	}

	public static MyPageFrag_back newInstance() {
		MyPageFrag_back frag = new MyPageFrag_back();
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

		txtName = (TextView) view.findViewById(R.id.txt_name);
		txtIntroduce = (TextView) view.findViewById(R.id.txt_introduce);
		txtFollowerCount = (TextView) view.findViewById(R.id.txt_follower_count);
		txtFollowingCount = (TextView) view.findViewById(R.id.txt_following_count);
		txtLikeCount = (TextView) view.findViewById(R.id.txt_like_count);
		txtPG21Day = (TextView) view.findViewById(R.id.txt_pg21_day);
		txtPGPoint = (TextView) view.findViewById(R.id.txt_pg_point);
		txtListCount = (TextView) view.findViewById(R.id.txt_list_count);
		imgProfile = (ImageView) view.findViewById(R.id.img_profile);
		btnShowGrid = (Button) view.findViewById(R.id.btn_show_grid);
		btnShowList = (Button) view.findViewById(R.id.btn_show_list);
		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
		recyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_1)));

		btnShowGrid.setOnClickListener(this);
		btnShowList.setOnClickListener(this);
		btnShowGrid.setSelected(true);

		view.findViewById(R.id.btn_profile_img_edit).setOnClickListener(this);
		view.findViewById(R.id.layout_follower).setOnClickListener(this);
		view.findViewById(R.id.layout_following).setOnClickListener(this);
		view.findViewById(R.id.layout_like).setOnClickListener(this);
		view.findViewById(R.id.layout_pg21).setOnClickListener(this);
		view.findViewById(R.id.layout_pg_point).setOnClickListener(this);

		linearLayoutManager = new LinearLayoutManager(getContext());
		gridLayoutManager = new GridLayoutManager(getContext(), 3);
		timelineAdapter = new Timeline2TypeAdapter(getContext(), this, recyclerView);
		recyclerView.setLayoutManager(gridLayoutManager);
		recyclerView.setAdapter(timelineAdapter);

		initToolbar();

		page = 1;
		netFunc.requestUserInfo(PlaygreenManager.getUserInfo().MEMB_ID);
		netFunc.requestTimeline("M", PlaygreenManager.getUserInfo().MEMB_ID, page + "", null);

		return view;
	}

	public void setTimelineMode(int mode) {
		if (mode == 0) {
			btnShowGrid.setSelected(true);
			btnShowList.setSelected(false);
			recyclerView.setLayoutManager(gridLayoutManager);
			//recyclerView.setAdapter(timelineGridAdapter);
		} else {
			btnShowGrid.setSelected(false);
			btnShowList.setSelected(true);
			recyclerView.setLayoutManager(linearLayoutManager);
			//recyclerView.setAdapter(timelineAdapter);
		}
	}

	private void initToolbar() {
		mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
		view.findViewById(R.id.btn_option).setOnClickListener(this);
		view.findViewById(R.id.layout_alarm).setOnClickListener(this);
		((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	@Override
	public void onAdapterItemClick(View view, int position) {

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
			case R.id.btn_show_grid:
				if (v.isSelected()) return;
				setTimelineMode(0);
				break;
			case R.id.btn_show_list:
				if (v.isSelected()) return;
				setTimelineMode(1);
				break;
			case R.id.btn_profile_img_edit:
				intent = new Intent(getActivity(), ProfileEditAct.class);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			case R.id.layout_follower:
				intent = new Intent(getActivity(), UserListAct.class);
				intent.putExtra(INTENT_KEY.USER_TYPE, USER_LIST_TYPE.FOLLOWER);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			case R.id.layout_following:
				intent = new Intent(getActivity(), UserListAct.class);
				intent.putExtra(INTENT_KEY.USER_TYPE, USER_LIST_TYPE.FOLLOWING);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			case R.id.layout_like:
				intent = new Intent(getActivity(), UsersLikeDetailAct.class);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			case R.id.layout_pg21:
				intent = new Intent(getActivity(), BridgeAct.class);
				intent.putExtra(INTENT_KEY.DATA, Definitions.GOTO.PG21);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			case R.id.layout_pg_point:
				intent = new Intent(getActivity(), PGPointAct.class);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
		}
	}

	@Subscribe
	public void onEvent(PlayGreenEvent event) {
		if (event.getEvent() == EVENT_TYPE.MYPAGE_REFRESH) {
			netFunc.requestUserInfo(PlaygreenManager.getUserInfo().MEMB_ID);
			netFunc.requestTimeline("M", PlaygreenManager.getUserInfo().MEMB_ID, page + "", null);
		}
	}

	@Override
	public void onUIRefresh() {
		super.onUIRefresh();
		if (userData == null) return;
		if (!TextUtil.isNull(userData.MEMB_NAME)) {
			txtName.setText(userData.MEMB_NAME);
		}
		if (!TextUtil.isNull(userData.STATE_TEXT)) {
			txtIntroduce.setText(userData.STATE_TEXT);
		}
		if (!TextUtil.isNull(userData.MEMB_IMG)) {
			JYLog.D(userData.MEMB_IMG, new Throwable());
			Picasso.with(getContext()).load(userData.MEMB_IMG)
					.transform(new BitmapCircleResize(getContext(), getResources().getDimensionPixelOffset(R.dimen.dp_80))).into(imgProfile);
		}
		txtFollowerCount.setText(userData.FOLLOWER_COUNT + "");
		txtFollowingCount.setText(userData.FOLLOWING_COUNT + "");
		txtLikeCount.setText(userData.LIKE_COUNT + "");
		txtPG21Day.setText(userData.PG21_DAY + "day");
		txtPGPoint.setText(TextUtil.makeStringWithComma(userData.POINT + "", false) + "p");
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
			case APIKEY.MEMB_INFO:
				userData = json.DATA;
				onUIRefresh();
				break;
			case APIKEY.TIMELINE_LIST:
				JYLog.D(json.LIST.size() + "", new Throwable());
				txtListCount.setText(json.TIMELINE_COUNT + "");
				userTimelineArray = json.LIST;
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
