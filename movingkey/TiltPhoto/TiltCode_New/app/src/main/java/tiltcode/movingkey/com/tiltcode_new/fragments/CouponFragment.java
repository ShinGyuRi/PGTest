package tiltcode.movingkey.com.tiltcode_new.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import tiltcode.movingkey.com.tiltcode_new.Model.Coupon;
import tiltcode.movingkey.com.tiltcode_new.Model.ListCouponResult;
import tiltcode.movingkey.com.tiltcode_new.Model.ListViewItem;
import tiltcode.movingkey.com.tiltcode_new.R;
import tiltcode.movingkey.com.tiltcode_new.adapter.ListViewAdapter;
import tiltcode.movingkey.com.tiltcode_new.library.BaseApplication;
import tiltcode.movingkey.com.tiltcode_new.library.ParentFragment;
import tiltcode.movingkey.com.tiltcode_new.library.util.JsinPreference;
import tiltcode.movingkey.com.tiltcode_new.library.util.NetworkUtil;
import tiltcode.movingkey.com.tiltcode_new.library.util.Util;
import tiltcode.movingkey.com.tiltcode_new.view.CouponDialog;

/**
 * Created by Gyul on 2016-06-26.
 */
public class CouponFragment extends ParentFragment{

    public static String TAG = CouponFragment.class.getSimpleName();

    ListView listView;
    ListViewAdapter adapter;

    String imgUrl, option, brandname, desc, exp, imgBrand, couponId, barcode;

    String username;
    int page;

    JsinPreference jsinPreference;

    public List<Coupon> couponList;

    ListViewItem item;
    CouponDialog dialog;

    @SuppressLint("ValidFragment")
    public CouponFragment() {
    }

    public static CouponFragment newInstance() {
        CouponFragment frag = new CouponFragment();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coupon, container, false);

        adapter = new ListViewAdapter();

        listView = (ListView)view.findViewById(R.id.listview1);

        getCouponList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()   {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get item
                item = (ListViewItem) parent.getItemAtPosition(position) ;

//                String titleStr = item.getTitle() ;
//                String descStr = item.getDesc() ;
//                Drawable iconDrawable = item.getIcon() ;

                Log.d(TAG, "coupon id: "+item.getCouponId());
                jsinPreference.put("couponId", item.getCouponId());

                dialog = new CouponDialog(getContext(), adapter, position);

                dialog.setOnShowListener(new DialogInterface.OnShowListener()   {
                    @Override
                    public void onShow(DialogInterface dia) {
//                        dialog.setImgBrandUrl(item.getImgBrand());
//                        dialog.setImgUrl(item.getImgProduct());
//                        dialog.setBrandname(item.getTvBrandname());
//                        dialog.setDesc(item.getTvProduct());
//                        dialog.setExp(item.getTvExpDate());

                        dialog.setImgBrand(item.getImgBrand());
                        dialog.setImgProduct(item.getImgProduct());
                        dialog.setTvBrandname(item.getTvBrandname());
                        dialog.setTvDesc(item.getTvProduct());
                        dialog.setTvExp(item.getTvExpDate());
                        dialog.setBarcode(item.getBarcode(), (int) getResources().getDimension(R.dimen.barcode_width), (int) getResources().getDimension(R.dimen.barcode_height));
                    }
                });

                dialog.show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getCouponList() {
        Log.d("TAG", "getCouponList");
        jsinPreference = new JsinPreference(BaseApplication.getContext());
        username = jsinPreference.getValue("username", "");
        page = 0;

        NetworkUtil.getHttpSerivce().listCoupon(username, String.valueOf(page),
                new Callback<ListCouponResult>() {
                    @Override
                    public void success(ListCouponResult listCouponResult, Response response) {
                        if (listCouponResult.coupon != null)    {
                            couponList = listCouponResult.coupon;

                            if (couponList != null) {
                                for(page=0; page<couponList.size(); page++)  {
                                    Log.d(TAG, "page: "+String.valueOf(page));
                                    imgUrl = couponList.get(page).getImage();
                                    option = couponList.get(page).getOption();
                                    brandname = couponList.get(page).getTitle();
                                    desc = couponList.get(page).getDescription();
                                    exp = Util.unixTimeToDate(Long.parseLong(couponList.get(page).getExp()));
                                    imgBrand = couponList.get(page).getBrandimage();
                                    couponId = couponList.get(page).get_id();
                                    barcode = couponList.get(page).getCode();
                                    Log.d(TAG, "exp: "+couponList.get(page).getExp());

                                    listView.setAdapter(adapter);
                                    adapter.addItem(imgUrl, option, brandname, desc, exp, imgBrand, couponId, barcode);
                                }
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
    }
}
