package kr.innisfree.playgreen.activity.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ParentAct;
import com.android.volley.VolleyError;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.adapter.NoticeAdapter;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;
import kr.innisfree.playgreen.listener.RecyclerOnScrollListener;

/**
 * Created by jooyoung on 2016-04-18.
 */
public class NoticeAct extends ParentAct implements View.OnClickListener, AdapterItemClickListener {

	private TextView txtTitle;
	private RecyclerView recyclerView;
	private NoticeAdapter adapter;
	private LinearLayout layoutEmpty;
	private  TextView txtEmpty;

	private int page;
	private NetworkData paging;
	private RecyclerOnScrollListener recyclerOnScrollListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_simple_list_whitebg);
		setLoading(this, true);

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
		txtTitle.setText(R.string.str_setting_notice);

		recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		adapter = new NoticeAdapter(this);
		adapter.setAdapterItemClickListener(this);

		layoutEmpty = (LinearLayout)findViewById(R.id.empty_view);
		txtEmpty = (TextView)findViewById(R.id.txt_empty_message);

		page = 1;
		netFunc.requestNotice(page);

		recyclerOnScrollListener = new RecyclerOnScrollListener() {
			@Override
			public void onLoadMore() {
				if (paging != null && paging.TOTAL_PAGE > page) {
					page += 1;
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							netFunc.requestNotice(page);
						}
					}, 300);
				}
			}
		};

		recyclerOnScrollListener.setLinearLayoutManager((LinearLayoutManager) recyclerView.getLayoutManager());
		recyclerView.addOnScrollListener(recyclerOnScrollListener);
		recyclerView.setAdapter(adapter);

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
		switch (view.getId()){
			case R.id.layout_title:
				adapter.setExpandPosition(position);
				break;
			case R.id.txt_update:
				Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
				marketLaunch.setData(Uri.parse("market://details?id=" + getPackageName()));
				startActivity(marketLaunch);
				break;
		}
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx){
			case APIKEY.NOTICE_LIST:
				paging = json.PAGING;
				adapter.setItemArray(json.LIST);
				if (adapter.getItemCount() == 0) {
					layoutEmpty.setVisibility(View.VISIBLE);
				} else {
					layoutEmpty.setVisibility(View.GONE);
				}
				if (recyclerOnScrollListener != null)
					recyclerOnScrollListener.setLoading(true);
				break;
		}
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
	}
}
