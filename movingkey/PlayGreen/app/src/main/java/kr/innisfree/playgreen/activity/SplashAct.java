package kr.innisfree.playgreen.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ParentAct;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.PREFKEY;
import com.moyusoft.util.DeviceUtil;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.PrefUtil;
import com.moyusoft.util.TextUtil;
import com.moyusoft.util.Util;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.NetworkConstantUtil.URLS;
import com.volley.network.NetworkController;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import java.util.HashMap;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.user.LoginAct;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.common.view.viewpagerindicator.CirclePageIndicator;
import kr.innisfree.playgreen.common.view.viewpagerindicator.PageIndicator;
import kr.innisfree.playgreen.fragment.IntroFrag;

/**
 * Created by jooyoung on 2016-02-16.
 */
public class SplashAct extends ParentAct {

	private final static String TAG = "SplashAct::";

	private LinearLayout layoutSplash;
	private LinearLayout layoutIntro;

	private ViewPager pager;
	private PageIndicator indicator;
	private MyPagerAdapter adapterPager;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLoading(this, true);
		setContentView(R.layout.act_splash);

		TextView txtSplash = (TextView) findViewById(R.id.txt_splash);
		if (Build.VERSION.SDK_INT >= 21) {
			txtSplash.setPadding(0, DeviceUtil.getStatusBarHeight(this), 0, 0);
		}

		layoutSplash = (LinearLayout) findViewById(R.id.layout_splash);
		layoutIntro = (LinearLayout) findViewById(R.id.layout_intro);
		layoutIntro.setVisibility(View.GONE);

		pager = (ViewPager) findViewById(R.id.pager);
		adapterPager = new MyPagerAdapter(getSupportFragmentManager());
		indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		pager.setAdapter(adapterPager);
		indicator.setViewPager(pager);

//		Intent intent = new Intent(this, MainAct.class);
//		startActivity(intent);
//		finish();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				requestVersionInfo();
			}
		}, 1600);

	}

	private Animation fadeout, fadein;

	public void startIntroAnimation() {
		fadeout = AnimationUtils.loadAnimation(this, R.anim.alpha_out);
		fadein = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
		fadein.setDuration(100);
		fadein.setStartOffset(900);
		fadeout.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				PrefUtil.getInstance().putPreference(PREFKEY.IS_VISIT_EXPERIENCE_BOOL, true);
				layoutSplash.setVisibility(View.GONE);
				layoutIntro.setVisibility(View.VISIBLE);
			}
		});
		layoutSplash.startAnimation(fadeout);
		layoutIntro.startAnimation(fadein);

	}

	public void checkServiceInfo(NetworkData data) {
		if (data == null) {
			return;
		}
		Definitions.APP_RECENT_VERSION = data.VERSION_CODE;
		int currentVersionCode = data.VERSION_CODE;
		int minimumVersionCode = data.MINIMUM_VERSION_CODE;
		int deviceVersionCode = DeviceUtil.getVersionCode(this);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		if (deviceVersionCode < minimumVersionCode) {
			builder.setMessage(R.string.str_new_version_message);
			builder.setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			builder.setPositiveButton(R.string.str_confirm, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
					marketLaunch.setData(Uri.parse("market://details?id=" + getPackageName()));
					startActivity(marketLaunch);
				}
			});
			builder.create().show();
		} else if (deviceVersionCode < currentVersionCode) {
			builder.setMessage(R.string.str_new_version_message);
			builder.setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					checkVisitInfo();
				}
			});
			builder.setPositiveButton(R.string.str_confirm, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
					marketLaunch.setData(Uri.parse("market://details?id=" + getPackageName()));
					startActivity(marketLaunch);
				}
			});
			builder.create().show();
		} else {
			checkVisitInfo();
		}
	}

	/**
	 * 처음 진입인 경우 인트로 화면으로, 아닌경우 로그인체크 후 로그인페이지 OR 메인페이지 분기
	 */
	public void checkVisitInfo() {
		boolean isVisited = PrefUtil.getInstance().getBooleanPreference(PREFKEY.IS_VISIT_EXPERIENCE_BOOL);
		if (isVisited) {
			JYLog.D(TAG + "isLogin::" + TextUtil.isNull(PlaygreenManager.getAuthToken()), null);
			if (!TextUtil.isNull(PlaygreenManager.getAuthToken()))
				requestLoginCheck();
			else
				gotoLoginAct();
		} else {
			startIntroAnimation();
		}
	}

	public void requestVersionInfo() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("OS", "A");
		StringRequest myReq = netUtil.requestPost(APIKEY.VERSION_INFO, URLS.VERSION_INFO, params);
		NetworkController.addToRequestQueue(myReq);
	}

	public void requestLoginCheck() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken());
		StringRequest myReq = netUtil.requestPost(APIKEY.LOGIN_CHECK, URLS.LOGIN_CHECK, params);
		NetworkController.addToRequestQueue(myReq);
	}

	public void gotoMainAct() {
		Intent i = new Intent(SplashAct.this, MainAct.class);
		Util.moveActivity(SplashAct.this, i, R.anim.alpha_in, R.anim.alpha_out, true, true);
	}

	public void gotoLoginAct() {
		Intent i = new Intent(SplashAct.this, LoginAct.class);
		Util.moveActivity(SplashAct.this, i, R.anim.alpha_in, R.anim.alpha_out, true, true);
	}

	@Override
	public void onNetworkResult(int idx, NetworkJson json) {
		super.onNetworkResult(idx, json);
		switch (idx) {
			case APIKEY.VERSION_INFO:
				checkServiceInfo(json.DATA);
				break;
			case APIKEY.LOGIN_CHECK:
				/** 로그인 성공, 메인페이지 이동 */
				if (json.DATA != null) {
					if (!TextUtil.isNull(json.DATA.PWD_UPDATE_YN) && json.DATA.PWD_UPDATE_YN.equals(Definitions.YN.YES)) {
						//TODO 비밀번호 변경 유도팝업
						Toast.makeText(getApplicationContext(), R.string.str_toast_change_password, Toast.LENGTH_LONG).show();
					}
					if (!TextUtil.isNull(json.DATA.TEMP_PWD_YN) && json.DATA.TEMP_PWD_YN.equals(Definitions.YN.YES)) {
						Toast.makeText(getApplicationContext(), R.string.str_toast_change_password_at_mypage, Toast.LENGTH_LONG).show();
					}
					PlaygreenManager.saveUserInfo(json.DATA);
					gotoMainAct();
				}
				break;
		}
	}

	@Override
	public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
		super.onNetworkError(idx, error, json);
		switch (idx) {
			case APIKEY.LOGIN_CHECK:
				/** 로그인 실패, 로그인페이지 이동 */
				gotoLoginAct();
//                gotoMainAct();
				break;
		}
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public Fragment getItem(int position) {
			return IntroFrag.newInstance(position);
		}
	}
}
