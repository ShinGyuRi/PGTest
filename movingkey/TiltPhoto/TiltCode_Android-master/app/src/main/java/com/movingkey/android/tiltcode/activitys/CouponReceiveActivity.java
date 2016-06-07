package com.movingkey.android.tiltcode.activitys;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.movingkey.android.tiltcode.Model.Coupon;
import com.movingkey.android.tiltcode.Model.CouponResult;
import com.movingkey.android.tiltcode.R;
import com.movingkey.android.tiltcode.Util.Util;
import com.movingkey.android.tiltcode.adapter.CouponListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Gyul on 2016-06-02.
 */
public class CouponReceiveActivity extends AppCompatActivity{

    //로그에 쓰일 tag
    public static final String TAG = CouponReceiveActivity.class.getSimpleName();

    int layoutid;
    Context context;

    public static PullToRefreshListView mListView;
    View mListTouchInterceptor;
    UnfoldableView mUnfoldableView;
    FrameLayout mDetailsLayout;
    View detailView;
    ScrollView detailScroll;

    TextView tv_couponlist_nocoupon;

    List<Coupon> couponList = new ArrayList<>();
    CouponListAdapter couponAdapter;

    View v = null;

    public static View.OnTouchListener interceptTouch = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }
    };

    public CouponReceiveActivity() {
        super();
        this.layoutid = R.layout.activity_coupon;
        this.context = MainActivity.context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (v == null) {
            this.context = MainActivity.context;
            v = inflater.inflate(layoutid, null);

            mListView = (PullToRefreshListView) v.findViewById(R.id.list_view);
            mListTouchInterceptor = v.findViewById(R.id.touch_interceptor_view);
            mUnfoldableView = (UnfoldableView) v.findViewById(R.id.unfoldable_view);
            mDetailsLayout = (FrameLayout)v.findViewById(R.id.details_layout);

            detailView = inflater.inflate(R.layout.item_foldable_detail,null);
            detailScroll = (ScrollView)detailView.findViewById(R.id.sv_foldable_detail);
            mDetailsLayout.addView(detailView);

            tv_couponlist_nocoupon = ((TextView)v.findViewById(R.id.tv_couponlist_nocoupon));

            init();

        }
        setContentView(v);

    }

