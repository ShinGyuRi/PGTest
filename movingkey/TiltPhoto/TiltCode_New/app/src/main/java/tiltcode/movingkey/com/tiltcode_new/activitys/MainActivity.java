package tiltcode.movingkey.com.tiltcode_new.activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import tiltcode.movingkey.com.tiltcode_new.R;
import tiltcode.movingkey.com.tiltcode_new.fragments.CouponFragment;
import tiltcode.movingkey.com.tiltcode_new.fragments.HomeFragment;
import tiltcode.movingkey.com.tiltcode_new.library.BaseApplication;
import tiltcode.movingkey.com.tiltcode_new.library.ParentActivity;
import tiltcode.movingkey.com.tiltcode_new.library.util.JsinPreference;

public class MainActivity extends ParentActivity implements View.OnClickListener{

    public static String LOG_NAME = MainActivity.class.getSimpleName();

    public static boolean GPSon = true;

    //context
    public static Context context;

    private LocationManager mLocationManager = null; //LocationManager 변
    Location mLastLocation; //마지막으로 수신된 gps의 주소
    private static final int LOCATION_INTERVAL = 10000; //gps업데이트 주기(1000=1초)    1000
    private static final float LOCATION_DISTANCE = 10f; // gps distance   10f

    ImageButton imgBtnCamera;
    ImageButton imgBtnGallery;
    ImageButton imgBtnCoupon;
    ImageButton imgBtnChat, imgBtnHome;

    private Toolbar mToolbar;

    JsinPreference jsinPreference;

    String lat, lng;

    public Fragment homeFragment, couponFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();

        imgBtnCamera = (ImageButton)findViewById(R.id.img_btn_camera);
        imgBtnGallery = (ImageButton)findViewById(R.id.img_btn_gallery);
        imgBtnCoupon = (ImageButton)findViewById(R.id.img_btn_coupon);
        imgBtnChat = (ImageButton)findViewById(R.id.img_btn_chat);
        imgBtnHome = (ImageButton)findViewById(R.id.img_btn_home);

        imgBtnGallery.setOnClickListener(this);
        imgBtnCamera.setOnClickListener(this);
//        imgBtnCoupon.setOnClickListener(this);
//        imgBtnHome.setOnClickListener(this);

        jsinPreference = new JsinPreference(BaseApplication.getContext());


        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.commit();

        initializeLocationManager();

        if(context == null) context = this;

    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ((TextView) findViewById(R.id.tv_title)).setText(R.string.str_title_tiltcode);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        turnonGPS();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        turnoffGPS();
    }

    //GPS서비스
    private void initializeLocationManager() {
        Log.e(LOG_NAME, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) BaseApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


    class LocationListener implements android.location.LocationListener{
        //생성자
        public LocationListener(String provider)
        {
            Log.e(LOG_NAME, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }
        //gps좌표가 변경되었을때
        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(LOG_NAME, "onLocationChanged: " + location.toString());
            Log.d(LOG_NAME, String.valueOf(location.getLatitude()));

            jsinPreference.put("lat", String.valueOf(location.getLatitude()));
            jsinPreference.put("lng", String.valueOf(location.getLongitude()));

            Log.d(LOG_NAME, "jsinprefrence lat : "+jsinPreference.getValue("lat", ""));

            // jhnunu 150924    gps 정확도 수정------------------------
            //    if( isBetterLocation(location,mLastLocation) == true) {

            mLastLocation.set(location);


            SharedPreferences pref = BaseApplication.getContext().getSharedPreferences("pref", MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("lat", String.valueOf(mLastLocation.getLatitude()));
            edit.putString("lng", String.valueOf(mLastLocation.getLongitude()));
            edit.commit();



        }
        // 기존코드
        // mLastLocation.set(location);
        //----------------------------------------------------------

        //gps가 disabled됬을때
        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(LOG_NAME, "onProviderDisabled: " + provider);
            GPSon = false;
        }
        //gps가 enable됬을때
        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(LOG_NAME, "onProviderEnabled: " + provider);
            GPSon = true;
        }
        //gps의 상태가 변경되었을때
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(LOG_NAME, "onStatusChanged: " + provider);
        }
    }
    //앱에서 사용할 gps수신방식, gps를 이용한 방식과 네트워크를 이용한방식 둘다 사용
    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    public void turnonGPS() {
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1], Looper.getMainLooper());
        } catch (java.lang.SecurityException ex) {
            Log.i(LOG_NAME, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(LOG_NAME, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0], Looper.getMainLooper());
        } catch (java.lang.SecurityException ex) {
            Log.i(LOG_NAME, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(LOG_NAME, "gps provider does not exist " + ex.getMessage());
        }


        SharedPreferences pref = BaseApplication.getContext().getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();

        if(mLastLocation ==null || mLastLocation.getLongitude()==0.0) {

            Location loc = getLastKnownLocation();

            if (loc==null) {
                Log.d(LOG_NAME,"location null");
                //    Toast.makeText(getBaseContext(), getResources().getText(R.string.message_gps), Toast.LENGTH_LONG).show();
                if (!pref.getString("lat", "").equals("") && !pref.getString("lng", "").equals("")) {
                    lat = pref.getString("lat", "");
                    lng = pref.getString("lng", "");

                } else
                return;
            }
            else {
                lat = String.valueOf(loc.getLatitude());
                lng = String.valueOf(loc.getLongitude());
                edit.putString("lat", String.valueOf(loc.getLatitude()));
                edit.putString("lng", String.valueOf(loc.getLongitude()));
                edit.commit();

            }

        }
        else{
            lat = String.valueOf(mLastLocation.getLatitude());
            lng = String.valueOf(mLastLocation.getLongitude());
            edit.putString("lat", String.valueOf(mLastLocation.getLatitude()));
            edit.putString("lng", String.valueOf(mLastLocation.getLongitude()));
            //Log.d(LOG_NAME,"tilt : "+tilt+"lat : "+lat+" lng : "+lng+"token : "+Util.getAccessToken().getToken());
        }

    }

    public void turnoffGPS() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR);

        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            // 권한 없음
        }else{
            // 권한 있음
            mLocationManager.removeUpdates(mLocationListeners[0]);
            mLocationManager.removeUpdates(mLocationListeners[1]);
        }


    }

    private Location getLastKnownLocation() {
        Location l = null;
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {

            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR);

            if(permissionCheck== PackageManager.PERMISSION_DENIED){
                // 권한 없음
            }else{
                // 권한 있음
                l = mLocationManager.getLastKnownLocation(provider);
            }

            if (l == null) {
                continue;
            }
            Log.d("gps",l.getLatitude()+""+l.getLongitude()+""+l.getAccuracy());
            // jhnunu
            if (bestLocation == null || l.getTime() > bestLocation.getTime()){
                // if (bestLocation == null ) {
                // Found best last known location: %s", l);
                Log.d(LOG_NAME,"getLastKnownLocation :"+String.valueOf(l.getTime()));
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())  {
            case R.id.img_btn_gallery:
                goGallery(0);

                break;
            case R.id.img_btn_camera:
                goCamera(0);

                break;
        }

    }

    public void ChangeFragment( View v ) {

        switch( v.getId() ) {
            default:
            case R.id.img_btn_home: {
                homeFragment = HomeFragment.newInstance();
                switchContent(homeFragment, R.id.container, false, false);
                break;
            }
            case R.id.img_btn_coupon: {
                couponFragment = CouponFragment.newInstance();
                switchContent(couponFragment, R.id.container, false, false);
                break;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
