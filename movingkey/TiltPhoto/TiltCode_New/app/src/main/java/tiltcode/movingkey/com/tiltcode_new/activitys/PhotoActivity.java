package tiltcode.movingkey.com.tiltcode_new.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import tiltcode.movingkey.com.tiltcode_new.R;
import tiltcode.movingkey.com.tiltcode_new.fragments.GridFragment;
import tiltcode.movingkey.com.tiltcode_new.library.BaseApplication;
import tiltcode.movingkey.com.tiltcode_new.library.ParentActivity;
import tiltcode.movingkey.com.tiltcode_new.library.util.BitmapCircleResize;
import tiltcode.movingkey.com.tiltcode_new.library.util.JsinPreference;

/**
 * Created by Gyul on 2016-07-06.
 */
public class PhotoActivity extends ParentActivity{

    public static String TAG = PhotoActivity.class.getSimpleName();

    private Toolbar mToolbar;

    TextView tvUsername;
    ImageView imgProfile;

    JsinPreference jsinPreference;


    String profileImgPath, username;

    public Fragment gridFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        initToolbar();

        tvUsername = (TextView)findViewById(R.id.tv_username);
        imgProfile = (ImageView)findViewById(R.id.img_profile);

        jsinPreference = new JsinPreference(BaseApplication.getContext());
        username = jsinPreference.getValue("username", "");
        profileImgPath = jsinPreference.getValue("profileImgPath", "");

        Picasso.with(this)
                .load(profileImgPath)
                .transform(new BitmapCircleResize(this, (int) getResources().getDimension(R.dimen.tiltphoto_profile_width)))
                .into(imgProfile);
        tvUsername.setText(username);



    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ((TextView) findViewById(R.id.tv_title)).setText(R.string.str_title_tiltcode);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void ChangeFragment( View v ) {

        switch( v.getId() ) {
            default:
            case R.id.img_btn_grid:
                gridFragment = GridFragment.newInstance();
                switchContent(gridFragment, R.id.container, false, false);
                break;

            case R.id.img_btn_list:
                break;
        }
    }


}
