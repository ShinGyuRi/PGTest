package kr.innisfree.playgreen.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ParentAct;
import com.android.volley.VolleyError;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.YN;
import com.moyusoft.util.Util;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.search.ProfileDetailAct;
import kr.innisfree.playgreen.adapter.BlockUserAdapter;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-04-11.
 */
public class BlockUserListAct extends ParentAct implements AdapterItemClickListener {

	private NetworkData selectUser;

	private TextView txtTitle;
	private RecyclerView recyclerView;
	private BlockUserAdapter adapter;
	private LinearLayout layoutEmpty, layoutDescription;
	private TextView txtEmpty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_block_list);
		setLoading(this);

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

		txtTitle = (TextView) findViewById(R.id.txt_title);
		recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		adapter = new BlockUserAdapter(this);
		adapter.setAdapterItemClickListener(this);
		recyclerView.setAdapter(adapter);

		layoutDescription = (LinearLayout)findViewById(R.id.layout_description);
		layoutDescription.setVisibility(View.INVISIBLE);

		layoutEmpty = (LinearLayout)findViewById(R.id.empty_view);
		txtEmpty = (TextView)findViewById(R.id.txt_empty_message);

		txtTitle.setText(R.string.str_setting_block_user);
		netFunc.requestBlockUserList();
	}

	@Override
	public void onAdapterItemClick(View view, int position) {
		selectUser = adapter.getItem(position);
		if (selectUser == null) return;
		switch (view.getId()) {
			case R.id.layout_block_toggle:
				netFunc.requestBlock(selectUser.MEMB_ID, selectUser.isBlockCancel ? YN.NO : YN.YES);
				break;
			case R.id.layout_item:
				Intent intent = new Intent(this, ProfileDetailAct.class);
				intent.putExtra(Definitions.INTENT_KEY.MEMB_ID, selectUser.MEMB_ID);
				Util.moveActivity(this, intent, -1, -1, false, false);
				break;
		}
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
			case APIKEY.BLOCK_LIST:
				adapter.setItemArray(json.LIST);
				if(adapter.getItemCount()==0){
					layoutEmpty.setVisibility(View.VISIBLE);
					txtEmpty.setText(R.string.str_empty_message_default);
				}else{
					layoutDescription.setVisibility(View.VISIBLE);
					layoutEmpty.setVisibility(View.GONE);
				}
				break;
			case APIKEY.USER_BLOCK:
			case APIKEY.USER_BLOCK_CANCEL:
				selectUser.isBlockCancel = !selectUser.isBlockCancel;
				adapter.notifyDataSetChanged();
				if (selectUser.isBlockCancel)
					Toast.makeText(getApplicationContext(), R.string.str_toast_block_cancel, Toast.LENGTH_SHORT).show();
//				else
//					Toast.makeText(getApplicationContext(), R.string.str_toast_block, Toast.LENGTH_SHORT).show();
				break;
		}
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
	}
}
