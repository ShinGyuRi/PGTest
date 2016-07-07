package kr.innisfree.playgreen.fragment.search;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ParentFrag;
import com.android.volley.VolleyError;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.TextUtil;
import com.moyusoft.util.Util;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.search.HashtagDetailAct;
import kr.innisfree.playgreen.activity.search.SearchAct;
import kr.innisfree.playgreen.adapter.SearchTagAdapter;
import kr.innisfree.playgreen.data.PlayGreenEvent;
import kr.innisfree.playgreen.data.PlayGreenEvent.EVENT_TYPE;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;

/**
 * Created by jooyoung on 2016-04-20.
 */
public class SearchTagFrag extends ParentFrag implements AdapterItemClickListener {

	private RecyclerView recyclerView;
	private SearchTagAdapter adapter;
	private LinearLayout layoutEmpty;
	private TextView txtEmpty;

	private ArrayList<NetworkData> searchHistoryArray;
	private ArrayList<NetworkData> searchResultArray;
	private NetworkData selectItem;
	private String searchKeyword;

	public SearchTagFrag() {
	}

	public static SearchTagFrag newInstance() {
		return new SearchTagFrag();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_search, null);
		setCutOffBackgroundTouch(view);

		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		adapter = new SearchTagAdapter(getContext(), this);
		recyclerView.setAdapter(adapter);

		layoutEmpty = (LinearLayout) view.findViewById(R.id.empty_view);
		txtEmpty = (TextView) view.findViewById(R.id.txt_empty_message);
		txtEmpty.setText(R.string.str_tag_search_message);

		netFunc.requestSearchHistory("T");

		return view;
	}

	@Subscribe
	public void onEvent(PlayGreenEvent event){
		if(event.getEvent() == EVENT_TYPE.ACTION_SEARCH_TAG){
			if(event.getData()==null){
				if(searchHistoryArray!=null && searchHistoryArray.size()>0){
					layoutEmpty.setVisibility(View.GONE);
					adapter.setItemArray(searchHistoryArray,true);
				}else{
					netFunc.requestSearchHistory("T");
				}
			}else{
				searchKeyword = (String)event.getData();
				netFunc.requestHashTagSearch(searchKeyword, null, null);
			}
		}
	}

	public void showDeleteSearchLog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog
				.setMessage(R.string.str_delete_search_history_confirm_message)
				.setNegativeButton(R.string.str_cancel, null)
				.setPositiveButton(R.string.str_confirm, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						netFunc.requestRemoveHistory("T",null);
					}
				})
				.show();
	}

	@Override
	public void onAdapterItemClick(View view, int position) {
		selectItem = adapter.getItem(position);
		if(selectItem ==null) return;
		switch (view.getId()){
			case R.id.layout_item:
				if(!TextUtil.isNull(selectItem.SEARCH_KEYWORD)){
					searchKeyword = selectItem.SEARCH_KEYWORD;
					((SearchAct)getActivity()).setSearchKeyword(searchKeyword);
					netFunc.requestHashTagSearch(selectItem.SEARCH_KEYWORD, null, null);
				}else if(!TextUtil.isNull(selectItem.HASHTAG)){
					Intent intent = new Intent(getActivity(), HashtagDetailAct.class);
					intent.putExtra(Definitions.INTENT_KEY.DATA, selectItem.HASHTAG);
					Util.moveActivity(getActivity(), intent, -1, -1, false, false);
				}
				break;
			case R.id.btn_item_remove_history:
				netFunc.requestRemoveHistory("T", selectItem.SEARCH_KEYWORD);
				break;
			case R.id.layout_item_remove_all_history:
				showDeleteSearchLog();
				break;
		}
	}

	@Override
	public void onNetworkStart(int idx) {
		//super.onNetworkStart(idx);
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		//super.onNetworkResult(idx, json);
		switch (idx) {
			case APIKEY.SEARCH_HISTORY:
				if (json.LIST == null || json.LIST.size() == 0) {
					layoutEmpty.setVisibility(View.VISIBLE);
					return;
				}
				layoutEmpty.setVisibility(View.GONE);
				searchHistoryArray = json.LIST;
				adapter.setItemArray(searchHistoryArray, true);
				break;
			case APIKEY.SEARCH_HASHTAG:
				if (json.LIST == null || json.LIST.size() == 0) {
					layoutEmpty.setVisibility(View.VISIBLE);
					return;
				}
				searchResultArray = json.LIST;
				layoutEmpty.setVisibility(View.GONE);
				adapter.setSearchKeyword(searchKeyword);
				adapter.setItemArray(json.LIST, false);
				break;
			case APIKEY.SEARCH_DELETE:
				netFunc.requestSearchHistory("T");
				break;
		}
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
