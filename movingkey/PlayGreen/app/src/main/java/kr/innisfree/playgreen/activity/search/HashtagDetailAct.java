package kr.innisfree.playgreen.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ParentAct;
import com.android.volley.VolleyError;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.INTENT_KEY;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.Util;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import net.yazeed44.imagepicker.ui.SpacesItemDecoration;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.TimelineDetailAct;
import kr.innisfree.playgreen.adapter.TimelineGridAdapter;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;
import kr.innisfree.playgreen.listener.RecyclerOnScrollListener;

/**
 * Created by jooyoung on 2016-04-20.
 */
public class HashtagDetailAct extends ParentAct implements View.OnClickListener, AdapterItemClickListener {

	private TextView txtTitle, txtListCount;
	private RecyclerView recyclerView;
	private TimelineGridAdapter adapter;
	private GridLayoutManager gridLayoutManager;
	private LinearLayout layoutRecommendTag, layoutEmpty;
	private RecyclerOnScrollListener recyclerOnScrollListener;

	private NetworkJson netResultData;
	private ArrayList<NetworkData> recommendHashtagArray;
	private String keyword;

	private int page;
	private NetworkData paging;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_hashtag_detail);
		setLoading(this);

		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		findViewById(R.id.layout_back).setOnClickListener(this);
		txtTitle = (TextView) findViewById(R.id.txt_title);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		findViewById(R.id.btn_sort_like).setOnClickListener(this);
		findViewById(R.id.btn_sort_time).setOnClickListener(this);

		layoutEmpty = (LinearLayout) findViewById(R.id.empty_view);
		recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
		recyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_1)));
		gridLayoutManager = new GridLayoutManager(this, 3);
		adapter = new TimelineGridAdapter(this, this, recyclerView);
		recyclerView.setLayoutManager(gridLayoutManager);
		recyclerView.setAdapter(adapter);
		keyword = getIntent().getStringExtra(INTENT_KEY.DATA);
		txtTitle.setText("#" + keyword);

		txtListCount = (TextView) findViewById(R.id.txt_list_count);
		layoutRecommendTag = (LinearLayout) findViewById(R.id.layout_recommend_tag);

		page = 1;
		findViewById(R.id.btn_sort_like).performClick();
		netFunc.requestHashTagSearch(keyword, null, null);

		recyclerOnScrollListener = new RecyclerOnScrollListener() {
			@Override
			public void onLoadMore() {
				if (paging != null && paging.TOTAL_PAGE > page) {
					page += 1;
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							String sort;
							if(findViewById(R.id.btn_sort_like).isSelected()){
								sort = "L";
							}else{
								sort = "D";
							}
							netFunc.requestHashTagImageList(keyword, null, sort, page + "");
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
			case R.id.btn_sort_like:
				if (v.isSelected()) return;
				findViewById(R.id.btn_sort_like).setSelected(true);
				findViewById(R.id.btn_sort_time).setSelected(false);
				netFunc.requestHashTagImageList(keyword, null, "L", page + "");
				break;
			case R.id.btn_sort_time:
				if (v.isSelected()) return;
				findViewById(R.id.btn_sort_like).setSelected(false);
				findViewById(R.id.btn_sort_time).setSelected(true);
				netFunc.requestHashTagImageList(keyword, null, "D", page + "");
				break;
		}
	}

	@Override
	public void onAdapterItemClick(View view, int position) {
		NetworkData selectItem = adapter.getItem(position);
		if (selectItem == null) return;
		Intent intent = new Intent(this, TimelineDetailAct.class);
		intent.putExtra(Definitions.INTENT_KEY.TIMELINE_ID, selectItem.TARGET_ID);
		Util.moveActivity(this, intent, -1, -1, false, false);
	}

	private View.OnClickListener recommendTagClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			NetworkData selectTag = (NetworkData) v.getTag();
			if (selectTag == null) return;
			Intent intent = new Intent(HashtagDetailAct.this, HashtagDetailAct.class);
			intent.putExtra(Definitions.INTENT_KEY.DATA, selectTag.HASHTAG);
			Util.moveActivity(HashtagDetailAct.this, intent, -1, -1, false, false);
		}
	};

	public void makeRecommendTagView() {
		if (recommendHashtagArray == null || recommendHashtagArray.size() == 0) return;
		layoutRecommendTag.removeAllViews();
		for (NetworkData data : recommendHashtagArray) {
			View view = getLayoutInflater().inflate(R.layout.view_text_tag, null);
			TextView txtRecommendTag = (TextView) view.findViewById(R.id.txt_recommend_tag);
			txtRecommendTag.setTag(data);
			txtRecommendTag.setText("#" + data.HASHTAG);
			txtRecommendTag.setOnClickListener(recommendTagClickListener);
			layoutRecommendTag.addView(view);
		}
	}

	@Override
	public void onUIRefresh() {
		super.onUIRefresh();
		if (netResultData == null) return;
		txtListCount.setText(netResultData.TOTAL_COUNT + "");
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
			case APIKEY.SEARCH_HASHTAG_IMG:
				JYLog.D(new Throwable());
				paging = json.PAGING;
				adapter.setItemArray(json.LIST);

				if (adapter.getItemCount() == 0) {
					layoutEmpty.setVisibility(View.VISIBLE);
				} else {
					layoutEmpty.setVisibility(View.GONE);
				}
				if (recyclerOnScrollListener != null)
					recyclerOnScrollListener.setLoading(true);

				netResultData = json;
				onUIRefresh();

				break;
			case APIKEY.SEARCH_HASHTAG:
				if (json.LIST == null || json.LIST.size() == 0) {
					return;
				}
				recommendHashtagArray = json.LIST;
				makeRecommendTagView();
				break;
		}
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
		if(json==null || json.RESULT_CODE ==null)	return;
		switch (json.RESULT_CODE){
			case NetworkConstantUtil.NETWORK_RESULT_CODE.NOT_EXIST_DATA:
				layoutEmpty.setVisibility(View.VISIBLE);
				break;
		}
	}
}
