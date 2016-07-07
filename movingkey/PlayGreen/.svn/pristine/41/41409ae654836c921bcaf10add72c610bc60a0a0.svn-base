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
public class InniBitmapThread3 extends Thread {

    private Context mContext;
    private Bitmap bitmap;
    private Handler handler;

    public InniBitmapThread3(Context mContext, Bitmap bitmap, Handler handler) {
        this.mContext = mContext;
        this.bitmap = bitmap;
        this.handler = handler;
    }

    public InniBitmapThread3(Context mContext, String absolutePath, int width, Handler handler) {
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

        Bitmap source = bitmap;

        /** alpha blend **/
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        Bitmap inniFilterBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.f_7fb5e7, options);

        GPUImageAlphaBlendFilter alphaBlendFilter = new GPUImageAlphaBlendFilter();
        alphaBlendFilter.setBitmap(inniFilterBitmap);
        alphaBlendFilter.setMix(0.3f);

        GPUImage gpuImageWithAlphaBlend = new GPUImage(mContext);
        gpuImageWithAlphaBlend.setImage(source);
        gpuImageWithAlphaBlend.setFilter(alphaBlendFilter);

        Bitmap bitmapWithAlphaBlend = gpuImageWithAlphaBlend.getBitmapWithFilterApplied();

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

        /** contrast recycle **/
        bitmapWithSaturation.recycle();

        Bundle bundle = new Bundle();
        bundle.putParcelable("bitmap", resultBitmap);
        Message message = new Message();
        message.setData(bundle);
        handler.sendMessage(message);

    }

}
