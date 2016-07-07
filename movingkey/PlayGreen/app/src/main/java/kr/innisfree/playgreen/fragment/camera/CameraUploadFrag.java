package kr.innisfree.playgreen.fragment.camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.ParentFrag;
import com.airfactory.service.GPSService;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.google.gson.Gson;
import com.kakao.auth.Session;
import com.moyusoft.util.Debug;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.PrefUtil;
import com.moyusoft.util.TextUtil;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.NetworkConstantUtil.APIKEY;
import com.volley.network.NetworkController;
import com.volley.network.NetworkErrorUtill;
import com.volley.network.VolleyMultipartRequest;
import com.volley.network.dto.NetworkData;
import com.volley.network.dto.NetworkJson;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.Util.ShareToSNSManager;
import kr.innisfree.playgreen.activity.CameraActivity;
import kr.innisfree.playgreen.adapter.HashtagAdapter;
import kr.innisfree.playgreen.common.PlaygreenManager;
import kr.innisfree.playgreen.data.DialogData;
import kr.innisfree.playgreen.listener.AdapterItemClickListener;
import kr.innisfree.playgreen.listener.DialogListener;

public class CameraUploadFrag extends ParentFrag implements View.OnClickListener {

    private final static String TAG = "CameraUploadFrag";

    public static final int TYPE_RECENT_HASHTAG_LIST = 0;
    public static final int TYPE_SEARCH_HASHTAG_LIST = 1;

    private String absolutePath;
    private String overlayAbsolutePath;
    private String firstFramAbsolutePath;
    private CameraActivity cameraActivity;

    private Toolbar mToolbar;
    private TextView txtTitle;
    public TextView txtLocation;
    private EditText editContent;
    private VideoView videoView;
    private ImageView imgContent;
    public FrameLayout flWrapperTag;
    private ImageButton imgBtnTag, imgBtnShareFacebook, imgBtnShareInsta, imgBtnShareKakao;
    public LinearLayout llWrapperLocationShare, llWrapperRecentTag;

    /**
     * hashtag
     **/
    private RecyclerView rvRecentTag, rvSearchTag;
    private HashtagAdapter recentHashtagAdapter, searchHashtagAdapter;
    private ArrayList<NetworkData> searchTagList;
    private ArrayList<String> recentTagList;
    private ArrayList<String> hashtags;
    private boolean isTag = false;
    private boolean isClickTag = false;
    private boolean isInputTag = false;
    private int selection = 0;
    private int countRegistHashtag = 0;
    private ProgressDialog progressDialog;

    private boolean isComplited = true;

    private NetworkData myInfo;
    private PrefUtil prefUtil;

    /**
     * share
     **/
    private NetworkJson resultData;
    private boolean isSharedFacebook = false;
    private boolean isSharedInsta = false;
    private boolean isSharedKakaostory = false;
    private boolean isSharing = false;
    private int countSharingInSns = 0;

    public CameraUploadFrag() {
    }

    @SuppressLint("ValidFragment")
    public CameraUploadFrag(String absolutePath, String overlayAbsolutePath, String firstFramAbsolutePath) {
        this.absolutePath = absolutePath;
        this.overlayAbsolutePath = overlayAbsolutePath;
        this.firstFramAbsolutePath = firstFramAbsolutePath;
    }

    public static CameraUploadFrag newInstance(String absolutePath, String overlayAbsolutePath, String firstFramAbsolutePath) {
        CameraUploadFrag frag = new CameraUploadFrag(absolutePath, overlayAbsolutePath, firstFramAbsolutePath);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() instanceof CameraActivity)
            this.cameraActivity = (CameraActivity) getActivity();

