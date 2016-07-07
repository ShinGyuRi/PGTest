package kr.innisfree.playgreen.activity.search;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ParentAct;
import com.android.volley.VolleyError;
import com.moyusoft.util.TextUtil;
import com.volley.network.dto.NetworkJson;

import org.greenrobot.eventbus.EventBus;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.data.PlayGreenEvent;
import kr.innisfree.playgreen.data.PlayGreenEvent.EVENT_TYPE;
import kr.innisfree.playgreen.fragment.search.SearchTagFrag;
import kr.innisfree.playgreen.fragment.search.SearchUserFrag;

/**
 * Created by jooyoung on 2016-04-18.
 */
public class SearchAct extends ParentAct implements View.OnClickListener, ViewPager.OnPageChangeListener,TextWatcher {

	private Toolbar mToolbar;
	private ViewPager pager;
	private MyPagerAdapter pagerAdapter;
	private EditText editSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_search);
		setLoading(this, true);

		pager = (ViewPager) findViewById(R.id.viewPager);
		pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(pagerAdapter);
		pager.addOnPageChangeListener(this);
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
		tabLayout.setupWithViewPager(pager);

		findViewById(R.id.layout_back).setOnClickListener(this);
		findViewById(R.id.btn_delete).setOnClickListener(this);
		editSearch = (EditText) findViewById(R.id.edit_search);
		editSearch.addTextChangedListener(this);
		editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					String keyword = editSearch.getText().toString();
					if (!TextUtil.isNull(keyword)) {
						if(pager.getCurrentItem()==0)
							EventBus.getDefault().post(new PlayGreenEvent(EVENT_TYPE.ACTION_SEARCH_TAG, keyword));
						else
							EventBus.getDefault().post(new PlayGreenEvent(EVENT_TYPE.ACTION_SEARCH_USER, keyword));
					}
					return false;
				}
				return true;
			}
		});
	}

	public void setSearchKeyword(String keyword){
		editSearch.setText(keyword);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.layout_back:
				onBackPressed();
				break;
			case R.id.btn_delete:
				editSearch.setText("");
				editSearch.requestFocus();
				if(pager.getCurrentItem()==0)
					EventBus.getDefault().post(new PlayGreenEvent(EVENT_TYPE.ACTION_SEARCH_TAG));
				else
					EventBus.getDefault().post(new PlayGreenEvent(EVENT_TYPE.ACTION_SEARCH_USER));
				break;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String keyword = editSearch.getText().toString();
		if (!TextUtil.isNull(keyword) && TextUtil.isNameCheck(keyword)) {
			if(pager.getCurrentItem()==0)
				EventBus.getDefault().post(new PlayGreenEvent(EVENT_TYPE.ACTION_SEARCH_TAG, keyword));
			else
				EventBus.getDefault().post(new PlayGreenEvent(EVENT_TYPE.ACTION_SEARCH_USER, keyword));
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		if (position == 0) {
			editSearch.setHint(R.string.str_tag_search);
		} else {
			editSearch.setHint(R.string.str_user_search);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		String[] pagerTitle;

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			pagerTitle = getResources().getStringArray(R.array.array_search_pager);
		}

		@Override
		public int getCount() {
			return pagerTitle.length;
		}

		@Override
		public Fragment getItem(int position) {
			if(position==0)
				return SearchTagFrag.newInstance();
			else
				return SearchUserFrag.newInstance();
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
