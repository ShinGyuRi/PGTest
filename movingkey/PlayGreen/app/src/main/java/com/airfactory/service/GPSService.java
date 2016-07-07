package com.airfactory.service;


import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

import com.moyusoft.util.Definitions;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.PrefUtil;

public class GPSService extends Service implements Runnable {

    private static final String TAG = "GPSService";

    public final static String GPS_START_ACTION = "com.airfactory.gpsstart";
    public final static String GPS_STOP_ACTION = "com.airfactory.gpsstop";

    public final static int SERVER_SEND_TERM_TIME = 30000;
    private final int MINTIME = 1000 * 60;    //1분마다 갱신
    private final int MINDISTANCE = 1000;

    public LocationManager locationManager = null;
    public static Location lastLoc;
    public GpsListener listener;

    public long sendTime;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    @Deprecated
    public void onStart(Intent intent, int startId) {
        this.onStartCommand(intent, 0, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        goAction(intent);
        return START_STICKY;
    }

    public void goAction(Intent intent) {
        String action = null;
        if (intent == null)
            return;
        action = intent.getAction();

        if (action == null || action.equals(""))
            return;
        if (action.equals(GPS_START_ACTION)) {
            startGps();
        } else if (action.equals(GPS_STOP_ACTION)) {
            stopGps();
        }
    }

    public void stopGps() {
        stopNoti();
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(listener);
        }

        if (locationManager != null)
            locationManager = null;
        if (listener != null)
            listener = null;
        stopSelf();
    }

    public void startGps() {
        if (locationManager == null)
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (listener == null)
            listener = new GpsListener();

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.NO_REQUIREMENT);
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
        String provider = locationManager.getBestProvider(criteria, true);
        updateCurrentLocation();

        //JYLog.D(provider, new Throwable());
        if (provider == null || provider.equals("")) {
            return;
        }
//		lastLoc = null;
//		lastLoc = locationManager.getLastKnownLocation(provider);
//		locationManager.requestLocationUpdates(provider, 0, 0, listener);
    }

    public void updateCurrentLocation() {
        String locationProvider = "";

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Intent intentBroadcast = new Intent(GPSReceiver.ACTION_ERROR_PERMISSION_GPS);
                sendBroadcast(intentBroadcast);
            }
            return;
        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
            locationManager.requestLocationUpdates(locationProvider, MINTIME, MINDISTANCE, listener);
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
            locationProvider = LocationManager.GPS_PROVIDER;
            locationManager.requestLocationUpdates(locationProvider, MINTIME, MINDISTANCE, listener);
        } else {
            Intent intentBroadcast = new Intent(GPSReceiver.ACTION_ERROR_OFF_GPS);
            sendBroadcast(intentBroadcast);
            return;
        }

        lastLoc = locationManager.getLastKnownLocation(locationProvider);

//        Intent intentBroadcast = new Intent(GPSReceiver.ACTION_ERROR_OFF_GPS);
//        sendBroadcast(intentBroadcast);

    }

//	public void sendLocation(){
//		NetworkRequestUtil netUtil = new NetworkRequestUtil(null);
//		HashMap<String, String> params = new HashMap<String, String>();
//		StringRequest myReq = null;
//		params.put("ACCESS_TOKEN", AirrabbitManager.getAccessToken());
//		params.put("RECENT_LAT", "" + lastLoc.getLatitude());
//		params.put("RECENT_LNG", "" + lastLoc.getLongitude());
//		myReq = netUtil.requestGet(NetworkConstantUtil.APIKEY.USER_UPDATE, NetworkConstantUtil.URLS.USER_UPDATE, params);
//		NetworkController.addToRequestQueue(myReq);
//	}

    public class GpsListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location == null)
                return;
            lastLoc = location;

            if (lastLoc != null) {
                long lastSendTime = PrefUtil.getInstance().getLongPreference(Definitions.PREFKEY.SERVER_SEND_LOCATION_INFO_TIME);
                long currentTime = System.currentTimeMillis();
//				if(currentTime-lastSendTime>SERVER_SEND_TERM_TIME && AirrabbitManager.isLogin() && lastLoc!=null){
//					PrefUtil.getInstance().putPreference(Definitions.PREFKEY.SERVER_SEND_LOCATION_INFO_TIME,currentTime);
//					sendLocation();
//				}
                JYLog.D(TAG + "::latitude::" + lastLoc.getLatitude(), new Throwable());
                JYLog.D(TAG + "::longitude::" + lastLoc.getLongitude(), new Throwable());
                PrefUtil.getInstance().putPreference(Definitions.PREFKEY.PREF_TMP_LAT, "" + lastLoc.getLatitude());
                PrefUtil.getInstance().putPreference(Definitions.PREFKEY.PREF_TMP_LNG, "" + lastLoc.getLongitude());
            }

            Intent i = new Intent(Definitions.INTENT_ACTION.GPS_CHANGE);
            sendBroadcast(i);
        }

        @Override
        public void onProviderDisabled(String provider) {
            findLocationFailedAlert();
        }

        @Override
        public void onProviderEnabled(String provider) {
            if (ActivityCompat.checkSelfPermission(GPSService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(GPSService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(listener);
            updateCurrentLocation();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }

    @Override
    public void run() {

    }

    public void stopNoti() {
        stopForeground(true);
    }

    public void findLocationFailedAlert() {
//		AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
//		mBuilder.setCancelable(true);
//		mBuilder.setMessage("asdasda");
//		btn = R.string.str_salah_quiblah_btn_open_location_setting;
//		mBuilder.setNeutralButton(btn, new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//				intent.addCategory(Intent.CATEGORY_DEFAULT);
//				startActivity(intent);
//			}
//		});
//		mBuilder.setTitle(R.string.str_dialog_title_notice);
//		mBuilder.create().show();
    }

}
