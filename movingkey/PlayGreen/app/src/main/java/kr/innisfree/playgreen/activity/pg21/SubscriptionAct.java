package kr.innisfree.playgreen.activity.pg21;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ParentAct;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.moyusoft.util.BitmapCircleResize;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.INTENT_KEY;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;
import com.squareup.picasso.Picasso;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.NetworkController;
import com.volley.network.NetworkRequestFunction;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.Util.ShareToSNSManager;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.data.PlayGreenEvent;
import kr.innisfree.playgreen.data.PlayGreenEvent.EVENT_TYPE;

/**
 * Created by jooyoung on 2016-03-11.
 */
public class SubscriptionAct extends ParentAct implements View.OnClickListener {

    private LinearLayout layoutSubscription, layoutSubscriptionComplete, layoutSuperGreenerReward, layoutCapture;
    private EditText editContact, editSuperGreenerName, editSuperGreenerContact;
    private ImageView imgProfile;
    private TextView txtName, txtPeriod, txtSubscriptionBtn, txtGrade, txtSuperGreenerRewardDescription, txtSuperGreenerRewardButton;

    private int type, lank;
    public static final int TYPE_EARTH_BOX = 1;
    public static final int TYPE_SUPER_GREENER = 2;
    public String superGreenerEnterID;
    private NetworkData netResultData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_subscription);
        setLoading(this);
        initToolbar();

        type = getIntent().getIntExtra(INTENT_KEY.DATA, TYPE_EARTH_BOX);
        lank = getIntent().getIntExtra(INTENT_KEY.GRADE_INT, -1);
        superGreenerEnterID = getIntent().getStringExtra(INTENT_KEY.SUPERGREENER_ID);

        //Earth box 응모
        layoutSubscription = (LinearLayout) findViewById(R.id.layout_subscription);
        txtSubscriptionBtn = (TextView) findViewById(R.id.txt_subscription);
        editContact = (EditText) findViewById(R.id.edit_contact);
        editContact.addTextChangedListener(watcher);
        txtSubscriptionBtn.setOnClickListener(this);
        txtSubscriptionBtn.setEnabled(false);

        //Earth box 응모 완료
        layoutCapture = (LinearLayout) findViewById(R.id.layout_capture);
        layoutSubscriptionComplete = (LinearLayout) findViewById(R.id.layout_subscription_complete);
        layoutSubscriptionComplete.setVisibility(View.GONE);
        imgProfile = (ImageView) findViewById(R.id.img_profile);
        txtName = (TextView) findViewById(R.id.txt_name);
        txtPeriod = (TextView) findViewById(R.id.txt_period);

        //슈퍼그리너 리워드
        layoutSuperGreenerReward = (LinearLayout) findViewById(R.id.layout_supergreener_reward);
        txtGrade = (TextView) findViewById(R.id.txt_grade);
        txtSuperGreenerRewardDescription = (TextView) findViewById(R.id.txt_sg_reward_description);
        editSuperGreenerName = (EditText) findViewById(R.id.edit_sg_name);
        editSuperGreenerContact = (EditText) findViewById(R.id.edit_sg_contact);
        txtSuperGreenerRewardButton = (TextView) findViewById(R.id.txt_sg_confirm);
        txtGrade = (TextView) findViewById(R.id.txt_grade);
        txtGrade.setText(getString(R.string.str_super_greener_grade, lank));
        SpannableStringBuilder sp = new SpannableStringBuilder(getString(R.string.str_super_greener_reward_description));
        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#4b9b50")), 0, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtSuperGreenerRewardDescription.setText(sp);
        txtSuperGreenerRewardButton.setOnClickListener(this);
        editSuperGreenerName.addTextChangedListener(watcher);
        editSuperGreenerContact.addTextChangedListener(watcher);
        txtSuperGreenerRewardButton.setEnabled(false);

        if (type == TYPE_EARTH_BOX) {
            layoutSubscription.setVisibility(View.VISIBLE);
            layoutSuperGreenerReward.setVisibility(View.GONE);
        } else {
            layoutSubscription.setVisibility(View.GONE);
            layoutSuperGreenerReward.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.txt_confirm).setOnClickListener(this);
        findViewById(R.id.btn_facebook).setOnClickListener(this);
        findViewById(R.id.btn_instagram).setOnClickListener(this);
        findViewById(R.id.btn_kakaostory).setOnClickListener(this);

        //TODO 레이아웃 캡쳐 테스트
