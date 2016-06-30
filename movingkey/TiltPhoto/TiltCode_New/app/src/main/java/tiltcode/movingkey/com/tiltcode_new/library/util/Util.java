package tiltcode.movingkey.com.tiltcode_new.library.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

/**
 * Created by Gyul on 2016-06-15.
 */
public class Util {

    public static String TAG = Util.class.getSimpleName();

    /**
     * 메서드의 기능설명은 한 두줄로 간결하게..
     *
     * @param long time 밀리세컨드 단위의 딜레이 시간
     * @return void
     */
    public static void runDelay(long time, Runnable runnable)
    {
        final Handler handler = new Handler();
        handler.postDelayed(runnable, time);

    }

    public static void moveActivity(Activity preAct, Intent intent, int enterAniRes, int exitAniRes, boolean isFinish,boolean isAnimation) {
        preAct.startActivity(intent);
        if (isAnimation)
            preAct.overridePendingTransition(enterAniRes, exitAniRes);
        if (isFinish)
            preAct.finish();
    }

    public static void moveActivity(Activity preAct, Intent intent, int enterAniRes, int exitAniRes, boolean isFinish, boolean isAnimation, int requestCode) {
        preAct.startActivityForResult(intent,requestCode);
        if (isAnimation)
            preAct.overridePendingTransition(enterAniRes, exitAniRes);
        if (isFinish)
            preAct.finish();
    }
}
