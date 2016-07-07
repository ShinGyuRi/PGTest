package kr.innisfree.playgreen.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

import kr.innisfree.playgreen.fragment.camera.CameraFrag;

/**
 * Created by wonderland on 16. 4. 18..
 */
public class CameraSurfaceView extends SurfaceView {

    private CameraFrag cameraFrag;

    public CameraSurfaceView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        setMeasuredDimension(
                MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
    }

    public void setCameraFrag(CameraFrag cameraFrag) {
        this.cameraFrag = cameraFrag;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
//		    float touchMajor = event.getTouchMajor();
//		    float touchMinor = event.getTouchMinor();
//
//		    Rect touchRect = new Rect(
//		    		(int)(x - touchMajor/2),
//		    		(int)(y - touchMinor/2),
//		    		(int)(x + touchMajor/2),
//		    		(int)(y + touchMinor/2));

//            if (cameraFrag != null)
//                cameraFrag.touchFocus((int) x, (int) y);
        }

        return true;
    }


}
