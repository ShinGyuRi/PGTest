package com.gcm;

import android.app.Activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.moyusoft.util.Debug;

/**
 * Created by preparkha on 2015. 11. 22..
 */
public class GcmUtil {

    public static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    public static String REGISTRATION_COMPLETE = "REGISTRATION_COMPLETE";

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public static boolean checkPlayServices(Activity activity) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity.getApplicationContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Debug.showDebug("This device is not supported.");
            }
            return false;
        }
        return true;
    }
}
