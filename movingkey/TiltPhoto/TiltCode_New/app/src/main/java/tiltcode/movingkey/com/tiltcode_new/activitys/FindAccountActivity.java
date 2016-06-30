package tiltcode.movingkey.com.tiltcode_new.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import tiltcode.movingkey.com.tiltcode_new.R;
import tiltcode.movingkey.com.tiltcode_new.library.ParentActivity;

/**
 * Created by Gyul on 2016-06-23.
 */
public class FindAccountActivity extends ParentActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);
        initToolbar();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ((TextView) findViewById(R.id.tv_title)).setText(R.string.str_title_email_find_account);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
