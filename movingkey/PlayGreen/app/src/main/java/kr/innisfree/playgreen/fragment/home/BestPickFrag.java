package kr.innisfree.playgreen.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ParentFrag;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.moyusoft.util.BitmapCircleResize;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.BESTPICK_TYPE;
import com.moyusoft.util.TextUtil;
import com.moyusoft.util.Util;
import com.squareup.picasso.Picasso;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.NetworkConstantUtil.NETWORK_RESULT_CODE;
import com.volley.network.NetworkConstantUtil.URLS;
import com.volley.network.NetworkController;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.BridgeAct;
import kr.innisfree.playgreen.activity.TimelineDetailAct;
import kr.innisfree.playgreen.activity.search.ProfileDetailAct;
import kr.innisfree.playgreen.activity.search.SearchAct;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.common.view.viewpagerindicator.CirclePageIndicator;
import kr.innisfree.playgreen.common.view.viewpagerindicator.PageIndicator;

/**
 * Created by jooyoung on 2016-03-21.
 */
public class BestPickFrag extends ParentFrag implements View.OnClickListener {

	private ArrayList<NetworkData> todayPickArray, bestPickArray, superGreenerArray;

	private View view;
	private TextView txtTitle;
	private LinearLayout layoutTodayPick, layoutBestPick, layoutSuperGreenerPick;

	private ViewPager todayPickPager;
	private MyPagerAdapter adapterPager;
	private PageIndicator indicator;
	private BestPickUIMaker bestPickUIMaker;
	private SuperGreenerUIMaker superGreenerUIMaker;

	public BestPickFrag() {
	}

	public static BestPickFrag newInstance() {
		BestPickFrag frag = new BestPickFrag();
		return frag;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_best_pick, null);
		setCutOffBackgroundTouch(view);
		initToolbar();

		layoutTodayPick = (LinearLayout) view.findViewById(R.id.layout_todaypick);
		layoutBestPick = (LinearLayout) view.findViewById(R.id.layout_bestpick);
		layoutSuperGreenerPick = (LinearLayout) view.findViewById(R.id.layout_supergreenerpick);

		todayPickPager = (ViewPager) view.findViewById(R.id.viewpager);
		indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
		if (adapterPager == null) adapterPager = new MyPagerAdapter();
		todayPickPager.setAdapter(adapterPager);

		indicator.setViewPager(todayPickPager);

	//	if (bestPickUIMaker == null)
			bestPickUIMaker = new BestPickUIMaker(getActivity(), layoutBestPick, viewClickListener);
	//	if (superGreenerUIMaker == null)
			superGreenerUIMaker = new SuperGreenerUIMaker(getActivity(), layoutSuperGreenerPick, viewClickListener);

		if(todayPickArray == null && bestPickArray == null && superGreenerArray == null)
			requestBestpick();
		else
			onUIRefresh();

