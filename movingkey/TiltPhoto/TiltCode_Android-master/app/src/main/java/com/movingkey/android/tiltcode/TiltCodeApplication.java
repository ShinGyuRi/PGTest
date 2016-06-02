package com.movingkey.android.tiltcode;

import android.app.Application;

import com.movingkey.android.tiltcode.Util.Util;
import com.squareup.picasso.Picasso;

/**
 * Created by Gyul on 2016-06-02.
 */
public class TiltCodeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    void init(){

        Util.context = getBaseContext();


        Picasso.Builder builder = new Picasso.Builder(this);
//        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(false);
        Picasso.setSingletonInstance(built);
    }

}
