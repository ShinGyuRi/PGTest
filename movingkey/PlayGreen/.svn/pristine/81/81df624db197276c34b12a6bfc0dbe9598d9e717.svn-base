package kr.innisfree.playgreen.ImageFilter.bitmapfilter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import kr.innisfree.playgreen.R;

/**
 * Created by wonderland on 16. 4. 29..
 */
public class EarthBitmapThread2 extends Thread {

    private Context mContext;
    private Bitmap bitmap;
    private Handler handler;

    public EarthBitmapThread2(Context mContext, Bitmap bitmap, Handler handler) {
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
        alphaBlendFilter.setMix(0.21f);

        GPUImage gpuImageWithAlphaBlend = new GPUImage(mContext);
        gpuImageWithAlphaBlend.setImage(source);
        gpuImageWithAlphaBlend.setFilter(alphaBlendFilter);

        Bitmap bitmapWithAlphaBlend = gpuImageWithAlphaBlend.getBitmapWithFilterApplied();

        /** source recycle **/
//        source.recycle();

        /** 1. brightness **/
        GPUImageBrightnessFilter brightnessFilter = new GPUImageBrightnessFilter();
        brightnessFilter.setBrightness(0.11f);

        GPUImage gpuImageWithBrightness = new GPUImage(mContext);
        gpuImageWithBrightness.setImage(bitmapWithAlphaBlend);
        gpuImageWithBrightness.setFilter(brightnessFilter);

        Bitmap bitmapWithBrightness = gpuImageWithBrightness.getBitmapWithFilterApplied();

        /** 1. alphablend recycle **/
        bitmapWithAlphaBlend.recycle();

        /** 2. gaussian hue **/
        GPUImageHueFilter hueFilter = new GPUImageHueFilter();
        hueFilter.setHue(7.1f);

        GPUImage gpuImageWithHue = new GPUImage(mContext);
        gpuImageWithHue.setImage(bitmapWithBrightness);
        gpuImageWithHue.setFilter(brightnessFilter);

        Bitmap bitmapWithHue = gpuImageWithHue.getBitmapWithFilterApplied();

        /** 2. brightness recycle **/
        bitmapWithBrightness.recycle();

        /** 3. saturation **/
        GPUImageSaturationFilter saturationFilter = new GPUImageSaturationFilter();
        saturationFilter.setSaturation(1.2f);

        GPUImage gpuImageWithSaturation = new GPUImage(mContext);
        gpuImageWithSaturation.setImage(bitmapWithHue);
        gpuImageWithSaturation.setFilter(saturationFilter);

        Bitmap bitmapWithSaturation = gpuImageWithSaturation.getBitmapWithFilterApplied();

        /** 3. gaussian hue recycle **/
        bitmapWithHue.recycle();

        /** 4. contrast **/
        GPUImageContrastFilter contrastFilter = new GPUImageContrastFilter();
        contrastFilter.setContrast(1.5f);

        GPUImage gpuImageWithContrast = new GPUImage(mContext);
        gpuImageWithContrast.setImage(bitmapWithSaturation);
        gpuImageWithContrast.setFilter(contrastFilter);

        Bitmap bitmapWithContrast = gpuImageWithContrast.getBitmapWithFilterApplied();

        /** alphablend recycle **/
        bitmapWithSaturation.recycle();

        /** 2. sharpen **/
        GPUImageSharpenFilter sharpenFilter = new GPUImageSharpenFilter();
        sharpenFilter.setSharpness(1.0f);

//        GPUImage gpuImageWithSharpen = new GPUImage(mContext);
//        gpuImageWithSharpen.setImage(bitmapWithContrast);
//        gpuImageWithSharpen.setFilter(sharpenFilter);
//
//        Bitmap bitmapWithSharpen = gpuImageWithSharpen.getBitmapWithFilterApplied();

        GPUImage playGpuImage = new GPUImage(mContext);
        playGpuImage.setImage(bitmapWithContrast);
        playGpuImage.setFilter(sharpenFilter);

        Bitmap resultBitmap = playGpuImage.getBitmapWithFilterApplied();

        bitmapWithContrast.recycle();

        Bundle bundle = new Bundle();
        bundle.putParcelable("bitmap", resultBitmap);
        Message message = new Message();
        message.setData(bundle);
        handler.sendMessage(message);

    }

}
