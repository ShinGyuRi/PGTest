package kr.innisfree.playgreen.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ParentAct;
import com.facebook.FacebookSdk;
import com.gcm.GcmUtil;
import com.gcm.RegistrationIntentService;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.ACTIVITY_REQUEST_CODE;
import com.moyusoft.util.Definitions.INTENT_KEY;
import com.moyusoft.util.Definitions.MAIN_TAB;
import com.moyusoft.util.Definitions.PREFKEY;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.PrefUtil;
import com.moyusoft.util.TextUtil;
import com.moyusoft.util.Util;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.dto.NetworkJson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

import bolts.AppLinks;
import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.data.PlayGreenEvent;
import kr.innisfree.playgreen.dialog.GuideDialog;
import kr.innisfree.playgreen.fragment.CommentListFrag;
import kr.innisfree.playgreen.fragment.LikeListFrag;
import kr.innisfree.playgreen.fragment.ReportFrag;
import kr.innisfree.playgreen.fragment.home.HomeFrag;
import kr.innisfree.playgreen.fragment.main.MyPageFrag;
import kr.innisfree.playgreen.fragment.main.TimeLinePagerFrag;

/**
 * Created by jooyoung on 2016-02-16.
 */
public class MainAct extends ParentAct implements View.OnClickListener {

    private final static String TAG = "MainAct::";

    public LinearLayout layoutBottomNavi;
    public FrameLayout layoutNaviHome, layoutNaviTimeline, layoutNaviCamera, layoutNaviMypage;
    public Fragment homeFrag, timelinePagerFrag, mypageFrag;
    private Stack<Fragment> mainFragmentStack;
    private TextView txtBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        setLoading(this, true);
        if (EventBus.getDefault().isRegistered(this) == false) EventBus.getDefault().register(this);
        if (mainFragmentStack == null)
            mainFragmentStack = new Stack<Fragment>();

        txtBadge = (TextView) findViewById(R.id.txt_badge);
        txtBadge.setVisibility(View.GONE);
        layoutBottomNavi = (LinearLayout) findViewById(R.id.layout_navi);
        layoutNaviHome = (FrameLayout) findViewById(R.id.layout_navi_home);
        layoutNaviTimeline = (FrameLayout) findViewById(R.id.layout_navi_timeline);
        layoutNaviCamera = (FrameLayout) findViewById(R.id.layout_navi_camera);
        layoutNaviMypage = (FrameLayout) findViewById(R.id.layout_navi_mypage);
        layoutNaviHome.setOnClickListener(this);
        layoutNaviTimeline.setOnClickListener(this);
        layoutNaviCamera.setOnClickListener(this);
        layoutNaviMypage.setOnClickListener(this);

        pushCheck();
        checkGCM();
        initalize();

