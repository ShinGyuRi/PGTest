package tiltcode.movingkey.com.tiltcode_new.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import tiltcode.movingkey.com.tiltcode_new.Model.PhotoListViewItem;
import tiltcode.movingkey.com.tiltcode_new.R;
import tiltcode.movingkey.com.tiltcode_new.library.util.ImageUtil;

/**
 * Created by Gyul on 2016-07-07.
 */
public class PhotoListViewAdapter extends BaseAdapter {
    public static String TAG = GridViewAdapter.class.getSimpleName();

    private ArrayList<PhotoListViewItem> photoListViewItemList = new ArrayList<PhotoListViewItem>() ;

    ImageView imgPhotoListviewItem, imgGpsMark, imgReport;
    TextView tvGpsAddress;

    Bitmap photoListItem;

    private Target target;

    public PhotoListViewAdapter()    {
    }

    @Override
    public int getCount() {
        Log.d(TAG, "getCount: "+photoListViewItemList.size());
        return photoListViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return photoListViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView");

        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_photo, parent, false);
        }

        imgPhotoListviewItem = (ImageView) convertView.findViewById(R.id.img_photo);

        final PhotoListViewItem photoListViewItem = photoListViewItemList.get(position);

        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d(TAG, "bitmap: "+bitmap.toString());
                int width = (int) context.getResources().getDimension(R.dimen.tiltphoto_listview_width);
                int height = (int) context.getResources().getDimension(R.dimen.tiltphoto_listview_height);
                Bitmap resizeBitmap;
                resizeBitmap = ImageUtil.scaleCenterCrop(bitmap, height, width);
//                resizeBitmap = ImageUtil.scaleSquareCrop(resizeBitmap, width, height);
                photoListItem = resizeBitmap;
                Log.d(TAG, "bitmap: "+photoListItem.toString());
                imgPhotoListviewItem.setImageBitmap(photoListItem);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(context)
                .load(photoListViewItem.getImgUrl())
                .into(target);
        imgGpsMark.setImageResource(R.drawable.oval_21);
        imgReport.setImageResource(R.drawable.option_bar);

        return convertView;
    }

    public void addItem(String imgUrl)   {
        Log.d(TAG, "addItem");
        PhotoListViewItem item = new PhotoListViewItem();

        item.setImgUrl(imgUrl);

        photoListViewItemList.add(item);
    }
}
