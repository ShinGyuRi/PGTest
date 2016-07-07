package com.moyusoft.util;


import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

public class BitmapHeightResizeForGlide implements Transformation<Bitmap> {

    private BitmapPool mBitmapPool;
    private int height;

    public BitmapHeightResizeForGlide(BitmapPool pool, int height) {
        this.mBitmapPool = pool;
        this.height = height;
    }

    public BitmapHeightResizeForGlide(Context context, int height) {
        this(Glide.get(context).getBitmapPool(), height);
    }

//	public Bitmap transform(Bitmap arg0) {
//		Bitmap loadedImage = FileUtil.resizeBitmapWithHeight(arg0, height);
//		if (arg0 != loadedImage) {
//			arg0.recycle();
//		}
//		return loadedImage;
//	}

    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap source = resource.get();

        int width = source.getWidth();
        int height = source.getHeight();

        float resizeFactor = this.height * 1f / height;

        int targetWidth, targetHeight;
        targetWidth = (int) (width * resizeFactor);
        targetHeight = (int) (height * resizeFactor);

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, true);


//		if (arg0 != loadedImage) {
//			arg0.recycle();
//		}

        return BitmapResource.obtain(resizedBitmap, mBitmapPool);
    }


    @Override public String getId() {
        return "BitmapHeightResizeForGlide()";
    }
}
