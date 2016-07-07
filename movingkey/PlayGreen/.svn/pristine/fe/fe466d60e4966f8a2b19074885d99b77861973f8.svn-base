package kr.innisfree.playgreen.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ParentAct;
import com.android.volley.VolleyError;
import com.moyusoft.util.Definitions.INTENT_KEY;
import com.moyusoft.util.Util;
import com.volley.network.dto.NetworkJson;

import org.greenrobot.eventbus.EventBus;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.MainAct;
import kr.innisfree.playgreen.data.PlayGreenEvent;
import kr.innisfree.playgreen.fragment.FriendContactFrag;
import kr.innisfree.playgreen.fragment.FriendRecommendPagerFrag;
import kr.innisfree.playgreen.fragment.FriendSnsFrag;

/**
 * Created by jooyoung on 2016-02-16.
 */
public class FriendAddAct extends ParentAct {

	private Toolbar mToolbar;
	private boolean isFromLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_add_friend);
		setLoading(this);

		isFromLogin = getIntent().getBooleanExtra(INTENT_KEY.FROM_LOGIN_BOOL, false);

		initToolbar();
		initViewPagerAndTabs();

	}

	public void gotoMain(){
		Intent i = new Intent(this, MainAct.class);
		Util.moveActivity(this, i, -1, -1, true, false);
	}

	private void initToolbar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		((TextView) findViewById(R.id.txt_title)).setText(R.string.str_title_add_friend);
		if(isFromLogin){
			findViewById(R.id.layout_back).setVisibility(View.INVISIBLE);
			findViewById(R.id.btn_option).setVisibility(View.VISIBLE);
			((Button) findViewById(R.id.btn_option)).setText(R.string.str_skip);
			findViewById(R.id.btn_option).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					gotoMain();
				}
			});
		}else{
			findViewById(R.id.layout_back).setVisibility(View.VISIBLE);
			findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onBackPressed();
				}
			});
			findViewById(R.id.btn_option).setVisibility(View.INVISIBLE);
		}
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	public void setToolbarScrollFlag(boolean isScroll) {
		AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
		params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
	}


	private void initViewPagerAndTabs() {
		final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
		tabLayout.addTab(tabLayout.newTab().setText(R.string.str_recommend_friend));
		tabLayout.addTab(tabLayout.newTab().setText(R.string.str_sns_friend));
		tabLayout.addTab(tabLayout.newTab().setText(R.string.str_contact_friend));
		tabSwitch(0);
		tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				int tabPostion = tabLayout.getSelectedTabPosition();
				tabSwitch(tabPostion);
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {
			}
		});
//		tabLayout.setupWithViewPager(pager);
	}

	private Fragment tabFragment01, tabFragment02, tabFragment03;

	public void tabSwitch(int tabPosition) {
		switch (tabPosition) {
			case 0:
				if (tabFragment01 == null)
					tabFragment01 = FriendRecommendPagerFrag.newInstance();
				switchContent(tabFragment01, R.id.container, false, false);
				break;
			case 1:
				if (tabFragment02 == null)
					tabFragment02 = FriendSnsFrag.newInstance();
				switchContent(tabFragment02, R.id.container, false, false);
				break;
			case 2:
				if (tabFragment03 == null)
					tabFragment03 = FriendContactFrag.newInstance();
				switchContent(tabFragment03, R.id.container, false, false);
				break;
		}
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
	}

	@Override
	public void onBackPressed() {
		if(isFromLogin){
			gotoMain();
			return;
		}
		EventBus.getDefault().post(new PlayGreenEvent(PlayGreenEvent.EVENT_TYPE.TIMELINE_REFRESH));
		super.onBackPressed();
	}
}

