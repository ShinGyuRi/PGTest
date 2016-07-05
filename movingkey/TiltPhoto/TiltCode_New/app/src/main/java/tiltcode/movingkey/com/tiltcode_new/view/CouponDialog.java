package tiltcode.movingkey.com.tiltcode_new.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.kyleduo.switchbutton.SwitchButton;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import tiltcode.movingkey.com.tiltcode_new.Model.ListCouponResult;
import tiltcode.movingkey.com.tiltcode_new.R;
import tiltcode.movingkey.com.tiltcode_new.adapter.ListViewAdapter;
import tiltcode.movingkey.com.tiltcode_new.library.BaseApplication;
import tiltcode.movingkey.com.tiltcode_new.library.util.JsinPreference;
import tiltcode.movingkey.com.tiltcode_new.library.util.LetterSpacingTextView;
import tiltcode.movingkey.com.tiltcode_new.library.util.NetworkUtil;

/**
 * Created by Gyul on 2016-06-29.
 */
public class CouponDialog extends Dialog implements View.OnClickListener    {
    public static String TAG = CouponDialog.class.getSimpleName();

    private String imgUrl, brandname, desc, exp, imgBrandUrl;
    ListViewAdapter adapter;
    int position;

    public CouponDialog(Context context, ListViewAdapter adapter, int position) {
        super(context);
        this.adapter = adapter;
        this.position = position;
    }

    ImageView imgBrand, imgProduct;
    TextView tvBrandname, tvDesc, tvExp;
    SwitchButton sbUse;
    ImageView imgBarcode;
    LinearLayout tvBarcode, layoutClose;

    JsinPreference jsinPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_coupon_use);

        imgBrand = (ImageView)findViewById(R.id.img_brand);
        tvBrandname = (TextView) findViewById(R.id.tv_brandname);
        tvDesc = (TextView)findViewById(R.id.tv_desc);
        tvExp = (TextView)findViewById(R.id.tv_exp);
        imgProduct = (ImageView)findViewById(R.id.img_product);
        imgBarcode = (ImageView)findViewById(R.id.img_barcode);
        tvBarcode = (LinearLayout)findViewById(R.id.tv_barcode);
        layoutClose = (LinearLayout)findViewById(R.id.layout_close);

        jsinPreference = new JsinPreference(BaseApplication.getContext());

//        Log.d(TAG, "getimgBrandUrl: "+getImgBrandUrl());
//
//        Picasso.with(getContext())
//                .load(getImgBrandUrl())
//                .into(imgBrand);
//        tvBrandname.setText(getBrandname());
//        tvDesc.setText(getDesc());
//        tvExp.setText(getExp());
//        Picasso.with(getContext())
//                .load(getImgUrl())
//                .into(imgProduct);

        sbUse = (SwitchButton)findViewById(R.id.sb_use);

        sbUse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()   {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                usedCoupon();
                adapter.removeItem(position);
                adapter.notifyDataSetChanged();
                cancel();
            }
        });

        layoutClose.setOnClickListener(this);
    }

    String username, couponId;

    public void usedCoupon()    {
        username = jsinPreference.getValue("username", "");
        couponId = jsinPreference.getValue("couponId", "");
        NetworkUtil.getHttpSerivce().usedCoupon(couponId, username,
                new Callback<ListCouponResult>() {
                    @Override
                    public void success(ListCouponResult listCouponResult, Response response) {
                        Log.d(TAG, "usedCoupon success");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, "error.getLocalizedMessage(): "+error.getLocalizedMessage());
                    }
                });
    }

    public void setImgBrand(String imgBrandUrl)   {
        Picasso.with(getContext())
                .load(imgBrandUrl)
                .into(imgBrand);
    }

    public void setTvBrandname(String brandname)  {
        tvBrandname.setText(brandname);
    }

    public void setTvDesc(String desc)  {
        tvDesc.setText(desc);
    }

    public void setTvExp(String exp)    {
        tvExp.setText(exp);
    }

    public void setImgProduct(String imgUrl)    {
        Picasso.with(getContext())
                .load(imgUrl)
                .into(imgProduct);
    }

    public void setBarcode(String barcode, int width, int height)    {
        MultiFormatWriter gen = new MultiFormatWriter();
        String data = barcode;
        try {
            final int WIDTH = width;
            final int HEIGHT = height;
            BitMatrix bytemap = gen.encode(data, BarcodeFormat.CODE_128, WIDTH, HEIGHT);
            Bitmap bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
            for (int i = 0 ; i < WIDTH ; ++i)
                for (int j = 0 ; j < HEIGHT ; ++j) {
                    bitmap.setPixel(i, j, bytemap.get(i,j) ? Color.BLACK : Color.WHITE);
                }

            imgBarcode.setImageBitmap(bitmap);
            imgBarcode.invalidate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        LetterSpacingTextView textView = new LetterSpacingTextView(getContext());
        textView.setLetterSpacing(15);
        textView.setText(barcode);
        tvBarcode.addView(textView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())  {
            case R.id.layout_close:
                cancel();
                break;
        }
    }
}
