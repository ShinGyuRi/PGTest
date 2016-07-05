package tiltcode.movingkey.com.tiltcode_new.fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import tiltcode.movingkey.com.tiltcode_new.Model.AccelData;
import tiltcode.movingkey.com.tiltcode_new.Model.CouponPhotoResult;
import tiltcode.movingkey.com.tiltcode_new.R;
import tiltcode.movingkey.com.tiltcode_new.library.BaseApplication;
import tiltcode.movingkey.com.tiltcode_new.library.ParentActivity;
import tiltcode.movingkey.com.tiltcode_new.library.ParentFragment;
import tiltcode.movingkey.com.tiltcode_new.library.swipe.Swipe;
import tiltcode.movingkey.com.tiltcode_new.library.swipe.SwipeListener;
import tiltcode.movingkey.com.tiltcode_new.library.util.JsinPreference;
import tiltcode.movingkey.com.tiltcode_new.library.util.NetworkUtil;
import tiltcode.movingkey.com.tiltcode_new.library.util.Util;
import tiltcode.movingkey.com.tiltcode_new.view.ScaleView;

/**
 * Created by Gyul on 2016-06-26.
 */
public class HomeFragment extends ParentFragment implements View.OnClickListener, SensorEventListener, View.OnTouchListener {

    public static String TAG = HomeFragment.class.getSimpleName();

    String username, loginType;
    String tilt;
    String[] degree;
    String lat, lng;

    private static AccelData prev = null, now = null;
    private float TOLERANCE_VALUE = 1.5f;
    private float SEARCH_VALUE = 2.5f;
    private float[] mMagnetic;
    private float[] mGravity = null;
    private static float[][] Arr_Accel = {
            {0f, 9.81f}, // 1 exclude {0f, 9.8f, 0f}
            {-4.9f, 4.9f},
            {-9.81f, 0f},
            {-4.9f, -4.9f},
            {0f, -9.81f}, // 5
            {4.9f, -4.9f},
            {9.81f, 0f},
            {4.9f, 4.9f},
//            {9999f, 9999f}, // 9 {0f, 6.8f, 6.8f}
//            {9999f, 9999f}, // 10 exclude {0f, 0f, 9.8f}
//            {0f, -6.8f},
//            //{0f, -9.8f, 0f}, // 12 Overlap with 5
//            {0f, -6.8f}, // Fixed 12 , prev 13
//            {0f, 0f},
//            {0f, 6.8f}
    };

    private boolean serviceRunning = false;

    private SensorManager mSensorManager;
    private Sensor accelerometerSensor;
    private Sensor magnetometer;
    private ActivityManager mActivityManager;

    ScaleView scaleView;
    ImageView imgTilt1, imgTilt2, imgTilt3;
    ImageView dialogImg, imgMark;

    Context context;

//    private LinearLayout fragmentHome;
    private FrameLayout layoutTilt;
    private TextView tvAlert;

    private ArrayList<String> tiltList = new ArrayList<String>();

    private Swipe swipe;

    JsinPreference jsinPreference;

    public boolean touchFlag = false;

    public Fragment couponFragment;

    public static HomeFragment newInstance() {
        HomeFragment frag = new HomeFragment();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        layoutTilt = (FrameLayout)view.findViewById(R.id.layout_tilt);
        tvAlert = (TextView)view.findViewById(R.id.tv_tiltalert);
        imgTilt1 = (ImageView)view.findViewById(R.id.img_tilt1);
        imgTilt2 = (ImageView)view.findViewById(R.id.img_tilt2);
        imgTilt3 = (ImageView)view.findViewById(R.id.img_tilt3);
//        fragmentHome = (LinearLayout)view.findViewById(R.id.fragment_home);
        imgMark = (ImageView) view.findViewById(R.id.img_mark);

        layoutTilt.setOnClickListener(this);
        tvAlert.setText(R.string.str_home_alert);
        layoutTilt.setOnTouchListener(this);

        jsinPreference = new JsinPreference(BaseApplication.getContext());

        swipe = new Swipe();
        swipe.addListener(new SwipeListener() {

            @Override
            public void onSwipingLeft(MotionEvent event) {

            }

            @Override
            public void onSwipedLeft(MotionEvent event) {

            }

            @Override
            public void onSwipingRight(MotionEvent event) {

            }

            @Override
            public void onSwipedRight(MotionEvent event) {
                Log.d(TAG, "onSwipedRight()");
                if (tiltList.size() < 3)
                    getResult(tiltList);
                touchFlag = true;
                tiltCount=0;
                tiltList.clear();
                imgTilt1.setImageResource(R.drawable.rectangle_76);
                imgTilt2.setImageResource(R.drawable.rectangle_76);
                imgTilt3.setImageResource(R.drawable.rectangle_76);
                buffer.setLength(0);

            }

            @Override
            public void onSwipingUp(MotionEvent event) {

            }

            @Override
            public void onSwipedUp(MotionEvent event) {

            }

            @Override
            public void onSwipingDown(MotionEvent event) {

            }

            @Override
            public void onSwipedDown(MotionEvent event) {

            }
        });

        mActivityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        mSensorManager = (SensorManager)getActivity().getSystemService(getActivity().SENSOR_SERVICE);
        accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mSensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);

