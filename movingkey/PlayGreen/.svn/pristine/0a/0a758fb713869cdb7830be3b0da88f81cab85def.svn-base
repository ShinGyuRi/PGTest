package kr.innisfree.playgreen.ImageFilter.bitmapfilter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import kr.innisfree.playgreen.R;

/**
 * Created by wonderland on 16. 4. 29..
 */
public class PlayBitmapThread2 extends Thread {

    private Context mContext;
    private Bitmap bitmap;
    private Handler handler;

    public PlayBitmapThread2(Context mContext, Bitmap bitmap, Handler handler) {
        this.mContext = mContext;
        this.bitmap = bitmap;
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();

        Bitmap source = bitmap;

        /** alpha blend **/
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        Bitmap filterBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.f_b2d0af, options);

        GPUImageAlphaBlendFilter alphaBlendFilter = new GPUImageAlphaBlendFilter();
        alphaBlendFilter.setBitmap(filterBitmap);
        alphaBlendFilter.setMix(0.25f);

        GPUImage gpuImageWithAlphaBlend = new GPUImage(mContext);
        gpuImageWithAlphaBlend.setImage(source);
        gpuImageWithAlphaBlend.setFilter(alphaBlendFilter);

        Bitmap bitmapWithAlphaBlend = gpuImageWithAlphaBlend.getBitmapWithFilterApplied();

        /** source recycle **/
//        source.recycle();

        /** 1. contrast **/
        GPUImageContrastFilter contrastFilter = new GPUImageContrastFilter();
        contrastFilter.setContrast(1.1f);

        GPUImage gpuImageWithContrast = new GPUImage(mContext);
        gpuImageWithContrast.setImage(bitmapWithAlphaBlend);
        gpuImageWithContrast.setFilter(contrastFilter);

        Bitmap bitmapWithContrast = gpuImageWithContrast.getBitmapWithFilterApplied();

        /** 1. alphablend recycle **/
        bitmapWithAlphaBlend.recycle();

        /** 2. sharpen **/
        GPUImageSharpenFilter sharpenFilter = new GPUImageSharpenFilter();
        sharpenFilter.setSharpness(1.0f);

        GPUImage gpuImageWithSharpen = new GPUImage(mContext);
        gpuImageWithSharpen.setImage(bitmapWithContrast);
        gpuImageWithSharpen.setFilter(sharpenFilter);

        Bitmap bitmapWithSharpen = gpuImageWithSharpen.getBitmapWithFilterApplied();

        /** 2. contrast recycle **/
        bitmapWithContrast.recycle();

        /** 3. saturation **/
        GPUImageSaturationFilter saturationFilter = new GPUImageSaturationFilter();
        saturationFilter.setSaturation(1.3f);

        GPUImage playGpuImage = new GPUImage(mContext);
        playGpuImage.setImage(bitmapWithSharpen);
        playGpuImage.setFilter(saturationFilter);

        Bitmap resultBitmap = playGpuImage.getBitmapWithFilterApplied();

        /** 3. sharpen recycle **/
        bitmapWithSharpen.recycle();

        Bundle bundle = new Bundle();
        bundle.putParcelable("bitmap", resultBitmap);
        Message message = new Message();
        message.setData(bundle);
        handler.sendMessage(message);

    }

}
