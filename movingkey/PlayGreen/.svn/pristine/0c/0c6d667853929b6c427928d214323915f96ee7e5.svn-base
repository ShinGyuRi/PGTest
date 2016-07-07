package kr.innisfree.playgreen.fragment.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ParentAct;
import com.ParentFrag;
import com.android.volley.VolleyError;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.moyusoft.util.Debug;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.ACTIVITY_REQUEST_CODE;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;
import com.moyusoft.util.Util;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import net.yazeed44.imagepicker.ui.SpacesItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URLDecoder;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.Util.ShareToSNSManager;
import kr.innisfree.playgreen.activity.BridgeAct;
import kr.innisfree.playgreen.activity.MainAct;
import kr.innisfree.playgreen.activity.TimelineDetailAct;
import kr.innisfree.playgreen.activity.search.HashtagDetailAct;
import kr.innisfree.playgreen.activity.search.ProfileDetailAct;
import kr.innisfree.playgreen.activity.setting.AlarmAct;
import kr.innisfree.playgreen.activity.setting.PGPointAct;
import kr.innisfree.playgreen.activity.setting.ProfileEditAct;
import kr.innisfree.playgreen.activity.setting.SettingAct;
import kr.innisfree.playgreen.activity.setting.UsersLikeDetailAct;
import kr.innisfree.playgreen.activity.user.UserListAct;
import kr.innisfree.playgreen.adapter.Timeline2TypeAdapter;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.data.DialogData;
import kr.innisfree.playgreen.data.PlayGreenEvent;
import kr.innisfree.playgreen.data.PlayGreenEvent.EVENT_TYPE;
import kr.innisfree.playgreen.dialog.FollowCancelDialog;
import kr.innisfree.playgreen.dialog.ShareDialog;
import kr.innisfree.playgreen.fragment.ReportFrag;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;
import kr.innisfree.playgreen.listener.DialogListener;
import kr.innisfree.playgreen.listener.RecyclerOnScrollListener;

/**
 * Created by jooyoung on 2016-02-22.
 */
public class MyPageFrag extends ParentFrag implements View.OnClickListener, AdapterItemClickListener {

	private View view;
	private Toolbar mToolbar;

	private RecyclerView recyclerView;
	private Timeline2TypeAdapter timelineAdapter;
	private LinearLayoutManager linearLayoutManager;
	private GridLayoutManager gridLayoutManager;
	private TextView txtBadge, txtTitle;
	private ImageButton btnFollow, btnEtc, btnOption;
	private FrameLayout layoutBack, layoutAlarm;
	private RecyclerOnScrollListener recyclerOnScrollListener;

	private int page;
	private NetworkData paging;
	private NetworkData selectItem;
	private NetworkData headerData;
	public String membId;
	private boolean isMyProfile, isProfileUserFollowAction;


	public MyPageFrag() {
	}

	@SuppressLint("ValidFragment")
	public MyPageFrag(String membId) {
		this.membId = membId;
	}

	public static MyPageFrag newInstance(String membId) {
		if (TextUtil.isNull(membId))
			membId = PlaygreenManager.getUserInfo().MEMB_ID;
		MyPageFrag frag = new MyPageFrag(membId);
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

		if (paging == null || page == 0) {
			page = 1;
		}
		recyclerView.setLayoutManager(gridLayoutManager);
		recyclerOnScrollListener = new RecyclerOnScrollListener() {
			@Override
			public void onLoadMore() {
				if (paging != null && paging.TOTAL_PAGE > page) {
					page += 1;
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							if(!TextUtils.isEmpty(membId))
								netFunc.requestTimeline("M", membId, page + "", null);
						}
					}, 300);
				}
			}
		};
		recyclerOnScrollListener.setGridLayoutManager(gridLayoutManager);
		recyclerView.addOnScrollListener(recyclerOnScrollListener);

		timelineAdapter = new Timeline2TypeAdapter(getContext(), this, recyclerView);
		timelineAdapter.setHeaderExist(true);
		recyclerView.setAdapter(timelineAdapter);