        serviceRunning = true;

        if(context == null) context = getActivity();
        scaleView = new ScaleView(context);
        layoutTilt.addView(scaleView);

        return view;
    }

    @Override
    public void onResume() {

        accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);

        super.onResume();
    }

    @Override
    public void onDestroy() {
        serviceRunning = false;
        mSensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()) {

            case Sensor.TYPE_ACCELEROMETER:
                mGravity = event.values.clone();
                float[] values = event.values;
                AccelData v = new AccelData(
                        Math.round(values[0] * 100d) / 100d,
                        Math.round(values[1] * 100d) / 100d);

                if(prev == null)
                    prev = now = v;
                else
                {
                    if( !((prev.x - TOLERANCE_VALUE < v.x && prev.x + TOLERANCE_VALUE > v.x)
                            && (prev.y - TOLERANCE_VALUE < v.y && prev.y + TOLERANCE_VALUE > v.y)))
                    {

                        prev = v;
                    }
                    now = v;
                }
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagnetic = event.values.clone();
                break;
            default:
                return;
        }
        if(mGravity != null && mMagnetic != null) {
            scaleView.tiltAngle = getDirection();
//            Log.d("s", "tilt X : " + getDirection());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private float getDirection() {
        float[] temp = new float[9];
        float[] R = new float[9];
        //Load rotation matrix into R
        SensorManager.getRotationMatrix(temp, null,
                mGravity, mMagnetic);

        //Remap to camera1's point-of-view
        SensorManager.remapCoordinateSystem(temp,
                SensorManager.AXIS_X,
                SensorManager.AXIS_Z, R);

        //Return the orientation values
        float[] values = new float[3];
        SensorManager.getOrientation(R, values);

        //Convert to degrees
        for (int i=0; i < values.length; i++) {
            Double degrees = (values[i] * 180) / Math.PI;
            values[i] = degrees.floatValue();
        }

        return values[2]; // 0 은 x 각도, 1은 y각도, 2는 z각도
    }

    String imgUrl, option, brandname, desc, exp;
    String _id = null;

    public void getResult(ArrayList<String> tiltList)    {

        username = jsinPreference.getValue("username", "");
        loginType = jsinPreference.getValue("loginType", "");

        lat = jsinPreference.getValue("lat", "");
        lng = jsinPreference.getValue("lng", "");

        String joined = "";
        if (buffer.length() != 0)
            joined = buffer.deleteCharAt(0).toString();
        degree = joined.split(",\\s+");

        Log.d(TAG,"degree : "+ Arrays.toString(degree)+" lat : "+lat+" lng : "+lng+" username : "+username + " loginType: "+loginType);

        NetworkUtil.getHttpSerivce().getCouponOrPhoto(username, loginType, Arrays.toString(degree), String.valueOf(lat), String.valueOf(lng), "FREE",
                new Callback<CouponPhotoResult>()   {

                    @Override
                    public void success(CouponPhotoResult couponPhotoResult, Response response) throws NullPointerException {
                        Log.d(TAG, "getResult to string: " + couponPhotoResult.toString());

                       if (couponPhotoResult.result != null) {
                           layoutTilt.removeView(scaleView);
                           imgMark.setImageResource(R.drawable.detected_mark);
                           tvAlert.setText(R.string.str_home_detected);


                           JSONObject jsonObject = null;
                           try {
                               jsonObject = new JSONObject((String) couponPhotoResult.getCoupon());
                               Log.d(TAG, "JSONObject.tostring: " + jsonObject.toString());
                               imgUrl = jsonObject.getString("image");
                               option = jsonObject.getString("option");
                               brandname = jsonObject.getString("brandname");
                               desc = jsonObject.getString("description");
                               exp = Util.unixTimeToDate(Long.parseLong(jsonObject.getString("exp")));
                               Log.d(TAG, "imgUrl: " + imgUrl.replace("\\", ""));
                               _id = jsonObject.getString("_id");
                           } catch (JSONException e) {
                               e.printStackTrace();
                           } catch (NullPointerException e) {
                               e.printStackTrace();
                           }

                           AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                           LayoutInflater inflater = getActivity().getLayoutInflater();
                           View dialogView = inflater.inflate(R.layout.dialog_coupon, null);
                           dialogImg = (ImageView) dialogView.findViewById(R.id.img_dig_coupon);
                           Picasso.with(getContext())
                                   .load(imgUrl.replace("\\", ""))
                                   .into(dialogImg);
                           builder.setView(dialogView)
                                   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
//                                           couponFragment = CouponFragment.newInstance(imgUrl.replace("\\", ""), option, brandname, desc, exp);


                                           Log.d(TAG, "username: "+username + " coupon_id: "+_id);
                                           ownCouponOrPhoto(username, _id, null);
//                                           ((CouponFragment)getTargetFragment()).getCouponList();
                                           couponFragment = CouponFragment.newInstance();
                                           ((ParentActivity) getActivity()).switchContent(couponFragment, R.id.container, true, false);
                                           touchFlag = false;

                                       }
                                   })
                                   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                           layoutTilt.removeAllViews();
                                           layoutTilt.addView(scaleView);
                                           tvAlert.setText(R.string.str_home_alert);
                                           touchFlag = false;

                                       }
                                   });

                           AlertDialog alertDialog = builder.create();
                           alertDialog.show();
                           alertDialog.getWindow().setLayout((int) getResources().getDimension(R.dimen.dialog_width), (int) getResources().getDimension(R.dimen.dialog_height));

                       }
                        if(couponPhotoResult.message != null)  {
                           AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                           builder.setMessage("쿠폰이 없습니다")
                           .setNegativeButton("확인", new DialogInterface.OnClickListener()  {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   touchFlag = false;

                               }
                           });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                       }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "error.getLocalizedMessage() : " + error.getLocalizedMessage());
                    }
                });

    }

    public void ownCouponOrPhoto(String username, String coupon_id, String photo_id)  {
        NetworkUtil.getHttpSerivce().ownCouponOrPhoto(username, coupon_id, photo_id,
                new Callback<CouponPhotoResult>()    {

                    @Override
                    public void success(CouponPhotoResult couponPhotoResult, Response response) {
                        Log.d(TAG, "ownCouponOrPhoto: "+couponPhotoResult.result);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "error.getLocalizedMessage()" + error.getLocalizedMessage());
                    }
                });
    }

    private int tiltCount=0;
    private ImageView imageView;

    @Override
    public void onClick(View v) {
        switch (v.getId())  {
            case R.id.layout_tilt:
                setTilt();

                break;
        }

    }

    StringBuilder buffer = new StringBuilder();

    private void setTilt() {

        for (int i = 0; i < Arr_Accel.length; i++) {
                            try {    // jhnunu 150929
                                if ((Arr_Accel[i][0] - SEARCH_VALUE < now.x && Arr_Accel[i][0] + SEARCH_VALUE > now.x) &&
                                        (Arr_Accel[i][1] - SEARCH_VALUE < now.y && Arr_Accel[i][1] + SEARCH_VALUE > now.y)) {
                                    Log.d("sensor", "Tilt : " + String.valueOf(i + 1));



                                        switch (tiltCount)  {
                                            case 0:
                                                imageView = imgTilt1;
                                                break;
                                            case 1:
                                                imageView = imgTilt2;
                                                break;
                                            case 2:
                                                imageView = imgTilt3;
                                                break;
                                        }

                                        tilt = String.valueOf(i+1);
                                        switch (tilt)   {
                                            case "1":
                                                imageView.setImageResource(R.drawable.tilt_mark_small_01);
                                                break;
                                            case "2":
                                                imageView.setImageResource(R.drawable.tilt_mark_small_02);
                                                break;
                                            case "3":
                                                imageView.setImageResource(R.drawable.tilt_mark_small_03);
                                                break;
                                            case "4":
                                                imageView.setImageResource(R.drawable.tilt_mark_small_04);
                                                break;
                                            case "5":
                                                imageView.setImageResource(R.drawable.tilt_mark_small_05);
                                                break;
                                            case "6":
                                                imageView.setImageResource(R.drawable.tilt_mark_small_06);
                                                break;
                                            case "7":
                                                imageView.setImageResource(R.drawable.tilt_mark_small_07);
                                                break;
                                            case "8":
                                                imageView.setImageResource(R.drawable.tilt_mark_small_08);
                                                break;
                                        }
                                        tiltCount++;
                                    if (tiltList.size()<3)  {
                                        tiltList.add(tilt);
                                        buffer.append(",").append("\"").append(tilt).append("\"");
                                    }

//                                        getResult(tilt);

                                }
                            } catch (java.lang.NullPointerException ex) {

                            }
                        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        swipe.dispatchTouchEvent(event);
        Log.d(TAG, "touchFlag: "+touchFlag);
        return touchFlag;
    }
}
