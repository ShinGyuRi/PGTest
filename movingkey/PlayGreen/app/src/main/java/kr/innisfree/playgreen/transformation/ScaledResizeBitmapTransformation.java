package kr.innisfree.playgreen.transformation;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

/**
 * Created by wonderland on 16. 5. 12..
 */
public class ScaledResizeBitmapTransformation implements Transformation<Bitmap> {

    private Context mContext;
    private BitmapPool mBitmapPool;
    private Bitmap convertBitmap;

    public ScaledResizeBitmapTransformation(Context context) {
        this(context, Glide.get(context).getBitmapPool());
    }

    public ScaledResizeBitmapTransformation(Context context, BitmapPool pool) {
        mContext = context.getApplicationContext();
        pool.clearMemory();
        mBitmapPool = pool;
    }

    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {

        Bitmap source = resource.get();

        Bitmap originBitmap = Bitmap.createScaledBitmap(source, source.getWidth() / 7, source.getHeight() / 7, true);

        return BitmapResource.obtain(originBitmap, mBitmapPool);
    }

    @Override
    public String getId() {
        return "ScaledResizeBitmapTransformation";
    }

}