//		recyclerView.setNestedScrollingEnabled(false);
//		recyclerView.setHasFixedSize(false);

		isMyProfile = false;
		String myId = PlaygreenManager.getUserInfo().MEMB_ID;
		if (!TextUtil.isNull(membId) && !TextUtil.isNull(myId) && membId.equals(myId)) {
			isMyProfile = true;
		}

		initToolbar();

		if(getActivity() instanceof MainAct){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					netFunc.requestAlarmCount();
				}
			}, 300);
		}

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		page = 1;
		netFunc.requestUserInfo(membId);
		netFunc.requestTimeline("M", membId, page + "", null);
		if(getActivity() instanceof  MainAct){
			updateBadgeCount();
			EventBus.getDefault().post(new PlayGreenEvent(EVENT_TYPE.ALARM_COUNT_UPDATE));
		}
	}

	private void initToolbar() {
		mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
		layoutBack = (FrameLayout) view.findViewById(R.id.layout_back);
		btnFollow = (ImageButton) view.findViewById(R.id.btn_follow);
		btnEtc = (ImageButton) view.findViewById(R.id.btn_etc);

		layoutAlarm = (FrameLayout) view.findViewById(R.id.layout_alarm);
		txtBadge = (TextView) view.findViewById(R.id.txt_mypage_badge);
		btnOption = (ImageButton) view.findViewById(R.id.btn_option);
		txtTitle = (TextView) view.findViewById(R.id.txt_title);

		if(Definitions.InnisfreeGothicBold !=null)
			txtTitle.setTypeface(Definitions.InnisfreeGothicBold);

		layoutBack.setVisibility(View.GONE);
		btnFollow.setVisibility(View.GONE);
		btnEtc.setVisibility(View.GONE);
		layoutAlarm.setVisibility(View.GONE);
		txtBadge.setVisibility(View.GONE);
		btnOption.setVisibility(View.GONE);
		txtTitle.setVisibility(View.GONE);

		layoutBack.setOnClickListener(this);
		btnFollow.setOnClickListener(this);
		btnEtc.setOnClickListener(this);
		layoutAlarm.setOnClickListener(this);
		btnOption.setOnClickListener(this);

		if (getActivity() instanceof ProfileDetailAct) {
			if (isMyProfile) {
				txtTitle.setVisibility(View.VISIBLE);
			} else {
				btnFollow.setVisibility(View.VISIBLE);
				btnEtc.setVisibility(View.VISIBLE);
			}
			layoutBack.setVisibility(View.VISIBLE);
		} else {
			layoutAlarm.setVisibility(View.VISIBLE);
			btnOption.setVisibility(View.VISIBLE);
			txtTitle.setVisibility(View.VISIBLE);
			updateBadgeCount();
		}

		((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	public void updateBadgeCount() {
		if (Definitions.ALARM_COUNT > 0) {
			txtBadge.setVisibility(View.VISIBLE);
			txtBadge.setText("" + Definitions.ALARM_COUNT);
		} else {
			txtBadge.setVisibility(View.GONE);
		}
		//PlaygreenManager.updateIconBadge(getContext());
	}


	@Override
	public void onAdapterItemClick(View v, int position) {
		Intent intent = null;
		selectItem = timelineAdapter.getItem(position);
		switch (v.getId()) {
			/** 헤더뷰 클릭(position = 0) */
			case R.id.btn_show_grid:
				recyclerView.setLayoutManager(gridLayoutManager);
				recyclerOnScrollListener.setGridLayoutManager(gridLayoutManager);
				break;
			case R.id.btn_show_list:
				recyclerView.setLayoutManager(linearLayoutManager);
				recyclerOnScrollListener.setLinearLayoutManager(linearLayoutManager);
				break;
			case R.id.btn_profile_img_edit:
				intent = new Intent(getActivity(), ProfileEditAct.class);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			case R.id.layout_follower:
				intent = new Intent(getActivity(), UserListAct.class);
				intent.putExtra(Definitions.INTENT_KEY.USER_TYPE, Definitions.USER_LIST_TYPE.FOLLOWER);
				if (isMyProfile == false) intent.putExtra(Definitions.INTENT_KEY.MEMB_ID, membId);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			case R.id.layout_following:
				intent = new Intent(getActivity(), UserListAct.class);
				intent.putExtra(Definitions.INTENT_KEY.USER_TYPE, Definitions.USER_LIST_TYPE.FOLLOWING);
				if (isMyProfile == false) intent.putExtra(Definitions.INTENT_KEY.MEMB_ID, membId);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			case R.id.layout_like:
				intent = new Intent(getActivity(), UsersLikeDetailAct.class);
				if (isMyProfile == false) intent.putExtra(Definitions.INTENT_KEY.MEMB_ID, membId);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			case R.id.layout_pg21:
				intent = new Intent(getActivity(), BridgeAct.class);
				intent.putExtra(Definitions.INTENT_KEY.DATA, Definitions.GOTO.PG21);
				if (isMyProfile == false) intent.putExtra(Definitions.INTENT_KEY.MEMB_ID, membId);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			case R.id.layout_pg_point:
				if (isMyProfile) {
					intent = new Intent(getActivity(), PGPointAct.class);
					Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				}
				break;
			/** 그리드뷰 아이템 클릭 */
			case R.id.img_item_content:
				if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
					intent = new Intent(getActivity(), TimelineDetailAct.class);
					intent.putExtra(Definitions.INTENT_KEY.TIMELINE_ID, selectItem.TIMELINE_ID);
					Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				}
				break;

			/**타임라인 리스트뷰 아이템 클릭*/
			case R.id.img_item_profile:
				intent = new Intent(getActivity(), ProfileDetailAct.class);
				intent.putExtra(Definitions.INTENT_KEY.MEMB_ID, selectItem.MEMB_ID);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			case R.id.txt_item_like_count:
				intent = new Intent(getActivity(), BridgeAct.class);
				intent.putExtra(Definitions.INTENT_KEY.DATA, Definitions.GOTO.LIKE_USER_LIST);
				intent.putExtra(Definitions.INTENT_KEY.TIMELINE_ID, selectItem.TIMELINE_ID);
				intent.putExtra(Definitions.INTENT_KEY.CATEGORY, "T");
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			case R.id.btn_item_follow:
				if (!TextUtil.isNull(selectItem.FRIEND_YN) && selectItem.FRIEND_YN.equals(Definitions.YN.YES)) {
					FollowCancelDialog dialog = new FollowCancelDialog();
					dialog.setupDialog(selectItem, new DialogListener() {
						@Override
						public void onSendDlgData(DialogData dialogData) {
							if (dialogData != null && dialogData.dialogButtonType == Definitions.DIALOG_SELECT.CONFIRM) {
								netFunc.requestFollow(selectItem.MEMB_ID, selectItem.FRIEND_YN);
							}
						}
					});
					((ParentAct) getActivity()).dialogShow(dialog, "cancel dialog");
				} else {
					netFunc.requestFollow(selectItem.MEMB_ID, selectItem.FRIEND_YN);
				}
				break;
			case R.id.btn_item_like:
				if (!TextUtil.isNull(selectItem.LIKE_YN) && selectItem.LIKE_YN.equals(Definitions.YN.YES))
					netFunc.requestLike(selectItem.TIMELINE_ID, Definitions.YN.NO, selectItem.LIKE_ID);
				else
					netFunc.requestLike(selectItem.TIMELINE_ID, Definitions.YN.YES, null);
				break;
			case R.id.btn_item_reply:
				intent = new Intent(getActivity(), BridgeAct.class);
				intent.putExtra(Definitions.INTENT_KEY.DATA, Definitions.GOTO.COMMENT_LIST);
				intent.putExtra(Definitions.INTENT_KEY.TIMELINE_ID, selectItem.TIMELINE_ID);
				intent.putExtra(Definitions.INTENT_KEY.CATEGORY, "T");
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			case R.id.btn_item_share:
				ShareDialog dialog = new ShareDialog();
				dialog.setDialogListener(dialogListener);
				((ParentAct) getActivity()).dialogShow(dialog, "share");
				break;
			case R.id.btn_item_etc:
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				if (!TextUtil.isNull(selectItem.OWN_YN) && selectItem.OWN_YN.equals(Definitions.YN.YES)) {
					builder.setItems(R.array.alert_etc_mine, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							netFunc.requestTimelineDelete(selectItem.TIMELINE_ID);
						}
					});
				} else {
					builder.setItems(R.array.alert_etc, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(getActivity(), BridgeAct.class);
							intent.putExtra(Definitions.INTENT_KEY.DATA, Definitions.GOTO.REPORT);
							intent.putExtra(Definitions.INTENT_KEY.TIMELINE_ID, selectItem.TIMELINE_ID);
							intent.putExtra(Definitions.INTENT_KEY.CATEGORY, ReportFrag.NOTIFY_CATEGORY_T);
							Util.moveActivity(getActivity(), intent, -1, -1, false, false);
						}
					});
				}
				builder.show();
				break;
			case R.id.txt_item_hashtag:
				NetworkData tagItem = (NetworkData) v.getTag();
				if (tagItem == null) return;
				intent = new Intent(getActivity(), HashtagDetailAct.class);
				intent.putExtra(Definitions.INTENT_KEY.DATA, tagItem.HASHTAG);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
			/** 탭 일때*/
			case R.id.btn_option:
				intent = new Intent(getActivity(), SettingAct.class);
				Util.moveActivity(getActivity(), intent, 0, 0, false, false, ACTIVITY_REQUEST_CODE.IS_LOGOUT);
				break;
			case R.id.layout_alarm:
				intent = new Intent(getActivity(), AlarmAct.class);
				Util.moveActivity(getActivity(), intent, 0, 0, false, false);
				break;
			/** 액티비티일때*/
			case R.id.layout_back:
				getActivity().onBackPressed();
				break;
			case R.id.btn_follow:
				if (!TextUtil.isNull(headerData.FRIEND_YN) && headerData.FRIEND_YN.equals(Definitions.YN.YES)) {
					FollowCancelDialog dialog = new FollowCancelDialog();
					dialog.setupDialog(headerData, new DialogListener() {
						@Override
						public void onSendDlgData(DialogData dialogData) {
							if (dialogData != null && dialogData.dialogButtonType == Definitions.DIALOG_SELECT.CONFIRM) {
								isProfileUserFollowAction = true;
								netFunc.requestFollow(membId, headerData.FRIEND_YN);
							}
						}
					});
					((ParentAct) getActivity()).dialogShow(dialog, "cancel dialog");
				} else {
					isProfileUserFollowAction = true;
					netFunc.requestFollow(membId, headerData.FRIEND_YN);
				}
				break;
			case R.id.btn_etc:
				showEtcDialog();
				break;
		}
	}

	@Subscribe
	public void onEvent(PlayGreenEvent event) {
		if (event.getEvent() == EVENT_TYPE.MYPAGE_REFRESH) {
			netFunc.requestUserInfo(PlaygreenManager.getUserInfo().MEMB_ID);
			netFunc.requestTimeline("M", PlaygreenManager.getUserInfo().MEMB_ID, page + "", null);
		}
		else if (event.getEvent() == EVENT_TYPE.ALARM_COUNT_UPDATE) {
			if (getActivity() instanceof ProfileDetailAct) return;
			updateBadgeCount();
		}
	}

	@Override
	public void onUIRefresh() {
		super.onUIRefresh();
		if (headerData == null) return;
		if (!TextUtil.isNull(headerData.FRIEND_YN) && headerData.FRIEND_YN.equals(Definitions.YN.YES)) {
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
				headerData = json.DATA;
				headerData.isMyProfile = isMyProfile;
				timelineAdapter.setHeaderData(headerData);
				onUIRefresh();
				break;
			case APIKEY.TIMELINE_LIST:
				paging = json.PAGING;
				if (paging != null && paging.CURRENT_PAGE == 1) {
					timelineAdapter.clearItemArray();
				}
				timelineAdapter.setTotalCount(json.TIMELINE_COUNT);
				timelineAdapter.setItemArray(json.LIST);

				if (recyclerOnScrollListener != null)
					recyclerOnScrollListener.setLoading(true);
				break;
			case APIKEY.FRIEND_FOLLOW:
			case APIKEY.FRIEND_UNFOLLOW:
				//프로필 조회 대상을 팔로우/취소 한경우
				if (isProfileUserFollowAction) {
					if (!TextUtil.isNull(headerData.FRIEND_YN) && headerData.FRIEND_YN.equals(Definitions.YN.YES)) {
						headerData.FRIEND_YN = Definitions.YN.NO;
					} else {
						headerData.FRIEND_YN = Definitions.YN.YES;
					}
					isProfileUserFollowAction = false;
					onUIRefresh();
				} else {
					//타임라인 리스트 팔로우
					if (selectItem != null && !TextUtil.isNull(selectItem.FRIEND_YN) && selectItem.FRIEND_YN.equals(Definitions.YN.NO)) {
						selectItem.FRIEND_YN = Definitions.YN.YES;
					} else {
						selectItem.FRIEND_YN = Definitions.YN.NO;
					}
					timelineAdapter.notifyDataSetChanged();
				}
				break;
			case APIKEY.USER_BLOCK:
			case APIKEY.USER_BLOCK_CANCEL:
				if (!TextUtil.isNull(headerData.BLOCK_YN) && headerData.BLOCK_YN.equals(Definitions.YN.YES)) {
					headerData.BLOCK_YN = Definitions.YN.NO;
				} else {
					headerData.BLOCK_YN = Definitions.YN.YES;
				}
				break;

			case APIKEY.LIKE_ACTION:
				if (selectItem != null && !TextUtil.isNull(selectItem.LIKE_YN) && selectItem.LIKE_YN.equals(Definitions.YN.NO)) {
					selectItem.LIKE_YN = Definitions.YN.YES;
					selectItem.LIKE_ID = json.LIKE_ID;
					selectItem.LIKE_COUNT += 1;
				} else {
					selectItem.LIKE_YN = Definitions.YN.NO;
					selectItem.LIKE_ID = "";
					selectItem.LIKE_COUNT -= 1;
				}
				timelineAdapter.notifyDataSetChanged();
				break;
			case APIKEY.TIMELINE_DELETE:
				timelineAdapter.removeItem(selectItem);
				break;

			case APIKEY.INFO_COUNT:
				Definitions.ALARM_COUNT = json.INFO_COUNT;
				updateBadgeCount();
				EventBus.getDefault().post(new PlayGreenEvent(EVENT_TYPE.ALARM_COUNT_UPDATE));
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

	public void showEtcDialog() {
		if (headerData == null) return;
		String[] etcs = new String[1];
		if (!TextUtil.isNull(headerData.BLOCK_YN) && headerData.BLOCK_YN.equals(Definitions.YN.YES)) {
			etcs[0] = getString(R.string.str_member_unblock, headerData.MEMB_NAME);
		} else {
			etcs[0] = getString(R.string.str_member_block, headerData.MEMB_NAME);
		}
		//etcs[1] = getString(R.string.str_member_report, userData.MEMB_NAME);
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setItems(etcs, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					netFunc.requestBlock(headerData.MEMB_ID, headerData.BLOCK_YN);
				} else {
					Intent intent = new Intent(getActivity(), BridgeAct.class);
					intent.putExtra(Definitions.INTENT_KEY.DATA, Definitions.GOTO.REPORT);
					intent.putExtra(Definitions.INTENT_KEY.MEMB_ID, membId);
					//Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				}
			}
		});
		builder.show();
	}


	/**
	 * 공유
	 */
	public DialogListener dialogListener = new DialogListener() {
		@Override
		public void onSendDlgData(DialogData dialogData) {
			if (selectItem == null) return;
			ShareToSNSManager shareToSNSManager = null;

			String shareImageUrl;
			if (!TextUtils.isEmpty(selectItem.TIMELINE_MP4_SCENE)) {
				shareImageUrl = selectItem.TIMELINE_MP4_SCENE;
			} else {
				shareImageUrl = selectItem.TIMELINE_IMG;
			}

			if (TextUtil.isNull(shareImageUrl)) return;

			switch (dialogData.dialogButtonType) {
				case Definitions.DIALOG_SELECT.SHARE_COPY_URL:
					ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
					ClipData clip = null;
						clip = ClipData.newPlainText("simple text", shareImageUrl);
					clipboard.setPrimaryClip(clip);
					Debug.showToast(getContext(), R.string.str_share_to_clipboard_message);
					break;
				case Definitions.DIALOG_SELECT.SHARE_FACEBOOK:
					shareToFacebook(shareImageUrl);
					break;
				case Definitions.DIALOG_SELECT.SHARE_INSTAGRAM:
					shareToSNSManager = new ShareToSNSManager(getActivity(), shareImageUrl);
					shareToSNSManager.shareToInstagram();
					break;
				case Definitions.DIALOG_SELECT.SHARE_KAKAOSTORY:
					shareToSNSManager = new ShareToSNSManager(getActivity(), shareImageUrl);
					shareToSNSManager.shareToKakaoStory();
					break;
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Definitions.ACTIVITY_REQUEST_CODE.FACEBOOK_SHARE) {
			callbackManager.onActivityResult(requestCode, resultCode, data);
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	CallbackManager callbackManager;
	public com.facebook.share.widget.ShareDialog facebookShareDialog;

	public void shareToFacebook(String imageUrl) {
		FacebookSdk.sdkInitialize(getContext());
		callbackManager = CallbackManager.Factory.create();
		facebookShareDialog = new com.facebook.share.widget.ShareDialog(this);
		facebookShareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
			@Override
			public void onSuccess(Sharer.Result result) {
			}

			@Override
			public void onCancel() {
			}

			@Override
			public void onError(FacebookException error) {
			}
		});

		String timelineContent = "";
		try {
			timelineContent = URLDecoder.decode(selectItem.TIMELINE_TEXT, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		Uri contentUri = Uri.parse(imageUrl);
		ShareLinkContent content = new ShareLinkContent.Builder()
				.setContentUrl(contentUri)            //페북에서 클릭하면 들어가는 링크, (사이트이면 사이트로 이동, 동영상 링크이면 링크를 통해 동영상 재생됨)
				.setContentTitle(getString(R.string.str_share_to_facebook_title))
				.setImageUrl(Uri.parse(imageUrl))
				.setContentDescription(timelineContent)
				.build();
		facebookShareDialog.show(content, com.facebook.share.widget.ShareDialog.Mode.AUTOMATIC);
	}
}
