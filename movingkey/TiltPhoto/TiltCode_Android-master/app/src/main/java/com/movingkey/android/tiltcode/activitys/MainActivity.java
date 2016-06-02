package com.movingkey.android.tiltcode.activitys;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.movingkey.android.tiltcode.Model.AccelData;
import com.movingkey.android.tiltcode.Model.Coupon;
import com.movingkey.android.tiltcode.Model.CouponResult;
import com.movingkey.android.tiltcode.Model.LoginResult;
import com.movingkey.android.tiltcode.R;
import com.movingkey.android.tiltcode.Util.Util;
import com.movingkey.android.tiltcode.View.TiltCodeView;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Gyul on 2016-06-01.
 */
public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private final String LOG_NAME = MainActivity.class.getSimpleName();

    public static List<Coupon> couponList;

    private float[] mMagnetic;
    private float[] mGravity = null;
    private ActivityManager mActivityManager;
    private SensorManager mSensorManager;
    private Sensor accelerometerSensor;
    private Sensor magnetometer;

    public static Thread mThread;
    private static int dt = 0;
    private int RECOGNIZE = 1000;  // 500  jhnunu
    private boolean serviceRunning = false;

    Context context;

    TiltCodeView tiltView;

    private static AccelData prev = null, now = null;
    private float TOLERANCE_VALUE = 1.5f;
    private float SEARCH_VALUE = 2.5f;
    private static float[][] Arr_Accel = {
            {9999f, 9999f, 9999f}, // 1 exclude {0f, 9.8f, 0f}
            {6.9f, 6.8f, 0f},
            {9.8f, 0f, 0f},
            {6.8f, -6.8f, 0f},
            {0f, -9.8f, 0f}, // 5
            {-6.8f, -6.8f, 0f},
            {-9.8f, 0f, 0f},
            {-6.8f, 6.8f, 0f},
            {9999f, 9999f, 9999f}, // 9 {0f, 6.8f, 6.8f}
            {9999f, 9999f, 9999f}, // 10 exclude {0f, 0f, 9.8f}
            {0f, -6.8f, 6.8f},
            //{0f, -9.8f, 0f}, // 12 Overlap with 5
            {0f, -6.8f, -6.8f}, // Fixed 12 , prev 13
            {0f, 0f, -9.8f},
            {0f, 6.8f, -6.8f}
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        mSensorManager = (SensorManager)this.getSystemService(this.SENSOR_SERVICE);
        accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mSensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);

        serviceRunning = true;

        initializeLocationManager();

        turnonGPS();

        if(context == null) context = this;
        tiltView = new TiltCodeView(context);
        ((LinearLayout)findViewById(R.id.tiltview_tiltcodefragment)).addView(tiltView);

        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String uuid = tManager.getDeviceId();

        String model = Build.BRAND + " " + Build.DEVICE;

        Util.getEndPoint().setPort("40001");
        Util.getHttpSerivce().signFacebook(uuid, "null", "null", "null", uuid, model,        //비회원 로그인시에는 uuid를 통해 페이스북 로그인인것처럼 로그인한다.
                new Callback<LoginResult>() {
                    @Override
                    public void success(LoginResult loginResult, Response response) {
                        Log.d(LOG_NAME, "login success / code : " + loginResult.code);
                        if (loginResult.code.equals("1") || loginResult.code.equals("2")) { //성공
                            Log.d(LOG_NAME, "token : " + loginResult.info.session);

                            Util.getAccessToken().setToken(loginResult.info.session)
                                    .setIsSkipedUser(true);
                            Util.getAccessToken().saveToken();

                        } else if (loginResult.code.equals("-1")) { //누락된게있음
                            Toast.makeText(getBaseContext(), getResources().getText(R.string.message_not_enough_data), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(LOG_NAME, "login failure : " + error.getMessage());
                        Toast.makeText(getBaseContext(), getResources().getText(R.string.message_network_error), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onDestroy() {
        serviceRunning = false;
        mSensorManager.unregisterListener(this);
        super.onDestroy();
    }
    @Override
    public void onResume()
    {

        Log.d("MainActivity", "onresume");

        accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);

        if (mThread == null) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (serviceRunning) {
                        SystemClock.sleep(60);   // 30 jhnunu

                        //설정페이지에서 onoff조작가능

                        turnonGPS();

                        for (int i = 0; i < Arr_Accel.length; i++) {
                            try {    // jhnunu 150929
                                if ((Arr_Accel[i][0] - SEARCH_VALUE < now.x && Arr_Accel[i][0] + SEARCH_VALUE > now.x) &&
                                        (Arr_Accel[i][1] - SEARCH_VALUE < now.y && Arr_Accel[i][1] + SEARCH_VALUE > now.y) &&
                                        (Arr_Accel[i][2] - SEARCH_VALUE < now.z && Arr_Accel[i][2] + SEARCH_VALUE > now.z)) {
                                    Log.d("sensor", "Tilt : " + String.valueOf(i + 1));

                                    ///// 받는 도중 같은 틸트 비활성  (150919 jhnunu)
                                    if (Util.receiving != String.valueOf(i + 1)) {

                                        getGPSCoupon(String.valueOf(i + 1));

                                    }   // jhnunu

                                }
                            } catch (java.lang.NullPointerException ex) {

                            }
                        }
                       /* boolean flag = false;
                        if(list.size() > 29) {
                            double[][] values = new double[list.size()][];
                            for (int i = 0; i < list.size(); i++) {
                                values[i] = new double[3];
                                values[i][0] = list.get(i).x;
                                values[i][1] = list.get(i).y;
                                values[i][2] = list.get(i).z;
                            }
                            double deviX = Float.parseFloat(Double.toString(standardDeviation(values[0], 0)));
                            double deviY = Float.parseFloat(Double.toString(standardDeviation(values[1], 0)));
                            double deviZ = Float.parseFloat(Double.toString(standardDeviation(values[2], 0)));
                            Log.d("sensor", "Deviation X : " + deviX +
                                    " Y : " + deviY +
                                    " Z : " + deviZ);
                            if (flag) {
                                Intent i = new Intent(TiltService.this, SplashActivity.class);
                                startActivity(i);
                                flag = false;
                            }
                        }*/
                    }
                }
            });

            mThread.start();
        } else if (mThread.isAlive() == false) {
            mThread.start();
        }

        super.onResume();
    }

    //GPS서비스
    private void initializeLocationManager() {
        Log.e(LOG_NAME, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

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

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()) {

            case Sensor.TYPE_ACCELEROMETER:
                mGravity = event.values.clone();
                float[] values = event.values;
                AccelData v = new AccelData(
                        Math.round(values[0] * 100d) / 100d,
                        Math.round(values[1] * 100d) / 100d,
                        Math.round(values[2] * 100d) / 100d);

                if(prev == null)
                    prev = now = v;
                else
                {
                    if( !((prev.x - TOLERANCE_VALUE < v.x && prev.x + TOLERANCE_VALUE > v.x)
                            && (prev.y - TOLERANCE_VALUE < v.y && prev.y + TOLERANCE_VALUE > v.y)
                            && (prev.z - TOLERANCE_VALUE < v.z && prev.z + TOLERANCE_VALUE > v.z)))
                    {

                        dt = 0;
                        prev = v;
                    }
                    now = v;
                }
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagnetic = event.values.clone();
                break;
            default:
                return;
        }
        if(mGravity != null && mMagnetic != null) {
            tiltView.tiltX = getDirection();
//            Log.d("s", "tilt X : " + getDirection());
        }
    }

    private float getDirection() {
        float[] temp = new float[9];
        float[] R = new float[9];
        //Load rotation matrix into R
        SensorManager.getRotationMatrix(temp, null,
                mGravity, mMagnetic);

        //Remap to camera1's point-of-view
        SensorManager.remapCoordinateSystem(temp,
                SensorManager.AXIS_X,
                SensorManager.AXIS_Z, R);

        //Return the orientation values
        float[] values = new float[3];
        SensorManager.getOrientation(R, values);

        //Convert to degrees
        for (int i=0; i < values.length; i++) {
            Double degrees = (values[i] * 180) / Math.PI;
            values[i] = degrees.floatValue();
        }

        return values[2]; // 0 은 x 각도, 1은 y각도, 2는 z각도
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private LocationManager mLocationManager = null; //LocationManager 변
    Location mLastLocation; //마지막으로 수신된 gps의 주소
    private static final int LOCATION_INTERVAL = 10000; //gps업데이트 주기(1000=1초)    1000
    private static final float LOCATION_DISTANCE = 10f; // gps distance   10f

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
            Log.e(LOG_NAME, "onLocationChanged: " + location);

            // jhnunu 150924    gps 정확도 수정------------------------
            //    if( isBetterLocation(location,mLastLocation) == true) {

            mLastLocation.set(location);


            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putLong("lat", Double.doubleToRawLongBits(mLastLocation.getLatitude()));
            edit.putLong("lng", Double.doubleToRawLongBits(mLastLocation.getLongitude()));
            edit.commit();
            //    }
        }
        // 기존코드
        // mLastLocation.set(location);
        //----------------------------------------------------------


        //gps가 disabled됬을때
        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(LOG_NAME, "onProviderDisabled: " + provider);
            Util.GPSon = false;
        }
        //gps가 enable됬을때
        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(LOG_NAME, "onProviderEnabled: " + provider);
            Util.GPSon = true;
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

    private void getGPSCoupon(String tilt){

        // guswo

        Util.currenttilt = tilt;
        double lat = 0.0;
        double lng = 0.0;


        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(LOG_NAME, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(LOG_NAME, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(LOG_NAME, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(LOG_NAME, "gps provider does not exist " + ex.getMessage());
        }

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();

        if(mLastLocation ==null || mLastLocation.getLongitude()==0.0) {

            Location loc = getLastKnownLocation();

            if (loc==null) {
                Log.d(LOG_NAME,"location null");
                //    Toast.makeText(getBaseContext(), getResources().getText(R.string.message_gps), Toast.LENGTH_LONG).show();
                if (Double.longBitsToDouble(pref.getLong("lat", 0))!=0 && Double.longBitsToDouble(pref.getLong("lng", 0))!=0) {
                    lat = Double.longBitsToDouble(pref.getLong("lat", 0));
                    lng = Double.longBitsToDouble(pref.getLong("lng", 0));

                } else
                    return;
            }
            else {
                lat = loc.getLatitude();
                lng = loc.getLongitude();
                edit.putLong("lat", Double.doubleToRawLongBits(loc.getLatitude()));
                edit.putLong("lng", Double.doubleToRawLongBits(loc.getLongitude()));
                edit.commit();

            }

        }
        else{
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();
            edit.putLong("lat", Double.doubleToRawLongBits(mLastLocation.getLatitude()));
            edit.putLong("lng", Double.doubleToRawLongBits(mLastLocation.getLongitude()));
            //Log.d(LOG_NAME,"tilt : "+tilt+"lat : "+lat+" lng : "+lng+"token : "+Util.getAccessToken().getToken());
        }
        Log.d(LOG_NAME,"tilt : "+tilt+"lat : "+lat+" lng : "+lng+"token : "+Util.getAccessToken().getToken());


        Util.getEndPoint().setPort("40003");

        Util.getHttpSerivce().backgroundCouponGetList(Util.getAccessToken().getToken(),
                String.valueOf(lat),
                String.valueOf(lng),
                tilt,
                new Callback<CouponResult>() {
                    @Override
                    public void success(CouponResult couponResult, Response response) {
                        Log.d(LOG_NAME, "backgroundcouponget success : " + couponResult.code);

                        if (couponResult.code.equals("1")) {
                            couponList = couponResult.coupon;

                            if (Util.currenttilt != Util.duplicatetilt) {

                                Util.duplicatetilt = Util.currenttilt;

                                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
                                alert_confirm.setMessage("쿠폰을 받아보시겠습니까?").setCancelable(false).setPositiveButton("확인",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // 'YES'
                                                Intent intent = new Intent(MainActivity.this, CouponReceiveActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        }).setNegativeButton("취소",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // 'No'
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = alert_confirm.create();
                                alert.show();
                            }

                        } else if (couponResult.code.equals("-1")) { //생략된 내용이 있음
                            Log.d(LOG_NAME, "background get gps coupon error : no entry");
                        } else if (couponResult.code.equals("-2")) { //받아올 쿠폰이 하나도 없음
                            Log.d(LOG_NAME, "background get gps coupon error : no coupon");
                        } else if (couponResult.code.equals("-3")) {//세션이 유효하지 않음
                            Log.d(LOG_NAME, "background get gps coupon error : invalid session");
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(LOG_NAME, "error background get gps coupon" + error.getMessage());
                    }
                });

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

}
