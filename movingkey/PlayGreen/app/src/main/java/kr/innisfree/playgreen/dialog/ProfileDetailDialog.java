package kr.innisfree.playgreen.dialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.moyusoft.util.BitmapCircleResize;
import com.moyusoft.util.Definitions.INTENT_KEY;
import com.moyusoft.util.TextUtil;
import com.moyusoft.util.Util;
import com.squareup.picasso.Picasso;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.NetworkConstantUtil.URLS;
import com.volley.network.NetworkController;
import com.volley.network.NetworkListener;
import com.volley.network.NetworkRequestUtil;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import net.yazeed44.imagepicker.ui.SpacesItemDecoration;

import java.util.HashMap;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.TimelineDetailAct;
import kr.innisfree.playgreen.adapter.TimelineGridAdapter;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;
import kr.innisfree.playgreen.listener.RecyclerOnScrollListener;

/**
 * Created by jooyoung on 2016-02-22.
 */
public class ProfileDetailDialog extends DialogFragment implements View.OnClickListener, NetworkListener, AdapterItemClickListener {

	private NetworkData userData;
	private CommonLoadingDialog loading;
	private ImageView imgProfile;
	private RecyclerView recyclerView;
	private TimelineGridAdapter timelineGridAdapter;
	private TextView txtName, txtIntroduce, txtListCount;
	private RecyclerOnScrollListener recyclerOnScrollListener;

	private int page;
	private NetworkData paging;

	public ProfileDetailDialog() {
	}

	@SuppressLint("ValidFragment")
	public ProfileDetailDialog(NetworkData userData) {
		this.userData = userData;
	}

	public static ProfileDetailDialog newInstance(NetworkData userData) {
		ProfileDetailDialog frag = new ProfileDetailDialog(userData);
		return frag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (getDialog() != null) {
			getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dlg_profile_detail, null);
		view.findViewById(R.id.btn_close).setOnClickListener(this);
		loading = new CommonLoadingDialog(getContext(), true);
		imgProfile = (ImageView) view.findViewById(R.id.img_profile);
		txtName = (TextView) view.findViewById(R.id.txt_user_nickname);
		txtIntroduce = (TextView) view.findViewById(R.id.txt_user_introduce);
		txtListCount = (TextView) view.findViewById(R.id.txt_list_count);
		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
		recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
		recyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_1)));
		timelineGridAdapter = new TimelineGridAdapter(getContext(), this, recyclerView);


		page = 1;

		onUIRefresh();
		requestTimeline();

		recyclerOnScrollListener = new RecyclerOnScrollListener() {
			@Override
			public void onLoadMore() {
				if (paging != null && paging.TOTAL_PAGE > page) {
					page += 1;
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							requestTimeline();
						}
					}, 300);
				}
			}
		};
		recyclerView.addOnScrollListener(recyclerOnScrollListener);
		recyclerView.setAdapter(timelineGridAdapter);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_close:
				dismiss();
				break;
		}
	}

	@Override
	public void onAdapterItemClick(View view, int position) {
		NetworkData data = timelineGridAdapter.getItem(position);
		if (data == null) return;
		Intent intent = new Intent(getActivity(), TimelineDetailAct.class);
		intent.putExtra(INTENT_KEY.TIMELINE_ID, data.TIMELINE_ID);
		Util.moveActivity(getActivity(), intent, -1, -1, false, false);
	}

	public void onUIRefresh() {
		if (userData == null) return;
		if (!TextUtil.isNull(userData.MEMB_IMG))
			Picasso.with(getContext()).load(userData.MEMB_IMG).error(R.drawable.img_user_null2)
					.transform(new BitmapCircleResize(getContext(), getResources().getDimensionPixelOffset(R.dimen.dp_64))).into(imgProfile);
		if (!TextUtil.isNull(userData.MEMB_NAME))
			txtName.setText(userData.MEMB_NAME);
		if (!TextUtil.isNull(userData.STATE_TEXT))
			txtIntroduce.setText(userData.STATE_TEXT);

	}

	public void requestTimeline() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
		params.put("CATEGORY", "M");
		params.put("MEMB_ID", userData.MEMB_ID);
		params.put("PAGE", page + "");

		NetworkRequestUtil netUtil = new NetworkRequestUtil(this);
		StringRequest myReq = netUtil.requestPost(APIKEY.TIMELINE_LIST, URLS.TIMELINE_LIST, params);
		NetworkController.addToRequestQueue(myReq);
	}

	@Override
	public void onNetworkStart(int idx) {
		loading.show();
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		loading.hide();
		switch (idx) {
			case APIKEY.TIMELINE_LIST:
				paging = json.PAGING;
				timelineGridAdapter.setItemArray(json.LIST);
				if (json.LIST != null) {
					txtListCount.setText(json.LIST.size() + "");
				}
				if (recyclerOnScrollListener != null)
					recyclerOnScrollListener.setLoading(true);
				break;
		}
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		loading.hide();
	}
}
