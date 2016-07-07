package kr.innisfree.playgreen.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ParentAct;
import com.android.volley.VolleyError;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.USER_LIST_TYPE;
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
 * Created by jooyoung on 2016-04-11.
 */
public class UserListAct extends ParentAct implements AdapterItemClickListener {

	private String userType, membId;

	private TextView txtTitle;
	private RecyclerView recyclerView;
	private UserAdapter adapter;
	private LinearLayout layoutEmpty;
	private  TextView txtEmpty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_simple_list_whitebg);

		/** Init Toolbar*/
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		membId = getIntent().getStringExtra(Definitions.INTENT_KEY.MEMB_ID);
		userType = getIntent().getStringExtra(Definitions.INTENT_KEY.USER_TYPE);

		txtTitle = (TextView) findViewById(R.id.txt_title);
		recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		adapter = new UserAdapter(this);
		adapter.setAdapterItemClickListener(this);
		recyclerView.setAdapter(adapter);

		layoutEmpty = (LinearLayout)findViewById(R.id.empty_view);
		txtEmpty = (TextView)findViewById(R.id.txt_empty_message);

		if (userType!= null && userType.equals(USER_LIST_TYPE.FOLLOWING)){
			txtTitle.setText(R.string.str_following);
		}else{
			txtTitle.setText(R.string.str_follower);
		}

		netFunc.requestUserList(userType, membId);
	}

	@Override
	public void onAdapterItemClick(View view, int position) {
		final NetworkData data = adapter.getItem(position);
		if (data == null) return;
		switch (view.getId()) {
			case R.id.layout_item:
				Intent intent = new Intent(this, ProfileDetailAct.class);
				intent.putExtra(Definitions.INTENT_KEY.MEMB_ID, data.MEMB_ID);
				Util.moveActivity(this, intent, -1, -1, false, false);
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
					dialogShow(dialog, "cancel dialog");
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
			case APIKEY.FRIEND_LIST:
				adapter.setItemArray(json.LIST);
				if(adapter.getItemCount()==0){
					layoutEmpty.setVisibility(View.VISIBLE);
					txtEmpty.setText(R.string.str_empty_message_default);
				}else{
					layoutEmpty.setVisibility(View.GONE);
				}
				break;
			case APIKEY.FRIEND_FOLLOW:
			case APIKEY.FRIEND_UNFOLLOW:
				netFunc.requestUserList(userType, membId);
				break;
		}
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
	}
}
