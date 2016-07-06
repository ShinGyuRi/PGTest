package tiltcode.movingkey.com.tiltcode_new.activitys;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import tiltcode.movingkey.com.tiltcode_new.R;
import tiltcode.movingkey.com.tiltcode_new.library.ParentActivity;
import tiltcode.movingkey.com.tiltcode_new.library.util.Util;

/**
 * Created by Gyul on 2016-06-17.
 */
public class SplashActivity extends ParentActivity {

    private View 	decorView;
    private int	uiOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        Util.runDelay(1600, new Runnable() {
            @Override
            public void run() {
                moveActivity();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        // super.onWindowFocusChanged(hasFocus);

        if( hasFocus ) {
            decorView.setSystemUiVisibility( uiOption );
        }
    }

    private void moveActivity() {
        Intent intent = new Intent(this, LoginActivity.class);

        startActivity(intent);
        this.finish();
    }
}