//        layoutSubscriptionComplete.setVisibility(View.VISIBLE);
//        layoutSubscription.setVisibility(View.GONE);
//		captureLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isSharing) {
            JYLog.D("onResume::" + shareCategory, new Throwable());
            JYLog.D("onResume::" + targetId, new Throwable());
            JYLog.D("onResume::" + sns, new Throwable());
            if (!TextUtil.isNull(shareCategory) && !TextUtil.isNull(targetId) && !TextUtil.isNull(sns)) {
                JYLog.D("onResume::yes::", new Throwable());
                NetworkRequestFunction networkRequestFunction = new NetworkRequestFunction(this.netUtil);
                networkRequestFunction.requestRegistPGpoint(shareCategory, targetId, sns, null, null);
            } else {
                JYLog.D("onResume::no::", new Throwable());
            }
            initializeShareInfo();
        }
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (type == TYPE_EARTH_BOX) {
                if (!TextUtil.isNull(editContact.getText().toString()))
                    txtSubscriptionBtn.setEnabled(true);
                else
                    txtSubscriptionBtn.setEnabled(false);
            } else {
                if (!TextUtil.isNull(editSuperGreenerName.getText().toString()) && !TextUtil.isNull(editSuperGreenerContact.getText().toString())) {
                    txtSuperGreenerRewardButton.setEnabled(true);
                } else {
                    txtSuperGreenerRewardButton.setEnabled(false);
                }
            }
        }
    };

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        findViewById(R.id.layout_close).setOnClickListener(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (type == TYPE_EARTH_BOX) {
            ((TextView) findViewById(R.id.txt_title)).setText(getString(R.string.str_pg21_earth_box_request));
        } else {
            ((TextView) findViewById(R.id.txt_title)).setText(getString(R.string.str_mission_end));
        }
    }

    public File saveCaptureImgFile() {
        Bitmap bitmap = null;
        try {
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }
            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "/PlayGreen/IMG_SHARE.jpg");
            if (f.exists()) f.delete();
            FileOutputStream out = new FileOutputStream(f);

            bitmap = Bitmap.createBitmap(layoutCapture.getWidth(), layoutCapture.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);
            layoutCapture.draw(c);

            int width = layoutCapture.getWidth();
            int height = layoutCapture.getHeight();

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();

            return f;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap saveCaptureBimap() {
        Bitmap bitmap = null;
        try {
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }

            bitmap = Bitmap.createBitmap(layoutCapture.getWidth(), layoutCapture.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);
            layoutCapture.draw(c);

            int width = layoutCapture.getWidth();
            int height = layoutCapture.getHeight();

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);

            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        ShareToSNSManager shareToSNSManager = null;
        switch (v.getId()) {
            case R.id.layout_close:
            case R.id.txt_confirm:
                hiddenKeyboard();
                EventBus.getDefault().post(new PlayGreenEvent(EVENT_TYPE.PG21_REFRESH));
                finish();
                break;
            case R.id.txt_subscription:
                hiddenKeyboard();
                requestSubscription();
                break;
            case R.id.btn_facebook:
                shareToFacebook();
                break;
            case Definitions.DIALOG_SELECT.SHARE_INSTAGRAM:
                setShareInfo("T", null, Definitions.AUTH_CHANNEL.INSTAGRAM);
                shareToSNSManager = new ShareToSNSManager(this, saveCaptureImgFile());
                shareToSNSManager.shareToInstagram();
                break;
            case Definitions.DIALOG_SELECT.SHARE_KAKAOSTORY:
                setShareInfo("T", null, Definitions.AUTH_CHANNEL.KAKAOTALK);
                shareToSNSManager = new ShareToSNSManager(this, saveCaptureImgFile());
                shareToSNSManager.shareToKakaoStory();
                break;
            case R.id.txt_sg_confirm:
                hiddenKeyboard();
                requestSubscription();
                break;
        }
    }

    @Override
    public void onUIRefresh() {
        super.onUIRefresh();
        if (netResultData == null) return;
        if (!TextUtil.isNull(netResultData.MEMB_IMG)) {
            Picasso.with(this).load(netResultData.MEMB_IMG)
                    .transform(new BitmapCircleResize(this, getResources().getDimensionPixelOffset(R.dimen.dp_100)))
                    .error(R.drawable.img_user_null2)
                    .skipMemoryCache().into(imgProfile);
        } else {
            imgProfile.setImageResource(R.drawable.img_user_null2);
        }
        if (!TextUtil.isNull(netResultData.MEMB_NAME))
            txtName.setText(netResultData.MEMB_NAME);
        txtPeriod.setText(PlaygreenManager.getTimeStampToDate(netResultData.START_DT, false)
                + " ~ " + PlaygreenManager.getTimeStampToDate(netResultData.END_DT, false));
    }

    public void requestSubscription() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken());
        // earth box 응모
        if (type == TYPE_EARTH_BOX) {
            params.put("PHONE", editContact.getText().toString());
        }
        // 슈퍼그리너 응모
        else {
            params.put("NAME", editSuperGreenerName.getText().toString());
            params.put("PHONE", editSuperGreenerContact.getText().toString());
            params.put("SUPERGREENER_ENTER_ID", superGreenerEnterID);
        }
        StringRequest myReq = netUtil.requestPost(APIKEY.PG21_SUBSCRIBE, NetworkConstantUtil.URLS.PG21_SUBSCRIBE, params);
        NetworkController.addToRequestQueue(myReq);
    }

    @Override
    public void onNetworkResult(int idx, NetworkJson json) {
        super.onNetworkResult(idx, json);
        switch (idx) {
            case APIKEY.PG21_SUBSCRIBE:
                //TODO 응모연동 성공 후
                if (type == TYPE_EARTH_BOX) {
                    ((TextView) findViewById(R.id.txt_title)).setText(R.string.str_subscription_complete);
                    layoutSubscriptionComplete.setVisibility(View.VISIBLE);
                    layoutSubscription.setVisibility(View.GONE);
                    netResultData = json.DATA;
                    setShareInfo("P", netResultData.PG21_MS_ID, null);
                    onUIRefresh();
                } else {
                    EventBus.getDefault().post(new PlayGreenEvent(EVENT_TYPE.PG21_REFRESH));
                    finish();
                }
                break;
        }
    }


    //facebook share
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Definitions.ACTIVITY_REQUEST_CODE.FACEBOOK_SHARE) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    CallbackManager callbackManager;
    public com.facebook.share.widget.ShareDialog facebookShareDialog;

    public void shareToFacebook() {
        /** set **/
        setShareInfo("T", null, Definitions.AUTH_CHANNEL.FACEBOOK);
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        facebookShareDialog = new com.facebook.share.widget.ShareDialog(this);
        facebookShareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                JYLog.D("Subscription::onSuccess::fb", new Throwable());
            }

            @Override
            public void onCancel() {
                JYLog.D("Subscription::onCancel::fb", new Throwable());
                initializeShareInfo();
            }

            @Override
            public void onError(FacebookException error) {
                JYLog.D("Subscription::onError::fb", new Throwable());
                initializeShareInfo();
            }
        });

        SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(saveCaptureBimap()).build();
        SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder().addPhoto(sharePhoto).build();
        facebookShareDialog.show(sharePhotoContent, com.facebook.share.widget.ShareDialog.Mode.AUTOMATIC);
    }

    private boolean isSharing = false;
    private String shareCategory, targetId, sns;

    private void initializeShareInfo() {
        this.isSharing = false;
        this.shareCategory = null;
        this.targetId = null;
        this.sns = null;
    }

    private void setShareInfo(String shareCategory, String targetId, String sns) {
        this.isSharing = true;
        if (!TextUtil.isNull(shareCategory))
            this.shareCategory = shareCategory;

        if (!TextUtil.isNull(targetId))
            this.targetId = targetId;

        if (!TextUtil.isNull(sns))
            this.sns = sns;
    }
}
