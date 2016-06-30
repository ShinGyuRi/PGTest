package tiltcode.movingkey.com.tiltcode_new.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import tiltcode.movingkey.com.tiltcode_new.R;

/**
 * Created by Gyul on 2016-06-29.
 */
public class CouponDialog extends Dialog    {

    String imgUrl;

    public CouponDialog(Context context) {
        super(context);
    }

    public CouponDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CouponDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Nullable @BindView(R.id.img_coupon)
    ImageView imgCoupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_coupon);
        ButterKnife.bind(this);

        imgCoupon = (ImageView)findViewById(R.id.img_coupon);
        setImage(imgUrl);
//        setContent(mContent);
//        setClickListener(mLeftClickListener , mRightClickListener);
    }

    public void setImage(String imgUrl) {
        Picasso.with(getContext())
                .load(imgUrl)
                .into(imgCoupon);
    }


}
