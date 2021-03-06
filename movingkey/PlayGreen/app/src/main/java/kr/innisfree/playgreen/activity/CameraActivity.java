package kr.innisfree.playgreen.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;

import com.ParentAct;
import com.airfactory.service.GPSService;
import com.android.volley.toolbox.StringRequest;
import com.moyusoft.util.Definitions;
import com.moyusoft.util.JYLog;
import com.moyusoft.util.TextUtil;
import com.volley.network.NetworkConstantUtil;
import com.volley.network.NetworkController;
import com.volley.network.NetworkRequestUtil;
import com.volley.network.dto.NetworkData;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import bolts.AppLinks;
import kr.innisfree.playgreen.R;
import kr.innisfree.playgreen.fragment.camera.CameraCgToolFrag;
import kr.innisfree.playgreen.fragment.camera.CameraFilterFrag;
import kr.innisfree.playgreen.fragment.camera.CameraFrag;
import kr.innisfree.playgreen.fragment.camera.CameraUploadFrag;
import kr.innisfree.playgreen.fragment.camera.SearchLocationFrag;
import kr.innisfree.playgreen.fragment.camera.WrapperCameraFrag;

public class CameraActivity extends ParentAct {

    private static final String TAG = "CameraActivity";

    /**
     * The type tell which activity call the camera activity.
     */
    public static final int TYPE_FROM_MAIN_ACT = 0;
    public static final int TYPE_FROM_PG21_TODAY_MISSION_ACT = 1;
    public static final int TYPE_FROM_PROFILE_EDIT_CAMERA = 3;
    public static final int TYPE_FROM_PROFILE_EDIT_GALLERY = 4;

    public static int mTypeFromAct;

    /**
     * The orientation of the device.
     */
    public static final int ORIENTATION_PORTRAIT_NORMAL = 1;
    public static final int ORIENTATION_PORTRAIT_INVERTED = 2;
    public static final int ORIENTATION_LANDSCAPE_NORMAL = 3;
    public static final int ORIENTATION_LANDSCAPE_INVERTED = 4;

    public static int mOrientation = ORIENTATION_PORTRAIT_NORMAL;

    private OrientationEventListener mOrientationEventListener;

    public static int duration = 10;

    /**
     * svWidth : width of the device
     * svHeight : width * 4 / 3 ( ratio --> 4 : 3 )
     **/
    public int svWidth;
    public int svHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mTypeFromAct = getIntent().getIntExtra(Definitions.INTENT_KEY.FROM_ACTIVITY, -1);

        if (mTypeFromAct == -1)
            finish();

        initializeDisplay();
        gotoWrapperCameraFrag();

        Uri targetUrl =
                AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        } else {
            Log.i("Activity", "App Link Target URL: fjalkdjfklajldfjljkfda");
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Definitions.INTENT_ACTION.GPS_CHANGE);
        registerReceiver(ChangeLocationBroadcastReceiver, filter);

        duration = 10;

