package kr.innisfree.playgreen.activity.setting;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ParentAct;
import com.android.volley.VolleyError;
import com.volley.network.dto.NetworkJson;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.fragment.setting.HelpFrag;

/**
 * Created by jooyoung on 2016-04-18.
 */
public class HelpAct extends ParentAct {

	public static final int TYPE_HELP_FAQ = 0;
	public static final int TYPE_HELP_INTRODUCE_SERVICE = 1;
	public static final int TYPE_HELP_HOW_TO_USE = 2;

	private Toolbar mToolbar;
	private ViewPager pager;
	private MyPagerAdapter pagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_simple_pager);
		setLoading(this);

		pager = (ViewPager) findViewById(R.id.viewPager);
		pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(pagerAdapter);

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		((TextView)findViewById(R.id.txt_title)).setText(R.string.str_setting_help);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
		tabLayout.setupWithViewPager(pager);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		String[] pagerTitle;

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			pagerTitle = getResources().getStringArray(R.array.array_help_pager);
		}

		@Override
		public int getCount() {
			return pagerTitle.length;
		}

		@Override
		public Fragment getItem(int position) {
			return HelpFrag.newInstance(position);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return pagerTitle[position];
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
}
