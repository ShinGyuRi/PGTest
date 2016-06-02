package Util;

import android.graphics.Typeface;

/**
 * Created by Gyul on 2016-05-24.
 */
public class Definitions {

    public static Typeface InnisfreeGothicBold;

    public interface INTENT_KEY {
        String FROM_ACTIVITY = "FROM_ACTIVITY";
    }

    public interface ACTIVITY_REQUEST_CODE {
        int CAMERA_ACT = 500;
    }

    public interface INTENT_ACTION {
        String GPS_CHANGE = "com.airfactory.gps.change";
        String ACTION_SERVICE_CHECK = "kr.tofor.service_check";
        String ACTION_TIMER_START = "kr.tofor.timerstart";
        String ACTION_TIMER_STOP = "kr.tofor.timerstop";

        String RECEIVER_TIME = "kr.tofor.receiver_time";
        String RECEIVER_NETWORK_START = "kr.tofor.networkstart";
        String RECEIVER_NETWORK_ERROR = "kr.tofor.networkerror";
        String RECEIVER_NETWORK_RESULT = "kr.tofor.networkresult";
        String RECEIVER_NETWORK_FINISH = "kr.tofor.networkfinish";
        String MEMBER_CODE_IS_NULL = "kr.airfactory.error.membercode.isnull";
        String RECEIVER_MESSAGE = "kr.tofor.receive_message";
    }
}
