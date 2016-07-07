package tiltcode.movingkey.com.tiltcode_new.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import tiltcode.movingkey.com.tiltcode_new.Model.GridViewItem;
import tiltcode.movingkey.com.tiltcode_new.R;
import tiltcode.movingkey.com.tiltcode_new.library.util.ImageUtil;

/**
 * Created by Gyul on 2016-07-06.
 */
public class GridViewAdapter extends BaseAdapter {

    public static String TAG = GridViewAdapter.class.getSimpleName();

    private ArrayList<GridViewItem> gridViewItemList = new ArrayList<GridViewItem>() ;

    ImageView imgGridviewItem;

    Bitmap gridItem;

    private Target target;

    public GridViewAdapter()    {
    }


    @Override
    public int getCount() {
        Log.d(TAG, "getCount: "+gridViewItemList.size());
        return gridViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return gridViewItemList.get(position);
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
            convertView = inflater.inflate(R.layout.gridview_item, parent, false);
        }

        imgGridviewItem = (ImageView) convertView.findViewById(R.id.img_gridview_item);

        final GridViewItem gridViewItem = gridViewItemList.get(position);

        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d(TAG, "bitmap: "+bitmap.toString());
                int width = (int) context.getResources().getDimension(R.dimen.tiltphoto_gridview_width);
                Bitmap resizeBitmap;
                resizeBitmap = ImageUtil.scaleCenterCrop(bitmap, width, width);
                resizeBitmap = ImageUtil.scaleSquareCrop(resizeBitmap, width, width);
                gridItem = resizeBitmap;
                Log.d(TAG, "bitmap: "+gridItem.toString());
                imgGridviewItem.setImageBitmap(gridItem);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(context)
                .load(gridViewItem.getImgUrl())
                .into(target);

        return convertView;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public void addItem(String imgUrl)   {
        Log.d(TAG, "addItem");
        GridViewItem item = new GridViewItem();

        item.setImgUrl(imgUrl);

        gridViewItemList.add(item);
    }

}
