package tiltcode.movingkey.com.tiltcode_new.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import tiltcode.movingkey.com.tiltcode_new.R;
import tiltcode.movingkey.com.tiltcode_new.adapter.ListViewAdapter;

/**
 * Created by Gyul on 2016-06-29.
 */
public class CouponDialog extends Dialog    {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_coupon_use);
        ButterKnife.bind(this);

        imgBrand = (ImageView)findViewById(R.id.img_brand);
        tvBrandname = (TextView) findViewById(R.id.tv_brandname);
        tvDesc = (TextView)findViewById(R.id.tv_desc);
        tvExp = (TextView)findViewById(R.id.tv_exp);
        imgProduct = (ImageView)findViewById(R.id.img_product);

        Picasso.with(getContext())
                .load(getImgBrandUrl())
                .into(imgBrand);
        tvBrandname.setText(getBrandname());
        tvDesc.setText(getDesc());
        tvExp.setText(getExp());
        Picasso.with(getContext())
                .load(getImgUrl())
                .into(imgProduct);

        sbUse = (SwitchButton)findViewById(R.id.sb_use);

        sbUse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()   {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.removeItem(position);
                adapter.notifyDataSetChanged();
                cancel();
            }
        });
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getImgBrandUrl() {
        return imgBrandUrl;
    }

    public void setImgBrandUrl(String imgBrandUrl) {
        this.imgBrandUrl = imgBrandUrl;
    }
}
