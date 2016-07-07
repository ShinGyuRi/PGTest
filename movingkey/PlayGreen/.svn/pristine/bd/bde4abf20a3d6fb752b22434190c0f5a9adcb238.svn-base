package kr.innisfree.playgreen.fragment.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ParentFrag;
import com.android.volley.VolleyError;
import com.moyusoft.util.TextUtil;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.dto.NetworkJson;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.adapter.PointHistoryAdapter;
import kr.innisfree.playgreen.common.PlaygreenManager;

/**
 * Created by jooyoung on 2016-04-12.
 */
public class PGPointHistoryFrag extends ParentFrag {

	private TextView txtMyPoint;
	private RecyclerView recyclerView;
	private PointHistoryAdapter adapter;

	public static PGPointHistoryFrag newInstance() {
		return new PGPointHistoryFrag();
	}

	public PGPointHistoryFrag() {
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_pg_point_history, null);

		txtMyPoint = (TextView) view.findViewById(R.id.txt_my_pg_point);
		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		adapter = new PointHistoryAdapter(getContext());
		recyclerView.setAdapter(adapter);

		netFunc.requestPointList(PlaygreenManager.getLanguagePG());
		return view;
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
			case APIKEY.POINT_LIST:
				txtMyPoint.setText(TextUtil.makeStringWithComma(json.MEMB_POINT + "", false)+"p");
				adapter.setItemArray(json.LIST);
				break;
		}
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
	}
}
