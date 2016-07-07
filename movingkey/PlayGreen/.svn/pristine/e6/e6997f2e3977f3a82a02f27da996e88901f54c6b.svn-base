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
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import kr.innisfree.playgreen.R;

/**
 * Created by wonderland on 16. 4. 29..
 */
public class InniBitmapThread extends Thread {

    private Context mContext;
    private Bitmap bitmap;
    private Handler handler;

    private String absolutePath;

    public InniBitmapThread(Context mContext, Bitmap bitmap, Handler handler) {
        this.mContext = mContext;
        this.bitmap = bitmap;
        this.handler = handler;
    }

    public InniBitmapThread(Context mContext, String absolutePath, int width, Handler handler) {
        this.mContext = mContext;
        this.handler = handler;
        bitmap = BitmapFactory.decodeFile(absolutePath);
        if (bitmap.getWidth() > width)
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);
        bitmap = ExifUtil.rotateBitmap(absolutePath, bitmap);
    }

    @Override
    public void run() {
        super.run();

//        Bitmap source = bitmap;
//
//        /** alpha blend **/
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 3;
//        Bitmap inniFilterBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.filter_inni, options);
//
//        GPUImageAlphaBlendFilter alphaBlendFilter = new GPUImageAlphaBlendFilter();
//        alphaBlendFilter.setBitmap(inniFilterBitmap);
//        alphaBlendFilter.setMix(0.25f);
//
//        GPUImage playGpuImage = new GPUImage(mContext);
//        playGpuImage.setImage(source);
//        playGpuImage.setFilter(alphaBlendFilter);
//
//        Bitmap resultBitmap = playGpuImage.getBitmapWithFilterApplied();
//
//        /** source recycle **/
////        source.recycle();

        Bitmap source = bitmap;

        /** alpha blend **/
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        Bitmap inniFilterBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.f_d7c9ac, options);

        GPUImageAlphaBlendFilter alphaBlendFilter = new GPUImageAlphaBlendFilter();
        alphaBlendFilter.setBitmap(inniFilterBitmap);
        alphaBlendFilter.setMix(0.18f);

        GPUImage gpuImageWithAlphaBlend = new GPUImage(mContext);
        gpuImageWithAlphaBlend.setImage(source);
        gpuImageWithAlphaBlend.setFilter(alphaBlendFilter);

        Bitmap bitmapWithAlphaBlend = gpuImageWithAlphaBlend.getBitmapWithFilterApplied();

        source.recycle();

        /** 1. brightness **/
        GPUImageBrightnessFilter brightnessFilter = new GPUImageBrightnessFilter();
        brightnessFilter.setBrightness(0.05f);

        GPUImage gpuImageWithBrightness = new GPUImage(mContext);
        gpuImageWithBrightness.setImage(bitmapWithAlphaBlend);
        gpuImageWithBrightness.setFilter(brightnessFilter);

        Bitmap bitmapWithBrightness = gpuImageWithBrightness.getBitmapWithFilterApplied();

        /** 1. alphablend recycle **/
        bitmapWithAlphaBlend.recycle();

        /** 2. contrast **/
        GPUImageContrastFilter contrastFilter = new GPUImageContrastFilter();
        contrastFilter.setContrast(1.5f);

        GPUImage gpuImageWithContrast = new GPUImage(mContext);
        gpuImageWithContrast.setImage(bitmapWithBrightness);
        gpuImageWithContrast.setFilter(contrastFilter);

        Bitmap bitmapWithContrast = gpuImageWithContrast.getBitmapWithFilterApplied();

        /** 2. brightness recycle **/
        bitmapWithBrightness.recycle();

        /** 3. saturation **/
        GPUImageSaturationFilter saturationFilter = new GPUImageSaturationFilter();
        saturationFilter.setSaturation(0.8f);

        GPUImage gpuImageWithSaturation = new GPUImage(mContext);
        gpuImageWithSaturation.setImage(bitmapWithContrast);
        gpuImageWithSaturation.setFilter(saturationFilter);

        Bitmap bitmapWithSaturation = gpuImageWithSaturation.getBitmapWithFilterApplied();

        /** 3. contrast recycle **/
        bitmapWithBrightness.recycle();

        /** 4. gaussian hue **/
        GPUImageHueFilter hueFilter = new GPUImageHueFilter();
        hueFilter.setHue(6.3f);

        GPUImage inniGpuImage = new GPUImage(mContext);
        inniGpuImage.setImage(bitmapWithSaturation);
        inniGpuImage.setFilter(hueFilter);

        Bitmap resultBitmap = inniGpuImage.getBitmapWithFilterApplied();

        /** 4. stration recycle **/
        bitmapWithSaturation.recycle();

        Bundle bundle = new Bundle();
        bundle.putParcelable("bitmap", resultBitmap);
        Message message = new Message();
        message.setData(bundle);
        handler.sendMessage(message);

    }

}
