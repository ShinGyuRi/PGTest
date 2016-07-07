package kr.innisfree.playgreen.activity.setting;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ParentAct;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.moyusoft.util.Debug;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.INTENT_KEY;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;
import com.moyusoft.util.Util;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import net.yazeed44.imagepicker.ui.SpacesItemDecoration;

import java.net.URLDecoder;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.Util.ShareToSNSManager;
import kr.innisfree.playgreen.activity.BridgeAct;
import kr.innisfree.playgreen.activity.TimelineDetailAct;
import kr.innisfree.playgreen.activity.search.HashtagDetailAct;
import kr.innisfree.playgreen.activity.search.ProfileDetailAct;
import kr.innisfree.playgreen.adapter.Timeline2TypeAdapter;
import kr.innisfree.playgreen.data.DialogData;
import kr.innisfree.playgreen.dialog.FollowCancelDialog;
import kr.innisfree.playgreen.dialog.ShareDialog;
import kr.innisfree.playgreen.fragment.ReportFrag;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;
import kr.innisfree.playgreen.listener.DialogListener;
import kr.innisfree.playgreen.listener.RecyclerOnScrollListener;

/**
 * Created by jooyoung on 2016-04-20.
 */
public class UsersLikeDetailAct extends ParentAct implements View.OnClickListener, AdapterItemClickListener {

	private TextView txtTitle, txtListCount;
	private Button btnShowGrid, btnShowList;
	private RecyclerView recyclerView;
	private Timeline2TypeAdapter adapter;
	private LinearLayoutManager linearLayoutManager;
	private GridLayoutManager gridLayoutManager;
	private LinearLayout layoutEmpty;
	private RecyclerOnScrollListener recyclerOnScrollListener;

