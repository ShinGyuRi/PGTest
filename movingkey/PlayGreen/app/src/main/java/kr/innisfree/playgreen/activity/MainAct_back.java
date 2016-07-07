package kr.innisfree.playgreen.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ParentAct;
import com.moyusoft.util.JYLog;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.fragment.home.HomeFrag;
import kr.innisfree.playgreen.fragment.main.MyPageFrag;
import kr.innisfree.playgreen.fragment.main.TimeLinePagerFrag;

/**
 * Created by jooyoung on 2016-02-16.
 */
public class MainAct_back extends ParentAct implements View.OnClickListener {

	public LinearLayout layoutBottomNavi;
	public FrameLayout layoutNaviHome, layoutNaviTimeline, layoutNaviCamera, layoutNaviMypage;
	public Fragment homeFrag, timelineFrag, mypageFrag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);
		setLoading(this);

		layoutBottomNavi = (LinearLayout) findViewById(R.id.layout_navi);
		layoutNaviHome = (FrameLayout) findViewById(R.id.layout_navi_home);
		layoutNaviTimeline = (FrameLayout) findViewById(R.id.layout_navi_timeline);
		layoutNaviCamera = (FrameLayout) findViewById(R.id.layout_navi_camera);
		layoutNaviMypage = (FrameLayout) findViewById(R.id.layout_navi_mypage);
		layoutNaviHome.setOnClickListener(this);
		layoutNaviTimeline.setOnClickListener(this);
		layoutNaviCamera.setOnClickListener(this);
		layoutNaviMypage.setOnClickListener(this);

		//Initialize
		homeFrag = HomeFrag.newInstance();
		getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFrag,"HOME").addToBackStack("HOME").commit();
		layoutNaviHome.setEnabled(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getSupportFragmentManager().addOnBackStackChangedListener(backStackChangedListener);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.layout_navi_home:
				initNaviButton(v);
				if (homeFrag == null)
					homeFrag = HomeFrag.newInstance();
				switchContent(homeFrag, "HOME");
				break;
			case R.id.layout_navi_timeline:
				initNaviButton(v);
				if (timelineFrag == null)
					timelineFrag = TimeLinePagerFrag.newInstance(false);
				switchContent(timelineFrag, "TIMELINE");
				break;
			case R.id.layout_navi_mypage:
				initNaviButton(v);
				if (mypageFrag == null)
					mypageFrag = MyPageFrag.newInstance(null);
				switchContent(mypageFrag, "MYPAGE");
				break;
			case R.id.layout_navi_camera:

				break;

		}
	}

	public void initNaviButton(View v) {
		layoutNaviHome.setEnabled(true);
		layoutNaviTimeline.setEnabled(true);
		layoutNaviMypage.setEnabled(true);
		v.setEnabled(false);
	}

	public void switchContent(Fragment fragment, String tag) {
		FragmentManager fm = getSupportFragmentManager();

		JYLog.D("back stack cnt:" + fm.getBackStackEntryCount(),new Throwable());
		for (Fragment fragment1 : fm.getFragments()) {
			if (fragment1 != null && fragment1.getTag().equals(tag)) {
				//이렇게 해도 직전 프래그먼트만 pop됨 ㅡㅡ
				boolean result = fm.popBackStackImmediate(tag,FragmentManager.POP_BACK_STACK_INCLUSIVE);
				JYLog.D(tag +" , rst:"+result + ","+fm.getBackStackEntryCount(),new Throwable());
				break;
			}
		}

		FragmentTransaction ft = fm.beginTransaction();
		ft.addToBackStack(tag);
		ft.replace(R.id.container, fragment, tag).commit();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		super.onPause();
		getSupportFragmentManager().removeOnBackStackChangedListener(backStackChangedListener);
	}

	private FragmentManager.OnBackStackChangedListener backStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
		@Override
		public void onBackStackChanged() {
			String currentTab = getSupportFragmentManager().findFragmentById(R.id.container).getTag();
			JYLog.D(currentTab, new Throwable());
			if(currentTab==null) return;
			switch(currentTab){
				case "HOME":
					initNaviButton(layoutNaviHome);
					break;
				case "TIMELINE":
					initNaviButton(layoutNaviTimeline);
					break;
				case "MYPAGE":
					initNaviButton(layoutNaviMypage);
					break;
			}
		}
	};

}
