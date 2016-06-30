package tiltcode.movingkey.com.tiltcode_new.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import tiltcode.movingkey.com.tiltcode_new.R;

/**
 * Created by Gyul on 2016-06-24.
 */
public class ScaleView extends View {

    public static String TAG = ScaleView.class.getSimpleName();

    private int scaleWidth, markWidth;
    private int scaleHeight, markHeight;
    private int marginLeft, marginTop;

    Bitmap scaleImage, tiltImage;

    public float tiltAngle;

    private float px;

    public ScaleView(Context context) {
        super(context);
        Log.d(TAG,"context");

        init();
    }

    public ScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG,"context attrs");


        init();
    }

    public ScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG,"context attrs defStyle");


        init();
    }

    private void init() {
        if(scaleImage == null)  {

            scaleWidth = (int) getResources().getDimension(R.dimen.tilt_scale_width);
            scaleHeight = (int) getResources().getDimension(R.dimen.tilt_scale_height);
            markWidth = (int) getResources().getDimension(R.dimen.tilt_mark_width);
            markHeight = (int) getResources().getDimension(R.dimen.tilt_mark_height);
            marginLeft = (int) getResources().getDimension(R.dimen.tilt_margin_left);
            marginTop = (int) getResources().getDimension(R.dimen.tilt_margin_top);

            BitmapFactory.Options o=new BitmapFactory.Options();
            o.inSampleSize = 1;
            o.inPreferredConfig = Bitmap.Config.RGB_565;
            o.inDither=false;                     //Disable Dithering mode
            o.inPurgeable=true;

            tiltImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.tilt_mark, o), markWidth, markHeight, true);
            scaleImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.scale, o), scaleWidth, scaleHeight, true);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        canvas.drawBitmap(tiltImage, null, new Rect(marginLeft, marginTop, marginLeft+markWidth, marginTop+markHeight), null);

        canvas.rotate(-tiltAngle, scaleWidth / 2, scaleHeight / 2);
        canvas.drawBitmap(scaleImage, null, new Rect(0, 0, scaleWidth, scaleHeight), null);



        invalidate();

    }
}
