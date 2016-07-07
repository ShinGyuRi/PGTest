package kr.innisfree.playgreen.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ParentAct;
import com.ParentFrag;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.TextUtil;
import com.moyusoft.util.Util;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.search.ProfileDetailAct;
import kr.innisfree.playgreen.adapter.UserAdapter;
import kr.innisfree.playgreen.data.DialogData;
import kr.innisfree.playgreen.dialog.FollowCancelDialog;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;
import kr.innisfree.playgreen.listener.DialogListener;

/**
 * Created by jooyoung on 2016-03-04.
 */
public class LikeListFrag extends ParentFrag implements AdapterItemClickListener {

	private String likeCategory, targetId;

	private TextView txtTitle;
	private RecyclerView recyclerView;
	private UserAdapter adapter;

	public LikeListFrag() {
	}

	@SuppressLint("ValidFragment")
	public LikeListFrag(String likeCategory, String targetId) {
		this.likeCategory = likeCategory;
		this.targetId = targetId;
	}

	public static LikeListFrag newInstance(String likeCategory, String targetId) {
		LikeListFrag frag = new LikeListFrag(likeCategory, targetId);
		return frag;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_like_user_list, null);
		setCutOffBackgroundTouch(view);

		/** Init Toolbar*/
		Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
		view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});
		((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

		txtTitle = (TextView) view.findViewById(R.id.txt_title);
		txtTitle.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.dp_5));
		txtTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like1, 0, 0, 0);
		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		adapter = new UserAdapter(getContext());
		adapter.setAdapterItemClickListener(this);
		recyclerView.setAdapter(adapter);

		netFunc.requestLikeList(likeCategory, targetId);
		return view;
	}


	@Override
	public void onAdapterItemClick(View view, int position) {
		final NetworkData data = adapter.getItem(position);
		if (data == null) return;
		switch (view.getId()) {
			case R.id.layout_item:
				Intent intent = new Intent(getActivity(), ProfileDetailAct.class);
				intent.putExtra(Definitions.INTENT_KEY.MEMB_ID, data.MEMB_ID);
				Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				break;
			case R.id.btn_item_follow:
				if(!TextUtil.isNull(data.FRIEND_YN) && data.FRIEND_YN.equals(Definitions.YN.YES)){
					FollowCancelDialog dialog = new FollowCancelDialog();
					dialog.setupDialog(data, new DialogListener() {
						@Override
						public void onSendDlgData(DialogData dialogData) {
							if(dialogData!=null && dialogData.dialogButtonType == Definitions.DIALOG_SELECT.CONFIRM){
								netFunc.requestFollow(data.MEMB_ID, data.FRIEND_YN);
							}
						}
					});
					((ParentAct)getActivity()).dialogShow(dialog, "cancel dialog");
				}else{
					netFunc.requestFollow(data.MEMB_ID, data.FRIEND_YN);
				}
				break;
		}
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
			case APIKEY.LIKE_LIST:
				adapter.setItemArray(json.LIST);
				txtTitle.setText(json.LIST.size() + "");
				break;
			case APIKEY.FRIEND_FOLLOW:
			case APIKEY.FRIEND_UNFOLLOW:

				netFunc.requestLikeList(likeCategory, targetId);
				break;
		}
	}
}
