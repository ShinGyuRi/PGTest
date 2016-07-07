package kr.innisfree.playgreen.activity.search;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.ParentAct;
import com.android.volley.VolleyError;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.GOTO;
import com.moyusoft.util.Definitions.INTENT_KEY;
import com.moyusoft.util.Definitions.YN;
import com.moyusoft.util.TextUtil;
import com.moyusoft.util.Util;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.BridgeAct;
import kr.innisfree.playgreen.activity.TimelineDetailAct;
import kr.innisfree.playgreen.activity.setting.PGPointAct;
import kr.innisfree.playgreen.activity.setting.ProfileEditAct;
import kr.innisfree.playgreen.activity.setting.UsersLikeDetailAct;
import kr.innisfree.playgreen.activity.user.UserListAct;
import kr.innisfree.playgreen.adapter.Timeline2TypeAdapter;
import kr.innisfree.playgreen.data.DialogData;
import kr.innisfree.playgreen.dialog.FollowCancelDialog;
import kr.innisfree.playgreen.fragment.main.MyPageFrag;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;
import kr.innisfree.playgreen.listener.DialogListener;

/**
 * Created by jooyoung on 2016-04-20.
 */
public class ProfileDetailAct extends ParentAct implements View.OnClickListener, AdapterItemClickListener {

	private Toolbar mToolbar;

	private RecyclerView recyclerView;
	private Timeline2TypeAdapter timelineAdapter;
	private LinearLayoutManager linearLayoutManager;
	private GridLayoutManager gridLayoutManager;
	private ImageButton btnFollow;

	private int page;
	private NetworkData paging;
	private NetworkData selectItem;
	private NetworkData userData;
	private String membId;
	private boolean isMyProfile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_profile_detail);
		setLoading(this);

		membId = getIntent().getStringExtra(INTENT_KEY.MEMB_ID);
//		isMyProfile = false;
//		String myId = PlaygreenManager.getUserInfo().MEMB_ID;
//		if (!TextUtil.isNull(membId) && !TextUtil.isNull(myId) && membId.equals(myId)) {
//			isMyProfile = true;
//		}
//
//		initToolbar();

		MyPageFrag frag =MyPageFrag.newInstance(membId);
		switchContent(frag, R.id.container, false,false);

