package kr.innisfree.playgreen.activity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;

import com.ParentAct;
import com.airfactory.service.GPSService;
import com.android.volley.toolbox.StringRequest;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.NetworkController;
import com.volley.network.NetworkRequestUtil;
import com.volley.network.dto.NetworkData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.fragment.camera.CameraCgToolFrag;
import kr.innisfree.playgreen.fragment.camera.CameraFilterFrag;
import kr.innisfree.playgreen.fragment.camera.CameraFrag;
import kr.innisfree.playgreen.fragment.camera.CameraUploadFrag;
import kr.innisfree.playgreen.fragment.camera.SearchLocationFrag;

public class CameraUploadActivity extends ParentAct {

    private final static String TAG = "CameraUploadActivity";

    public boolean isCurrentUploadFrag = false;
    private CameraUploadFrag cameraUploadFrag;
    private static CameraCgToolFrag cameraCgToolFrag;

    public NetworkData location;

    /**
     * from intent
     **/
    private int type;
    private String absolutePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_upload);

        type = getIntent().getIntExtra(Definitions.INTENT_KEY.CAMERA_TYPE, -1);
        absolutePath = getIntent().getStringExtra(Definitions.INTENT_KEY.CAMERA_FILE_ABSOLUTE_PATH);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    public void gotoCameraUploadFrag(String absolutePath, String overlayAbsolutePath, String firstFrameAbsolutePath) {
        if (!TextUtil.isNull(absolutePath))
            this.absolutePath = absolutePath;
        switchContent(CameraUploadFrag.newInstance(absolutePath, overlayAbsolutePath, firstFrameAbsolutePath), R.id.camera_upload_fl_container, true, false);
    }

    public void gotoSearchLocationFrag() {
        switchContent(SearchLocationFrag.newInstance(), R.id.camera_upload_fl_container, true, false);
    }

    public void requestPermission() {

        String[] permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission, Definitions.ACTIVITY_REQUEST_CODE.PERMISSION_LOCATION);
        }

    }

    public void requestSearchLocation(String location, NetworkRequestUtil netUtil) {

//        https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&name=cruise&key=AddYourOwnKeyHere
        HashMap<String, String> params = new HashMap<String, String>();
        if (GPSService.lastLoc != null) {
            String latitude_logitude = GPSService.lastLoc.getLatitude() + "," + GPSService.lastLoc.getLongitude();
            params.put("location", latitude_logitude);
        }
        if (!TextUtil.isNull(location)) {
            try {
                params.put("name", URLEncoder.encode(location, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        params.put("radius", String.valueOf(5000));
        params.put("key", getString(R.string.google_place_api_server_id));
        StringRequest myReq = netUtil.requestGet(NetworkConstantUtil.APIKEY.GOOGLE_SEARCH_PLACES, NetworkConstantUtil.URLS.GOOGLE_SEARCH_PLACES, params);
        NetworkController.addToRequestQueue(myReq);
    }

    @Override
    public void onBackPressed() {

        if (isCurrentUploadFrag) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            List<Fragment> fragmentList = fragmentManager.getFragments();
            for (Fragment fragment : fragmentList) {
                ft.remove(fragment);
                fragment.onDetach();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            ft.commit();
        }

        if (cameraCgToolFrag != null) {
            JYLog.D("TAG::onBackPressed00", new Throwable());
            if (!cameraCgToolFrag.isDetached() && cameraCgToolFrag.llcgTollDefault != null && cameraCgToolFrag.llcgTollDefault.getVisibility() == View.GONE) {
                JYLog.D("TAG::onBackPressed", new Throwable());
                cameraCgToolFrag.llcgTollDefault.setVisibility(View.VISIBLE);
                return;
            }
        }

        super.onBackPressed();

    }
}
