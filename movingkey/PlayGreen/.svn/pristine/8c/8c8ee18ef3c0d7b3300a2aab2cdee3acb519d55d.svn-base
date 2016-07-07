package kr.innisfree.playgreen.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ParentFrag;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.adapter.StickyAdapter;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;
import kr.innisfree.playgreen.listener.RecyclerViewLoadMoreListener;

/**
 * Created by jooyoung on 2016-02-24.
 */
public class StickyFrag extends ParentFrag implements SwipeRefreshLayout.OnRefreshListener, AdapterItemClickListener {

	private SwipeRefreshLayout swipeRefreshLayout;
	private RecyclerView recyclerView;
	private StickyAdapter timelineAdapter;

	private ArrayList<NetworkData> networkDataArray;

	private int listType;

	public StickyFrag() {
	}

	@SuppressLint("ValidFragment")
	public StickyFrag(int listType) {
		this.listType = listType;
	}

	public static StickyFrag newInstance(int listType) {
		StickyFrag frag = new StickyFrag(listType);
		return frag;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_timeline, null);
		swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_swipe);
		swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#69b06c"));
		swipeRefreshLayout.setOnRefreshListener(this);
		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		if (timelineAdapter == null) timelineAdapter = new StickyAdapter(getActivity());
		recyclerView.setAdapter(timelineAdapter);
		recyclerView.addOnScrollListener(new RecyclerViewLoadMoreListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
			@Override
			public void onLoadMore(int current_page) {

			}
		});

		final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(timelineAdapter);
		recyclerView.addItemDecoration(headersDecor);
		StickyRecyclerHeadersTouchListener touchListener =
				new StickyRecyclerHeadersTouchListener(recyclerView, headersDecor);
		touchListener.setOnHeaderClickListener(new StickyRecyclerHeadersTouchListener.OnHeaderClickListener() {
					@Override
					public void onHeaderClick(View header, int position, long headerId) {
					}
				});
		recyclerView.addOnItemTouchListener(touchListener);
//		recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
//			@Override
//			public void onItemClick(View view, int position) {
//				adapter.remove(adapter.getItem(position));
//			}
//		}));
		timelineAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
			@Override
			public void onChanged() {
				headersDecor.invalidateHeaders();
			}
		});

		networkDataArray = new ArrayList<>();
		networkDataArray.add(new NetworkData());
		networkDataArray.add(new NetworkData());
		networkDataArray.add(new NetworkData());
		timelineAdapter.setItemArray(networkDataArray);

		return view;
	}

	@Override
	public void onRefresh() {
		swipeRefreshLayout.setRefreshing(false);
	}

	@Override
	public void onAdapterItemClick(View view, int position) {

	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
	}

}