//		recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//		recyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_1)));
//		linearLayoutManager = new LinearLayoutManager(this);
//		gridLayoutManager = new GridLayoutManager(this, 3);
//		gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//			@Override
//			public int getSpanSize(int position) {
//				if (timelineAdapter.getItemViewType(position) == 90) return 3;
//				else return 1;
//			}
//		});
//		timelineAdapter = new Timeline2TypeAdapter(this, this, recyclerView);
//		timelineAdapter.setHeaderExist(true);
//		recyclerView.setLayoutManager(gridLayoutManager);
//		recyclerView.setAdapter(timelineAdapter);
//
//		netFunc.requestUserInfo(membId);
//		netFunc.requestTimeline("M", membId, null, null);
	}

	private void initToolbar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		findViewById(R.id.layout_back).setOnClickListener(this);
		findViewById(R.id.btn_follow).setOnClickListener(this);
		findViewById(R.id.btn_etc).setOnClickListener(this);
		btnFollow = (ImageButton) findViewById(R.id.btn_follow);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		if(isMyProfile){
			findViewById(R.id.txt_title).setVisibility(View.VISIBLE);
			findViewById(R.id.btn_follow).setVisibility(View.GONE);
			findViewById(R.id.btn_etc).setVisibility(View.GONE);
		}else{
			findViewById(R.id.txt_title).setVisibility(View.GONE);
			findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
			findViewById(R.id.btn_etc).setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onAdapterItemClick(View v, int position) {
		Intent intent = null;
		selectItem = timelineAdapter.getItem(position);
		switch (v.getId()) {
			case R.id.btn_show_grid:
				recyclerView.setLayoutManager(gridLayoutManager);
				break;
			case R.id.btn_show_list:
				recyclerView.setLayoutManager(linearLayoutManager);
				break;
			case R.id.btn_profile_img_edit:
				intent = new Intent(this, ProfileEditAct.class);
				Util.moveActivity(this, intent, -1, -1, false, false);
				break;
			case R.id.layout_follower:
				intent = new Intent(this, UserListAct.class);
				intent.putExtra(Definitions.INTENT_KEY.USER_TYPE, Definitions.USER_LIST_TYPE.FOLLOWER);
				intent.putExtra(INTENT_KEY.MEMB_ID, membId);
				Util.moveActivity(this, intent, -1, -1, false, false);
				break;
			case R.id.layout_following:
				intent = new Intent(this, UserListAct.class);
				intent.putExtra(Definitions.INTENT_KEY.USER_TYPE, Definitions.USER_LIST_TYPE.FOLLOWING);
				intent.putExtra(INTENT_KEY.MEMB_ID, membId);
				Util.moveActivity(this, intent, -1, -1, false, false);
				break;
			case R.id.layout_like:
				intent = new Intent(this, UsersLikeDetailAct.class);
				intent.putExtra(INTENT_KEY.MEMB_ID, membId);
				Util.moveActivity(this, intent, -1, -1, false, false);
				break;
			case R.id.layout_pg21:
				intent = new Intent(this, BridgeAct.class);
				intent.putExtra(Definitions.INTENT_KEY.DATA, GOTO.PG21);
				intent.putExtra(INTENT_KEY.MEMB_ID, membId);
				Util.moveActivity(this, intent, -1, -1, false, false);
				break;
			case R.id.layout_pg_point:
				if (isMyProfile) {
					intent = new Intent(this, PGPointAct.class);
					Util.moveActivity(this, intent, -1, -1, false, false);
				}
				break;
			case R.id.img_item_content:
				if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
					intent = new Intent(this, TimelineDetailAct.class);
					intent.putExtra(Definitions.INTENT_KEY.TIMELINE_ID, selectItem.TIMELINE_ID);
					Util.moveActivity(this, intent, -1, -1, false, false);
				}
				break;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
			case R.id.layout_back:
				onBackPressed();
				break;
			case R.id.btn_follow:
				if(!TextUtil.isNull(userData.FRIEND_YN) && userData.FRIEND_YN.equals(Definitions.YN.YES)){
					FollowCancelDialog dialog = new FollowCancelDialog();
					dialog.setupDialog(userData, new DialogListener() {
						@Override
						public void onSendDlgData(DialogData dialogData) {
							if(dialogData!=null && dialogData.dialogButtonType == Definitions.DIALOG_SELECT.CONFIRM){
								netFunc.requestFollow(membId, userData.FRIEND_YN);
							}
						}
					});
					dialogShow(dialog, "cancel dialog");
				}else{
					netFunc.requestFollow(membId, userData.FRIEND_YN);
				}
				break;
			case R.id.btn_etc:
				showEtcDialog();
				break;
		}
	}

	@Override
	public void onUIRefresh() {
		super.onUIRefresh();
		if (userData == null) return;
		if (!TextUtil.isNull(userData.FRIEND_YN) && userData.FRIEND_YN.equals(YN.YES)) {
			btnFollow.setSelected(true);
		} else {
			btnFollow.setSelected(false);
		}
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
			case APIKEY.MEMB_INFO:
				userData = json.DATA;
				userData.isMyProfile = isMyProfile;
				timelineAdapter.setHeaderData(json.DATA);
				onUIRefresh();
				break;
			case APIKEY.TIMELINE_LIST:
				timelineAdapter.setTotalCount(json.TIMELINE_COUNT);
				timelineAdapter.setItemArray(json.LIST);
				break;
			case APIKEY.FRIEND_FOLLOW:
			case APIKEY.FRIEND_UNFOLLOW:
				if (!TextUtil.isNull(userData.FRIEND_YN) && userData.FRIEND_YN.equals(YN.YES)) {
					userData.FRIEND_YN = YN.NO;
				} else {
					userData.FRIEND_YN = YN.YES;
				}
				onUIRefresh();
				break;
			case APIKEY.USER_BLOCK:
			case APIKEY.USER_BLOCK_CANCEL:
				if (!TextUtil.isNull(userData.BLOCK_YN) && userData.BLOCK_YN.equals(YN.YES)) {
					userData.BLOCK_YN = YN.NO;
				} else {
					userData.BLOCK_YN = YN.YES;
				}
				break;
		}
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
	}

	public void showEtcDialog() {
		if (userData == null) return;
		String[] etcs = new String[1];
		if (!TextUtil.isNull(userData.BLOCK_YN) && userData.BLOCK_YN.equals(YN.YES)) {
			etcs[0] = getString(R.string.str_member_unblock, userData.MEMB_NAME);
		} else {
			etcs[0] = getString(R.string.str_member_block, userData.MEMB_NAME);
		}
		//etcs[1] = getString(R.string.str_member_report, userData.MEMB_NAME);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(etcs, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					netFunc.requestBlock(userData.MEMB_ID, userData.BLOCK_YN);
				} else {
					Intent intent = new Intent(ProfileDetailAct.this, BridgeAct.class);
					intent.putExtra(Definitions.INTENT_KEY.DATA, GOTO.REPORT);
					intent.putExtra(INTENT_KEY.MEMB_ID, membId);
					Util.moveActivity(ProfileDetailAct.this, intent, -1, -1, false, false);
					//TODO 신고하기
//					Fragment fragment = ReportFrag.newInstance(category, targetId);
//					switchContent(fragment, R.id.container_full, true, false);
				}
			}
		});
		builder.show();
	}
}
