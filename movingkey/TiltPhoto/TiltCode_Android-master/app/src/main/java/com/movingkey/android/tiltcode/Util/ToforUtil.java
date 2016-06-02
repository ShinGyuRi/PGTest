package Util;

import android.app.ActivityManager;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Gyul on 2016-05-25.
 */
public class ToforUtil {

    public static String HASHKEY;

    public static String IMG_PNG_FORMAT = ".png";
    public static String LOCAL_FILE_PATH;
    public static String LOCAL_IMG_FILE_PATH;
    public static String LOCAL_TXT_FILE_PATH;
    public static float SCREENRATE;
    public final static int BASIC=350;
    public static int PHONE_W;
    public static int PHONE_H;

    public final static float PHONE_BASIC_W = 720.0f;
    public final static float PHONE_BASIC_H = 1280.0f;

    public final static int FILE_TYPE_IMG	=1;
    public final static int FILE_TYPE_TEXT	=	2;


    public static final String IMAGE_PATH = "/tofor/image";
    public static final String TXT_PATH = "/tofor/text";

    public static class Config {
        public static final boolean DEVELOPER_MODE = false;
    }

    public static boolean isAppForground(Context context) {
        boolean isForground = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo process : info) {
            String strPackage = "";
            if (process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                strPackage = process.processName;
                if (strPackage.equals(context.getPackageName()) && process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    isForground = true;
                    break;
                }

            }
        }
        return isForground;
    }

    public static String getStringType(String strtime){
        if(Util.TextUtil.isNull(strtime)) return null;
        String re = "";
        String CurrentTime = getTime(true);
        int Current[] = getStringTime(CurrentTime);
        int GetTime[] = getStringTime(strtime);
        if(Current==null || Current.length!=6)return null;
        if(GetTime==null || GetTime.length!=6)return null;

        Date d = new Date(Current[0], Current[1], Current[2], Current[3], Current[4], Current[5]);
        long lCurrent = d.getTime();
        d = null;
        d = new Date(GetTime[0], GetTime[1], GetTime[2], GetTime[3], GetTime[4], GetTime[5]);
        long lGetTime = d.getTime();
        d = null;

        long ltemp = (lCurrent - lGetTime) / 1000;
        int temp;

        temp = (int) (ltemp / 60);
        if (temp < 60)
        {
            if (temp == 0)
            {
                re = "1분전";
            } else
                re = temp + "분전";
        } else
        {
            temp = (int) ((ltemp / 60) / 60);
            // 날이 다르면
            if (temp < 24 && Current[2] == GetTime[2])
                re = temp + "시간전";
            else
            {
                // 2012-02-13 18:01:25
                re = GetTime[1]+ "월" +GetTime[2]+ "일";
            }
        }

        return re;
    }

    public static int[] getStringTime(String strtime){
        // 2012-02-14 18:01:25
        if(Util.TextUtil.isNull(strtime)) return null;
        int timearray[] = new int[6];

        timearray[0] = Integer.parseInt(strtime.substring(0, 4));
        timearray[1] = Integer.parseInt(strtime.substring(5, 7));
        timearray[2] = Integer.parseInt(strtime.substring(8, 10));
        timearray[3] = Integer.parseInt(strtime.substring(11, 13));
        timearray[4] = Integer.parseInt(strtime.substring(14, 16));
        timearray[5] = Integer.parseInt(strtime.substring(17, 19));

        // timearray = null;
        return timearray;
    }
    public static String getTime(boolean fdb)
    {
        SimpleDateFormat mLogDF = null;
        Date mLogDate = null;
        if (fdb)
            mLogDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        else
            mLogDF = new SimpleDateFormat("yyyyMMddHHmmss");

        mLogDate = new Date();
        return mLogDF.format(mLogDate);
    }



    public static Calendar getStringDateToCal(String date){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cal.setTime(sdf.parse(date));
        } catch (Exception e) {

        }
        return cal;
    }

}
