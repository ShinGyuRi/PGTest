package kr.innisfree.playgreen.fragment.camera;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ParentFrag;
import com.android.volley.VolleyError;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.dto.NetworkJson;

import java.util.ArrayList;
import java.util.List;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.CameraActivity;
import kr.innisfree.playgreen.widget.CustomTabLayout;
import kr.innisfree.playgreen.widget.CustomViewPager;

public class WrapperCameraFrag extends ParentFrag implements View.OnClickListener {

    private static final String TAG = "CameraActivity";

    public static CustomViewPager cameraVp;
    private CustomTabLayout tabLayoutCamera;
    private MyAdapter myAdapter;

    private static LibraryFrag libraryFrag;
    private static CameraFrag cameraFrag;

    private TabLayout.Tab libraryTab;
    private TabLayout.Tab cameraTab;
    private TabLayout.Tab cinemagraphTab;

    private GestureDetector gestureDetector;

    public static int duration = 10;

    public WrapperCameraFrag() {
    }

    public static WrapperCameraFrag newInstance() {
        WrapperCameraFrag frag = new WrapperCameraFrag();
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        libraryFrag = LibraryFrag.newInstance();
        cameraFrag = CameraFrag.newInstance();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_wrapper_camera, null);

        cameraVp = (CustomViewPager) view.findViewById(R.id.camera_vp);
        cameraVp.setPagingEnabled(true);
        tabLayoutCamera = (CustomTabLayout) view.findViewById(R.id.camera_tab);

        initializeViewPager();
        initializeTabLayout();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onNetworkResult(int idx, NetworkJson json) {
        super.onNetworkResult(idx, json);
        switch (idx) {
        }
    }

    @Override
    public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
        super.onNetworkError(idx, error, json);
        if (json == null || json.RESULT_CODE == null) return;
        switch (json.RESULT_CODE) {
            case NetworkConstantUtil.NETWORK_RESULT_CODE.NOT_EXIST_DATA:
                break;
        }
    }

    private void initializeViewPager() {

        myAdapter = new MyAdapter(getChildFragmentManager());
        if (CameraActivity.mTypeFromAct == CameraActivity.TYPE_FROM_PROFILE_EDIT_CAMERA) {
            myAdapter.addFragment(cameraFrag);
        } else if (CameraActivity.mTypeFromAct == CameraActivity.TYPE_FROM_PROFILE_EDIT_GALLERY) {
            myAdapter.addFragment(libraryFrag);
        } else {
            myAdapter.addFragment(libraryFrag);
            myAdapter.addFragment(cameraFrag);
        }

        cameraVp.setAdapter(myAdapter);
        cameraVp.setOffscreenPageLimit(2);
        cameraVp.setOverScrollMode(View.OVER_SCROLL_NEVER);
        cameraVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                JYLog.D(TAG + "::" + position, new Throwable());
                refreshViewPager(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        if (CameraActivity.mTypeFromAct == CameraActivity.TYPE_FROM_MAIN_ACT) {
            /** Gesture detection **/
            gestureDetector = new GestureDetector(getActivity(), new MyGestureDetector());
            View.OnTouchListener gestureListener = new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            };
            cameraVp.setOnTouchListener(gestureListener);
        }

    }

    private void initializeTabLayout() {

        libraryTab = tabLayoutCamera.newTab().setText(getString(R.string.str_camera_tab_library));
        cameraTab = tabLayoutCamera.newTab().setText(getString(R.string.str_camera_tab_camera));


        if (CameraActivity.mTypeFromAct == CameraActivity.TYPE_FROM_PROFILE_EDIT_CAMERA) {
            tabLayoutCamera.addTab(cameraTab);
        } else if (CameraActivity.mTypeFromAct == CameraActivity.TYPE_FROM_PROFILE_EDIT_GALLERY) {
            tabLayoutCamera.addTab(libraryTab);
        } else {
            tabLayoutCamera.addTab(libraryTab);
            tabLayoutCamera.addTab(cameraTab);
        }

        if (CameraActivity.mTypeFromAct == CameraActivity.TYPE_FROM_MAIN_ACT) {
            cinemagraphTab = tabLayoutCamera.newTab().setText(getString(R.string.str_camera_tab_cinemagraph));
            tabLayoutCamera.addTab(cinemagraphTab);
        }

        tabLayoutCamera.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab != null && !TextUtil.isNull(tab.getText().toString())) {

                    if (tab.getText().toString().equals(getString(R.string.str_camera_tab_library))) {
                        refreshViewPager(0);
                    } else if (tab.getText().toString().equals(getString(R.string.str_camera_tab_camera))) {
                        cameraFrag.setTypeCamera(cameraFrag.TYPE_CAMERA);
                        refreshViewPager(1);
                    } else {
                        cameraFrag.setTypeCamera(cameraFrag.TYPE_CINEMAGRAPH);
                        refreshViewPager(1);
                    }

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void refreshViewPager(int position) {

        if (cameraVp.getCurrentItem() != position) {
            cameraVp.setCurrentItem(position);
            return;
        }

        switch (position) {
            case 0:
                cameraVp.setPagingEnabled(true);
                if (!libraryTab.isSelected())
                    libraryTab.select();
                break;
            case 1:
                if (cameraFrag.getTypeCamera() == cameraFrag.TYPE_CAMERA) {
                    cameraVp.setPagingEnabled(true);
                    if (!cameraTab.isSelected())
                        cameraTab.select();
                } else {
                    cameraVp.setPagingEnabled(false);
                    if (!cinemagraphTab.isSelected())
                        cinemagraphTab.select();
                }
                break;
        }

    }

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (e1 == null || e2 == null)
                return false;

            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;

            // left to right swipe
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                if (cameraVp.getCurrentItem() == 1) {
                    if (cameraFrag.getTypeCamera() == cameraFrag.TYPE_CAMERA) {
                        cameraFrag.setTypeCamera(cameraFrag.TYPE_CINEMAGRAPH);
                        refreshViewPager(1);
                    }
                }
            }
            // right to left swipe
            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                if (cameraVp.getCurrentItem() == 1) {
                    if (cameraFrag.getTypeCamera() == cameraFrag.TYPE_CINEMAGRAPH) {
                        cameraFrag.setTypeCamera(cameraFrag.TYPE_CAMERA);
                        refreshViewPager(1);
                    }
                }
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

    public class MyAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
            mFragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

    }
}