//        camera = getCameraInstance();
        if (mOrientationEventListener == null) {
            mOrientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {

                @Override
                public void onOrientationChanged(int orientation) {

                    // determine our orientation based on sensor response
                    int lastOrientation = mOrientation;

                    if (orientation >= 315 || orientation < 45) {
                        if (mOrientation != ORIENTATION_PORTRAIT_NORMAL) {
                            mOrientation = ORIENTATION_PORTRAIT_NORMAL;
                        }
                    } else if (orientation < 315 && orientation >= 225) {
                        if (mOrientation != ORIENTATION_LANDSCAPE_NORMAL) {
                            mOrientation = ORIENTATION_LANDSCAPE_NORMAL;
                        }
                    } else if (orientation < 225 && orientation >= 135) {
                        if (mOrientation != ORIENTATION_PORTRAIT_INVERTED) {
                            mOrientation = ORIENTATION_PORTRAIT_INVERTED;
                        }
                    } else { // orientation <135 && orientation > 45
                        if (mOrientation != ORIENTATION_LANDSCAPE_INVERTED) {
                            mOrientation = ORIENTATION_LANDSCAPE_INVERTED;
                        }
                    }

                }
            };
        }
        if (mOrientationEventListener.canDetectOrientation()) {
            mOrientationEventListener.enable();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(ChangeLocationBroadcastReceiver);
        mOrientationEventListener.disable();

    }

    /**
     * We get current device's diaplay widthPixels and heightPixels
     * because make surfaceview square.
     */
    private void initializeDisplay() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        JYLog.D(TAG + "::displaymetrics::width::" + width, new Throwable());
        JYLog.D(TAG + "::displaymetrics::height::" + height, new Throwable());

        svWidth = width;
        svHeight = (width * 4) / 3;

    }

    public static Bitmap mByteToBitmapConvert(byte[] by) {
        Bitmap bm = null;

        try {

            ByteArrayInputStream in = new ByteArrayInputStream(by);

            bm = BitmapFactory.decodeStream(in);
            if (bm == null) {

            }
        } catch (Exception e) {
            bm = null;
        }

        return bm;
    }

    public static Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            JYLog.D("TAG::bitmap::width::" + bitmap.getWidth(), new Throwable());
            JYLog.D("TAG::bitmap::height::" + bitmap.getHeight(), new Throwable());
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getHeight(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {
            }
        }
        return bitmap;
    }

    public static String saveImageToSdcard(int type, File file, Bitmap bm, Context con) {

        OutputStream outStream = null;
        File fFile = null;

        String path = file.getAbsolutePath();

        fFile = file;
        try {
            outStream = new FileOutputStream(fFile);
            if (type == CameraFrag.TYPE_CINEMAGRAPH_OVERAY) {
                bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            } else {
                bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            }
        } catch (Exception err) {

            path = null;
            fFile = null;
            try {
                outStream.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
            outStream = null;
            return null;
        }

        fFile = null;
        try {
            outStream.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
        outStream = null;

        if (type != CameraFrag.TYPE_CINEMAGRAPH_OVERAY && type != CameraFrag.TYPE_CINEMAGRAPH_FIRST_FRAME) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.parse("file://" + path);
            intent.setData(uri);
            con.sendBroadcast(intent);
        }

        return path;
    }

    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "PlayGreen");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                JYLog.D(TAG + "failed to create directory", new Throwable());
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == CameraFrag.TYPE_CAMERA) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "PG_IMG_" + timeStamp + ".jpg");
        } else if (type == CameraFrag.TYPE_CINEMAGRAPH) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "PG_VID_" + timeStamp + ".mp4");
        } else if (type == CameraFrag.TYPE_CAMERA_FILTER) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "PG_IMG_FILTER_" + timeStamp + ".mp4");
        } else if (type == CameraFrag.TYPE_CINEMAGRAPH_OVERAY) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "PG_IMG_OVERLAY_" + timeStamp + ".png");
        } else if (type == CameraFrag.TYPE_CINEMAGRAPH_FIRST_FRAME) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "PG_IMG_FIRST_FRAME_" + timeStamp + ".jpg");
        } else if (type == CameraFrag.TYPE_LIBRARY) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "PG_IMG_LIB_" + timeStamp + ".jpg");
        } else {
            return null;
        }


        return mediaFile;
    }

    private static CameraCgToolFrag cameraCgToolFrag;
    private static CameraUploadFrag cameraUploadFrag;
    private Fragment currentFrag;

    public NetworkData location;

    private BroadcastReceiver ChangeLocationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if (currentFrag instanceof CameraUploadFrag) {
//                if (((CameraUploadFrag) currentFrag).txtLocation != null
//                        && location == null){
//                    requestSearchLocation(null, CameraActivity.this.netUtil);
//                }
//            }
            if (location == null && currentFrag instanceof CameraUploadFrag
                    && ((CameraUploadFrag) currentFrag).netUtil != null) {
                requestSearchLocation(null, ((CameraUploadFrag) currentFrag).netUtil);
            }
        }
    };

    private void initializeFrag() {
        cameraCgToolFrag = null;
    }

    public void gotoWrapperCameraFrag() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        switchContent(WrapperCameraFrag.newInstance(), R.id.camera_fl_container, false, false);
    }

    public void gotoToolFrag(int type, String absolutePath) {

        if (type == CameraFrag.TYPE_CINEMAGRAPH) {
            gotoCameraCgToolFrag(absolutePath);
        } else {
            gotoCameraFilterFrag(type, absolutePath);
        }
    }

    public void gotoCameraFilterFrag(int type, String absolutePath) {
        initializeFrag();
        currentFrag = CameraFilterFrag.newInstance(type, absolutePath);
        switchContent(currentFrag, R.id.camera_fl_container, true, false);
    }

    public void gotoCameraCgToolFrag(String absolutePath) {
        initializeFrag();
        currentFrag = CameraCgToolFrag.newInstance(absolutePath);
        switchContent(currentFrag, R.id.camera_fl_container, true, false);
    }

    public void gotoCameraUploadFrag(String absolutePath, String overlayAbsolutePath, String firstFrameAbsolutePath) {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        cameraUploadFrag = CameraUploadFrag.newInstance(absolutePath, overlayAbsolutePath, firstFrameAbsolutePath);
        currentFrag = cameraUploadFrag;
        switchContent(currentFrag, R.id.camera_fl_container, true, false);
    }

    public void gotoSearchLocationFrag() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        currentFrag = SearchLocationFrag.newInstance();
        switchContent(SearchLocationFrag.newInstance(), R.id.camera_fl_container, true, false);
    }


    public void requestPermission() {

        String[] permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission, Definitions.ACTIVITY_REQUEST_CODE.PERMISSION_LOCATION);
        }

    }

    public void requestSearchLocation(String location, NetworkRequestUtil netUtil) {

//        https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&name=cruise&key=AddYourOwnKeyHere
        HashMap<String, String> params = new HashMap<String, String>();
        if (GPSService.lastLoc != null) {
            String latitude_logitude = GPSService.lastLoc.getLatitude() + "," + GPSService.lastLoc.getLongitude();
            params.put("location", latitude_logitude);
        }
        if (!TextUtil.isNull(location)) {
            try {
                params.put("name", URLEncoder.encode(location, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        params.put("radius", String.valueOf(100));
        params.put("language", Locale.getDefault().getLanguage());
        params.put("key", getString(R.string.google_place_api_server_id));

        StringRequest myReq = netUtil.requestGet(NetworkConstantUtil.APIKEY.GOOGLE_SEARCH_PLACES, NetworkConstantUtil.URLS.GOOGLE_SEARCH_PLACES, params);
        NetworkController.addToRequestQueue(myReq);
    }

    @Override
    public void onBackPressed() {

        hiddenKeyboard();

        if (currentFrag instanceof CameraCgToolFrag) {
            if (!currentFrag.isDetached()
                    && ((CameraCgToolFrag) currentFrag).llcgTollDefault != null
                    && ((CameraCgToolFrag) currentFrag).llcgTollDefault.getVisibility() == View.GONE) {
                ((CameraCgToolFrag) currentFrag).llBrush.callOnClick();
                ((CameraCgToolFrag) currentFrag).llcgTollDefault.setVisibility(View.VISIBLE);
                return;
            }
        } else if (currentFrag instanceof CameraUploadFrag) {
            if (((CameraUploadFrag) currentFrag).flWrapperTag.getVisibility() == View.VISIBLE) {
                ((CameraUploadFrag) currentFrag).flWrapperTag.setVisibility(View.GONE);
                ((CameraUploadFrag) currentFrag).llWrapperLocationShare.setVisibility(View.VISIBLE);
                return;
            }
        }

        if (currentFrag instanceof CameraUploadFrag) {
            JYLog.D(TAG + "::currentFrag::" + currentFrag.getClass().getName(), new Throwable());
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (currentFrag instanceof SearchLocationFrag) {
            JYLog.D(TAG + "::getFragments::size::" + getSupportFragmentManager().getFragments().size(), new Throwable());
            JYLog.D(TAG + "::currentFrag::" + currentFrag.getClass().getName(), new Throwable());
        }

        super.onBackPressed();

    }

}
