package tiltcode.movingkey.com.tiltcode_new.library.util;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Locale;

/**
 * Created by Gyul on 2016-06-15.
 */
public class DeviceUtil {

    public static String TAG = DeviceUtil.class.getSimpleName();

    public static String getDevicePhoneNumber(Context context) {
        TelephonyManager telManager = null;
        String mPhoneNum = null;
        try {
            telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneNum = telManager.getLine1Number();
//			mPhoneNum = PhoneNumberUtils.formatNumber(mPhoneNum);
            mPhoneNum = mPhoneNum.replace("+820", "0");
            mPhoneNum = mPhoneNum.replace("+82", "0");
            if (mPhoneNum.subSequence(0, 2).equals("01") == false) {
                mPhoneNum = "";
            }

        } catch (Exception err) {
            mPhoneNum = null;
            Log.e(TAG, err.toString());
        } finally {
            if (telManager != null)
                telManager = null;
        }

        return mPhoneNum;
    }

    /**
     * 상태바 높이
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 비율 구하기
     *
     * @param context
     * @param SCREEN_BASE_WIDTH
     * @param SCREEN_BASE_HEIGHT
     * @return
     */
    public static float getScreenRate(Context context, int SCREEN_BASE_WIDTH, int SCREEN_BASE_HEIGHT, float phoneWidth, float phoneHeight) {
        float Rate = 0.0f;
        float tmpRate = 0.0f;
        // 폰 기본 사이즈 가져오기

        Rate = phoneWidth / SCREEN_BASE_WIDTH;
        tmpRate = phoneHeight / SCREEN_BASE_HEIGHT;

        // 마이너스 비율은 배제함
        if (Rate < 0.0)
            Rate = 0.0f;
        if (tmpRate < 0.0)
            tmpRate = 0.0f;

        if (Rate > tmpRate && tmpRate > 0.0)
            Rate = tmpRate;

        // 소수점 2째 자리까지만 계산(내림)
        Rate = GetMathFloor(Rate, 2);
        return Rate;
    }

    /**
     * 해당 자릿수 만큼 버림
     *
     * @param var
     * @param number
     * @return
     */
    public static float GetMathFloor(float var, int number) {
        float rVal = 0.0f;

        double numberVal = Math.pow(10, number);

        rVal = (float) (Math.floor(var * numberVal) / numberVal);

        return rVal;
    }

    public static String getDeviceIdObtain(Context Ctx) {

        String DID = null;
        // USIM 등이 탑재된 기기의 경우 TelephonyManager를 통해 유니크한 Device ID 를 가져옴
        try {
            TelephonyManager manager = (TelephonyManager) Ctx.getSystemService(Context.TELEPHONY_SERVICE);
            DID = manager.getDeviceId();
            Debug.showDebug("usim address-------->" + DID);
            manager = null;
        } catch (Exception err) {
            DID = "";
        }
        if (DID == null)
            DID = "";

        // USIM 등이 탑재되어 있지 않은 경우 WIFI 가 켜져 있다는 가정 하에 MAC Address 로 유니크한 Device
        // ID 를 삼음
        if (DID.length() < 1) {
            DID = null;
            try {
                WifiManager wm = (WifiManager) Ctx.getSystemService(Context.WIFI_SERVICE);
                DID = wm.getConnectionInfo().getMacAddress();
                wm = null;
            } catch (Exception err) {
                DID = "";
                Debug.showDebug("mac address-------->" + err.toString());
            }
            if (DID == null)
                DID = "";
            // MAC Address 에 구분자 제거
            DID = DID.replace(":", "");
            Debug.showDebug("mac address-------->" + DID);
        }
        if (DID == null)
            DID = "";

        // 간혹 공백이 섞여 들어 오는 경우가 있습니다.
        DID = DID.replace(" ", "");

        return DID;

    }

    /**
     * 구글 이메일 찾기
     *
     * @param context
     * @return
     */
    public static String getAccountEmail(Context context) {
        AccountManager am = AccountManager.get(context);
        String email = null;
        Account[] accts = am.getAccounts();
        int count = accts.length;
        if (count > 0) {
            Account acct = null;
            for (int i = 0; i < count; i++) {
                acct = accts[i];
                if (acct.type.equals("com.google")) {
                    email = null;
                    email = acct.name;
                    break;
                }

            }
        }

        accts = null;

        return email;
    }

    /**
     * 해당 폰에 대한 SDK버전 값을 가져옴
     *
     * @return SDK버전
     */
    public static int getSdkVersion() {
        int sdkVersion;
        sdkVersion = Build.VERSION.SDK_INT;
        return sdkVersion;
    }

    public static String getPkgName(Context context) {
        return context.getPackageName();
    }

    public static void goCall(Context act, String phoneNum) {
        Debug.showDebug(phoneNum);
        Uri uri = null;
        Intent it = null;
        try {
            uri = Uri.parse("tel:" + phoneNum);
            it = new Intent(Intent.ACTION_CALL, uri);
            int permissionCheck = ContextCompat.checkSelfPermission(act.getApplicationContext(), Manifest.permission.WRITE_CALENDAR);

            if(permissionCheck== PackageManager.PERMISSION_DENIED){
                // 권한 없음
            }else{
                // 권한 있음
                act.startActivity(it);
            }

        } catch (Exception err) {
            Debug.showDebug(err.toString());
        } finally {
            uri = null;
            it = null;
        }
        ;
    }

    /**
     * 디바이스 모델명 가져오기
     */
    public static String getDeviceModel() {
        String model_name = null;
        model_name = Build.MODEL;
        model_name = model_name.replaceAll("\\p{Space}", "");
        return model_name;
    }

    /**
     * 외부 브라우저 연결 인텐트 메소드
     *
     * @param context
     * @param url     해당 URL값
     */
    public static void doExternalInternetConnectionIntent(Context context, String url) {
        Intent mIntent = null;
        Uri mWebUri = null;
        Context mContext = null;

        mContext = context;
        mWebUri = Uri.parse(url);
        mIntent = new Intent(Intent.ACTION_VIEW, mWebUri);
        mContext.startActivity(mIntent);

        mWebUri = null;
        mIntent = null;
        mContext = null;
    }

    public static void goGoogleStreetWay(Context context, String daddr) {
        Uri uri = Uri.parse("http://maps.google.com/maps?f=d&hl=ko&daddr=" + daddr);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(it);
    }

    public static void goGoogleMap(Context context, String lat, String lng) {
        String geo = String.format("geo:%s,%s", lat, lng);
        Uri uri = Uri.parse(geo);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(it);
    }

    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_NOT_CONNECTED = 0;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        String status = null;
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }
}
