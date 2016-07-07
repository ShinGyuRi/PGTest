package kr.innisfree.playgreen.activity.pg21;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.ParentAct;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lyft.android.scissors.CropView;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.Definitions.INTENT_KEY;
import com.moyusoft.util.FileUtil;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;
import com.navercorp.volleyextensions.volleyer.Volleyer;
import com.navercorp.volleyextensions.volleyer.builder.PostBuilder;
import com.navercorp.volleyextensions.volleyer.factory.DefaultRequestQueueFactory;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.NetworkErrorUtill;
import com.volley.network.dto.NetworkJson;

import java.io.File;
import java.util.concurrent.Future;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.activity.CameraActivity;
import kr.innisfree.playgreen.activity.setting.ProfileEditAct;
import kr.innisfree.playgreen.common.PlaygreenManager;

import static android.graphics.Bitmap.CompressFormat.PNG;

/**
 * Created by jooyoung on 2016-03-14.
 */
public class PG21MissionImageAct extends ParentAct implements View.OnClickListener {

    private static final String TAG = "PG21MissionImageAct";

    private final int REQ_CODE_GALLERY = 123;
    private CropView imgContent;

    private File cropFile, originalFile;
    private String missionId, missionTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_today_mission_image);
        setLoading(this);

//		goGallery();
        gotoCameraActivity(CameraActivity.TYPE_FROM_PG21_TODAY_MISSION_ACT);
        initToolbar();
        imgContent = (CropView) findViewById(R.id.img_content);
        cropFile = new File(getCacheDir(), "cropped.png");
        originalFile = new File(getCacheDir(), "original.png");

        missionId = getIntent().getStringExtra(INTENT_KEY.MISSION_ID_STR);
        missionTitle = getIntent().getStringExtra(INTENT_KEY.MISSION_TITLE_STR);

    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        findViewById(R.id.layout_back).setOnClickListener(this);
        findViewById(R.id.txt_confirm).setOnClickListener(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void goGallery() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType(MediaStore.Images.Media.CONTENT_TYPE);
        i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQ_CODE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_GALLERY) {
            if (resultCode == RESULT_OK) {
                if (data == null) return;
                Uri uri = data.getData();
                FileUtil.copyUriToFile(this, uri, FileUtil.getTempImageFile(this));
                imgContent.setViewportRatio(108f / 50f); //16f/9f
                imgContent.extensions().load(uri);
//				float newRatio = 6f / 4f;
//				ObjectAnimator.ofFloat(imgContent, "aspectRatio", imgContent.getImageRatio(), newRatio).start();
            } else {
                finish();
            }
        } else if (requestCode == Definitions.ACTIVITY_REQUEST_CODE.CAMERA_ACT) {
            if (resultCode == RESULT_OK) {
                if (data == null) return;
                Uri uri = data.getData();
                FileUtil.copyUriToFile(this, uri, originalFile);
                imgContent.setViewportRatio(108f / 50f); //16f/9f
                imgContent.extensions().load(uri);
////				float newRatio = 6f / 4f;
//				ObjectAnimator.ofFloat(imgContent, "aspectRatio", imgContent.getImageRatio(), newRatio).start();
            } else {
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_back:
                onBackPressed();
                break;
            case R.id.txt_confirm:
                Future<Void> future = imgContent.extensions().crop().quality(100).format(PNG).into(cropFile);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        JYLog.D("result:" + cropFile.exists(), new Throwable());
                        requestUploadMission();
                    }
                }, 1000);
                break;
        }
    }

    public void requestUploadMission() {
        showLoading();
        final RequestQueue q = DefaultRequestQueueFactory.create(this);
        q.start();


        PostBuilder pb = Volleyer.volleyer(q).post(NetworkConstantUtil.URLS.PG21_MISSION_REGIST)
                .addStringPart("AUTH_TOKEN", PlaygreenManager.getAuthToken())
                .addStringPart("PG21_MS_TITLE", missionTitle);
        if (!TextUtil.isNull(missionId))
            pb.addStringPart("PG21_MS_ID", missionId);

        if (cropFile.exists())
            pb.addFilePart("HIGHLIGHT_IMG", cropFile);
        if (originalFile.exists())
            pb.addFilePart("PG21_MS_ENTER_IMG", originalFile);
        pb.withListener(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JYLog.D("NETWORK", response.trim(), new Throwable());
                if (response.contains("DOCTYPE") || response.contains("doctype")) {
                    hideLoading();
                    Toast.makeText(PG21MissionImageAct.this, R.string.str_server_error_occured, Toast.LENGTH_SHORT).show();
                    return;
                }
                hideLoading();
                Gson gson = new Gson();
                NetworkJson networkJson = gson.fromJson(response, NetworkJson.class);
                if (NetworkErrorUtill.isJsonResultCheck(networkJson)) {
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    onNetworkError(NetworkConstantUtil.APIKEY.PG21_MISSION_REGIST, null, networkJson);
                }
                q.stop();
            }
        }).withErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                JYLog.D("NETWORK", arg0.getMessage(), new Throwable());
                hideLoading();
                q.stop();
            }
        }).execute().setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    @Override
    public void onNetworkResult(int idx, NetworkJson json) {
        super.onNetworkResult(idx, json);

    }

    @Override
    public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
        super.onNetworkError(idx, error, json);
        if (json == null || json.RESULT_CODE == null) return;
        switch (json.RESULT_CODE) {
            case NetworkConstantUtil.NETWORK_RESULT_CODE.TODAY_MISSION_ALREADY_REGIST:
                Toast.makeText(this, R.string.str_toast_today_mission_already_regist, Toast.LENGTH_SHORT).show();
                break;
        }
        setResult(Activity.RESULT_OK);
        finish();
    }
}