        String recentHashtagsWithComma = PrefUtil.getInstance().getStringPreference(Definitions.PREFKEY.RECENT_HASH_TAG);
        if (!TextUtil.isNull(recentHashtagsWithComma)) {
            String[] recentHashtags = recentHashtagsWithComma.split(",");
            recentTagList = new ArrayList<>();
            for (String recentHashtag : recentHashtags) {
                recentTagList.add(recentHashtag);
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_camera_upload, null);
        setKeyboardVisibilityListener(view);

        /** Init Toolbar*/
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        txtTitle = (TextView) view.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.str_camera_upload_title));

        /** photo or video with overlay photo **/
        videoView = (VideoView) view.findViewById(R.id.camera_upload_video_view);
        imgContent = (ImageView) view.findViewById(R.id.camera_upload_img_content);
        editContent = (EditText) view.findViewById(R.id.camera_upload_edit_content);
        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtil.isNull(s.toString())) {
                    // s is not null.
                    String tempS = s.toString().substring(s.length() - 1);

                    /**
                     * We input "#" by button.
                     * When we input "#" buy button, editContent.getSelectionStart() is 0.
                     */
                    if (isClickTag) {
                        isClickTag = false;
                        editContent.requestFocus();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showKeyboard(editContent);
                            }
                        }, 0);

                    } else if (isInputTag) {
                        isInputTag = false;
                    } else {
                        selection = editContent.getSelectionStart();
                    }

                    if (selection < s.toString().length()) {
                        tempS = s.toString().substring(selection - 1, selection);
                    }

                    if (tempS.equals("#") || tempS.equals("\\#")) {
                        isTag = true;
                    } else if (tempS.equals(" ") || tempS.equals("\n")) {
                        isTag = false;
                    } else {
                        String temp = s.toString().substring(0, selection);
                        JYLog.D(TAG + "::temp::" + temp, new Throwable());
                        if (temp.contains("#") || temp.contains("\\#")) {
                            String isCheckTag = temp.substring(temp.lastIndexOf("#"), selection);
                            JYLog.D(TAG + "::temp::isCheckTag::" + isCheckTag, new Throwable());
                            if (isCheckTag.contains(" ") || isCheckTag.contains("\\n")) {
                                isTag = false;
                            } else {
                                isTag = true;
                            }
                        } else {
                            isTag = false;
                        }
                    }

                    if (s.toString().contains("#") || s.toString().contains("\\#")) {
                        if (s.toString().contains("\\n") || s.toString().contains("\n")) {
                            String[] tempTags = s.toString().split("\n");
                            int end = 0;
                            for (int i = 0; i < tempTags.length; i++) {
                                String str = tempTags[i];
                                String[] temp = str.split(" ");
                                Spannable spannable = editContent.getEditableText();
                                for (int j = 0; j < temp.length; j++) {
                                    String strTag = temp[j];
                                    if (strTag.contains("#") || strTag.contains("\\#")) {
                                        int index = strTag.indexOf("#");
                                        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), end, end + index, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#4b9b50")), end + index, end + strTag.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    } else {
                                        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), end, end + strTag.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    }
                                    end += strTag.length() + 1;
                                }
                            }
                        } else {
                            String[] tempTags = s.toString().split(" ");
                            Spannable spannable = editContent.getEditableText();
                            int end = 0;
                            for (int i = 0; i < tempTags.length; i++) {
                                String str = tempTags[i];
                                if (str.contains("#") || str.contains("\\#")) {
                                    int index = str.indexOf("#");
                                    spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), end, end + index, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#4b9b50")), end + index, end + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                } else {
                                    spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), end, end + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                                end += str.length() + 1;
                            }
                        }
                    }

                    int countLength = s.toString().length();
                    if (countLength > 1000) {
                        editContent.setText(s.toString().substring(0, s.toString().length() - 1));
                        if (!TextUtil.isNull(editContent.getText().toString()))
                            editContent.setSelection(editContent.getText().toString().length());
                        Toast.makeText(getActivity(), getString(R.string.str_upload_limit_content_length), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    isTag = false;
                }

                if (isTag) {
                    if (flWrapperTag.getVisibility() == View.GONE) {
                        llWrapperLocationShare.setVisibility(View.GONE);
                        flWrapperTag.setVisibility(View.VISIBLE);
                    }
                    String current = editContent.getText().toString().substring(0, selection);
                    JYLog.D(TAG + "::search::selection::" + selection, new Throwable());
                    JYLog.D(TAG + "::search::currentTag::" + current, new Throwable());
                    String[] strHashtags = current.split("#");
                    if (strHashtags != null && strHashtags.length > 0) {
                        String lastStrHashTagSentance = strHashtags[strHashtags.length - 1];
                        String[] tempStrBySplitWhiteSpace = lastStrHashTagSentance.split(" ");
                        String[] tempStrBySplitEnter = tempStrBySplitWhiteSpace[tempStrBySplitWhiteSpace.length - 1].split("\n");
                        String currentHashtag = tempStrBySplitEnter[tempStrBySplitEnter.length - 1];
                        JYLog.D(TAG + "::search::lastStr::" + currentHashtag, new Throwable());
                        searchHashtagAdapter.setKeyword(currentHashtag);
                        netFunc.requestHashTagSearch(currentHashtag, null, null);
                    }
                } else {
                    if (flWrapperTag.getVisibility() == View.VISIBLE) {
                        llWrapperLocationShare.setVisibility(View.VISIBLE);
                        flWrapperTag.setVisibility(View.GONE);
                        searchTagList = null;
                        searchHashtagAdapter.setItemArray(searchTagList);
                        llWrapperRecentTag.setVisibility(View.VISIBLE);
                        rvSearchTag.setVisibility(View.INVISIBLE);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /** footer (location, tag, share) **/

        /* tag */
        initializeHashtag(view);

        llWrapperLocationShare = (LinearLayout) view.findViewById(R.id.camera_upload_ll_wrapper_location_share);
        txtLocation = (TextView) view.findViewById(R.id.camera_upload_tv_location);
        imgBtnTag = (ImageButton) view.findViewById(R.id.camera_upload_imgbtn_tag);
        imgBtnShareFacebook = (ImageButton) view.findViewById(R.id.camera_upload_imgbtn_facebook);
        imgBtnShareInsta = (ImageButton) view.findViewById(R.id.camera_upload_imgbtn_instagram);
        imgBtnShareKakao = (ImageButton) view.findViewById(R.id.camera_upload_imgbtn_kakaostory);
        llWrapperLocationShare.setVisibility(View.VISIBLE);

        /* sharing */
        prefUtil = PrefUtil.getInstance();
        myInfo = PlaygreenManager.getUserInfo();
        if (myInfo.isFacebookConnected) {
            imgBtnShareFacebook.setSelected(true);
        }

        view.findViewById(R.id.camera_upload_imgbtn_back).setOnClickListener(this);
        view.findViewById(R.id.camera_upload_tv_complete).setOnClickListener(this);
        txtLocation.setOnClickListener(this);
        imgBtnTag.setOnClickListener(this);
        imgBtnShareFacebook.setOnClickListener(this);
        imgBtnShareInsta.setOnClickListener(this);
        imgBtnShareKakao.setOnClickListener(this);

        if (!TextUtil.isNull(overlayAbsolutePath)) {
            /** cinemagraph **/
            Glide.with(getContext())
                    .load(firstFramAbsolutePath)
                    .into(new GlideDrawableImageViewTarget(imgContent) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                            JYLog.D(TAG + "::onResourceReady()", new Throwable());
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            JYLog.D(TAG + "::onLoadFailed()" + e.getMessage(), new Throwable());
                        }
                    });

//            videoView.setVideoURI(Uri.parse(absolutePath));
//            videoView.setVisibility(View.VISIBLE);
//            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    if (isComplited)
//                        videoView.start();
//                }
//            });
//            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mp.setVolume(0f, 0f);
//                }
//            });
//            videoView.seekTo(0);
//            videoView.requestFocus();
//            videoView.start();

        } else {
            /** photo **/
            Glide.with(getContext())
                    .load(absolutePath)
                    .into(new GlideDrawableImageViewTarget(imgContent) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                            JYLog.D(TAG + "::onResourceReady()", new Throwable());
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            JYLog.D(TAG + "::onLoadFailed()" + e.getMessage(), new Throwable());
                        }
                    });
        }

        if (cameraActivity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (ActivityCompat.checkSelfPermission(cameraActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(cameraActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    cameraActivity.requestPermission();
                }

            }

            if (cameraActivity.location == null)
                cameraActivity.requestSearchLocation(null, netUtil);

        }

        if (chkGpsService()) {
            Intent intent = new Intent(getActivity(), GPSService.class);
            intent.setAction(GPSService.GPS_START_ACTION);
            getActivity().startService(intent);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (cameraActivity.location != null) {
            if (!TextUtil.isNull(cameraActivity.location.name))
                txtLocation.setText(cameraActivity.location.name);
        }

        if (isSharing) {
            JYLog.D("checkShareSns::" + isSharing, new Throwable());
            checkShareSns(resultData);
        }

    }

    @Override
    public void onDestroy() {

        cameraActivity.location = null;

        Intent intent = new Intent(getActivity(), GPSService.class);
        intent.setAction(GPSService.GPS_STOP_ACTION);
        getActivity().startService(intent);

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_upload_imgbtn_back:
                ((CameraActivity) getActivity()).onBackPressed();
                break;
            case R.id.camera_upload_tv_complete:
                requestUploadTimeLine();
                break;
            case R.id.camera_upload_tv_location:
                hiddenKeyboard();
                cameraActivity.gotoSearchLocationFrag();
                break;
            case R.id.camera_upload_imgbtn_tag:
                /** total content **/
                String allContent = editContent.getText().toString();
                selection = 0;
                if (TextUtil.isNull(allContent)) {
                    // edittext is empty. (null)
                    allContent = "#";
                    selection = 1;
                } else {
                    // edittext is not empty. (not null)
                    /** cursor **/
                    String previousContent;
                    if (editContent.getSelectionStart() == 0) {
                        previousContent = "#";
                        selection = 1;
                        allContent = previousContent + allContent;
                    } else if (editContent.getSelectionStart() == allContent.length()) {
                        previousContent = editContent.getText().toString().substring(0, editContent.getSelectionStart()) + "#";
                        selection = previousContent.length();
                        allContent = previousContent;
                    } else {
                        previousContent = editContent.getText().toString().substring(0, editContent.getSelectionStart()) + "#";
                        selection = previousContent.length();
                        allContent = previousContent + allContent.substring(editContent.getSelectionStart() + 1, allContent.length());
                    }
                }

                isClickTag = true;
                editContent.setText(allContent);
                editContent.setSelection(selection);
                break;

            case R.id.camera_upload_imgbtn_facebook:
                imgBtnShareFacebook.setSelected(!imgBtnShareFacebook.isSelected());
                if (myInfo != null) {
                    if (imgBtnShareFacebook.isSelected() && !myInfo.isFacebookConnected) {
                        facebookLogin();
                    }
                }
                break;
            case R.id.camera_upload_imgbtn_instagram:
                imgBtnShareInsta.setSelected(!imgBtnShareInsta.isSelected());
                break;
            case R.id.camera_upload_imgbtn_kakaostory:
                imgBtnShareKakao.setSelected(!imgBtnShareKakao.isSelected());
                break;

        }
    }

    private void initializeHashtag(View view) {
        flWrapperTag = (FrameLayout) view.findViewById(R.id.camera_upload_fl_wrapper_tag);
        flWrapperTag.setVisibility(View.GONE);
        llWrapperRecentTag = (LinearLayout) view.findViewById(R.id.camrea_upload_ll_wrapper_recent_hashtag);
        rvRecentTag = (RecyclerView) view.findViewById(R.id.camera_upload_rv_recent_tag_that_is_input);
        rvSearchTag = (RecyclerView) view.findViewById(R.id.camera_upload_rv_search_similar_tag_that_is_input);

        LinearLayoutManager llmRecentTag = new LinearLayoutManager(getActivity());
        llmRecentTag.setOrientation(LinearLayoutManager.VERTICAL);
        rvRecentTag.setLayoutManager(llmRecentTag);
        recentHashtagAdapter = new HashtagAdapter(getActivity());
        recentHashtagAdapter.setTypeList(TYPE_RECENT_HASHTAG_LIST);
        recentHashtagAdapter.setItemStrArray(recentTagList);
        recentHashtagAdapter.setAdapterItemClickListener(new AdapterItemClickListener() {
            @Override
            public void onAdapterItemClick(View view, int position) {
                if (recentTagList == null || recentTagList.size() <= position)
                    return;
                switch (view.getId()) {
                    case R.id.camera_upload_tv_hash_tag:
                        String current = editContent.getText().toString().substring(0, selection);
                        String currentContent = editContent.getText().toString();
                        if (current.contains("#") || current.contains("\\#")) {
                            if (selection == currentContent.length()) {
                                currentContent = current.substring(0, current.lastIndexOf("#")) + "#" + recentTagList.get(position) + " ";
                                selection = currentContent.length();
                            } else {
                                currentContent = current.substring(0, current.lastIndexOf("#")) + "#" + recentTagList.get(position) + " " + currentContent.substring(selection, currentContent.length());
                                selection = (current.substring(0, current.lastIndexOf("#")) + "#" + recentTagList.get(position) + " ").length();
                            }
                        }
                        JYLog.D(TAG + "::input::current::" + current, new Throwable());
                        JYLog.D(TAG + "::input::currentContent::" + currentContent, new Throwable());
                        isInputTag = true;
                        editContent.setText(currentContent);
                        editContent.setSelection(selection);
                        break;
                    case R.id.camera_upload_imgbtn_delete_current_hash_tag:
                        JYLog.D(TAG + "::remove::recent::" + recentTagList.get(position), new Throwable());
                        recentTagList.remove(position);
                        recentHashtagAdapter.removeItemStr(position);
                        if (recentTagList != null) {
                            String tempRecentTags = "";
                            for (int i = 0; i < recentTagList.size(); i++) {
                                if ((i + 1) == recentTagList.size()) {
                                    tempRecentTags += recentTagList.get(i);
                                } else {
                                    tempRecentTags += recentTagList.get(i) + ",";
                                }
                            }
                            PrefUtil.getInstance().putPreference(Definitions.PREFKEY.RECENT_HASH_TAG, tempRecentTags);
                        }
                        break;
                }
            }
        });
        rvRecentTag.setAdapter(recentHashtagAdapter);

        LinearLayoutManager llmSearchTag = new LinearLayoutManager(getActivity());
        llmSearchTag.setOrientation(LinearLayoutManager.VERTICAL);
        rvSearchTag.setLayoutManager(llmSearchTag);
        searchHashtagAdapter = new HashtagAdapter(getActivity());
        searchHashtagAdapter.setTypeList(TYPE_SEARCH_HASHTAG_LIST);
        searchHashtagAdapter.setAdapterItemClickListener(new AdapterItemClickListener() {
            @Override
            public void onAdapterItemClick(View view, int position) {
                if (searchTagList == null || searchTagList.size() <= position)
                    return;
                JYLog.D(TAG + "::input::HASHTAG::" + searchTagList.get(position).HASHTAG, new Throwable());
                String current = editContent.getText().toString().substring(0, selection);
                String currentContent = editContent.getText().toString();
                if (current.contains("#") || current.contains("\\#")) {
                    if (selection == currentContent.length()) {
                        currentContent = current.substring(0, current.lastIndexOf("#")) + "#" + searchTagList.get(position).HASHTAG + " ";
                        selection = currentContent.length();
                    } else {
                        currentContent = current.substring(0, current.lastIndexOf("#")) + "#" + searchTagList.get(position).HASHTAG + " " + currentContent.substring(selection, currentContent.length());
                        selection = (current.substring(0, current.lastIndexOf("#")) + "#" + searchTagList.get(position).HASHTAG + " ").length();
                    }
                }
                JYLog.D(TAG + "::input::current::" + current, new Throwable());
                JYLog.D(TAG + "::input::currentContent::" + currentContent, new Throwable());
                isInputTag = true;
                editContent.setText(currentContent);
                editContent.setSelection(selection);

            }
        });
        rvSearchTag.setAdapter(searchHashtagAdapter);
    }

    private void requestUploadTimeLine() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("upload...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, NetworkConstantUtil.URLS.TIMELINE_REGIST, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                resultResponse.trim();
                if (resultResponse.contains("DOCTYPE") || resultResponse.contains("doctype")) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), R.string.str_server_error_occured, Toast.LENGTH_SHORT).show();
                    return;
                }
                Gson gson = new Gson();
                final NetworkJson networkJson = gson.fromJson(resultResponse, NetworkJson.class);
                JYLog.D(TAG + "::RESULT_CODE::" + networkJson.RESULT_CODE, new Throwable());
                if (NetworkErrorUtill.isJsonResultCheck(networkJson)) {

                    if (hashtags != null && hashtags.size() > 0) {
                        for (int i = 0; i < hashtags.size(); i++) {
                            final int finalI = i;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    requestHashTagRegist(hashtags.get(finalI), "T", networkJson.DATA.TIMELINE_ID);
                                }
                            }, i * 150);
                        }
                    }

                    resultData = networkJson;

                    if (imgBtnShareFacebook.isSelected())
                        countSharingInSns++;
                    if (imgBtnShareInsta.isSelected())
                        countSharingInSns++;
                    if (imgBtnShareKakao.isSelected())
                        countSharingInSns++;

                    checkShareSns(resultData);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                JYLog.D(TAG + "::error::" + error.getLocalizedMessage(), new Throwable());
                JYLog.D(TAG + "::error::" + error.getMessage(), new Throwable());

                if (progressDialog != null)
                    progressDialog.dismiss();

                Toast.makeText(getActivity(), getString(R.string.str_upload_fail), Toast.LENGTH_SHORT).show();

                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
                if (cameraActivity.location != null && !TextUtil.isNull(cameraActivity.location.name)) {
                    try {
                        params.put("LOCATION", URLEncoder.encode(cameraActivity.location.name, "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                String timelineText = editContent.getText().toString();
                hashtags = new ArrayList<>();
                if (!TextUtil.isNull(timelineText)) {
                    try {
                        params.put("TIMELINE_TEXT", URLEncoder.encode(timelineText, "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    JYLog.D(TAG + "::timelineText::\n" + timelineText, new Throwable());
                    JYLog.D(TAG + "::timelineText::" + StringEscapeUtils.escapeJava(timelineText), new Throwable());
                    if (timelineText.contains("#") || timelineText.contains("\\#")) {
                        // timelineText has a hashtag or hashtags.
                        if (timelineText.contains("\\n") || timelineText.contains("\n")) {
                            // timelineText has \n
                            String[] tempSentenceWithEnter = timelineText.split("\n");
                            for (int i = 0; i < tempSentenceWithEnter.length; i++) {
                                String tempSentence = tempSentenceWithEnter[i];
                                if (tempSentence.contains(" ")) {
                                    // timelineText has white space.
                                    String[] tempSentenceWithWhiteSpace = tempSentence.split(" ");
                                    for (int j = 0; j < tempSentenceWithWhiteSpace.length; j++) {
                                        String strTag = tempSentenceWithWhiteSpace[j];
                                        JYLog.D(TAG + "::timelineText::1.1::" + strTag, new Throwable());
                                        if (strTag.contains("#") || strTag.contains("\\#")) {
                                            String[] tempHashtags = strTag.split("#");
                                            for (int k = 0; k < tempHashtags.length; k++) {
                                                if (k > 0) {
                                                    JYLog.D(TAG + "::timelineText::1.2::" + tempHashtags[k], new Throwable());
                                                    hashtags.add(tempHashtags[k]);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    // timelineText does not have white space.
                                    JYLog.D(TAG + "::timelineText::1.3::" + tempSentence, new Throwable());
                                    if (tempSentence.contains("#") || tempSentence.contains("\\#")) {
                                        String[] tempHashtags = tempSentence.split("#");
                                        for (int j = 0; j < tempHashtags.length; j++) {
                                            if (j > 0) {
                                                JYLog.D(TAG + "::timelineText::1.4::" + tempHashtags[j], new Throwable());
                                                hashtags.add(tempHashtags[j]);
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (timelineText.contains(" ")) {
                            // timelineText has white space.
                            String[] tempSentence = timelineText.split(" ");
                            for (int i = 0; i < tempSentence.length; i++) {
                                String strTag = tempSentence[i];
                                JYLog.D(TAG + "::timelineText::2.1::" + strTag, new Throwable());
                                if (strTag.contains("#") || strTag.contains("\\#")) {
                                    String[] tempHashtags = strTag.split("#");
                                    for (int j = 0; j < tempHashtags.length; j++) {
                                        if (j > 0) {
                                            JYLog.D(TAG + "::timelineText::2.2::" + tempHashtags[j], new Throwable());
                                            hashtags.add(tempHashtags[j]);
                                        }
                                    }
                                }
                            }
                        } else {
                            JYLog.D(TAG + "::timelineText::3::" + timelineText, new Throwable());
                            String[] tempHashtags = timelineText.split("#");
                            for (int i = 0; i < tempHashtags.length; i++) {
                                if (i > 0) {
                                    JYLog.D(TAG + "::timelineText::3::" + tempHashtags[i], new Throwable());
                                    hashtags.add(tempHashtags[i]);
                                }
                            }
                        }
                    }
                }
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
//                params.put("avatar", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mAvatarImage.getDrawable()), "image/jpeg"));
//                params.put("cover", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mCoverImage.getDrawable()), "image/jpeg"));

                if (!TextUtil.isNull(overlayAbsolutePath)) {
                    JYLog.D(TAG + "::" + absolutePath, new Throwable());
                    JYLog.D(TAG + "::" + overlayAbsolutePath, new Throwable());
                    JYLog.D(TAG + "::" + firstFramAbsolutePath, new Throwable());
                    try {
                        /**
                         * 이미지 업로드 변경.
                         * TIMELINE_MP4_SCENE --> TIMELINE_IMG
                         * TIMELINE_IMG --> TIMELINE_MP4_SCENE
                         **/
                        params.put("TIMELINE_MP4", new DataPart(new File(absolutePath).getName(), getBytesFromFile(new File(absolutePath)), "video/mp4"));
                        params.put("TIMELINE_MP4_SCENE", new DataPart(new File(overlayAbsolutePath).getName(), getBytesFromFile(new File(overlayAbsolutePath)), "image/png"));
                        params.put("TIMELINE_IMG", new DataPart(new File(firstFramAbsolutePath).getName(), getBytesFromFile(new File(firstFramAbsolutePath)), "image/jpeg"));
//                        params.put("TIMELINE_IMG", new DataPart(new File(overlayAbsolutePath).getName(), getBytesFromFile(new File(overlayAbsolutePath)), "image/png"));
//                        params.put("TIMELINE_MP4_SCENE", new DataPart(new File(firstFramAbsolutePath).getName(), getBytesFromFile(new File(firstFramAbsolutePath)), "image/jpeg"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    JYLog.D(TAG + "::" + absolutePath, new Throwable());
                    try {
                        params.put("TIMELINE_IMG", new DataPart(new File(absolutePath).getName(), getBytesFromFile(new File(absolutePath)), "image/jpeg"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return params;
            }
        };

        multipartRequest.setShouldCache(false);


        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        NetworkController.addToRequestQueue(multipartRequest);

//        final RequestQueue q = DefaultRequestQueueFactory.create(getActivity());
//
//        q.start();
//        PostBuilder pb = Volleyer.volleyer(q).post(NetworkConstantUtil.URLS.TIMELINE_REGIST)
//                .addStringPart("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
//
//        JYLog.D("AUTH_TOKEN::" + PlaygreenManager.getAuthToken(), new Throwable());
//
//        String timelineText = editContent.getText().toString();
//        hashtags = new ArrayList<>();
//        if (!TextUtil.isNull(timelineText)) {
//            try {
//                pb.addStringPart("TIMELINE_TEXT", URLEncoder.encode(timelineText, "utf-8"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            JYLog.D(TAG + "::timelineText::\n" + timelineText, new Throwable());
//            JYLog.D(TAG + "::timelineText::" + StringEscapeUtils.escapeJava(timelineText), new Throwable());
//            if (timelineText.contains("#") || timelineText.contains("\\#")) {
//                // timelineText has a hashtag or hashtags.
//                if (timelineText.contains("\\n") || timelineText.contains("\n")) {
//                    // timelineText has \n
//                    String[] tempSentenceWithEnter = timelineText.split("\n");
//                    for (int i = 0; i < tempSentenceWithEnter.length; i++) {
//                        String tempSentence = tempSentenceWithEnter[i];
//                        if (tempSentence.contains(" ")) {
//                            // timelineText has white space.
//                            String[] tempSentenceWithWhiteSpace = tempSentence.split(" ");
//                            for (int j = 0; j < tempSentenceWithWhiteSpace.length; j++) {
//                                String strTag = tempSentenceWithWhiteSpace[j];
//                                JYLog.D(TAG + "::timelineText::1.1::" + strTag, new Throwable());
//                                if (strTag.contains("#") || strTag.contains("\\#")) {
//                                    String[] tempHashtags = strTag.split("#");
//                                    for (int k = 0; k < tempHashtags.length; k++) {
//                                        if (k > 0) {
//                                            JYLog.D(TAG + "::timelineText::1.2::" + tempHashtags[k], new Throwable());
//                                            hashtags.add(tempHashtags[k]);
//                                        }
//                                    }
//                                }
//                            }
//                        } else {
//                            // timelineText does not have white space.
//                            JYLog.D(TAG + "::timelineText::1.3::" + tempSentence, new Throwable());
//                            if (tempSentence.contains("#") || tempSentence.contains("\\#")) {
//                                String[] tempHashtags = tempSentence.split("#");
//                                for (int j = 0; j < tempHashtags.length; j++) {
//                                    if (j > 0) {
//                                        JYLog.D(TAG + "::timelineText::1.4::" + tempHashtags[j], new Throwable());
//                                        hashtags.add(tempHashtags[j]);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                } else if (timelineText.contains(" ")) {
//                    // timelineText has white space.
//                    String[] tempSentence = timelineText.split(" ");
//                    for (int i = 0; i < tempSentence.length; i++) {
//                        String strTag = tempSentence[i];
//                        JYLog.D(TAG + "::timelineText::2.1::" + strTag, new Throwable());
//                        if (strTag.contains("#") || strTag.contains("\\#")) {
//                            String[] tempHashtags = strTag.split("#");
//                            for (int j = 0; j < tempHashtags.length; j++) {
//                                if (j > 0) {
//                                    JYLog.D(TAG + "::timelineText::2.2::" + tempHashtags[j], new Throwable());
//                                    hashtags.add(tempHashtags[j]);
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    JYLog.D(TAG + "::timelineText::3::" + timelineText, new Throwable());
//                    String[] tempHashtags = timelineText.split("#");
//                    for (int i = 0; i < tempHashtags.length; i++) {
//                        if (i > 0) {
//                            JYLog.D(TAG + "::timelineText::3::" + tempHashtags[i], new Throwable());
//                            hashtags.add(tempHashtags[i]);
//                        }
//                    }
//                }
//            }
//        }
//
//        if (!TextUtil.isNull(overlayAbsolutePath)) {
//            JYLog.D(TAG + "::" + absolutePath, new Throwable());
//            JYLog.D(TAG + "::" + overlayAbsolutePath, new Throwable());
//            JYLog.D(TAG + "::" + firstFramAbsolutePath, new Throwable());
//            if (new File(absolutePath) != null)
////                pb.addFilePart("TIMELINE_MP4", new File(absolutePath));
////            if (new File(overlayAbsolutePath) != null)
////                pb.addFilePart("TIMELINE_IMG", new File(overlayAbsolutePath));
//                pb.addPart(new FilePart("TIMELINE_MP4", new File(absolutePath)));
////            pb.addPart(new FilePart("TIMELINE_IMG", new File(overlayAbsolutePath)));
////            if (new File(firstFramAbsolutePath) != null)
////                pb.addFilePart("TIMELINE_MP4_SCENE", new File(firstFramAbsolutePath));
//        } else {
//            JYLog.D(TAG + "::" + absolutePath, new Throwable());
//            pb.addFilePart("TIMELINE_IMG", new File(absolutePath));
//        }
//
//        if (cameraActivity.location != null && !TextUtil.isNull(cameraActivity.location.name)) {
//            pb.addStringPart("LOCATION", cameraActivity.location.name);
//        }
//
//        pb.withListener(new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                JYLog.D(TAG + "::???::" + response, new Throwable());
//                Gson gson = new Gson();
//                final NetworkJson networkJson = gson.fromJson(response, NetworkJson.class);
//                JYLog.D(TAG + "::RESULT_CODE::" + networkJson.RESULT_CODE, new Throwable());
//                if (NetworkErrorUtill.isJsonResultCheck(networkJson)) {
//                    if (hashtags != null && hashtags.size() > 0) {
//                        for (int i = 0; i < hashtags.size(); i++) {
//                            final int finalI = i;
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    requestHashTagRegist(hashtags.get(finalI), "T", networkJson.DATA.TIMELINE_ID);
//                                }
//                            }, i * 150);
//                        }
//                    } else {
//
//                        resultData = networkJson;
//
//                        if (imgBtnShareFacebook.isSelected())
//                            countSharingInSns++;
//                        if (imgBtnShareInsta.isSelected())
//                            countSharingInSns++;
//                        if (imgBtnShareKakao.isSelected())
//                            countSharingInSns++;
//
//                        checkShareSns(resultData);
//
//                    }
//                }
//            }
//        }).withErrorListener(new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                JYLog.D(TAG + "::error::" + error.getLocalizedMessage(), new Throwable());
//                JYLog.D(TAG + "::error::" + error.getMessage(), new Throwable());
//
//                if (progressDialog != null)
//                    progressDialog.dismiss();
//
//                Toast.makeText(getActivity(), getString(R.string.str_upload_fail), Toast.LENGTH_SHORT).show();
//
//            }
//        }).execute();

    }

    public void requestHashTagRegist(String hashtag, String category, String targetId) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("AUTH_TOKEN", PlaygreenManager.getAuthToken() + "");
        if (TextUtil.isNull(hashtag) || TextUtil.isNull(category) || TextUtil.isNull(targetId)) {
            return;
        }
        params.put("HASHTAG", hashtag);
        params.put("HASHTAG_CATEGORY", category);
        params.put("TARGET_ID", targetId);
        StringRequest myReq = netUtil.requestPost(APIKEY.REGIST_HASHTAG, NetworkConstantUtil.URLS.REGIST_HASHTAG, params);
        NetworkController.addToRequestQueue(myReq);
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    @Override
    public void onNetworkResult(int idx, NetworkJson json) {
        super.onNetworkResult(idx, json);
        switch (idx) {
            case APIKEY.SEARCH_HASHTAG:
                searchTagList = json.LIST;
                searchHashtagAdapter.setItemArray(searchTagList);
                if (searchTagList != null && searchTagList.size() > 0) {
                    llWrapperRecentTag.setVisibility(View.INVISIBLE);
                    rvSearchTag.setVisibility(View.VISIBLE);
                } else {
                    llWrapperRecentTag.setVisibility(View.VISIBLE);
                    rvSearchTag.setVisibility(View.INVISIBLE);
                }
                break;
            case APIKEY.REGIST_HASHTAG:
                if (hashtags != null && hashtags.size() > 0) {
                    countRegistHashtag++;
                    JYLog.D(TAG + "::hashtag::countRegistHashtag::" + countRegistHashtag, new Throwable());
                    JYLog.D(TAG + "::hashtag::size::" + hashtags.size(), new Throwable());
                    JYLog.D(TAG + "::hashtag::HASHTAG::" + json.DATA.HASHTAG, new Throwable());
                    JYLog.D(TAG + "::hashtag::TARGET_ID::" + json.DATA.TARGET_ID, new Throwable());
                    JYLog.D(TAG + "::hashtag::HASHTAG_ID::" + json.DATA.HASHTAG_ID, new Throwable());

                    if (recentTagList == null)
                        recentTagList = new ArrayList<>();

                    boolean isContain = false;
                    if (recentTagList.size() > 0) {
                        for (String tag : recentTagList) {
                            if (tag.equals(json.DATA.HASHTAG)) {
                                isContain = true;
                                break;
                            }
                        }
                    }

                    if (!isContain) {
                        recentTagList.add(json.DATA.HASHTAG);
                    }

                    if (countRegistHashtag == hashtags.size()) {
                        if (recentTagList != null) {
                            String tempRecentTags = "";
                            for (int i = 0; i < recentTagList.size(); i++) {
                                if ((i + 1) == recentTagList.size()) {
                                    tempRecentTags += recentTagList.get(i);
                                } else {
                                    tempRecentTags += recentTagList.get(i) + ",";
                                }
                            }
                            PrefUtil.getInstance().putPreference(Definitions.PREFKEY.RECENT_HASH_TAG, tempRecentTags);
                        }
                        countRegistHashtag = 0;
                        if (!isSharing) {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            hiddenKeyboard();
                            Toast.makeText(getActivity(), getString(R.string.str_upload_success), Toast.LENGTH_SHORT).show();
                            cameraActivity.finish();
                        }
                    }
                }
                break;
            case APIKEY.GOOGLE_SEARCH_PLACES:
                break;
        }
    }

    @Override
    public void onNetworkError(int idx, VolleyError error, NetworkJson json) {
        super.onNetworkError(idx, error, json);
        switch (idx) {
            case APIKEY.GOOGLE_SEARCH_PLACES:
                if (json != null && json.results != null && json.results.size() > 0) {
                    cameraActivity.location = json.results.get(0);
//                    JYLog.D("Dfadfa::" + cameraActivity.location, new Throwable());
                    if (!TextUtil.isNull(cameraActivity.location.name))
                        txtLocation.setText(cameraActivity.location.name);
                }
                break;
        }
    }

    private boolean chkGpsService() {

        String gps = android.provider.Settings.Secure.getString(getActivity().getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {

            // GPS OFF 일때 Dialog 표시
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(getActivity());
            gsDialog.setTitle("위치 서비스 설정");
            gsDialog.setMessage("무선 네트워크 사용, GPS 위성 사용을 모두 체크하셔야 정확한 위치 서비스가 가능합니다.\n위치 서비스 기능을 설정하시겠습니까?");
            gsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // GPS설정 화면으로 이동
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
            })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).create().show();
            return false;

        } else {

            Intent intent = new Intent(getActivity(), GPSService.class);
            getActivity().startService(intent);

            return true;
        }
    }

    public void snsAccountStateChange(String whatSns, boolean isConnected) {
        switch (whatSns) {
            case Definitions.AUTH_CHANNEL.FACEBOOK:
                imgBtnShareFacebook.setSelected(isConnected);
                prefUtil.putPreference(Definitions.PREFKEY.SNS_ACCOUNT_FACEBOOK_BOOL, isConnected);
                break;
            case Definitions.AUTH_CHANNEL.INSTAGRAM:
                imgBtnShareInsta.setSelected(isConnected);
                prefUtil.putPreference(Definitions.PREFKEY.SNS_ACCOUNT_INSTAGRAM_BOOL, isConnected);
                break;
            case Definitions.AUTH_CHANNEL.KAKAOTALK:
                imgBtnShareKakao.setSelected(isConnected);
                prefUtil.putPreference(Definitions.PREFKEY.SNS_ACCOUNT_KAKAO_BOOL, isConnected);
                break;
        }
    }

    /***************************
     * Facebook 로그인
     ***************************************/
    private LoginManager loginManager;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private String facebookReferenceId = null;
    private String facebookImagePath = null;
    private String facebookName = null;
    private String facebookEmail = null;
    private ProfileTracker profileTracker;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        if (requestCode == Definitions.ACTIVITY_REQUEST_CODE.FACEBOOK_CONNECT) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            Profile.fetchProfileForCurrentAccessToken();
        }
    };

    public void facebookLogin() {
        loginManager = LoginManager.getInstance();
        loginManager.logOut();
        callbackManager = CallbackManager.Factory.create();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                JYLog.D("token : \n" + loginResult.getAccessToken().getToken(), new Throwable());
                JYLog.D("profileTracker : " + profileTracker.isTracking(), new Throwable());
                if (profileTracker.isTracking() == false) profileTracker.startTracking();
                accessToken = loginResult.getAccessToken();
                Profile.fetchProfileForCurrentAccessToken();
            }

            @Override
            public void onCancel() {
                JYLog.D(new Throwable());
                profileTracker.stopTracking();
                accessTokenTracker.stopTracking();
                snsAccountStateChange(Definitions.AUTH_CHANNEL.FACEBOOK, false);
                Toast.makeText(getActivity(), getString(R.string.str_guide_share_to_facebook), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                JYLog.D(new Throwable());
                if (exception != null)
                    Debug.showDebug(exception.toString());
                profileTracker.stopTracking();
                accessTokenTracker.stopTracking();
                snsAccountStateChange(Definitions.AUTH_CHANNEL.FACEBOOK, false);
            }
        });

        ArrayList<String> permissions = new ArrayList<>();
        permissions.add("user_friends");
        permissions.add("public_profile");
        permissions.add("email");
        loginManager.logInWithReadPermissions(this, permissions);

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    facebookReferenceId = currentProfile.getId();
                    facebookImagePath = currentProfile.getProfilePictureUri(500, 500).toString();
                    facebookName = currentProfile.getName();
                    GraphRequest request = GraphRequest.newMeRequest(accessToken,
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    if (response != null)
                                        JYLog.D(response.toString(), new Throwable());
                                    try {
                                        facebookEmail = object.getString("email");
                                        snsAccountStateChange(Definitions.AUTH_CHANNEL.FACEBOOK, true);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "email");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
                profileTracker.stopTracking();
            }
        };
    }

    private void checkShareSns(NetworkJson networkJson) {

        JYLog.D("checkShareSns::" + countSharingInSns, new Throwable());
        JYLog.D("checkShareSns::fb::" + imgBtnShareFacebook.isSelected(), new Throwable());
        JYLog.D("checkShareSns::insta::" + imgBtnShareInsta.isSelected(), new Throwable());
        JYLog.D("checkShareSns::kakao::" + imgBtnShareKakao.isSelected(), new Throwable());
        isSharing = true;

        /** not share **/
        if (countSharingInSns == 0) {
            if (progressDialog != null)
                progressDialog.dismiss();

            isSharing = false;
            Toast.makeText(getActivity(), getString(R.string.str_upload_success), Toast.LENGTH_SHORT).show();
            cameraActivity.finish();
            return;
        }
//
        if (imgBtnShareFacebook.isSelected() && !isSharedFacebook) {
            if (networkJson != null && networkJson.DATA != null && !TextUtil.isNull(networkJson.DATA.TIMELINE_MP4_SCENE)) {
                shareToFacebook(networkJson.DATA.TIMELINE_MP4_SCENE, networkJson.DATA.TIMELINE_TEXT);
            } else if (networkJson != null && networkJson.DATA != null && !TextUtil.isNull(networkJson.DATA.TIMELINE_IMG)) {
                shareToFacebook(networkJson.DATA.TIMELINE_IMG, networkJson.DATA.TIMELINE_TEXT);
            }
            isSharedFacebook = true;
            countSharingInSns--;
        } else if (imgBtnShareKakao.isSelected() && !isSharedKakaostory) {
            if (!TextUtil.isNull(firstFramAbsolutePath)) {
                shareKakaostory(firstFramAbsolutePath);
            } else {
                shareKakaostory(absolutePath);
            }
            isSharedKakaostory = true;
            countSharingInSns--;
        } else if (imgBtnShareInsta.isSelected() && !isSharedInsta) {
            if (!TextUtil.isNull(firstFramAbsolutePath)) {
                shareInstagram(firstFramAbsolutePath);
            } else {
                shareInstagram(absolutePath);
            }
            isSharedInsta = true;
            countSharingInSns--;
        }

    }

    /**
     * share facebook
     **/
    public com.facebook.share.widget.ShareDialog facebookShareDialog;

    public void shareToFacebook(String url, String contentText) {
        FacebookSdk.sdkInitialize(getActivity());
        callbackManager = CallbackManager.Factory.create();
        facebookShareDialog = new com.facebook.share.widget.ShareDialog(this);
        facebookShareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                JYLog.D(TAG + "::checkShareSns::onSuccess", new Throwable());
            }

            @Override
            public void onCancel() {
                JYLog.D(TAG + "::checkShareSns::onCancel", new Throwable());
                isSharedFacebook = false;
                if (countSharingInSns != 0)
                    checkShareSns(resultData);
            }

            @Override
            public void onError(FacebookException error) {
                JYLog.D(TAG + "::checkShareSns::onCancel", new Throwable());
                isSharedFacebook = false;
                if (countSharingInSns != 0)
                    checkShareSns(resultData);
            }
        });

        String timelineContent = "";
        try {
            timelineContent = URLDecoder.decode(contentText, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Uri contentUri = Uri.parse(url);
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(contentUri)            //페북에서 클릭하면 들어가는 링크, (사이트이면 사이트로 이동, 동영상 링크이면 링크를 통해 동영상 재생됨)
                .setContentTitle(getString(R.string.str_share_to_facebook_title))
                .setImageUrl(Uri.parse(url))
                .setContentDescription(timelineContent)
                .build();
        facebookShareDialog.show(content, com.facebook.share.widget.ShareDialog.Mode.AUTOMATIC);
    }

    public void shareInstagram(String absolutePath) {

        ShareToSNSManager shareToSNSManager = new ShareToSNSManager(getActivity(), new File(absolutePath));
        shareToSNSManager.setDialogListener(new DialogListener() {
            @Override
            public void onSendDlgData(DialogData dialogData) {
                if (dialogData == null)
                    return;

                // TODO: 16. 5. 4. how to do?

                if (dialogData.isCancle) {

                }
            }
        });
        shareToSNSManager.shareToInstagram();

    }

    public void shareKakaostory(String absolutePath) {

        ShareToSNSManager shareToSNSManager = new ShareToSNSManager(getActivity(), new File(absolutePath));
        shareToSNSManager.setDialogListener(new DialogListener() {
            @Override
            public void onSendDlgData(DialogData dialogData) {
                if (dialogData == null)
                    return;

                // TODO: 16. 5. 4. how to do?

                if (dialogData.isCancle) {
                    if (isSharing) {
                        JYLog.D("checkShareSns::" + isSharing, new Throwable());
                        checkShareSns(resultData);
                    }
                }
            }
        });
        shareToSNSManager.shareToKakaoStory();

    }


    private void setKeyboardVisibilityListener(final View view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private boolean alreadyOpen;
            private final int defaultKeyboardHeightDP = 100;
            private final int EstimatedKeyboardDP = defaultKeyboardHeightDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
            private final Rect rect = new Rect();

            @Override
            public void onGlobalLayout() {
                int estimatedKeyboardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, view.getResources().getDisplayMetrics());
                view.getWindowVisibleDisplayFrame(rect);
                int heightDiff = view.getRootView().getHeight() - (rect.bottom - rect.top);
                boolean isShown = heightDiff >= estimatedKeyboardHeight;
                if (isShown == alreadyOpen) {
                    Log.i("Keyboard state", "Ignoring global layout change...");
                    return;
                }
                if (isShown == false) {
                    if (flWrapperTag.getVisibility() == View.VISIBLE) {
                        flWrapperTag.setVisibility(View.GONE);
                        llWrapperLocationShare.setVisibility(View.VISIBLE);
                        return;
                    }
                }
                alreadyOpen = isShown;
            }
        });
    }

}