//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        if (v == null) {
//            v = inflater.inflate(layoutid, null);
//
//            mListView = (PullToRefreshListView) v.findViewById(R.id.list_view);
//            mListTouchInterceptor = v.findViewById(R.id.touch_interceptor_view);
//            mUnfoldableView = (UnfoldableView) v.findViewById(R.id.unfoldable_view);
//            mDetailsLayout = (FrameLayout)v.findViewById(R.id.details_layout);
//
//            detailView = inflater.inflate(R.layout.item_foldable_detail,null);
//            detailScroll = (ScrollView)detailView.findViewById(R.id.sv_foldable_detail);
//            mDetailsLayout.addView(detailView);
//
//            tv_couponlist_nocoupon = ((TextView)v.findViewById(R.id.tv_couponlist_nocoupon));
//
//            init();
//
//        }
//        return v;
//    }

    Handler refreshComplete = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mListView.onRefreshComplete();

        }
    };


    void init() {

        Collections.reverse(couponList);

        couponAdapter = new CouponListAdapter(couponList,context,mUnfoldableView,mDetailsLayout);
        mListView.setAdapter(couponAdapter);

        ((PullToRefreshListView)mListView).setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {

                Util.getEndPoint().setPort("40002");
                Util.getHttpSerivce().couponGet(Util.getAccessToken().getToken()
                        , new Callback<CouponResult>() {
                            @Override
                            public void success(CouponResult couponResult, Response response) {
                                Log.d(TAG, "couponget success / code : " + couponResult.code);
                                refreshComplete.sendEmptyMessageDelayed(0, 1000);

                                if (couponResult.code.equals("1")) { //성공

                                    if (couponResult.coupon != null) {

                                        Collections.reverse(couponResult.coupon);
                                        Log.d(TAG, "count : " + couponResult.coupon.size());

                                        couponAdapter.couponList = couponResult.coupon;
                                        couponAdapter.notifyDataSetChanged();

                                        if (couponResult.coupon.size() == 0) {
                                            tv_couponlist_nocoupon.setVisibility(View.VISIBLE);
                                        } else {
                                            tv_couponlist_nocoupon.setVisibility(View.GONE);
                                        }

                                    } else {
                                        tv_couponlist_nocoupon.setVisibility(View.GONE);
                                    }

                                } else if (couponResult.code.equals("-1")) { //누락된게있음
                                    Toast.makeText(context, getResources().getText(R.string.message_not_enough_data), Toast.LENGTH_LONG).show();
                                } else if (couponResult.code.equals("-2")) { //유효하지않은 토큰
                                    Toast.makeText(context, getResources().getText(R.string.message_not_allow_permission), Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e(TAG, "login failure : " + error.getMessage());
                                Toast.makeText(context, getResources().getText(R.string.message_network_error), Toast.LENGTH_LONG).show();

                                refreshComplete.sendEmptyMessageDelayed(0, 1000);

                            }
                        });
            }
        });

        mListTouchInterceptor.setClickable(false);

        mDetailsLayout.setVisibility(View.INVISIBLE);


        Util.getEndPoint().setPort("40002");
        Util.getHttpSerivce().couponGet(Util.getAccessToken().getToken()
                , new Callback<CouponResult>() {
                    @Override
                    public void success(CouponResult couponResult, Response response) {
                        Log.d(TAG,"couponget success / code : "+couponResult.code);
                        if (couponResult.code.equals("1")) { //성공

                            if(couponResult.coupon!=null) {
                                Log.d(TAG, "count : " + couponResult.coupon.size());

                                Collections.reverse(couponResult.coupon);

                                couponAdapter.couponList = couponResult.coupon;
                                couponAdapter.notifyDataSetChanged();

                                if(couponResult.coupon.size()==0){
                                    tv_couponlist_nocoupon.setVisibility(View.VISIBLE);
                                }
                                else{
                                    tv_couponlist_nocoupon.setVisibility(View.GONE);
                                }

                            }
                            else{
                                tv_couponlist_nocoupon.setVisibility(View.GONE);
                            }

                        } else if (couponResult.code.equals("-1")) { //누락된게있음
                            Toast.makeText(context,getResources().getText(R.string.message_not_enough_data),Toast.LENGTH_LONG).show();
                        } else if (couponResult.code.equals("-2")) { //유효하지않은 토큰
                            Toast.makeText(context,getResources().getText(R.string.message_not_allow_permission),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG,"login failure : "+error.getMessage());
                        Toast.makeText(context,getResources().getText(R.string.message_network_error),Toast.LENGTH_LONG).show();

                    }
                });


        detailScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                interceptTouch.onTouch(view,motionEvent);

                touchList.add((int)motionEvent.getY());
                if(touchList.size()>5){
                    touchList.remove(0);
                }
                boolean sw = false;
                if(touchList.size()==5)
                    for(int i=1;i<5;i++){
                        if(touchList.get(i)>touchList.get(0)){
                            sw = true;
                            break;
                        }
                    }
                else{
                    sw=true;
                }
                Log.d(TAG,"y : "+detailScroll.getScrollY() + "touchY : "+motionEvent.getY()+" sw : "+sw);


                if(detailScroll.getScrollY()<10 && sw){
                    if(!lastPressed){
                        motionEvent.setAction(MotionEvent.ACTION_DOWN);
                        Log.d(TAG,"send press down");
                    }
                    interceptTouch.onTouch(view, motionEvent);

                    lastPressed = sw;
                    return true;
                }
                lastPressed = sw;
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    lastPressed = false;
                    touchList.clear();
                }

//                Log.d(TAG,"y : "+detailScroll.getScrollY());
//                Log.d(TAG,"y : "+detailScroll.getScrollY());
                return false;
            }
        });
    }

    boolean lastPressed;
    ArrayList<Integer> touchList = new ArrayList<>();
}
