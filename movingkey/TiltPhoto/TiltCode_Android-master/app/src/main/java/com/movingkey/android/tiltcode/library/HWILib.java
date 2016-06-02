package com.movingkey.android.tiltcode.library;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.TypedValue;

/**
 * Created by jhkim on 2016. 4. 15..
 */
public class HWILib
{
    /**
     * 메서드의 기능설명은 한 두줄로 간결하게..
     *
     * @param long time 밀리세컨드 단위의 딜레이 시간
     * @return void
     */
    public static void hwi_func01_runDelay(long time, Runnable runnable)
    {
        final Handler handler = new Handler();
        handler.postDelayed(runnable, time);

    }


    public static int hwi_func02_getPxFromDP(Context context,int dpValue)
    {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, r.getDisplayMetrics());

        return (int)px;
    }
}