		return view;
	}

	private void initToolbar() {
		Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
		view.findViewById(R.id.layout_back).setOnClickListener(this);
		view.findViewById(R.id.btn_option).setOnClickListener(this);
		txtTitle = (TextView)view.findViewById(R.id.txt_title);
		txtTitle.setText(R.string.str_best_pick);
		if(Definitions.Gotham != null)	txtTitle.setTypeface(Definitions.Gotham);
		((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.layout_back:
				getActivity().onBackPressed();
				break;
			case R.id.btn_option:
				Intent intent = new Intent(getActivity(), SearchAct.class);
				Util.moveActivity(getActivity(), intent, 0, 0, false, false);
				break;
		}
	}

	private View.OnClickListener viewClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			NetworkData data = null;
			Intent intent = null;
			switch (v.getId()) {
				case R.id.img_today_profile:
				case R.id.img_bestpick_profile_item:
					if (v.getTag() == null) return;
					data = (NetworkData) v.getTag();
					intent = new Intent(getActivity(), ProfileDetailAct.class);
					intent.putExtra(Definitions.INTENT_KEY.MEMB_ID, data.MEMB_ID);
					Util.moveActivity(getActivity(), intent, -1, -1, false, false);
					break;
				case R.id.id_todayspick_item:
					if (v.getTag() == null) return;
					data = (NetworkData) v.getTag();
					intent = new Intent(getActivity(), TimelineDetailAct.class);
					intent.putExtra(Definitions.INTENT_KEY.TIMELINE_ID, data.TIMELINE_ID);
					Util.moveActivity(getActivity(), intent, -1, -1, false, false);
					break;
				case R.id.layout_bestpick_more:
					bestPickUIMaker.openMore(true);
					break;
				case R.id.id_bestpick_item:
					if (v.getTag() == null) return;
					data = (NetworkData) v.getTag();
					intent = new Intent(getActivity(), TimelineDetailAct.class);
					intent.putExtra(Definitions.INTENT_KEY.TIMELINE_ID, data.TIMELINE_ID);
					Util.moveActivity(getActivity(), intent, -1, -1, false, false);
					break;
				case R.id.layout_supergreener_more:
					superGreenerUIMaker.makeLankView(true);
					break;
				case R.id.id_supergreener_item:
					if (v.getTag() == null) return;
					data = (NetworkData) v.getTag();
					intent = new Intent(getActivity(), BridgeAct.class);
					intent.putExtra(Definitions.INTENT_KEY.DATA, Definitions.GOTO.PG21);
					intent.putExtra(Definitions.INTENT_KEY.MEMB_ID, data.MEMB_ID);
					Util.moveActivity(getActivity(), intent, -1, -1, false, false);
					break;
				case R.id.btn_supergreener_info:
					superGreenerUIMaker.toggleTooltip();
					break;
			}
		}
	};


	public void requestBestpick() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
		StringRequest myReq = netUtil.requestPost(APIKEY.BEST_PICK, URLS.BEST_PICK, params);
		NetworkController.addToRequestQueue(myReq);
	}

	@Override
	public void onUIRefresh() {
		super.onUIRefresh();
		if (todayPickArray == null || todayPickArray.size() == 0) {
			layoutTodayPick.setVisibility(View.GONE);
		} else {
			adapterPager.notifyDataSetChanged();
		}
		if (bestPickArray == null || bestPickArray.size() == 0)
			layoutBestPick.setVisibility(View.GONE);
		else{
			bestPickUIMaker.setData(bestPickArray);
		}


		if (superGreenerArray == null || superGreenerArray.size() == 0)
			layoutSuperGreenerPick.setVisibility(View.GONE);
		else
			superGreenerUIMaker.setData(superGreenerArray);

	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
			case APIKEY.BEST_PICK:
				if (json.LIST == null) return;
				todayPickArray = new ArrayList<NetworkData>();
				bestPickArray = new ArrayList<NetworkData>();
				superGreenerArray = new ArrayList<NetworkData>();

				for (NetworkData data : json.LIST) {
					if (!TextUtil.isNull(data.CATEGORY)) {
						switch (data.CATEGORY) {
							case BESTPICK_TYPE.TODAY:
								todayPickArray.add(data);
								break;
							case BESTPICK_TYPE.BEST:
								bestPickArray.add(data);
								break;
							case BESTPICK_TYPE.SUPERGREENER:
								superGreenerArray.add(data);
								break;
						}
					}
				}
				onUIRefresh();
				break;
		}
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
		if (json == null) return;
		switch (json.RESULT_CODE) {
			case NETWORK_RESULT_CODE.NOT_EXIST_DATA:
				break;
		}
	}


	public class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			if (todayPickArray == null) return 0;
			else return todayPickArray.size();
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			if (todayPickArray.size() <= position) return null;

			View view = LayoutInflater.from(getContext()).inflate(R.layout.view_pick_today_item, null);
			MyPagerAdapter.ViewHolder vh = new ViewHolder(view);

			NetworkData data = todayPickArray.get(position);
			if (!TextUtil.isNull(data.TIMELINE_IMG)) {
				Picasso.with(getContext()).load(data.TIMELINE_IMG).into(vh.imgContent);
			}
			if (!TextUtil.isNull(data.MEMB_IMG)) {
				Picasso.with(getContext()).load(data.MEMB_IMG).error(R.drawable.img_user_null2)
						.transform(new BitmapCircleResize(getContext(), getResources().getDimensionPixelOffset(R.dimen.dp_84))).into(vh.imgProfile);
			}

			/** 프로필 뱃지 */
			boolean isSuperGreener = false, isBestGreener = false;
			if(!TextUtils.isEmpty(data.SUPERGREENER_YN) && data.SUPERGREENER_YN.equals(Definitions.YN.YES))
				isSuperGreener = true;
			if(!TextUtils.isEmpty(data.BESTGREENER_YN) && data.BESTGREENER_YN.equals(Definitions.YN.YES))
				isBestGreener = true;
			if(isSuperGreener || isBestGreener){
				vh.imgBadge.setVisibility(View.VISIBLE);
				if(isSuperGreener && isBestGreener)
					vh.imgBadge.setBackgroundResource(R.drawable.icon_badge_both);
				else if(isSuperGreener)
					vh.imgBadge.setBackgroundResource(R.drawable.icon_badge_sg);
				else if(isBestGreener)
					vh.imgBadge.setBackgroundResource(R.drawable.icon_badge_bestpg);
			}else{
				vh.imgBadge.setVisibility(View.GONE);
			}


			if (!TextUtil.isNull(data.MEMB_NAME)) {
				vh.txtName.setText(data.MEMB_NAME);
			}
			vh.txtDate.setText(PlaygreenManager.getTimeStampToDate(data.REG_DT, true));
			if (!TextUtil.isNull(data.LOCATION)) {
//				vh.txtDate.append(" | " + PlaygreenManager.getCountryName(data.LOCATION));
				try {
					vh.txtDate.append(" | " + URLDecoder.decode(data.LOCATION, "utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}

			vh.imgProfile.setTag(data);
			vh.imgProfile.setOnClickListener(viewClickListener);

			view.setId(R.id.id_todayspick_item);
			view.setTag(data);
			view.setOnClickListener(viewClickListener);

			((ViewPager) container).addView(view, 0);

			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			//TODO ??
			((ViewPager) container).removeView((View) view);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		public class ViewHolder extends RecyclerView.ViewHolder {
			public ImageView imgContent, imgProfile, imgBadge;
			public TextView txtName, txtDate;

			public ViewHolder(View v) {
				super(v);
				imgContent = (ImageView) v.findViewById(R.id.img_today_content);
				imgProfile = (ImageView) v.findViewById(R.id.img_today_profile);
				imgBadge = (ImageView) v.findViewById(R.id.img_today_badge);
				txtName = (TextView) v.findViewById(R.id.txt_today_name);
				txtDate = (TextView) v.findViewById(R.id.txt_today_info);
			}
		}
	}
}
