package kr.innisfree.playgreen.ImageFilter.bitmapfilter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.moyusoft.util.ExifUtil;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import kr.innisfree.playgreen.R;

/**
 * Created by wonderland on 16. 4. 29..
 */
public class PlayBitmapThread extends Thread {

    private Context mContext;
    private Bitmap bitmap;
    private Handler handler;

    public PlayBitmapThread(Context mContext, Bitmap bitmap, Handler handler) {
        this.mContext = mContext;
        this.bitmap = bitmap;
        this.handler = handler;
    }

    public PlayBitmapThread(Context mContext, String absolutePath, int width, Handler handler) {
        this.mContext = mContext;
        this.handler = handler;
        this.bitmap = BitmapFactory.decodeFile(absolutePath);
        if (this.bitmap.getWidth() > width)
            this.bitmap = Bitmap.createScaledBitmap(this.bitmap, this.bitmap.getWidth() / 2, this.bitmap.getHeight() / 2, false);
        this.bitmap = ExifUtil.rotateBitmap(absolutePath, this.bitmap);
    }
    @Override
    public void run() {
        super.run();

//        Bitmap source = bitmap;
//
//        /** alpha blend **/
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 5;
//        Bitmap filterBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.filter_play, options);
//
//        GPUImageAlphaBlendFilter alphaBlendFilter = new GPUImageAlphaBlendFilter();
//        alphaBlendFilter.setBitmap(filterBitmap);
//        alphaBlendFilter.setMix(0.19f);
//
//        GPUImage gpuImageWithAlphaBlend = new GPUImage(mContext);
//        gpuImageWithAlphaBlend.setImage(source);
//        gpuImageWithAlphaBlend.setFilter(alphaBlendFilter);
//
//        Bitmap bitmapWithAlphaBlend = gpuImageWithAlphaBlend.getBitmapWithFilterApplied();
//
//        /** source recycle **/
////        source.recycle();
//
//        /** contrast **/
//        GPUImageContrastFilter contrastFilter = new GPUImageContrastFilter();
//        contrastFilter.setContrast(1.3f);
//
//        GPUImage gpuImageWithContrast = new GPUImage(mContext);
//        gpuImageWithContrast.setImage(bitmapWithAlphaBlend);
//        gpuImageWithContrast.setFilter(contrastFilter);
//
//        Bitmap bitmapWithContrast = gpuImageWithContrast.getBitmapWithFilterApplied();
//
//        /** alphablend recycle **/
//        bitmapWithAlphaBlend.recycle();
//
//        /** gamma **/
//        GPUImageGammaFilter gammaFilter = new GPUImageGammaFilter();
//        gammaFilter.setGamma(1.3f);
//
//        GPUImage gpuImageWithGamma = new GPUImage(mContext);
//        gpuImageWithGamma.setImage(bitmapWithContrast);
//        gpuImageWithGamma.setFilter(gammaFilter);
//
//        Bitmap bitmapWithGamma = gpuImageWithGamma.getBitmapWithFilterApplied();
//
//        /** contrast recycle **/
//        bitmapWithContrast.recycle();
//
//        /** gamma **/
//        GPUImageSharpenFilter sharpenFilter = new GPUImageSharpenFilter();
//        gammaFilter.setGamma(1.13f);
//
//        GPUImage gpuImageWithSharpen = new GPUImage(mContext);
//        gpuImageWithSharpen.setImage(bitmapWithGamma);
//        gpuImageWithSharpen.setFilter(sharpenFilter);
//
//        Bitmap bitmapWithSharpen = gpuImageWithSharpen.getBitmapWithFilterApplied();
//
//        /** gamma recycle **/
//        bitmapWithGamma.recycle();
//
//        /** saturation **/
//        GPUImageSaturationFilter saturationFilter = new GPUImageSaturationFilter();
//        saturationFilter.setSaturation(1.3f);
//
//        GPUImage playGpuImage = new GPUImage(mContext);
//        playGpuImage.setImage(bitmapWithSharpen);
//        playGpuImage.setFilter(saturationFilter);
//
//        Bitmap resultBitmap = playGpuImage.getBitmapWithFilterApplied();
//
//        /** sharpen recycle **/
//        bitmapWithSharpen.recycle();

        Bitmap source = bitmap;

        /** alpha blend **/
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        Bitmap filterBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.f_d7c9ac, options);

        GPUImageAlphaBlendFilter alphaBlendFilter = new GPUImageAlphaBlendFilter();
        alphaBlendFilter.setBitmap(filterBitmap);
        alphaBlendFilter.setMix(1.4f);

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