        FacebookSdk.sdkInitialize(this);
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            JYLog.D("target uri : "+targetUrl.toString(),new Throwable());
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        }

    }

    public void pushCheck() {
        boolean isPushReceive = getIntent().getBooleanExtra(INTENT_KEY.IS_FROM_PUSH, false);
        if (isPushReceive) {
            Intent intent = new Intent(this, PushReceiveAct.class);
            intent.putExtra(INTENT_KEY.INFO_LINK, getIntent().getStringExtra(INTENT_KEY.INFO_LINK));
            intent.putExtra(INTENT_KEY.MEMB_ID, getIntent().getStringExtra(INTENT_KEY.MEMB_ID));
            intent.putExtra(INTENT_KEY.TIMELINE_ID, getIntent().getStringExtra(INTENT_KEY.TIMELINE_ID));
            Util.moveActivity(this, intent, -1, -1, false, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        netFunc.requestAlarmCount();
    }

    public void initalize() {
        homeFrag = HomeFrag.newInstance();
        switchNaviContent(homeFrag);

        /** 가이드 뷰 */
        boolean isGuideViewNotShow = PrefUtil.getInstance().getBooleanPreference(PREFKEY.IS_GUIDE_VIEW_NEVER_SEE_BOOL);
        if (isGuideViewNotShow == false) {
            Fragment guideDialog = GuideDialog.newInstance(false);
            dialogShow(guideDialog, "GuideDialog");
        }

        /** 커버 뷰 */
        String coverViewDate = PrefUtil.getInstance().getStringPreference(PREFKEY.COVER_PAGE_VIEW_DATE_STR);
        //테스트용으로 항상띄움
        //coverViewDate = null;
        if (TextUtil.isNull(coverViewDate)) {
            gotoCoverPage();
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            try {
                Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
                boolean bool = today.after(simpleDateFormat.parse(coverViewDate));
                if (bool) gotoCoverPage();
                //JYLog.D(bool + ", " + simpleDateFormat.parse(coverViewDate).toString() + ", " + today.toString(), new Throwable());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void gotoCoverPage() {
        Intent i = new Intent(this, CoverAct.class);
        Util.moveActivity(this, i, -1, -1, false, false);
    }

    public void gotoLikeList(String category, String targetId) {
        Fragment fragment = LikeListFrag.newInstance(category, targetId);
        switchContent(fragment, R.id.container_full, true, false);
    }

    public void gotoCommentList(String category, String targetId) {
        Fragment fragment = CommentListFrag.newInstance(category, targetId);
        switchContent(fragment, R.id.container_full, true, false);
    }

    public void gotoReport(String category, String targetId) {
        Fragment fragment = ReportFrag.newInstance(category, targetId);
        switchContent(fragment, R.id.container_full, true, false);
    }

    public void gotoTimeline() {
        Fragment fragment = TimeLinePagerFrag.newInstance(true);
        switchNaviContent(fragment);
        //switchContent(fragment, R.id.container, true, false);
    }

    public void timelinePerformClick() {
        layoutNaviTimeline.performClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_navi_home:
                if (homeFrag == null) homeFrag = HomeFrag.newInstance();
                if (((HomeFrag) homeFrag).homeFragmentStack.size() > 1) {
                    ((HomeFrag) homeFrag).clearHomeStack();
                    if (mainFragmentStack.contains(homeFrag)) {
                        mainFragmentStack.remove(homeFrag);
                    }
                    homeFrag = HomeFrag.newInstance();
                }
                switchNaviContent(homeFrag);
                break;
            case R.id.layout_navi_timeline:
                if (timelinePagerFrag == null)
                    timelinePagerFrag = TimeLinePagerFrag.newInstance(false);
                switchNaviContent(timelinePagerFrag);
                break;
            case R.id.layout_navi_mypage:
                if (mypageFrag == null) mypageFrag = MyPageFrag.newInstance(null);
                switchNaviContent(mypageFrag);
                break;
            case R.id.layout_navi_camera:
                gotoCameraActivity(CameraActivity.TYPE_FROM_MAIN_ACT);
                break;
        }
    }

    public void switchNaviContent(Fragment fragment) {
        String tag = null;
        String fragName = fragment.getClass().getSimpleName();
        if (fragName.equals(HomeFrag.class.getSimpleName())) {
            tag = MAIN_TAB.HOME;
        } else if (fragName.equals(TimeLinePagerFrag.class.getSimpleName())) {
            tag = MAIN_TAB.TIMELINE;
        } else if (fragName.equals(MyPageFrag.class.getSimpleName())) {
            tag = MAIN_TAB.MYPAGE;
        } else {
        }
        if (tag == null) return;
        refreshNaviButton(tag);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (mainFragmentStack.contains(fragment)) {
            mainFragmentStack.remove(fragment);
        }
//		JYLog.D(TAG + "switch::size::" + mainFragmentStack.size(), null);
        mainFragmentStack.push(fragment);
        ft.replace(R.id.container, fragment).commit();
    }

    public void refreshNaviButton(String tag) {
        layoutNaviHome.setEnabled(true);
        layoutNaviHome.setSelected(false);
        layoutNaviTimeline.setEnabled(true);
        layoutNaviMypage.setEnabled(true);
        switch (tag) {
            case MAIN_TAB.HOME:
//                layoutNaviHome.setEnabled(false);
                layoutNaviHome.setSelected(true);
                break;
            case MAIN_TAB.TIMELINE:
                layoutNaviTimeline.setEnabled(false);
                break;
            case MAIN_TAB.MYPAGE:
                layoutNaviMypage.setEnabled(false);
                break;
        }
    }

    @Subscribe
    public void onEvent(PlayGreenEvent event) {
        if (event.getEvent() == PlayGreenEvent.EVENT_TYPE.ALARM_COUNT_UPDATE) {
            if (Definitions.ALARM_COUNT > 0) {
                txtBadge.setText(Definitions.ALARM_COUNT + "");
                txtBadge.setVisibility(View.VISIBLE);
            } else {
                txtBadge.setVisibility(View.GONE);
            }
            PlaygreenManager.updateIconBadge(this);
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        JYLog.D("cnt: " + count, new Throwable());
        if (count > 0) {
            super.onBackPressed();
        } else {
            if (mainFragmentStack != null && mainFragmentStack.size() > 0) {
                if (mainFragmentStack.lastElement() instanceof HomeFrag) {
                    if (homeFrag != null && ((HomeFrag) homeFrag).homeFragmentStack.size() > 0) {
                        controlHomeFragStack();
                    } else {
                        controlMainFragStack();
                    }
                } else {
                    controlMainFragStack();
                }
            } else {
                controlHomeFragStack();
            }
        }
    }

    private void controlHomeFragStack() {
        if (((HomeFrag) homeFrag).homeFragmentStack != null && ((HomeFrag) homeFrag).homeFragmentStack.size() > 0) {
            if (((HomeFrag) homeFrag).homeFragmentStack.contains(((HomeFrag) homeFrag).homeFragmentStack.lastElement())) {
                ((HomeFrag) homeFrag).homeFragmentStack.remove(((HomeFrag) homeFrag).homeFragmentStack.lastElement());
            }
        }
        if (((HomeFrag) homeFrag).homeFragmentStack.size() == 0) {
            if (mainFragmentStack != null && mainFragmentStack.size() > 0) {
                controlMainFragStack();
            } else {
                super.onBackPressed(); //종료
            }
        } else {
            Fragment fragment = ((HomeFrag) homeFrag).homeFragmentStack.lastElement();
            ((HomeFrag) homeFrag).switchNaviContent(fragment);
        }
    }

    private void controlMainFragStack() {
        if (mainFragmentStack.contains(mainFragmentStack.lastElement())) {
            mainFragmentStack.remove(mainFragmentStack.lastElement());
        }
        JYLog.D(TAG + "onBackPressed::size::" + mainFragmentStack.size(), null);
        if (mainFragmentStack.size() == 0) {
            super.onBackPressed(); //종료
        } else {
            Fragment fragment = mainFragmentStack.lastElement();
            switchNaviContent(fragment);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTIVITY_REQUEST_CODE.IS_LOGOUT:
                if (resultCode == RESULT_OK) {
                    finish();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void checkGCM() {
        if (TextUtil.isNull(PlaygreenManager.getAuthToken())) return;
        if (GcmUtil.checkPlayServices(this)) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (activityList != null) activityList.clear();
        activityList = null;
    }

    @Override
    public void onNetworkResult(int idx, NetworkJson json) {
        super.onNetworkResult(idx, json);
        switch (idx) {
            case NetworkConstantUtil.APIKEY.INFO_COUNT:
                Definitions.ALARM_COUNT = json.INFO_COUNT;
                EventBus.getDefault().post(new PlayGreenEvent(PlayGreenEvent.EVENT_TYPE.ALARM_COUNT_UPDATE));
                break;
        }
    }
}
