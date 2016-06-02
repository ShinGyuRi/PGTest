package com.movingkey.android.tiltcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.movingkey.android.tiltcode.activitys.MainActivity;
import com.movingkey.android.tiltcode.library.HWILib;

public class SplashActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        HWILib.hwi_func01_runDelay(2000, new Runnable()
        {
            @Override
            public void run()
            {
                moveGuideScreen();
            }
        });

    }


    void moveGuideScreen()
    {
        Intent mIntent = new Intent(this, MainActivity.class);

        startActivity(mIntent);
        this.finish();
    }
}