	private String membId;
	private int page;
	private NetworkData selectItem;
	private NetworkData paging;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_users_like);
		setLoading(this);

		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		findViewById(R.id.layout_back).setOnClickListener(this);
		txtTitle = (TextView) findViewById(R.id.txt_title);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		btnShowGrid = (Button) findViewById(R.id.btn_show_grid);
		btnShowList = (Button) findViewById(R.id.btn_show_list);
		btnShowGrid.setOnClickListener(this);
		btnShowList.setOnClickListener(this);

		recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
		gridLayoutManager = new GridLayoutManager(this, 3);
		linearLayoutManager = new LinearLayoutManager(this);

		recyclerView.setLayoutManager(gridLayoutManager);
		recyclerOnScrollListener = new RecyclerOnScrollListener() {
			@Override
			public void onLoadMore() {
				if (paging != null && paging.TOTAL_PAGE > page) {
					page += 1;
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							netFunc.requestLikePohoto(membId, page + "");
						}
					}, 300);
				}
			}
		};
		recyclerOnScrollListener.setGridLayoutManager(gridLayoutManager);
		recyclerView.addOnScrollListener(recyclerOnScrollListener);
		recyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_1)));
		adapter = new Timeline2TypeAdapter(this, this, recyclerView);
		recyclerView.setAdapter(adapter);

		membId = getIntent().getStringExtra(INTENT_KEY.MEMB_ID);
		txtTitle.setText(R.string.str_like);
		layoutEmpty = (LinearLayout) findViewById(R.id.empty_view);
		txtListCount = (TextView) findViewById(R.id.txt_list_count);

		page = 1;
		btnShowGrid.setSelected(true);
		netFunc.requestLikePohoto(membId, page + "");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.layout_back:
				onBackPressed();
				break;
			case R.id.btn_show_grid:
				if (v.isSelected()) return;
				setTimelineMode(0);
				break;
			case R.id.btn_show_list:
				if (v.isSelected()) return;
				setTimelineMode(1);
				break;
		}
	}

	public void setTimelineMode(int mode) {
		if (mode == 0) {
			btnShowGrid.setSelected(true);
			btnShowList.setSelected(false);
			recyclerView.setLayoutManager(gridLayoutManager);
			recyclerOnScrollListener.setGridLayoutManager(gridLayoutManager);
		} else {
			btnShowGrid.setSelected(false);
			btnShowList.setSelected(true);
			recyclerView.setLayoutManager(linearLayoutManager);
			recyclerOnScrollListener.setLinearLayoutManager(linearLayoutManager);
		}
	}

	@Override
	public void onAdapterItemClick(View view, int position) {
		selectItem = adapter.getItem(position);
		Intent intent = null;
		if (selectItem == null) return;
		switch (view.getId()) {
			/** 그리드뷰 아이템 클릭 */
			case R.id.img_item_content:
				if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
					intent = new Intent(this, TimelineDetailAct.class);
					intent.putExtra(Definitions.INTENT_KEY.TIMELINE_ID, selectItem.TIMELINE_ID);
					Util.moveActivity(this, intent, -1, -1, false, false);
				}
				break;

			/**타임라인 리스트뷰 아이템 클릭*/
			case R.id.img_item_profile:
				intent = new Intent(this, ProfileDetailAct.class);
				intent.putExtra(Definitions.INTENT_KEY.MEMB_ID, selectItem.MEMB_ID);
				Util.moveActivity(this, intent, -1, -1, false, false);
				break;
			case R.id.txt_item_like_count:
				intent = new Intent(this, BridgeAct.class);
				intent.putExtra(Definitions.INTENT_KEY.DATA, Definitions.GOTO.LIKE_USER_LIST);
				intent.putExtra(INTENT_KEY.TIMELINE_ID, selectItem.TIMELINE_ID);
				intent.putExtra(INTENT_KEY.CATEGORY, "T");
				Util.moveActivity(this, intent, -1, -1, false, false);
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
					dialogShow(dialog, "cancel dialog");
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
				intent = new Intent(this, BridgeAct.class);
				intent.putExtra(Definitions.INTENT_KEY.DATA, Definitions.GOTO.COMMENT_LIST);
				intent.putExtra(INTENT_KEY.TIMELINE_ID, selectItem.TIMELINE_ID);
				intent.putExtra(INTENT_KEY.CATEGORY, "T");
				Util.moveActivity(this, intent, -1, -1, false, false);
				break;
			case R.id.btn_item_share:
				ShareDialog dialog = new ShareDialog();
				dialog.setDialogListener(dialogListener);
				dialogShow(dialog, "share");
				break;
			case R.id.btn_item_etc:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
							Intent intent = new Intent(UsersLikeDetailAct.this, BridgeAct.class);
							intent.putExtra(Definitions.INTENT_KEY.DATA, Definitions.GOTO.REPORT);
							intent.putExtra(INTENT_KEY.TIMELINE_ID, selectItem.TIMELINE_ID);
							intent.putExtra(INTENT_KEY.CATEGORY, ReportFrag.NOTIFY_CATEGORY_T);
							Util.moveActivity(UsersLikeDetailAct.this, intent, -1, -1, false, false);
						}
					});
				}
				builder.show();
				break;
			case R.id.txt_item_hashtag:
				NetworkData tagItem = (NetworkData) view.getTag();
				if (tagItem == null) return;
				intent = new Intent(this, HashtagDetailAct.class);
				intent.putExtra(Definitions.INTENT_KEY.DATA, tagItem.HASHTAG);
				Util.moveActivity(this, intent, -1, -1, false, false);

				JYLog.D(tagItem.HASHTAG + ", " + tagItem.HASHTAG_ID, new Throwable());
				break;
		}
	}


	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
			case APIKEY.LIKE_PHOTO_LIST:
				paging = json.PAGING;
				if (json.PAGING != null) txtListCount.setText(json.PAGING.TOTAL_COUNT + "");
				adapter.setItemArray(json.LIST);

				layoutEmpty.setVisibility(View.GONE);
				if (adapter.getItemCount() == 0) {
					layoutEmpty.setVisibility(View.VISIBLE);
				}
				break;
			case APIKEY.FRIEND_FOLLOW:
			case APIKEY.FRIEND_UNFOLLOW:
				//타임라인 리스트 팔로우
				if (selectItem != null && !TextUtil.isNull(selectItem.FRIEND_YN) && selectItem.FRIEND_YN.equals(Definitions.YN.NO)) {
					selectItem.FRIEND_YN = Definitions.YN.YES;
				} else {
					selectItem.FRIEND_YN = Definitions.YN.NO;
				}
				adapter.notifyDataSetChanged();
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
				adapter.notifyDataSetChanged();
				break;
			case APIKEY.TIMELINE_DELETE:
				adapter.removeItem(selectItem);
				break;
		}
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
					ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					ClipData clip = null;
					clip = ClipData.newPlainText("simple text", shareImageUrl);
					clipboard.setPrimaryClip(clip);
					Debug.showToast(getApplicationContext(), R.string.str_share_to_clipboard_message);
					break;
				case Definitions.DIALOG_SELECT.SHARE_FACEBOOK:
					shareToFacebook(shareImageUrl);
					break;
				case Definitions.DIALOG_SELECT.SHARE_INSTAGRAM:
					shareToSNSManager = new ShareToSNSManager(UsersLikeDetailAct.this, shareImageUrl);
					shareToSNSManager.shareToInstagram();
					break;
				case Definitions.DIALOG_SELECT.SHARE_KAKAOSTORY:
					shareToSNSManager = new ShareToSNSManager(UsersLikeDetailAct.this, shareImageUrl);
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
		FacebookSdk.sdkInitialize(this);
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
