package tiltcode.movingkey.com.tiltcode_new.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import tiltcode.movingkey.com.tiltcode_new.Model.ListCouponResult;
import tiltcode.movingkey.com.tiltcode_new.Model.ListViewItem;
import tiltcode.movingkey.com.tiltcode_new.R;
import tiltcode.movingkey.com.tiltcode_new.library.BaseApplication;
import tiltcode.movingkey.com.tiltcode_new.library.util.JsinPreference;
import tiltcode.movingkey.com.tiltcode_new.library.util.NetworkUtil;
import tiltcode.movingkey.com.tiltcode_new.library.util.Util;

/**
 * Created by Gyul on 2016-06-30.
 */
public class ListViewAdapter extends BaseAdapter {
    public static String TAG = ListViewAdapter.class.getSimpleName();

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;

    private RelativeLayout listView1, listView2;
    private ImageView imgOption, imgClose;
    private LinearLayout layoutClose;

    JsinPreference jsinPreference;

    public ListViewAdapter()    {

    }

    @Override
    public int getCount() {
        Log.d(TAG, "getCount: "+listViewItemList.size());
        return listViewItemList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView");

        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView imgProduct = (ImageView) convertView.findViewById(R.id.img_product) ;
        TextView tvSale = (TextView) convertView.findViewById(R.id.tv_sale) ;
        TextView tvBrandname = (TextView) convertView.findViewById(R.id.tv_brandname) ;
        TextView tvProductname = (TextView) convertView.findViewById(R.id.tv_productname);
        TextView tvDday = (TextView) convertView.findViewById(R.id.tv_dday);
        TextView tvExpDate = (TextView) convertView.findViewById(R.id.tv_expdate);
        listView1 = (RelativeLayout)convertView.findViewById(R.id.listview1);
        listView2 = (RelativeLayout)convertView.findViewById(R.id.listview2);
        imgOption = (ImageView)convertView.findViewById(R.id.img_circle_option);
        imgClose = (ImageView)convertView.findViewById(R.id.img_close);
        layoutClose = (LinearLayout)convertView.findViewById(R.id.layout_close);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        Picasso.with(context)
                .load(listViewItem.getImgProduct())
                .into(imgProduct);
        tvSale.setText(listViewItem.getTvSale());
        tvBrandname.setText(listViewItem.getTvBrandname());
        tvProductname.setText(listViewItem.getTvProduct());
        tvDday.setText(listViewItem.getTvDday());
        tvExpDate.setText(listViewItem.getTvExpDate());
        tvDday.setText(listViewItem.getTvDday());

        listView1.setBackgroundResource(R.color.colorWhite);
        listView2.setBackgroundResource(R.color.colorListView);
        imgOption.setImageResource(R.drawable.oval_47);
        imgClose.setImageResource(R.drawable.material_icons_blackclose);
        layoutClose.setOnClickListener(new View.OnClickListener()   {

            @Override
            public void onClick(View v) {
                jsinPreference = new JsinPreference(BaseApplication.getContext());
                jsinPreference.put("couponId", listViewItem.getCouponId());
                usedCoupon();
                removeItem(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String imgUrl, String option, String brandname, String desc, String exp, String imgBrand, String couponId, String barcode) {
        Log.d(TAG, "addItem");
        ListViewItem item = new ListViewItem();

        item.setImgProduct(imgUrl);
        item.setTvSale(option);
        item.setTvBrandname(brandname);
        item.setTvProduct(desc);
        item.setTvExpDate("Valid through "+exp);
        item.setTvDday("D-"+String.valueOf(Util.dDay(Integer.valueOf(exp.substring(6, 10)), Integer.valueOf(exp.substring(3, 5)), Integer.valueOf(exp.substring(0, 2)))));
        item.setImgBrand(imgBrand);
        item.setCouponId(couponId);
        item.setBarcode(barcode);

        listViewItemList.add(item);
    }

    public void removeItem(int position)    {
        listViewItemList.remove(position);
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

}
